package com.tibco.tct.fom.database;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.tct.framework.actions.OptionAction;
import com.tibco.tct.framework.utils.TCTHelper;

public class DatabaseVisibleAction extends OptionAction {

	private String controlId;

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	protected void selectionAction(boolean selected, XForm form, IActionContext actionContext) {
		UIControl control = form.getUI().getControl(controlId);
		UIControl createdUsernamecontrol = form.getUI().getControl("createdusername");
		UIControl createdUserpasswordcontrol = form.getUI().getControl("createduserpassword");
		UIControl existtablespaceControl = form.getUI().getControl("existtablespacename");
		UIControl existtablespacesizeControl = form.getUI().getControl("existtablespacesize");
		
		
		UIControl tablespaceNamecontrol = form.getUI().getControl("tablespacename");
		UIControl tablespaceMinSizecontrol = form.getUI().getControl("tablespaceminsize");
		UIControl tablespaceMaxSizecontrol = form.getUI().getControl("tablespacemaxsize");
		UIControl tablespaceLocationcontrol = form.getUI().getControl("tablespacelocation");
		UIControl tablespacedbfilename = form.getUI().getControl("tablespacedbfilename");
		
		if("group: createuser".equals(control.getName())){
			setControlEnable(control, true);
		}else{
			setControlEnable(control, selected);
		}
		
		setControlEnable(createdUsernamecontrol, true);
		setControlEnable(createdUserpasswordcontrol, true);
		setControlEnable(existtablespaceControl, true);
		setControlEnable(existtablespacesizeControl, selected);
		
		setControlEnable(tablespaceNamecontrol, selected);
		setControlEnable(tablespaceMinSizecontrol, selected);
		setControlEnable(tablespaceMaxSizecontrol, selected);
		setControlEnable(tablespaceLocationcontrol, selected);
		setControlEnable(tablespacedbfilename, selected);
	
		TCTHelper.revalidateCurrentPage(actionContext.getWizardInstance());
	}
	
	private void setControlEnable(UIControl control, boolean selected){
		if(control != null){
			control.setEnabled(selected);
		}
	}
}
