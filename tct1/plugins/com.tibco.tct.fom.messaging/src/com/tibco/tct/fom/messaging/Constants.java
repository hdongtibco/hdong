package com.tibco.tct.fom.messaging;

public class Constants {

	public static final String EMS_MACHINE = "AF_EMS_MACHINE";
	
	public static final String EMS_PORT = "AF_EMS_PORT";
	
	public static final String EMS_USERNAME = "AF_EMS_USERNAME";
	
	public static final String EMS_PASSWORD = "AF_EMS_PASSWORD";
	
	public static final String EMS_HOME = "AF_EMS_HOME";
	
	public static final String JNDI_URL = "com.tibco.af.oms.jms.jndi.url";       // in *_OMS.xml and *_JEOMS.xml
	public static final String JNDI_USERNAME_OMS = "com.tibco.af.oms.jms.jndi.security.principal"; 
	public static final String JNDI_PASSWORD_OMS = "com.tibco.af.oms.jms.jndi.security.credentials"; 
	
	public static final String JNDI_URL_AOPD = "com.tibco.af.aopd.jms.jndi.url";  // in *_AOPD.xml
	public static final String JNDI_USERNAME_AOPD = "com.tibco.af.aopd.jms.jndi.security.principal"; 
	public static final String JNDI_PASSWORD_AOPD = "com.tibco.af.aopd.jms.jndi.security.credentials";
	
	public static final String JNDI_URL_OPE = "com.tibco.af.ope.jms.jndi.url";  // in *_OPE.xml
	public static final String JNDI_USERNAME_OPE = "com.tibco.af.ope.jms.jndi.security.principal"; 
	public static final String JNDI_PASSWORD_OPE = "com.tibco.af.ope.jms.jndi.security.credentials";
	
	public static final String JMS_URL = "AF_JMS_Url";
	
	public static final String EMS_URLS = "AF_EMS_SERVER";
	
	public static final String TIBCO_HOME = "AF_TIBCO_HOME";
	
	// Log4j file
	public static final String PROVIDER_URL_LOG4J = "ProviderURL";
	public static final String EMS_USERNAMEL_LOG4J = "UserName";
	public static final String EMS_PASSWORD_LOG4J = "Password";
	public static final String EMS_SECURITY_PRINCIPAL_NAME = "SecurityPrincipalName";
	public static final String EMS_SECURITY_CREDENTIALS = "SecurityCredentials";
	
	public static final String[] NEED_CONFIG_FILES = { "AOPDLog4j.xml", "JeoMSLog4j.xml", "OMSServerLog4j.xml", "OPELog4j.xml" };
}
