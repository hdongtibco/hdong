package com.tibco.tct.admin;

import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;
import com.tibco.tct.amx.actions.ConnectionFactoryVisibleAction;

public class ConnectFactoryValidator implements ICustomValidator {

	public ValidateResult validate(IValidationContext validationContext) {
		String selected = ((IXFormValidationContext) validationContext).getForm().getUI().getControl("factorycontrol").getXMLValue();
		String controlValue = ((IXFormValidationContext) validationContext).getControl().getXMLValue();
		if(Boolean.parseBoolean(selected)){
			if(controlValue == null || controlValue.length() < 1){
				ConnectionFactoryVisibleAction.Reload = true;
				return new ValidateResult("First: Please ensure that the TIBCO Enterprise Messaging Service(s) you input are running. " +
						"Second: Please check whether the user that you input with appropriate permissions. " + 
						"Third: If you input multiple EMS URLs in previous page, Please ensure they have a common 'FT Connection Factory' at least.");
			}
		}
		return ValidateResult.VALID;
	}
}
