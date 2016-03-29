package com.tibco.tct.fom.environment;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class HostSameValidator implements ICustomValidator {
	
	public ValidateResult validate(IValidationContext validationContext) {
		UIControl uc = ((IXFormValidationContext) validationContext).getControl();
		String fieldName = uc.getName();
		String value = uc.getXMLValue();
		if (value == null || value.trim().length() == 0) {
			return new ValidateResult(fieldName + " must be nonempty.");
		}
		
		XForm form = ((IXFormValidationContext) validationContext).getForm();
		String hostValue = form.getUI().getControl("host").getXMLValue();
		String rvHost = value.split(":")[1];
		if(rvHost.startsWith("//")){
			rvHost = rvHost.substring(2);
		}
		if(!rvHost.equals(hostValue)){
			return new ValidateResult("'" + fieldName + "'" + " The host value must as same as FOM Host");
		}
		return ValidateResult.VALID;
	}
}
