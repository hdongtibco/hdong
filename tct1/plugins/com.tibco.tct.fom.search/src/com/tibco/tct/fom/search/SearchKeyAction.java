package com.tibco.tct.fom.search;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.xforms.IXFormActionContext;

import static com.tibco.tct.fom.search.Constants.propertiesMap;

public class SearchKeyAction implements ICustomAction {

	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		UIControl inputKeyControl = form.getUI().getControl("inputKey");
		String inputedKey = inputKeyControl.getXMLValue();
		String tableXMLValue = OperateTableValueUtils.getTableValue(propertiesMap, inputedKey);
		form.getUI().getControl("propertyTable").setXMLValue(tableXMLValue);
		actionContext.getDataModel().setValue("/fom/search/propertiesInfo",tableXMLValue);
	}
}
