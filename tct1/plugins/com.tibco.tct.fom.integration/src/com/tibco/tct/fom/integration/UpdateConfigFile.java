package com.tibco.tct.fom.integration;

import static com.tibco.tct.fom.integration.Constants.ENTERPRISE_NAME;
import static com.tibco.tct.fom.integration.Constants.FP_HOST_NAME;
import static com.tibco.tct.fom.integration.Constants.FP_NODE;
import static com.tibco.tct.fom.integration.Constants.FP_PORT;
import static com.tibco.tct.fom.integration.Constants.PASSWORD;
import static com.tibco.tct.fom.integration.Constants.USERNAME;
import static com.tibco.tct.fom.integration.Constants.FP_CONFIG_ENABLE;
import static com.tibco.tct.fom.integration.Constants.OWNER_FP;
import static com.tibco.tct.fom.integration.Constants.SERVER_HOST_PORT;
import static com.tibco.tct.fom.integration.Constants.SERVER_HOST_NAME;
import static com.tibco.tct.fom.integration.Constants.TIBCO_HOME;

import static com.tibco.tct.fom.integration.Constants.OFFLINE_ACTION;
import static com.tibco.tct.fom.integration.Constants.OFFLINE_CUSTOMER;
import static com.tibco.tct.fom.integration.Constants.OFFLINE_PLANFRAGMENT;
import static com.tibco.tct.fom.integration.Constants.OFFLINE_PRODUCT;
import static com.tibco.tct.fom.integration.Constants.OFFLINE_SEGMENT;
import static com.tibco.tct.fom.integration.Constants.OFFLINE_PRICE;
import static com.tibco.tct.fom.integration.Constants.OFFLINE_DISCOUNT;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.tibco.tct.fom.common.UpdatePropFiles;

public class UpdateConfigFile extends Task {

	static final String ENV_TIBCO_HOME = "fom.tibco.home";
	static final String INT_HOST_NAME = "int.host.name";
	static final String INT_PORT = "int.port";
	static final String INT_NODE = "int.node";
	static final String INT_ENTERPRISE = "int.enterprise";
	static final String INT_IS_OFFLINE = "int.isoffline";
	static final String INT_USER = "int.user";
	static final String INT_FP_CONFIG_ENABLE = "int.fp.config.enable";
	static final String INT_PASSWORD = "int.password";
	static final String INT_OWNER_FP = "int.owner.fp";
	static final String INT_SERVER_HOST = "int.server.host";
	static final String INT_SERVER_PORT = "int.server.port";
	
	static final String INT_OFFLINE_DIR = "int.offline.dir";
	
	
	public void execute(){
		Hashtable<?, ?> table = getProject().getProperties();
		Map<String,String>configMap = new HashMap<String,String>();
		if(Boolean.parseBoolean(table.get(INT_FP_CONFIG_ENABLE).toString())){
			configMap.put(FP_HOST_NAME, (String)table.get(INT_HOST_NAME));
			configMap.put(FP_NODE, (String)table.get(INT_NODE));
			configMap.put(FP_PORT, (String)table.get(INT_PORT));
			configMap.put(OWNER_FP, (String)table.get(INT_OWNER_FP));
		}
		if(!Boolean.parseBoolean(table.get(INT_IS_OFFLINE).toString())){
			configMap.put(ENTERPRISE_NAME, (String)table.get(INT_ENTERPRISE));
			configMap.put(PASSWORD, (String)table.get(INT_PASSWORD));
			configMap.put(USERNAME, (String)table.get(INT_USER));
			configMap.put(SERVER_HOST_NAME, (String)table.get(INT_SERVER_HOST));
			configMap.put(SERVER_HOST_PORT, (String)table.get(INT_SERVER_PORT));
		}
		
		configMap.put("IS_OFFLINE", (String)table.get(INT_IS_OFFLINE));
		configMap.put("OFFLINE_DIR", (String)table.get(INT_OFFLINE_DIR));
		
		configMap.put(OFFLINE_ACTION, (String)table.get(INT_IS_OFFLINE));
		configMap.put(OFFLINE_CUSTOMER, (String)table.get(INT_IS_OFFLINE));
		configMap.put(OFFLINE_PLANFRAGMENT, (String)table.get(INT_IS_OFFLINE));
		configMap.put(OFFLINE_PRODUCT, (String)table.get(INT_IS_OFFLINE));
		configMap.put(OFFLINE_SEGMENT, (String)table.get(INT_IS_OFFLINE));
		configMap.put(OFFLINE_PRICE, (String)table.get(INT_IS_OFFLINE));
		configMap.put(OFFLINE_DISCOUNT, (String)table.get(INT_IS_OFFLINE));
		
		configMap.put(TIBCO_HOME, (String)table.get(ENV_TIBCO_HOME));
		configMap.put(FP_CONFIG_ENABLE, (String)table.get(INT_FP_CONFIG_ENABLE));
		String filePath = IntgrationUpdateXML.getFilePath((String) table.get(ENV_TIBCO_HOME));
		try {
			if(!configMap.isEmpty()){
				IntgrationUpdateXML.updateXMLFile(filePath, configMap);
				UpdatePropFiles.updatePropFile(configMap);
			}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
	
	private void printInfo(Hashtable tab){
		this.log("Below are the configuration informations: \n");
		this.log("INTEGERATION_HOST_NAME: " + tab.get(INT_HOST_NAME));
		this.log("INTEGERATION_PORT: " + tab.get(INT_PORT));
		this.log("INTEGERATION_NODE: " + tab.get(INT_NODE));
		this.log("INTEGERATION ENTERPRISE: " + tab.get(INT_ENTERPRISE));
		this.log("INTEGERATION_IS_OFFLINE: " + tab.get(INT_IS_OFFLINE));
		this.log("INTEGERATION_USER: " + tab.get(INT_USER));
		this.log("INTEGERATION_FP_CONFIG_ENABLE: " + tab.get(INT_FP_CONFIG_ENABLE));
		this.log("INTEGERATION_PASSWORD: " + tab.get(INT_PASSWORD));
		this.log("INTEGERATION_OWNER_FP: " + tab.get(INT_OWNER_FP));
		this.log("INTEGERATION_SERVER_HOST: " + tab.get(INT_SERVER_HOST));
		this.log("INTEGERATION_SERVER_PORT: " + tab.get(INT_SERVER_PORT));
	}
}
