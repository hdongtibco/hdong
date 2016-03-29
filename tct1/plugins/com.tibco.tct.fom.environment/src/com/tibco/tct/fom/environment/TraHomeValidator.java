package com.tibco.tct.fom.environment;

import java.io.File;
import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class TraHomeValidator implements ICustomValidator {
	
	public ValidateResult validate(IValidationContext validationContext) {
		UIControl uc = ((IXFormValidationContext) validationContext).getControl();
		String fieldName = uc.getName();
		String folderStr = uc.getXMLValue();
		if (folderStr == null || folderStr.length() == 0) {
			return new ValidateResult("Please select the " + fieldName + " Path.");
		}
		File folder = new File(folderStr);
		if (!folder.exists() || !folder.isDirectory()) {
			return new ValidateResult("The directory " + folderStr + " does not exist.");
		}
		File traFile = new File(folder,"tra.tra");
		if(!traFile.exists() || !traFile.isFile()){
			return new ValidateResult("The '" + fieldName +"' is invalid, must include 'tra.tra' file under the '" + folder.getAbsolutePath() + "' directory.");
		}
		return ValidateResult.VALID;
	}
}
