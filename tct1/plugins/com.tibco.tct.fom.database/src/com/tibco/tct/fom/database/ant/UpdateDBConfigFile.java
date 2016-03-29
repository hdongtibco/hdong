package com.tibco.tct.fom.database.ant;

import static com.tibco.tct.fom.common.Constants.JDBC_HOST;
import static com.tibco.tct.fom.common.Constants.JDBC_JAR_PATH;
//import static com.tibco.tct.fom.common.Constants.JDBC_PASSWORD;
import static com.tibco.tct.fom.common.Constants.JDBC_PORT;
import static com.tibco.tct.fom.common.Constants.JDBC_SID;
//import static com.tibco.tct.fom.common.Constants.JDBC_USERNAME;
import static com.tibco.tct.fom.common.Constants.TIBCO_HOME;
import static com.tibco.tct.fom.common.Constants.SOURCE_PASSWORD;
import static com.tibco.tct.fom.common.Constants.SOURCE_USERNAME;
import static com.tibco.tct.fom.common.Constants.SOURCE_DATABASE;
import static com.tibco.tct.fom.common.Constants.SOURCE_HOST;
import static com.tibco.tct.fom.common.Constants.SOURCE_PORT;
import static com.tibco.tct.fom.common.Constants.SOURCE_URL;
import static com.tibco.tct.fom.common.Constants.HIBERNATE_CATALOG;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_DRIVER_PATH;
//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_HOST_NAME;
//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_PORT;
//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_SID;
//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_USER;
//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_PASSWORD;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_URL;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_AF_TIBCO_HOME;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_USER_ENABLE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_USER;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_PASSWORD;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_AF_TCT_HOME;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_USER;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_PASSWORD;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.tibco.customwizard.config.IDataModel;
import com.tibco.tct.fom.common.UpdatePropFiles;
import com.tibco.tct.fom.common.FileOperationUtil;

public class UpdateDBConfigFile extends Task {

	public void execute(){
		//IDataModel dataMode = actionContext.getDataModel();
		Hashtable<?, ?> table = getProject().getProperties();
		Map<String,String>configMap = new HashMap<String,String>();
		String dbUrl = (String)table.get(DB_URL);
		String dbHost = getPropValue("host", dbUrl);
		String dbPort = getPropValue("port", dbUrl);

		configMap.put(JDBC_HOST, dbHost);
		configMap.put(JDBC_JAR_PATH, toUnixPath((String)table.get(DB_DRIVER_PATH)));
		//configMap.put(JDBC_PASSWORD, (String)table.get(DB_PASSWORD));
		configMap.put(JDBC_PORT, dbPort);
		configMap.put(JDBC_SID, getPropValue("db_id", dbUrl));
		//configMap.put(JDBC_USERNAME, (String)table.get(DB_USER));
		configMap.put(TIBCO_HOME, (String)table.get(DB_AF_TIBCO_HOME));
		
		//other relevant variable
		configMap.put(SOURCE_USERNAME, (String)table.get(DB_CREATE_USER));
		configMap.put(SOURCE_PASSWORD, (String)table.get(DB_CREATE_PASSWORD));
		configMap.put(HIBERNATE_CATALOG, (String)table.get(DB_CREATE_USER));
		
//		if(Boolean.parseBoolean(table.get(DB_CREATE_USER_ENABLE).toString())){
//			configMap.put(SOURCE_USERNAME, (String)table.get(DB_CREATE_USER));
//			configMap.put(SOURCE_PASSWORD, (String)table.get(DB_CREATE_PASSWORD));
//			configMap.put(HIBERNATE_CATALOG, (String)table.get(DB_CREATE_USER));
//		}else{
//			configMap.put(SOURCE_USERNAME, "");
//			configMap.put(SOURCE_PASSWORD, "");
//			configMap.put(HIBERNATE_CATALOG, "");
//		}
		
		configMap.put(SOURCE_DATABASE, getPropValue("db_id", dbUrl));
		configMap.put(SOURCE_HOST, getPropValue("host", dbUrl));
		configMap.put(SOURCE_PORT, getPropValue("port", dbUrl));
		configMap.put(SOURCE_URL, dbUrl);
		
		String filePath = FileOperationUtil.getFilePath((String) table.get(DB_AF_TIBCO_HOME));
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			String tctHome = (String)table.get(DB_AF_TCT_HOME);
			Properties pro = new Properties();
			File file = new File(tctHome+File.separator+"fom_dba_credential.properties");
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			fis = new FileInputStream(file);
			fos = new FileOutputStream(file);
			pro.load(fis);
			pro.setProperty("dbaUserName", (String)table.get(DB_USER));
			pro.setProperty("dbaPassWord", (String)table.get(DB_PASSWORD));
			pro.store(fos, "sava fom dba credential");
			FileOperationUtil.updateXMLFile(filePath, configMap);
			UpdatePropFiles.updatePropFile(configMap);
		} catch (Exception e) {
			throw new BuildException(e);
		}
		finally{
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
//	private String getDatabaseURL(Hashtable table){
//		return "jdbc:oracle:thin:@//" + table.get(DB_HOST_NAME) + ":" + table.get(DB_PORT) + "/" + table.get(DB_SID);
//	}
	
	private String getPropValue(String propName, String dbUrl){
		String db_fromat_1 = "(jdbc:oracle:thin:@)(.*):(.*):(.*)()";
		Pattern pt = Pattern.compile(db_fromat_1);
		Matcher mt = pt.matcher(dbUrl);
		if(mt.find()){
			if("host".equalsIgnoreCase(propName)){
				return mt.group(2);
			}
			if("port".equalsIgnoreCase(propName)){
				return mt.group(3);
			}
			if("db_id".equalsIgnoreCase(propName)){
				return mt.group(4);
			}
		}
		String db_fromat_2 = "(HOST=)(.*)(\\)\\()(PORT=)(.*)(\\)\\)\\)\\()(CONNECT_DATA=\\(SID=)(.*)(\\)\\(.*)";
		pt = Pattern.compile(db_fromat_2);
		mt = pt.matcher(dbUrl);
		if(mt.find()){
			if("host".equalsIgnoreCase(propName)){
				return mt.group(2);
			}
			if("port".equalsIgnoreCase(propName)){
				return mt.group(5);
			}
			if("db_id".equalsIgnoreCase(propName)){
				return mt.group(8);
			}
		}
		String db_fromat_3 = "(HOST=)(.*)(\\)\\()(PORT=)(.*)(\\)\\)\\)\\()(CONNECT_DATA=\\(SERVICE_NAME=)(.*)(\\)\\(.*)";
		pt = Pattern.compile(db_fromat_3);
		mt = pt.matcher(dbUrl);
		if(mt.find()){
			if("host".equalsIgnoreCase(propName)){
				return mt.group(2);
			}
			if("port".equalsIgnoreCase(propName)){
				return mt.group(5);
			}
			if("db_id".equalsIgnoreCase(propName)){
				return mt.group(8);
			}
		}
		return null;
	}
	
	private static String toUnixPath(String path) {
		return path.replace('\\', '/');
	}
}
