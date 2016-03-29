package com.tibco.tct.fom.database;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.util.ControlUtils;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class MinValueValidator implements ICustomValidator {

	public ValidateResult validate(IValidationContext validationContext) {
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
			
			/*XForm form = ((IXFormValidationContext) validationContext).getForm();
			form.getUI().getControl("tablespacemaxsize").setXMLValue((size+10)+"");*/
		}
		return ValidateResult.VALID;
	}
}
