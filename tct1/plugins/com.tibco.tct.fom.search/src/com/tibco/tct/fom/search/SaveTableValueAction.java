package com.tibco.tct.fom.search;

import static com.tibco.tct.fom.search.Constants.RESULT_SPLIT_CHAR;

import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.xforms.IXFormActionContext;

import static com.tibco.tct.fom.search.Constants.propertiesMap;

public class SaveTableValueAction implements ICustomAction{
	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		
		if(form.getUI().getControl("propertyTable") != null){
			String tableContent = form.getUI().getControl("propertyTable").getXMLValue();
			if(tableContent != null){
				if(tableContent.contains(RESULT_SPLIT_CHAR)){
					propertiesMap = OperateTableValueUtils.getNewestProperties(tableContent, propertiesMap );
				}
			}
			actionContext.getDataModel().setValue("/fom/search/propertiesInfo",OperateTableValueUtils.getTableValue(propertiesMap, null));
		}
	}
}