package com.tibco.tct.fom.database;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.xforms.IXFormActionContext;

public class ChangeDBUrlAction implements ICustomAction {

	private final String OTHER_VERSION_ORACLE_URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521)))(CONNECT_DATA=(SID=orcl)(SERVER=dedicated)))";
	private final String ORACLE_12C_URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=orcl)(SERVER=dedicated)))";

	@Override
	public void execute(IActionContext actionContext) throws Exception {

		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		String dbVersion = xformActionContext.getForm().getUI().getControl("dbVersion").getXMLValue();
		
		if (dbVersion.equals("Other versions of oracle")) {
			xformActionContext.getForm().getUI().getControl("dburl").setXMLValue(OTHER_VERSION_ORACLE_URL);
		} 
		
		if (dbVersion.equals("Oracle 12c")) {
			xformActionContext.getForm().getUI().getControl("dburl").setXMLValue(ORACLE_12C_URL);
		} 
	}
}
