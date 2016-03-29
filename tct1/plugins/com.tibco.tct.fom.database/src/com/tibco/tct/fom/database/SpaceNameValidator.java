package com.tibco.tct.fom.database;

import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.util.ControlUtils;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class SpaceNameValidator implements ICustomValidator {

	public ValidateResult validate(IValidationContext validationContext) {
		UIControl uc = ((IXFormValidationContext)validationContext).getControl();
		if(ControlUtils.isEnabled(uc)){
			String value = uc.getXMLValue();
			if(value.length() > 30){
				return new ValidateResult("The Tablespace Name must be within 30 characters.");
			}
			if (!value.matches("^[a-zA-Z]{1}[\\s\\S]*")) {
				return new ValidateResult("The Tablespace Name must start with letter.");
			}
		}
		return ValidateResult.VALID;
	}
}
