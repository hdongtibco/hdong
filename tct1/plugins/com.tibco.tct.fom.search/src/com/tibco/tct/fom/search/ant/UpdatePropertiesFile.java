package com.tibco.tct.fom.search.ant;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import static com.tibco.tct.fom.search.Constants.COLUMN_SPLIT_CHAR;
import static com.tibco.tct.fom.search.Constants.ROW_SPLIT_CHAR;
import static com.tibco.tct.fom.search.Constants.RESULT_SPLIT_CHAR;

public class UpdatePropertiesFile extends Task{

	static final String PROPERTIES_FILE_PATH = "search.file.path";
	static final String PROPERTIES_VALUE = "search.properties.info";
	
	public void execute(){
		Hashtable<?, ?> table = getProject().getProperties();
		String filePath = table.get(PROPERTIES_FILE_PATH).toString();
		String propertiesValue = table.get(PROPERTIES_VALUE).toString();
		try {
			updateFile(filePath, propertiesValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void bakFile(String filePath){
		File oldFile = new File(filePath);
		File bakFile = new File(oldFile.getAbsoluteFile() + "." + new SimpleDateFormat("yyyy-MM-dd.hh-mm-ss").format(new Date()) + ".bak");
		oldFile.renameTo(bakFile);
	}
	
	private Map<String,String> getProps(String propertiesValue){
		Map<String,String>temMap = new HashMap<String,String>();
		if(propertiesValue.contains(RESULT_SPLIT_CHAR)){
			propertiesValue = propertiesValue.substring(0, propertiesValue.indexOf(RESULT_SPLIT_CHAR));
		}
		String[] propValue = propertiesValue.split(ROW_SPLIT_CHAR);
		propValue[0] = null;  // KEY,Value
		for(String prop:propValue){
			if(prop != null && !"".equals(prop)){
				String[] values = prop.split(COLUMN_SPLIT_CHAR);
				temMap.put(values[0], values[1]);
			}
		}
		return temMap;
	}
	
	private void updateFile(String filePath, String propertiesValue) throws Exception{
		if(!(new File(filePath).exists())){
			throw new BuildException(filePath+" is not exist !");
		}
		bakFile(filePath);
		
		Map<String,String>propMap = getProps(propertiesValue);
		
		File oldFile = new File(filePath);
		String line = System.getProperty("line.separator");
		StringBuffer property = new StringBuffer();
		FileWriter propertyFileWriter = new FileWriter(oldFile, false);
		Iterator<?> it = propMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,String> entry = (Entry<String, String>) it.next();
			property.append(entry.getKey() + "=" + entry.getValue());
			property.append(line);
		}
		propertyFileWriter.write(property.toString());
		propertyFileWriter.close();
	}
}
