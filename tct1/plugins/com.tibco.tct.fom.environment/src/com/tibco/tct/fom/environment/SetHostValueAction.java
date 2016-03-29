package com.tibco.tct.fom.environment;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.xforms.IXFormActionContext;
import com.tibco.tct.framework.utils.TCTHelper;

public class SetHostValueAction implements ICustomAction{

	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		//String hawkStr = form.getUI().getControl("hawkdaemon").getXMLValue();
		//String rvStr = form.getUI().getControl("rvdaemon").getXMLValue();
		IDataModel dataMode = actionContext.getDataModel();
		String rvStr = dataMode.getValue("/fom/EnvConfiguration/rvdaemon");
		String hawkStr= dataMode.getValue("/fom/EnvConfiguration/hawkdaemon");
		String hostName = form.getUI().getControl("host").getXMLValue();
		
		if(hawkStr != null && rvStr != null){
			String[] hawkDaemon = hawkStr.split(":");
			String[] rvDaemon = rvStr.split(":");
			setValue(form.getUI().getControl("hawkdaemon"), hawkDaemon, hostName);
			setValue(form.getUI().getControl("rvdaemon"), rvDaemon, hostName);
			try{
				TCTHelper.revalidateCurrentPage(actionContext.getWizardInstance());
			}catch(NullPointerException e){
				// not current page.
			}
		}
	}
	
	public void setValue(UIControl uc, String[] strs, String hostName){
		
		switch(strs.length){
			case 1:
				String value1 = hostName;
				uc.setXMLValue(value1);
				break; 
			case 2:
				String value2 = strs[0] + "://" + hostName + ":";
				uc.setXMLValue(value2);
				break; 
			default:
				String value3 = strs[0] + "://" + hostName + ":" + strs[2];
				uc.setXMLValue(value3);
				break; 
		}
	}
}
