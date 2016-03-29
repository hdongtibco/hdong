package com.tibco.tct.fom.oms;

import static com.tibco.tct.fom.oms.Constants.TIBCO_HOME;
import static com.tibco.tct.fom.oms.Constants.HTTP_PORT;
import static com.tibco.tct.fom.oms.Constants.WS_PORT;
import static com.tibco.tct.fom.oms.Constants.OMS_SERVER_PORT;
import static com.tibco.tct.fom.oms.Constants.ADMIN_MACHINE;
import static com.tibco.tct.fom.oms.Constants.OMS_SERVER_NAME;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.tibco.tct.fom.common.UpdatePropFiles;
import com.tibco.tct.fom.common.FileOperationUtil;

public class UpdateOMSConfigFile extends Task {

	final String OMS_HTTP_PORT = "oms.http.port";
	final String OMS_WS_PORT = "oms.ws.port";
	final String ENV_TIBCO_HOME = "fom.tibco.home";
	final String FOM_OMS_HOST = "fom.oms.host";
	public void execute(){
		Hashtable<?, ?> table = getProject().getProperties();
		printInfo(table);
		Map<String,String>configMap = new HashMap<String,String>();
		configMap.put(HTTP_PORT, (String)table.get(OMS_HTTP_PORT));
		configMap.put(WS_PORT, (String)table.get(OMS_WS_PORT));
		configMap.put(TIBCO_HOME, (String)table.get(ENV_TIBCO_HOME));
		configMap.put(OMS_SERVER_PORT, (String)table.get(OMS_HTTP_PORT));
		configMap.put(ADMIN_MACHINE, (String)table.get(FOM_OMS_HOST));
		configMap.put(OMS_SERVER_NAME, (String)table.get(FOM_OMS_HOST));
		
		String filePath = FileOperationUtil.getFilePath((String) table.get(ENV_TIBCO_HOME));
		try {
			FileOperationUtil.updateXMLFile(filePath, configMap);
			UpdatePropFiles.updatePropFile(configMap);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
	
	private void printInfo(Hashtable tab){
		this.log("Below are the configuration informations: \n");
		this.log("OMS_HTTP_PORT: " + tab.get(OMS_HTTP_PORT));
		this.log("OMS_WS_PORT: " + tab.get(OMS_WS_PORT));
		this.log("FOM_OMS_HOST: " + tab.get(FOM_OMS_HOST));
	}
}
