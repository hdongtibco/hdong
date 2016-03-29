package com.tibco.tct.fom.common;

import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateLog4jFiles {

	public static boolean isUpdated = false;
	
	// attributeId = "name", updateAttribute = "value", xPathExpression = "//appender/param";
	public static void updateLog4jFile(String configFilePath, String xPathExpression, String attributeId, String updateAttribute, Map<String, String> newConfigValue, List<String> needLoadFileNameList)throws Exception {
		Map<String,Document> documentsMap = XMLHelper.getDocuments(configFilePath, needLoadFileNameList);
		for(String filePath:documentsMap.keySet()){
			isUpdated = false;
			NodeList configurationsList = XMLHelper.getNodes(xPathExpression, documentsMap.get(filePath));
			if (null != configurationsList) {
				for(String propName:newConfigValue.keySet()){
					for (int i = 0; i < configurationsList.getLength(); i++) {
						Node configuration = configurationsList.item(i);
						String attributeValue = XMLHelper.getAttributeValue(configuration, attributeId);
						if(attributeValue != null && attributeValue.equals(propName)){
							XMLHelper.updateAttributeValue(configuration, updateAttribute, newConfigValue.get(propName));
						}
					}
				}
			}
			if(isUpdated){
				XMLHelper.bakXMLFile(filePath);
				XMLHelper.saveXmlFile(filePath, documentsMap.get(filePath));
				System.out.println("File: " + filePath + " is updated successfully.");
			}
		}
	}
}
