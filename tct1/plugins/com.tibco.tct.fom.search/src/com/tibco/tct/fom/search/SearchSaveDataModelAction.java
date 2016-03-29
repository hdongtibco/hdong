package com.tibco.tct.fom.search;

import static com.tibco.tct.fom.search.Constants.propertiesMap;

import com.tibco.customwizard.config.IDataModel;
import com.tibco.tct.framework.actions.SaveDataModelAction;

public class SearchSaveDataModelAction extends SaveDataModelAction {

	protected void prepareDataModel(IDataModel dataModel) throws Exception {
		dataModel.setValue("/fom/search/propertiesInfo",OperateTableValueUtils.getTableValue(propertiesMap, null));
	}
}
