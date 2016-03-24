package com.tibco.tct.admin;

import org.eclipse.jface.dialogs.MessageDialog;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.support.SystemConsoleProxy;
import com.tibco.customwizard.support.WizardApplicationContext;
import com.tibco.customwizard.util.SWTHelper;
import com.tibco.customwizard.xforms.IXFormActionContext;

public class PrintWarningAction implements ICustomAction{

	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		String timeOut = form.getUI().getControl("searchTimeOut").getXMLValue();
		String warningInfo = "When currently input 'Search Timeout' time is less than 90000ms ,it will be replaced with 90000ms when user does 'Test Connection' ";
		if(Long.parseLong(timeOut) < 90000){
			if(isSWTMode()){
				SWTHelper.openMessage(warningInfo, MessageDialog.WARNING, false);
			}else{
				SystemConsoleProxy.println("\nWarning:\n");
				SystemConsoleProxy.println(warningInfo);
				SystemConsoleProxy.println("\n");
			}
		}
	}

	private boolean isSWTMode() {
		return !WizardApplicationContext.hasArg("-consoleMode") && !WizardApplicationContext.hasArg("-silentMode")
				&& !WizardApplicationContext.hasArg("-help");
	}
}
