package com.tibco.tct.fom.search;

import static com.tibco.tct.fom.search.Constants.COLUMN_SPLIT_CHAR;
import static com.tibco.tct.fom.search.Constants.RESULT_SPLIT_CHAR;
import static com.tibco.tct.fom.search.Constants.ROW_SPLIT_CHAR;
import static com.tibco.tct.fom.search.Constants.TABLE_HEADER;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class OperateTableValueUtils {
	
	public static Map<String, String> getNewestProperties(String tableContent, Map<String, String> PropertiesMap){
		int selectedRow = Integer.valueOf(tableContent.substring(tableContent.lastIndexOf(RESULT_SPLIT_CHAR)+1));
		String[] rows = tableContent.substring(0, tableContent.lastIndexOf(RESULT_SPLIT_CHAR)).split(ROW_SPLIT_CHAR);
		String[] cells = rows[selectedRow+1].split(COLUMN_SPLIT_CHAR);
		String selectedKey = cells[0];
		PropertiesMap.remove(selectedKey);
		if(cells.length > 1){
			PropertiesMap.put(selectedKey, cells[1]);
		}else{
			PropertiesMap.put(selectedKey, "");
		}
		return PropertiesMap;
	}
	
	public static Map<String, String> changePropertiesToMap(String filePath) throws IOException{
	    Map<String, String> temMap = new HashMap<String, String>();
	    InputStream input = new FileInputStream(filePath);
		Properties prop = new Properties();
		prop.load(input);
		input.close();
		Set<String> popertyKeys = prop.stringPropertyNames();
		for(String key: popertyKeys){
			temMap.put(key, prop.getProperty(key));
		}
		return temMap;
	}
	
	public static String getTableValue(Map<String, String> PropertiesMap, String inputedKey){
		String tableXMLValue = TABLE_HEADER;

		if(PropertiesMap == null || PropertiesMap.isEmpty()){
			return tableXMLValue;
		}
		Set<String> PropertyKeys = PropertiesMap.keySet();
		String[] maybeKeys = new String[PropertyKeys.size()];
		if(inputedKey != null && inputedKey.length() > 0){
			int len = 0;
			for (String key : PropertyKeys) {
				if(key.contains(inputedKey) || key.equals(inputedKey))
					maybeKeys[len++] = key;
			}
		}else{
			for(int i=0; i<PropertyKeys.size(); i++)
				maybeKeys[i] = (PropertyKeys.toArray()[i]).toString();
		}
		
		if(maybeKeys != null){
			for (String key : maybeKeys) {
				if(key != null){
					String property = PropertiesMap.get(key);
					String rowValue = "";
					rowValue = rowValue + key + COLUMN_SPLIT_CHAR + property;
					tableXMLValue = tableXMLValue + ROW_SPLIT_CHAR + rowValue;
				}
			}
		}
		//tableXMLValue = tableXMLValue + RESULT_SPLIT_CHAR + 0;
		return tableXMLValue;
	}
}
