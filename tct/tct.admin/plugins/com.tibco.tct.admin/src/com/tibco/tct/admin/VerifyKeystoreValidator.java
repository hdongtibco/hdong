package com.tibco.tct.admin;

import org.nuxeo.xforms.ui.UIControl;
import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.util.ControlUtils;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class VerifyKeystoreValidator implements ICustomValidator{

	/**
	 * [Cailiang Fang, 2012/10/17]
	 * Repaired: TOOL-1323
	 * [Description]
	 * Currently, we add a post action in the last page. If keystore was not verified
	 * and click the configure button in the last page, there will pop up a error window to tell you
	 * which page should verify the keystore and terminate the process of configuration.
	 * Now the user should be allowed to go to the next screen only after the keystore is verified.
	 * [Solution]
	 * add a validator --> 'VerifyKeystoreValidator.java' before you click 'Next' button .
	 * you must 'Verify Keystore' , and must verified success if you selected 'Fetch Keystore' .
	 */
	
	public ValidateResult validate(IValidationContext validationContext) {
		
		/**
		 * [Cailiang Fang, 2013/01/21]
		 * Repaired: TOOL-1416
		 * [Description]
		 * Caused by the fixed Tool-1323. Didn't consider carefully when repaired tool-1323.
		 */
		
		IXFormValidationContext xformValidatContext = (IXFormValidationContext) validationContext;
		XForm form = xformValidatContext.getForm();
		UIControl groupControl = form.getUI().getControl("aliasgroup");
		boolean isGroupEnable = ControlUtils.isEnabled(groupControl);
		if(isGroupEnable){
			if(validationContext.getWizardInstance().getAttribute("verifyKeystoreResult").equals("success")){
				return ValidateResult.VALID;
			}else{
				return new ValidateResult("Before the 'Next' button is enabled, you must run 'Verify Keystore' first and the verification must be successful.");
			}
		}else{
			return ValidateResult.VALID;
		}
	}
}
