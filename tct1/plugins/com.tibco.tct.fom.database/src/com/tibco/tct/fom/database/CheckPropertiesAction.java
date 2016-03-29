package com.tibco.tct.fom.database;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.config.IDataModel;

public class CheckPropertiesAction implements ICustomAction{

	public void execute(IActionContext actionContext) throws Exception {
		IDataModel dataMode = actionContext.getDataModel();
		boolean createUserEnable = Boolean.parseBoolean(dataMode.getValue("/fom/databaseConfig/createuser/createuserenable"));
		boolean createTableEnable = Boolean.parseBoolean(dataMode.getValue("/fom/databaseConfig/createtablespace/createtablespaceenable"));
		if(createUserEnable || createTableEnable){
			dataMode.setValue("/fom/databaseConfig/runscript", "true");
		}
	}
}
