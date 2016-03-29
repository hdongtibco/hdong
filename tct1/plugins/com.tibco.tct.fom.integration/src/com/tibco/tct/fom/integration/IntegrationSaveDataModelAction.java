package com.tibco.tct.fom.integration;

import com.tibco.customwizard.config.IDataModel;
import com.tibco.tct.framework.actions.SaveDataModelAction;

public class IntegrationSaveDataModelAction extends SaveDataModelAction {

	protected void prepareDataModel(IDataModel dataModel) throws Exception {
		saveRadioValue(dataModel);
	}
	
	private void saveRadioValue(IDataModel dataModel){
		if("true".equals(dataModel.getValue("/fom/integration/isoffline"))){
			dataModel.setValue("/fom/integration/isonline", "false");
		}
		if("false".equals(dataModel.getValue("/fom/integration/isoffline"))){
			dataModel.setValue("/fom/integration/isonline", "true");
		}
	}
}
