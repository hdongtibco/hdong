package com.tibco.tct.fom.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

import static com.tibco.tct.fom.common.Constants.ILLEGAL_CHAR_REGEX;

public class IllegalCharValidator implements ICustomValidator {
	
	public ValidateResult validate(IValidationContext validationContext) {
		return validate(((IXFormValidationContext) validationContext).getControl(), validationContext.getWizardInstance()
				.getDataModel());
	}
	
	protected ValidateResult validate(UIControl control, IDataModel dataModel) {
		String str = control.getXMLValue();
		String fieldName = control.getName();
		if(str != null){
			Pattern pt = Pattern.compile(ILLEGAL_CHAR_REGEX);
			Matcher mt = pt.matcher(str);
			if(mt.find()){
				return new ValidateResult(fieldName + " Illegal Character " + mt.toMatchResult().group());
			}
		}
		return ValidateResult.VALID;
	}
}
