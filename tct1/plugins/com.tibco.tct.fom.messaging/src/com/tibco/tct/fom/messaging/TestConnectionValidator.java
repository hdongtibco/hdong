package com.tibco.tct.fom.messaging;

import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;

public class TestConnectionValidator implements ICustomValidator {

	public ValidateResult validate(IValidationContext validationContext) {

		if (validationContext.getWizardInstance().getAttribute("testResult") == null) {
			// return ValidateResult.VALID;
			return new ValidateResult(
					"Before you click the 'Next' button,you must 'Test Connection' first,and must test success.");
		}

		if (validationContext.getWizardInstance().getAttribute("testResult")
				.equals("success")) {
			return ValidateResult.VALID;
		} else {
			return new ValidateResult(
					"Before you click the 'Next' button,you must 'Test Connection' first,and must test success.");
		}
	}
}
