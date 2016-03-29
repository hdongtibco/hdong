package com.tibco.tct.fom.environment;

import java.io.File;
import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class FOMHomeValidator implements ICustomValidator {
	
	public ValidateResult validate(IValidationContext validationContext) {
		UIControl uc = ((IXFormValidationContext) validationContext).getControl();
		String fieldName = uc.getName();
		String folderStr = uc.getXMLValue();
		if (folderStr == null || folderStr.length() == 0) {
			return new ValidateResult("Please select the " + fieldName + " Path.");
		}
		File folder = new File(folderStr);
		if (!folder.exists() || !folder.isDirectory()) {
			return new ValidateResult("The directory " + folderStr + " doesn't exist");
		}
		
		File afEMSDir = new File(folder,"ems");
		if(!afEMSDir.exists() || !afEMSDir.isDirectory()){
				return new ValidateResult("The '" + fieldName + "' is invalid, in the current directory must include 'ems' directory.");
		}
		File afConfigDir = new File(folder,"config");
		if(!afConfigDir.exists() || !afConfigDir.isDirectory()){
			return new ValidateResult("The '" + fieldName + "' is invalid, in the current directory must include 'config' directory.");
		}
		File afDBDir = new File(folder,"db/oracle/oms");
		if(!afDBDir.exists() || !afDBDir.isDirectory()){
			return new ValidateResult("The '" + fieldName + "' is invalid, in the current directory must include 'db/oracle/oms' directory.");
		}
		
		return ValidateResult.VALID;
	}
}
