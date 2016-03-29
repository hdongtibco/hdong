package com.tibco.tct.fom.common;

public class Constants {

	public static final String ATTRIBUTES_ID = "propname";
	
	public static final String SAVED_ATTRIBUTE = "value";
	
	//public static final String VALID_NODE_FLAG = "ConfString";
	public static final int WHICH_CHILD_NODE = 1; // The second child node.
	
	public static final String JDBC_JAR_PATH = "AF_ORACLE_PATH";
	
	//public static final String JDBC_DRIVER_NAME = "AF_ORACLE_DRIVER";
	
	public static final String JDBC_HOST = "AFRE_Events_JDBC_Host";
	
	public static final String JDBC_PORT = "AFRE_Events_JDBC_Port";
	
	public static final String JDBC_SID = "AFRE_Events_JDBC_SID";
	
	public static final String JDBC_USERNAME = "AFRE_Events_JDBC_Username";
	
	public static final String JDBC_PASSWORD = "AFRE_Events_JDBC_Password";
	
	public static final String TIBCO_HOME = "AF_TIBCO_HOME";
	
	// other relevant variable
	public static final String SOURCE_USERNAME = "com.tibco.af.oms.pooledDataSource.username";
	
	public static final String SOURCE_PASSWORD = "com.tibco.af.oms.pooledDataSource.password";
	
	public static final String HIBERNATE_CATALOG = "com.tibco.af.oms.hibernate.default_catalog";
	
	public static final String SOURCE_HOST = "com.tibco.af.oms.pooledDataSource.host";
	
	public static final String SOURCE_PORT = "com.tibco.af.oms.pooledDataSource.port";
	
	public static final String SOURCE_DATABASE = "com.tibco.af.oms.pooledDataSource.database";
	
	public static final String SOURCE_URL = "com.tibco.af.oms.pooledDataSource.url";
	
	public static final String[] NEED_CONFIG_FILES = {"ConfigValues_AFOA.xml", "ConfigValues_AFOC.xml", "ConfigValues_AOPD.xml", 
		"ConfigValues_ENV.xml", "ConfigValues_GEN.xml", "ConfigValues_JEOMS.xml", "ConfigValues_OCVC.xml", 
			"ConfigValues_OCVI.xml", "ConfigValues_OMS.xml", "ConfigValues_OPE.xml"};
	
	public static final String PROP_DIR_PATH = "configurator/properties";
	
	public static final String DELLETE_CHAR_REGEX = "[''\"]";
	
	public static final String ILLEGAL_CHAR_REGEX = "[`~!@$%^&*()+=|{}':;',\\[\\]<>/?~£¡@£¤%¡­¡­&*£¨£©¡ª¡ª+|{}¡¾¡¿¡®£»£º¡±¡°¡¯¡££¬¡¢£¿]";
	
	public static final String FILE_PROP_SEPARATOR = "~";
}
