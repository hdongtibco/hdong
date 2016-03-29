package com.tibco.tct.fom.database;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.config.IDataModel;

public class SetTablespace implements ICustomAction{

	private static boolean init = false;
	
	public void execute(IActionContext actionContext) throws Exception {
		if(!init){
			IDataModel dataMode = actionContext.getDataModel();
			boolean createTableEnable = Boolean.parseBoolean(dataMode.getValue("/fom/databaseConfig/createtablespace/createtablespaceenable"));
			if(createTableEnable){
				String createdSpace = dataMode.getValue("/fom/databaseConfig/createtablespace/tablespacename");
				String spaceMinSize = dataMode.getValue("/fom/databaseConfig/createtablespace/tablespaceminsize");
				dataMode.setValue("/fom/databaseConfig/createuser/existtablespacename", createdSpace);
				dataMode.setValue("/fom/databaseConfig/createuser/existtablespacesize", spaceMinSize);
				init = true;
			}
		}
	}
}
