package com.tibco.tct.admin;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.xforms.IXFormActionContext;

public class SetFactoryCheckBoxAction implements ICustomAction {

	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		IDataModel dataModel = actionContext.getDataModel();
		String savedEMSUrl = dataModel.getValue("/admin/emsconfig/hostportlist");
		UIControl factoryControl = form.getUI().getControl("factorycontrol");
		//Cailiang 2013-8-17  Modified to fixed tool-1648
		String isSelected = factoryControl.getXMLValue();
		if(savedEMSUrl.split(",").length > 1){
			factoryControl.setXMLValue("true");
			factoryControl.setEnabled(false);
		}else{
			factoryControl.setXMLValue(isSelected);
			factoryControl.setEnabled(true);
		}
	}

}
