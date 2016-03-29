package com.tibco.tct.fom.environment;

import static com.tibco.tct.fom.environment.Constants.ADMIN_DOMAIN;
//import static com.tibco.tct.fom.environment.Constants.ADMIN_MACHINE;
import static com.tibco.tct.fom.environment.Constants.ADMIN_PASSWORD;
import static com.tibco.tct.fom.environment.Constants.ADMIN_USERNAME;
import static com.tibco.tct.fom.environment.Constants.RV_DAEMON;
import static com.tibco.tct.fom.environment.Constants.HAWK_DAEMON;
import static com.tibco.tct.fom.environment.Constants.TRA_HOME;
import static com.tibco.tct.fom.environment.Constants.TIBCO_HOME;
import static com.tibco.tct.fom.environment.Constants.ADMIN_HAWK;
import static com.tibco.tct.fom.environment.Constants.ENABLE_OCV;
//import static com.tibco.tct.fom.environment.Constants.OMS_SERVER_NAME;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.tibco.tct.fom.common.UpdatePropFiles;
import com.tibco.tct.fom.common.FileOperationUtil;

public class UpdateConfigFile extends Task {

	//static final String ENV_HOST_NAME = "env.host.name";
	static final String ENV_TRA_HOME = "env.tra.home";
	static final String ENV_TIBCO_HOME = "fom.tibco.home";
	static final String ENV_DOMAIN = "env.domain";
	static final String ENV_RV_DAEMON = "env.rv.daemon";
	static final String ENV_HAWK_DAEMON = "env.hawk.daemon";
	static final String ENV_USER = "env.user";
	static final String ENV_PASSWORD = "env.password";
	static final String ENV_ADMIN_HAWK= "env.admin.hawk";
	static final String OMS_ENABLE_OCV= "env.enable.ocv";
	
	public void execute(){
		Hashtable<?, ?> table = getProject().getProperties();
		printInfo(table);
		Map<String,String>configMap = new HashMap<String,String>();
		configMap.put(TIBCO_HOME, (String)table.get(ENV_TIBCO_HOME));
		configMap.put(TRA_HOME, (String)table.get(ENV_TRA_HOME));
		configMap.put(RV_DAEMON, (String)table.get(ENV_RV_DAEMON));
		configMap.put(HAWK_DAEMON, (String)table.get(ENV_HAWK_DAEMON));
		configMap.put(ADMIN_USERNAME, (String)table.get(ENV_USER));
		configMap.put(ADMIN_PASSWORD, (String)table.get(ENV_PASSWORD));
		//configMap.put(ADMIN_MACHINE, (String)table.get(ENV_HOST_NAME));
		configMap.put(ADMIN_DOMAIN, (String)table.get(ENV_DOMAIN));
		configMap.put(ADMIN_HAWK, table.get(ENV_ADMIN_HAWK).toString());
		configMap.put(ENABLE_OCV, (String)table.get(OMS_ENABLE_OCV));
		
		//other relevant variable
		//configMap.put(OMS_SERVER_NAME, (String)table.get(ENV_HOST_NAME));
		
		String filePath = FileOperationUtil.getFilePath(table.get(ENV_TIBCO_HOME).toString()).replace('\\', '/');
		this.log("CONFIG FILE PATH: " + filePath);
		
		try {
			FileOperationUtil.updateXMLFile(filePath, configMap);
			UpdatePropFiles.updatePropFile(configMap);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
	
	private void printInfo(Hashtable tab){
		this.log("Below are the configuration informations: \n");
		this.log("AF_TIBCO_HOME: " + tab.get(ENV_TIBCO_HOME));
		this.log("ADMIN_HAWK: " + tab.get(ENV_ADMIN_HAWK));
		this.log("TRA_HOME: " + tab.get(ENV_TRA_HOME));
		//this.log("HOST_NAME: " + tab.get(ENV_HOST_NAME));
		this.log("DOMAIN: " + tab.get(ENV_DOMAIN));
		this.log("RV_DAEMON: " + tab.get(ENV_RV_DAEMON));
		this.log("HAWK_DAEMON: " + tab.get(ENV_HAWK_DAEMON));
		this.log("USER: " + tab.get(ENV_USER));
		this.log("PASSWORD: " + tab.get(ENV_PASSWORD));
	}
}
