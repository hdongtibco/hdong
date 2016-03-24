package com.tibco.tct.admin;

import java.util.Map;
import java.util.Properties;

import com.tibco.customwizard.config.IDataModel;
import com.tibco.tct.amx.utils.MachineModelUtils;
import com.tibco.tct.amx.utils.TPCLShellsUtils;
import com.tibco.tct.framework.support.TCTDataModelLoader;
import com.tibco.tct.framework.utils.URLParser;

public class AdminDataModelLoader extends TCTDataModelLoader {
    static final String HSQL_VENDOR = "com.tibco.tpcl.org.hsqldb.feature";

    protected void processChangedProperties(Map<String, String> changedMap, Map<String, String> targetMap, IDataModel currentDataModel) throws Exception {
        for (String key : changedMap.keySet()) {
            // check if the db vendor is configurated
            Map<String, String> identifierMap = TPCLShellsUtils.getInstantiatedShellIdentifierMap(MachineModelUtils.getMachine(), TPCLShellsUtils.SHELL_TYPE_JDBC);
            if (key.endsWith("/vendor")) {
                String vendor = targetMap.get(key);
                if (vendor.contains(HSQL_VENDOR)) {
                    String hsqlVendor = HSQL_VENDOR;
                    for (String value : identifierMap.keySet()) {
                        if (value.contains(HSQL_VENDOR)) {
                            hsqlVendor = value;
                        }
                    }
                    currentDataModel.setValue(key, hsqlVendor);
                    continue;
                } else if (!identifierMap.containsKey(vendor)) {

                    String displayName =
                            TPCLShellsUtils.getShellDisplayName(TPCLShellsUtils.queryTPCLShellConfig(
                                MachineModelUtils.getMachine(),
                                TPCLShellsUtils.SHELL_TYPE_JDBC,
                                vendor));
                    throw new Exception(
                        "The loading data requires "
                                + displayName
                                + " to be configured, please use the \"Configure Third-Party Driver\" wizard to configure the database driver first.");
                }
            }

            /**
             * Modified by George 2013/3/14 fix TOOL-1436 
             * [Cailiang Fang, 2013/3/8] Repaired: TOOL-1435 [Description]
             * The "Keystore Location" of "Connection Settings" panel is incorrect when load the 3.1.5 scripts in 3.2.1.
             * [Solution] If the keystore is disable, set them value="" ;
             */
            if ("/admin/serverconnsetting/keystorelocation".equals(key)
                    || "/admin/serverconnsetting/keystorepassword".equals(key)
                    || "/admin/serverconnsetting/keystoretype".equals(key)) {
                if (!Boolean.parseBoolean(targetMap.get("/admin/serverconnsetting/autogeneratekeystore"))) {
                    currentDataModel.setValue(key, targetMap.get(key));
                }
            } else if ("/admin/credentialkeystore/keystorepassword".equals(key)
                    || "/admin/credentialkeystore/keystorelocation".equals(key)
                    || "/admin/credentialkeystore/keystoretype".equals(key)) {
                if (!Boolean.parseBoolean(targetMap.get("/admin/credentialkeystore/autogenerate"))) {
                    currentDataModel.setValue(key, targetMap.get(key));
                }
            } else if ("/admin/serverconnsetting/truststorelocation".equals(key)
                    || "/admin/serverconnsetting/truststorepassword".equals(key)) {
                // do not change the value
            } else {
                currentDataModel.setValue(key, targetMap.get(key));
            }
            
        }
        convertEMSURL(currentDataModel);
    }
    
    protected void processMissedProperties(Map<String, String> missedMap, Map<String, String> targetMap, IDataModel currentDataModel) throws Exception {
        // bypass /admin/ldaprealm/followReferrals
        missedMap.remove("/admin/ldaprealm/followReferrals");
        missedMap.remove("/admin/tibcohost/nodejvmargs");

        //fixed tool-1467  --cailiang 2013-5-16
        missedMap.remove("/admin/provisiondrivers/provisiondriver/id");
        missedMap.remove("/admin/provisiondrivers/provisiondriver/version");
        
        //fixed tool-1628  --cailiang 2013-8-1
        missedMap.remove("/admin/factoryconfig/enablnewfactory");
        missedMap.remove("/admin/factoryconfig/enablenewfactory");
        missedMap.remove("/admin/factoryconfig/connectfactoryname");
        missedMap.remove("/admin/serverconnsetting/importedCert");
        
      //fixed tool-1628  --cailiang 2013-8-15
        if("true".equals(targetMap.get("/admin/serverconnsetting/autogeneratekeystore"))){
    	    currentDataModel.setValue("/admin/serverconnsetting/importedCert", "false");
        }
        if("false".equals(targetMap.get("/admin/serverconnsetting/autogeneratekeystore"))){
            currentDataModel.setValue("/admin/serverconnsetting/importedCert", "true");
        }
        
        if(currentDataModel.getValue("/admin/factoryconfig/enablnewfactory") != null && currentDataModel.getValue("/admin/factoryconfig/enablnewfactory").length() >1){
        	if(targetMap.get("/admin/factoryconfig/enablnewfactory") != null && targetMap.get("/admin/factoryconfig/enablnewfactory") .length() >1){
        		currentDataModel.setValue("/admin/factoryconfig/enablnewfactory", targetMap.get("/admin/factoryconfig/enablnewfactory"));
        	}else{
        		currentDataModel.setValue("/admin/factoryconfig/enablnewfactory", targetMap.get("/admin/factoryconfig/enablenewfactory"));
        	}
        }else{
        	if(targetMap.get("/admin/factoryconfig/enablnewfactory") != null && targetMap.get("/admin/factoryconfig/enablnewfactory") .length() >1){
        		currentDataModel.setValue("/admin/factoryconfig/enablenewfactory", targetMap.get("/admin/factoryconfig/enablnewfactory"));
        	}else{
        		currentDataModel.setValue("/admin/factoryconfig/enablenewfactory", targetMap.get("/admin/factoryconfig/enablenewfactory"));
        	}
        }
        
        convertLDAPUrlProperty(missedMap, targetMap, currentDataModel);
        super.processMissedProperties(missedMap, targetMap, currentDataModel);
    }

    protected void convertLDAPUrlProperty(
            Map<String, String> missedMap,
            Map<String, String> targetMap,
            IDataModel currentDataModel) throws Exception {
        String convertKey = "/admin/ldaprealm/hostportlist";
        if (missedMap.remove(convertKey) != null) {
            String targetValue = targetMap.get("/admin/ldaprealm/url");
            Properties props = URLParser.parse(targetValue);
            currentDataModel.setValue(
                convertKey,
                props.getProperty(URLParser.HOST) + ":" + props.getProperty(URLParser.PORT));
        }
    }

    private void convertEMSURL(IDataModel currentDataModel) {
        Boolean isSSL = Boolean.valueOf(currentDataModel.getValue("/admin/emsconfig/enablessl"));
        String emsURL = currentDataModel.getValue("/admin/emsconfig/hostportlist");
        if (isSSL && !emsURL.startsWith("ssl:")) {
            currentDataModel.setValue("/admin/emsconfig/hostportlist", "ssl://" + emsURL);
        } else if (!isSSL && !emsURL.startsWith("tcp:")) {
            currentDataModel.setValue("/admin/emsconfig/hostportlist", "tcp://" + emsURL);
        }
    }
}
