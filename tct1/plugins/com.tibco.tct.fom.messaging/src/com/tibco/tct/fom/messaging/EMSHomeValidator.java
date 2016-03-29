package com.tibco.tct.fom.messaging;

import java.io.File;

import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class EMSHomeValidator implements ICustomValidator {
	
	public ValidateResult validate(IValidationContext validationContext) {
		UIControl uc = ((IXFormValidationContext) validationContext).getControl();
		String fieldName = uc.getName();
		String folderStr = uc.getXMLValue();
		if (folderStr == null || folderStr.length() == 0) {
			return new ValidateResult(
					"Please select the " + fieldName + " Path.");
		}
		File folder = new File(folderStr);
		if (!folder.exists() || !folder.isDirectory()) {
			return new ValidateResult("The directory " + folderStr + " doesn't exist");
		}
		
		File emsHomeFolder = new File(folder, "bin");
		if (!emsHomeFolder.exists() || !emsHomeFolder.isDirectory()) {
			return new ValidateResult("The EMS Home " + folder + " is invalid. In the current directory, the 'bin' directory must exist!");
		}
		
		return ValidateResult.VALID;
	}
}
