package com.tibco.tct.fom.messaging.ant;

import static com.tibco.tct.fom.messaging.Constants.TIBCO_HOME;
import static com.tibco.tct.fom.messaging.Constants.EMS_MACHINE;
import static com.tibco.tct.fom.messaging.Constants.EMS_PORT;
import static com.tibco.tct.fom.messaging.Constants.EMS_USERNAME;
import static com.tibco.tct.fom.messaging.Constants.EMS_PASSWORD;
import static com.tibco.tct.fom.messaging.Constants.EMS_HOME;
import static com.tibco.tct.fom.messaging.Constants.JNDI_URL;
import static com.tibco.tct.fom.messaging.Constants.JMS_URL;
import static com.tibco.tct.fom.messaging.Constants.EMS_URLS;
import static com.tibco.tct.fom.messaging.Constants.JNDI_URL_AOPD;
import static com.tibco.tct.fom.messaging.Constants.PROVIDER_URL_LOG4J;
import static com.tibco.tct.fom.messaging.Constants.EMS_USERNAMEL_LOG4J;
import static com.tibco.tct.fom.messaging.Constants.EMS_PASSWORD_LOG4J;
import static com.tibco.tct.fom.messaging.Constants.NEED_CONFIG_FILES;
import static com.tibco.tct.fom.messaging.Constants.JNDI_USERNAME_OMS;
import static com.tibco.tct.fom.messaging.Constants.JNDI_PASSWORD_OMS;
import static com.tibco.tct.fom.messaging.Constants.JNDI_USERNAME_AOPD;
import static com.tibco.tct.fom.messaging.Constants.JNDI_PASSWORD_AOPD;
import static com.tibco.tct.fom.messaging.Constants.JNDI_URL_OPE;
import static com.tibco.tct.fom.messaging.Constants.JNDI_USERNAME_OPE;
import static com.tibco.tct.fom.messaging.Constants.JNDI_PASSWORD_OPE;
import static com.tibco.tct.fom.messaging.Constants.EMS_SECURITY_PRINCIPAL_NAME;
import static com.tibco.tct.fom.messaging.Constants.EMS_SECURITY_CREDENTIALS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.tibco.tct.fom.common.UpdateLog4jFiles;
import com.tibco.tct.fom.common.UpdatePropFiles;
import com.tibco.tct.fom.common.FileOperationUtil;
import com.tibco.tct.fom.common.EncrypterDecryptorUtil;

public class UpdateConfigFile extends Task {
	
	public void execute(){
		Hashtable<?, ?> table = getProject().getProperties();
		Map<String,String>configMap = new HashMap<String,String>();
		printInfo(table);
		String emsUrls = table.get("mess.ems.servers").toString();
		String decryptPassword = null;
		try {
			decryptPassword = EncrypterDecryptorUtil.getCipher().decrypt((String)table.get("mess.ems.password"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		configMap.put(EMS_URLS, emsUrls);
		configMap.put(EMS_MACHINE, getEMSProps(emsUrls, "host"));
		configMap.put(EMS_PORT, getEMSProps(emsUrls, "port"));
		configMap.put(EMS_USERNAME, (String)table.get("mess.ems.user"));
		configMap.put(EMS_PASSWORD, (String)table.get("mess.ems.password"));
		configMap.put(EMS_HOME, (String)table.get("mess.ems.home"));
		configMap.put(TIBCO_HOME, (String)table.get("fom.tibco.home"));
		
		configMap.put(JNDI_URL, getJNDIUrl(emsUrls));
		configMap.put(JMS_URL, getJNDIUrl(emsUrls));
		configMap.put(JNDI_URL_AOPD, getJNDIUrl(emsUrls));
		configMap.put(JNDI_URL_OPE, getJNDIUrl(emsUrls));
		configMap.put(PROVIDER_URL_LOG4J, emsUrls);
		configMap.put(EMS_USERNAMEL_LOG4J, (String)table.get("mess.ems.user"));
		configMap.put(EMS_PASSWORD_LOG4J, (String)table.get("mess.ems.password"));
		configMap.put(EMS_SECURITY_PRINCIPAL_NAME, (String)table.get("mess.ems.user"));
		configMap.put(EMS_SECURITY_CREDENTIALS, decryptPassword);
		
		configMap.put(JNDI_USERNAME_OMS, (String)table.get("mess.ems.user"));
		configMap.put(JNDI_PASSWORD_OMS, (String)table.get("mess.ems.password"));
		
		configMap.put(JNDI_USERNAME_AOPD, (String)table.get("mess.ems.user"));
		configMap.put(JNDI_PASSWORD_AOPD, (String)table.get("mess.ems.password"));
		
		configMap.put(JNDI_USERNAME_OPE, (String)table.get("mess.ems.user"));
		configMap.put(JNDI_PASSWORD_OPE, (String)table.get("mess.ems.password"));
		String filePath = FileOperationUtil.getFilePath((String) table.get("fom.tibco.home"));
		try {
			FileOperationUtil.updateXMLFile(filePath, configMap);
			UpdatePropFiles.updatePropFile(configMap);
			UpdateLog4jFiles.updateLog4jFile(filePath, "//appender/param", "name", "value", configMap, Arrays.asList(NEED_CONFIG_FILES));
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
	
	private String getJNDIUrl(String emsurls){
		String jndiUrls = emsurls.replaceAll("\\btcp://|\\bssl://", "tibjmsnaming://");
		return jndiUrls;
	}
	
	private String getEMSProps(String emsUrls, String propName){
		String[] urls = emsUrls.split(",");
		return getEMSProp(urls[0], propName);
	}
	
	private String getEMSProp(String emsUrl, String propName){
		emsUrl = emsUrl.replaceAll("\\Q//\\E", "");
		String[] props = emsUrl.split(":");
		if("host".equalsIgnoreCase(propName)){
			return props[1];
		}
		if("port".equalsIgnoreCase(propName)){
			return props[2];
		}
		return null;
	}
	
	private void printInfo(Hashtable tab){
		this.log("Below are the configuration informations: \n");
		this.log("AF_TIBCO_HOME: " + tab.get("fom.tibco.home"));
		this.log("EMS_SERVERS: " + tab.get("mess.ems.servers"));
		this.log("EMS_USER_NAME: " + tab.get("mess.ems.user"));
		this.log("EMS_PASSWORD: " + tab.get("mess.ems.password"));
		this.log("EMS_HOME: " + tab.get("mess.ems.home"));
	}
}
