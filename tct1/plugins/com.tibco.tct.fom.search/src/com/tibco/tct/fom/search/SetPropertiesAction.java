package com.tibco.tct.fom.search;

import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.xforms.IXFormActionContext;

import static com.tibco.tct.fom.search.Constants.propertiesMap;

public class SetPropertiesAction implements ICustomAction {

    public void execute(IActionContext actionContext) throws Exception {
    	IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
    	IDataModel dataModel = actionContext.getDataModel();
    	String oldPath = dataModel.getValue("/fom/search/previousfilepath");
    	String newPath = form.getUI().getControl("filepath").getXMLValue();
    	if(!newPath.equals(oldPath)){
    		dataModel.setValue("/fom/search/isInit", "false");
    	}
    	dataModel.setValue("/fom/search/previousfilepath", newPath);
    	if(!Boolean.valueOf(dataModel.getValue("/fom/search/isInit"))){
    		propertiesMap = OperateTableValueUtils.changePropertiesToMap(newPath);
        	dataModel.setValue("/fom/search/propertiesInfo", OperateTableValueUtils.getTableValue(propertiesMap, null));
        	dataModel.setValue("/fom/search/isInit", "true");
    	}
    }
}
