package com.tibco.tct.fom.environment;

import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.xforms.IXFormActionContext;

public class SaveAction implements ICustomAction{

	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		xformActionContext.getDataModel().setValue("/fom/EnvConfiguration/enableocv", form.getUI().getControl("enableocv").getXMLValue());
	}
}
