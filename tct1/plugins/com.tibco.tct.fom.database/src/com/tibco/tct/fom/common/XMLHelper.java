package com.tibco.tct.fom.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLHelper {
	
	
	public static void updateAttributeValue(Node node, String attributeFlag, String newAttributeValue){
		NamedNodeMap attributes = node.getAttributes();
		if(null != attributes){
			for(int j=0; j<attributes.getLength(); j++){
				Node attrNode = attributes.item(j);
				String oldValue = attrNode.getNodeValue();
				if(attributeFlag.equals(attrNode.getNodeName()) && !oldValue.equals(newAttributeValue)){
					System.out.println("The attribute "+ attrNode.getNodeName() + "'s value: '" + oldValue + "' will be updated to '" + newAttributeValue + "'");
					UpdateLog4jFiles.isUpdated = true;
					attrNode.setNodeValue(newAttributeValue);
				}
			}
		}
	}
	
	public static String getAttributeValue(Node node, String attributeFlag){
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
	
	public static NodeList getNodes(String express, Object source) throws XPathExpressionException{
		if(express == null){
			express = "//*";
		}
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		return ((NodeList) xp.evaluate(express, source, XPathConstants.NODESET));
	}
	
	public static void bakXMLFile(String filePath) throws Exception{
		System.out.println("start to backup '" + filePath + "' file...");
		Document doc = loadXMLFile(new File(filePath));
		filePath = filePath + ".bak";
		saveXmlFile(filePath, doc);
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
	
	public static Map<String,Document> getDocuments(final String configFilePath, final List<String> needLoadFileNameList) throws Exception {
		Map<String,Document> documentsMap = new HashMap<String,Document>();
		File configFile = new File(configFilePath);
		if(configFile.isFile()){
			processLoadFile(configFile, needLoadFileNameList, documentsMap);
		}
		if(configFile.isDirectory()){
			File[] files = configFile.listFiles();
			for(File f:files){
				processLoadFile(f, needLoadFileNameList, documentsMap);
			}
		}
		return documentsMap;
	}
	
	private static void processLoadFile(final File configFile, final List<String> needLoadFileNameList, Map<String,Document> documentsMap) throws Exception{
		if(needLoadFileNameList != null){
			if(needLoadFileNameList.contains(configFile.getName())){
				Document doc = loadXMLFile(configFile);
				if(doc != null)
					documentsMap.put(configFile.getAbsolutePath(), doc);
			}
		}
	}
	
	public static Document loadXMLFile(final File configFile) throws Exception{
		if(configFile.isFile() && configFile.getName().endsWith(".xml")){
			System.out.println("load the config file: " + configFile.getAbsolutePath());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setEntityResolver(new EntityResolver(){
				public InputSource resolveEntity(String arg0, String arg1)throws SAXException, IOException  {  
					return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));   
				}  
			});
			return db.parse(configFile);
		}
		return null;
	}
}
