package com.tibco.tct.fom.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import static com.tibco.tct.fom.common.Constants.PROP_DIR_PATH;
import static com.tibco.tct.fom.common.Constants.TIBCO_HOME;
import static com.tibco.tct.fom.common.Constants.FILE_PROP_SEPARATOR;


public class UpdatePropFiles {
	
	private static Map<String, Map<Integer,String>> propertiesMap = new HashMap<String, Map<Integer,String>>();
	
	//eg: fom_home\config\ConfigValues_AFOA.xml~AF_Events_JMS_Username, value
	//because maybe exists many "AF_Events_JMS_Username" peop in different files, and their value are different.
	public static Map<String,String> referenceVariableMap = new HashMap<String,String>();
	
	private static boolean isUpdated = false;
	
	private static final String SEPARATOR = "_";
	
	private static final String SPECIFIC_PROP_ID = "-FOM_";

	public static void updatePropFile(Map<String, String> newConfigValue)throws Exception {
		if(!(new File(newConfigValue.get(TIBCO_HOME)).exists())){
			throw new BuildException(newConfigValue.get(TIBCO_HOME)+" is not exist !");
		}
		System.out.println("\n-------------------------------------Start to update the property files.----------------------------------------\n");
		propertiesMap.clear();
		loadProps(getPropFilesPath(newConfigValue.get(TIBCO_HOME)));
		for(String fielpath:propertiesMap.keySet()){
			isUpdated = false;
			updatePropFile(fielpath, newConfigValue);
			if(isUpdated){
				bakPropFile(fielpath);
				savePropFile(fielpath);
			}
		}
	}
	
	private static void updateReferenceProp(String filePath){
		Map<Integer,String> propMap = propertiesMap.get(filePath);
		String lineSep = CommonUtils.getSysLineSep();
		for(String key:referenceVariableMap.keySet()){
			String propName = key.substring(key.indexOf(FILE_PROP_SEPARATOR) + FILE_PROP_SEPARATOR.length());
			String oldProp = getOldProp(propName, propMap);
			if(oldProp != null){
				String oldValue = oldProp.substring(oldProp.indexOf(SEPARATOR) + SEPARATOR.length());
				int lineNumber = Integer.parseInt(oldProp.substring(0, oldProp.indexOf(SEPARATOR)));
				String newPropValue = referenceVariableMap.get(key);
				if(oldValue != null && !newPropValue.equals(oldValue)){
					isUpdated = true;
					System.out.println("The property "+ propName + "'s value: '" + oldValue + "' will be updated to '" + newPropValue + "'");
					propMap.put(lineNumber, propName + "=" + newPropValue + lineSep);
				}
			}
		}
		propertiesMap.put(filePath, propMap);
	}
	
	private static void updatePropFile(String filePath, Map<String, String> newConfigValue){
		Map<Integer,String> propMap = propertiesMap.get(filePath);
		String lineSep = CommonUtils.getSysLineSep();
		for(String propName:newConfigValue.keySet()){
			String oldProp = getOldProp(propName, propMap);
			if(oldProp != null){
				String oldValue = oldProp.substring(oldProp.indexOf(SEPARATOR) + SEPARATOR.length());
				int lineNumber = Integer.parseInt(oldProp.substring(0, oldProp.indexOf(SEPARATOR)));
				String newPropValue = newConfigValue.get(propName);
				if(oldValue != null && !newPropValue.equals(oldValue)){
					isUpdated = true;
					System.out.println("The property "+ propName + "'s value: '" + oldValue + "' will be updated to '" + newPropValue + "'");
					propMap.put(lineNumber, propName + "=" + newPropValue + lineSep);
				}
			}
		}
		propertiesMap.put(filePath, propMap);
	}
	
	private static String getOldProp(String propName, Map<Integer,String> propMap){
		String lineSep = CommonUtils.getSysLineSep();
		for(Integer lineNum:propMap.keySet()){
			String lineInfo = propMap.get(lineNum);
			if(lineInfo != null && !lineInfo.equals(lineSep) && lineInfo.trim().startsWith(propName)){
				int index = lineInfo.indexOf('=');
				if(index > 0){
					return lineNum + SEPARATOR + lineInfo.substring(index + 1).trim();
				}
			}
		}
		return null;
	}
	
	private static String getPropFilesPath(String fomHome) {
		File file = new File(fomHome, PROP_DIR_PATH);
		return file.getAbsolutePath();
	}
	
	private static void loadProps(String filesDirPath) throws Exception{
		File propFilesDir = new File(filesDirPath);
		if(propFilesDir.isDirectory()){
			File[] files = propFilesDir.listFiles();
			for(File f:files){
				loadProps(f.getAbsolutePath());
			}
		}else{
			if(propFilesDir.isFile() && propFilesDir.getName().endsWith(".properties")){
				propertiesMap.put(propFilesDir.getAbsolutePath(), getPropFileInfo(propFilesDir.getAbsolutePath()));
			}
		}
	}
	
	private static Map<Integer,String> getPropFileInfo(String filePath) throws Exception{
		System.out.println("load the property file: " + filePath);
		Map<Integer,String> propMap = new LinkedHashMap<Integer,String>(); //Store line number and line info
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		int lineNumber = 1;
		String lineSep = CommonUtils.getSysLineSep();
		while(true){
			String line = bufReader.readLine();
			if(line == null){
				break;
			}
			line = line + lineSep;
			propMap.put(lineNumber ++, line);
		}
		bufReader.close();
		return propMap;
	}
	
	private static void bakPropFile(String filePath) throws Exception{
		System.out.println("start to backup '" + filePath + "' file...");
		CommonUtils.writeFile(CommonUtils.readFile(filePath), filePath + ".bak");
		System.out.println("backup successfully .");
	}
	
	private static void savePropFile(String filePath) throws Exception{
		OutputStream out = new FileOutputStream(filePath);
		out.write(getPropNewContent(filePath));
		System.out.println("save the file: " + filePath + " successfully.");
	}
	
	private static byte[] getPropNewContent(String filePath){
		StringBuffer sbuf = new StringBuffer();
		Map<Integer,String> propMap = propertiesMap.get(filePath);
		for(Integer lineNum:propMap.keySet()){
			sbuf.append(propMap.get(lineNum));
		}
		return sbuf.toString().getBytes();
	}
}
