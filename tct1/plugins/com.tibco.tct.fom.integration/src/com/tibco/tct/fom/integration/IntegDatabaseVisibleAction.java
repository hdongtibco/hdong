package com.tibco.tct.fom.integration;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.tct.framework.actions.OptionAction;
import com.tibco.tct.framework.utils.TCTHelper;

public class IntegDatabaseVisibleAction extends OptionAction {

	private String controlId;

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	protected void selectionAction(boolean selected, XForm form, IActionContext actionContext) {
		UIControl control = form.getUI().getControl(controlId);
		UIControl hostname = form.getUI().getControl("hostname");
		UIControl port = form.getUI().getControl("port");
		UIControl node = form.getUI().getControl("node");
		UIControl ownerFP = form.getUI().getControl("ownerFP");
		setControlEnable(control, selected);
		setControlEnable(ownerFP, selected);
		setControlEnable(node, selected);
		setControlEnable(port, selected);
		setControlEnable(hostname, selected);
		
		TCTHelper.revalidateCurrentPage(actionContext.getWizardInstance());
	}
	
	private void setControlEnable(UIControl control, boolean selected){
		if(control != null){
			control.setEnabled(selected);
		}
	}
}
