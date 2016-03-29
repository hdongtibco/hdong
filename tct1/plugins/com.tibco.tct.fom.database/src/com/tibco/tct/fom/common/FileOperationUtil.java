package com.tibco.tct.fom.common;

import static com.tibco.tct.fom.common.Constants.ATTRIBUTES_ID;
import static com.tibco.tct.fom.common.Constants.SAVED_ATTRIBUTE;
import static com.tibco.tct.fom.common.Constants.WHICH_CHILD_NODE;
import static com.tibco.tct.fom.common.Constants.NEED_CONFIG_FILES;
import static com.tibco.tct.fom.common.Constants.DELLETE_CHAR_REGEX;
import static com.tibco.tct.fom.common.Constants.FILE_PROP_SEPARATOR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tibco.tct.fom.common.UpdatePropFiles;

public class FileOperationUtil {

	public static Map<String,Document> documentsList = new HashMap<String,Document>();
	
	public static boolean isUpdated = false;
	
	public static boolean isNeedConfigFile(String fileName){
		if(null != fileName){
			for(int i=0; i<NEED_CONFIG_FILES.length; i++){
				if(NEED_CONFIG_FILES[i].equals(fileName)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static void updateXMLFile(String configFilePath, Map<String, String> newConfigValue)throws Exception {
		if(!(new File(configFilePath).exists())){
			throw new BuildException(configFilePath+" is not exist !");
		}
		System.out.println("\n-------------------------------------Start to update the XML files.----------------------------------------\n");
		documentsList.clear();
		getDocuments(configFilePath);
		for(String fileName:documentsList.keySet()){
			isUpdated = false;
			NodeList configurationsList = getNodes("//*", documentsList.get(fileName));
			if (null != configurationsList) {
				for(String propName:newConfigValue.keySet()){
					for (int i = 0; i < configurationsList.getLength(); i++) {
						Node configuration = configurationsList.item(i);
						String attributeValue = getAttributeValue(configuration, ATTRIBUTES_ID);
						if(attributeValue != null && attributeValue.equals(propName)){
							Node childNode = getValidChildNode(configuration);
							updateAttributeValue(childNode, SAVED_ATTRIBUTE, newConfigValue.get(propName));
						}
					}
				}
				updateReferenceVariable(fileName, configurationsList, newConfigValue);
			}
			if(isUpdated){
				bakXMLFile(fileName);
				saveXmlFile(fileName, documentsList.get(fileName));
				System.out.println("File: " + fileName + " is updated successfully.");
			}
		}
	}
	
	public static void updateReferenceVariable(String fileName, NodeList configurationsList, Map<String, String> newConfigValue){
		for(String propName:newConfigValue.keySet()){
			for (int i = 0; i < configurationsList.getLength(); i++) {
				Node configuration = configurationsList.item(i);
				String defaultAttributeValue = delIllegalChar(getDefaultAttributeValue(configuration));
				if(!isDefinedConfig(configuration, newConfigValue.keySet()) && defaultAttributeValue != null && defaultAttributeValue.contains(("${" + propName + "}"))){
					int replaceLocation = getReplaceLocation(defaultAttributeValue, propName);
					if(replaceLocation > -1){
						updateReferenceVariable(fileName, configuration, newConfigValue.get(propName), defaultAttributeValue, replaceLocation);
					}
				}
			}
		}
	}
	
	private static boolean isDefinedConfig(Node configuration, Set<String> propNames){
		String parentpropNameValue = getParentPropName(configuration);
		if(parentpropNameValue != null && propNames.contains(parentpropNameValue)){
			return true;
		}
		return false;
	}
	
	private static String getParentPropName(Node configuration){
		Node parentNode = configuration.getParentNode();
		if(parentNode != null){
			return getAttributeValue(parentNode, ATTRIBUTES_ID);
		}
		return null;
	}
	
	private static void updateReferenceVariable(String fileName, Node configuration, String newValue , String defaultAttributeValue, int replaceLocation){
		StringBuffer regex = new StringBuffer();
		String valueAttrRegex = bufferToString(generateRegex(defaultAttributeValue, regex, false));
		String oldValue = getAttributeValue(configuration, "value");
		String newAttrValue = getNewAttrValue(valueAttrRegex, oldValue, newValue, replaceLocation);
		updateAttributeValue(configuration, SAVED_ATTRIBUTE, newAttrValue);
		UpdatePropFiles.referenceVariableMap.put(fileName + FILE_PROP_SEPARATOR + getParentPropName(configuration), newAttrValue);
	}
	
	private static String getNewAttrValue(String valueAttrRegex, String oldValue, String newValue, int replaceLocation){
		Pattern pt = Pattern.compile(valueAttrRegex);
		Matcher mt = pt.matcher(oldValue);
		StringBuffer newValueBuf = new StringBuffer();
		if(mt.find()){
			for(int i=1; i<mt.groupCount(); i++){
				if(replaceLocation == i){
					newValueBuf.append(newValue);
				}else{
					newValueBuf.append(mt.group(i));
				}
			}
			return newValueBuf.toString();
		}
		return newValue;
	}
	
	private static int getReplaceLocation(String defaultAttributeValue, String propName){
		StringBuffer regex = new StringBuffer();
		String defaultAttrRegex = bufferToString(generateRegex(defaultAttributeValue, regex, true));
		String[] Variables =  defaultAttrRegex.split("\\)");
		for(int i=0; i<Variables.length; i++){
			if(Variables[i] != null && Variables[i].contains(("${" + propName + "}"))){
				return i+1;
			}
		}
		return -1;
	}
	
	private static StringBuffer generateRegex(String expression, StringBuffer regex, boolean isDefaultAttr){
		String[] variables = expression.split("\\$\\{", 2);
		if(variables[0] != null && variables[0].length() > 0){
			regex.append("(").append(processProtocol(variables[0])).append(")");
		}
		
		String[] temps = variables[1].split("\\}", 2);
		if(temps[0] != null && temps[0].length() > 0){
			if(!isDefaultAttr){
				regex.append("(").append(".*").append(")");
			}else{
				regex.append("(").append("${" + temps[0] + "}").append(")");
			}
		}
		
		if(temps[1] != null && temps[1].contains("${")){
			generateRegex(temps[1], regex, isDefaultAttr);
		}else{
			if(temps[1] != null && temps[1].length() > 0){
				regex.append("(").append(temps[1]).append(")");
			}
		}
		
		return regex;
	}
	
	private static String processProtocol(String protocol){
		if(protocol.startsWith("tcp:") || protocol.startsWith("ssl:")){
			protocol = "\\btcp://|\\bssl://";
		}
		return protocol;
	}
	
	protected static String bufferToString(StringBuffer regex){
		return regex.append("()").toString();
	}
	
	public static void bakXMLFile(String needBakFilePath) throws Exception{
		System.out.println("start to backup '" + needBakFilePath + "' file...");
		Document doc = getDocument(needBakFilePath);
		needBakFilePath = needBakFilePath + ".bak";
		saveXmlFile(needBakFilePath, doc);
		System.out.println("backup file successfully .");
	}
	
	public static void saveXmlFile(String filePath, Document doc) throws Exception {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		DOMSource source = new DOMSource();
		source.setNode(doc);
		StreamResult result = new StreamResult();
		result.setOutputStream(new FileOutputStream(filePath));
		transformer.transform(source, result);
		System.out.println("save the file: " + filePath + " successfully.");
	}
	

	protected static NodeList getNodes(String express, Object source) throws XPathExpressionException{
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		return ((NodeList) xp.evaluate(express, source, XPathConstants.NODESET));
	}
	
	private static Document loadXMLFile(String configFilePath) throws Exception{
		System.out.println("load the config file: " + configFilePath);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new FileInputStream(configFilePath));
		return doc;
	}
	
	public static String getFilePath(String configFilePath) {
		File file = new File(configFilePath,"config");
		return file.getAbsolutePath();
	}
	
	protected static void updateAttributeValue(Node node, String attributeFlag, String newAttributeValue){
		NamedNodeMap attributes = node.getAttributes();
		if(null != attributes){
			for(int j=0; j<attributes.getLength(); j++){
				Node attrNode = attributes.item(j);
				String oldValue = attrNode.getNodeValue();
				if(attributeFlag.equals(attrNode.getNodeName()) && !oldValue.equals(newAttributeValue)){
					System.out.println("The attribute "+ attrNode.getNodeName() + "'s value: '" + oldValue + "' will be updated to '" + newAttributeValue + "'");
					isUpdated = true;
					attrNode.setNodeValue(newAttributeValue);
				}
			}
		}
	}
	
	protected static Node getValidChildNode(Node paretnNode){
		return paretnNode.getChildNodes().item(WHICH_CHILD_NODE);
	}
	
	// <ConfString default="${AF_ADMIN_DOMAIN}-FOM_ORCH_AGENT" value="${AF_ADMIN_DOMAIN}-FOM_ORCH_AGENT"/>
	// get "${AF_ADMIN_DOMAIN}-FOM_ORCH_AGENT"
	private static String getDefaultAttributeValue(Node node){
		NamedNodeMap attributes = node.getAttributes();
		if(null != attributes){
			for(int j=0; j<attributes.getLength(); j++){
				if("default".equals(attributes.item(j).getNodeName()) && 
						attributes.item(j).getNodeValue().contains("${") ){
					return attributes.item(j).getNodeValue().trim().replaceAll("\"", "");
				}
			}
		}
		return null;
	}
	
	private static String delIllegalChar(String expression){
		if(expression != null && expression.length() > 0){
			Pattern p = Pattern.compile(DELLETE_CHAR_REGEX);     
			Matcher m = p.matcher(expression);     
			return m.replaceAll("").trim(); 
		}
		return expression;
	}
	
	protected static String getAttributeValue(Node node, String attributeFlag){
		NamedNodeMap attributes = node.getAttributes();
		if(null != attributes){
			for(int j=0; j<attributes.getLength(); j++){
				if(attributeFlag.equals(attributes.item(j).getNodeName())){
					return attributes.item(j).getNodeValue().trim();
				}
			}
		}
		return null;
	}
	
	public static Document getDocument(String configFilepath) throws Exception {
		return loadXMLFile(configFilepath);
	}
	
	public static Map<String,Document> getDocuments(String configFilePath) throws Exception {
		File configFile = new File(configFilePath);
		if(configFile.isDirectory()){
			File[] files = configFile.listFiles();
			for(File f:files){
				getDocuments(f.getAbsolutePath());
			}
		}else{
			if(isNeedConfigFile(configFile.getName())){
				documentsList.put(configFile.getAbsolutePath(),loadXMLFile(configFilePath));
			}
		}
		return documentsList;
	}
}
