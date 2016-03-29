package com.tibco.tct.fom.database;

import org.eclipse.jface.dialogs.MessageDialog;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.support.SystemConsoleProxy;
import com.tibco.customwizard.support.WizardApplicationContext;
import com.tibco.customwizard.util.SWTHelper;
import com.tibco.customwizard.xforms.IXFormActionContext;

public class PrintWarningAction implements ICustomAction{

	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		IDataModel dataMode = actionContext.getDataModel();
		boolean createTableEnable = Boolean.parseBoolean(dataMode.getValue("/fom/databaseConfig/createtablespace/createtablespaceenable"));
		//boolean createUserEnable = Boolean.parseBoolean(dataMode.getValue("/fom/databaseConfig/createuser/createuserenable"));
		String existSpace = form.getUI().getControl("existtablespacename").getXMLValue();
		if(!createTableEnable){
			String warningInfo = "You did not choice 'Create TableSpace', will use you inputted Tablespace '" + existSpace + "', please ensure it exist in current database.";
			printWarning(warningInfo);
		}
		if(createTableEnable){
			String createdSpace = dataMode.getValue("/fom/databaseConfig/createtablespace/tablespacename");
			if(!createdSpace.equals(existSpace)){
				String warningInfo = "You inputted another Tablespace '" + existSpace + "', please make sure it exist in current database.";
				printWarning(warningInfo);
			}
		}
	}

	private void printWarning(String warningInfo){
		if(isSWTMode()){
			SWTHelper.openMessage(warningInfo, MessageDialog.WARNING, false);
		}else{
			SystemConsoleProxy.println("\nWarning:\n");
			SystemConsoleProxy.println(warningInfo);
			SystemConsoleProxy.println("\n");
		}
	}
	
	private boolean isSWTMode() {
		return !WizardApplicationContext.hasArg("-consoleMode") && !WizardApplicationContext.hasArg("-silentMode")
				&& !WizardApplicationContext.hasArg("-help");
	}
}
