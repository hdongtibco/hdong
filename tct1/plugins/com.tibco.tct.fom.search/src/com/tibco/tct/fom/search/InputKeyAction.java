package com.tibco.tct.fom.search;

import java.util.Map;

import static com.tibco.tct.fom.search.Constants.COLUMN_SPLIT_CHAR;
import static com.tibco.tct.fom.search.Constants.RESULT_SPLIT_CHAR;
import static com.tibco.tct.fom.search.Constants.ROW_SPLIT_CHAR;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.xforms.IXFormActionContext;

import static com.tibco.tct.fom.search.Constants.propertiesMap;

public class InputKeyAction implements ICustomAction {

	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		
		UIControl inputKeyControl = form.getUI().getControl("inputKey");
		String inputedKey = inputKeyControl.getXMLValue();
		
		String tableContent = form.getUI().getControl("propertyTable").getXMLValue();
		if(tableContent != null){
			if(tableContent.contains(RESULT_SPLIT_CHAR)){
				propertiesMap = getNewestProperties(tableContent, propertiesMap);
			}
		}
		
		String tableXMLValue = OperateTableValueUtils.getTableValue(propertiesMap, inputedKey);
		form.getUI().getControl("propertyTable").setXMLValue(tableXMLValue);
		actionContext.getDataModel().setValue("/fom/search/propertiesInfo",tableXMLValue);
	}
	
	public Map<String, String> getNewestProperties(String tableContent, Map<String, String> PropertiesMap){
		int selectedRow = Integer.valueOf(tableContent.substring(tableContent.lastIndexOf(RESULT_SPLIT_CHAR)+1));
		String[] rows = tableContent.substring(0, tableContent.lastIndexOf(RESULT_SPLIT_CHAR)).split(ROW_SPLIT_CHAR);
		String[] cells = rows[selectedRow+1].split(COLUMN_SPLIT_CHAR);
		String selectedKey = cells[0];
		PropertiesMap.remove(selectedKey);
		PropertiesMap.put(selectedKey, cells[1]);
		return PropertiesMap;
	}
}
