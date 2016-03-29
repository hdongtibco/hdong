package com.tibco.tct.fom.database;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.util.ControlUtils;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class MaxBigThanMinValidator implements ICustomValidator {

	public ValidateResult validate(IValidationContext validationContext) {
		XForm form = ((IXFormValidationContext) validationContext).getForm();
		UIControl uc = ((IXFormValidationContext)validationContext).getControl();
		if(ControlUtils.isEnabled(uc)){
			String fieldName = uc.getName();
			String value = uc.getXMLValue();
			if (value == null || value.equals("") || !value.matches("[0-9]*")) {
				return new ValidateResult(fieldName + " The value should be an integer.");
			}
			long size = Long.parseLong(value);
			if (size <= 0 || size > 10240) {
				return new ValidateResult(fieldName + " The value should be between 1 to 10240 .");
			}
			if(!isMaxBigThanMin(form)){
				return new ValidateResult("Maximum value cann't be smaller than the minimum");
			}
		}
		return ValidateResult.VALID;
	}
	
	private boolean isMaxBigThanMin(XForm form){
		String maxStr = form.getUI().getControl("tablespacemaxsize").getXMLValue().replaceAll(" ", "");
		String minStr = form.getUI().getControl("tablespaceminsize").getXMLValue().replaceAll(" ", "");
		if(minStr != null && maxStr != null && !"".equalsIgnoreCase(minStr) && !"".equals(maxStr)){
			int max = Integer.parseInt(maxStr);
			int min = Integer.parseInt(minStr);
			if(max < min){
				return false;
			}
		}
		return true;
	}
}
