package com.tibco.tct.fom.common;

import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.util.ControlUtils;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;
import com.tibco.tct.framework.utils.URLParser;

public class DBUrlValidator implements ICustomValidator {
	public ValidateResult validate(IValidationContext validationContext) {
		UIControl urlControl = ((IXFormValidationContext) validationContext).getControl();
		if (ControlUtils.isEnabled(urlControl)) {
			String url = urlControl.getXMLValue();
			try {
				URLParser.parse(url);
			} catch (Exception e) {
				return new ValidateResult(e.getMessage());
			}
		}
		return ValidateResult.VALID;
	}
}
