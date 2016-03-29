package com.tibco.tct.fom.integration;

import static com.tibco.tct.fom.common.Constants.ATTRIBUTES_ID;
import static com.tibco.tct.fom.common.Constants.SAVED_ATTRIBUTE;
import static com.tibco.tct.fom.common.Constants.TIBCO_HOME;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tibco.tct.fom.common.FileOperationUtil;

public class IntgrationUpdateXML extends FileOperationUtil{

	private static final String OFFLINE_CONFIG_FILE = "ConfigValues_OMS.xml";
	
	private static final String NEED_UPDATE_PATH = "/usr/tibco";
	
	public static void updateOfflineIntDir(String offlineDir, String fomHome, String filePath) throws Exception{
		NodeList configurationsList = getNodes("//ConfString", documentsList.get(filePath));
		if (null != configurationsList) {
			for (int i = 0; i < configurationsList.getLength(); i++) {
				Node configuration = configurationsList.item(i);
				String defaultValue = getAttributeValue(configuration, "default");
				if(defaultValue.startsWith(NEED_UPDATE_PATH)){
					String oldValue = getAttributeValue(configuration, SAVED_ATTRIBUTE);
					String newValue = getNewValue(defaultValue, oldValue, offlineDir);
					if(newValue != null && newValue.length() > 0 && !newValue.endsWith(oldValue)){
						updateAttributeValue(configuration, SAVED_ATTRIBUTE, newValue);
						createDir(newValue);
					}
				}
				
			}
		}
	}
	
	private static void createDir(String path){
		File dir = new File(path);
		if(!dir.exists()){
			System.out.println("Create directory: " + path);
			dir.mkdirs();
		}
	}
	
	private static String getNewValue(String defaultValue, String oldValue, String offlineDir){
		String regex = generatePathRegex(defaultValue);
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(oldValue);
		if(mt.find()){
			return (offlineDir + mt.group(2));
		}
		return null;
	}
	
	private static String generatePathRegex(String path){
		StringBuffer regex = new StringBuffer();
		int index = path.indexOf(NEED_UPDATE_PATH);
		if(index > -1){
			String groupValue = path.substring(index + NEED_UPDATE_PATH.length());
			regex = regex.append("(.*)").append("(").append(groupValue).append(")");
		}
		
		return bufferToString(regex);
	}
	
	public static void updateXMLFile(String configFilePath, Map<String, String> newConfigValue)throws Exception {
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
				if(fileName.endsWith(OFFLINE_CONFIG_FILE) && Boolean.parseBoolean(newConfigValue.get("IS_OFFLINE")) && newConfigValue.get("OFFLINE_DIR") != null && newConfigValue.get("OFFLINE_DIR").length()>0){
					updateOfflineIntDir(newConfigValue.get("OFFLINE_DIR"), newConfigValue.get(TIBCO_HOME), fileName);
				}
			}
			if(isUpdated){
				bakXMLFile(fileName);
				saveXmlFile(fileName, documentsList.get(fileName));
				System.out.println("File: " + fileName + " updated successfully.");
			}
		}
	}
}
