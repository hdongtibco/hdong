package com.tibco.tct.fom.search;

import java.io.File;


import org.nuxeo.xforms.ui.UIControl;
import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class PropertyFileValidator implements ICustomValidator{
	
	final String FILE_SUFFIX = ".properties"; 
	
	public ValidateResult validate(IValidationContext validationContext) {
		return validate(((IXFormValidationContext) validationContext).getControl(), validationContext.getWizardInstance()
				.getDataModel());
	}

	protected ValidateResult validate(UIControl control, IDataModel dataModel) {
		String fileStr = control.getXMLValue();      
		
		if (fileStr == null || fileStr.length() == 0 || (!fileStr.endsWith(FILE_SUFFIX))) 
			return new ValidateResult("Please select a Property File.");
		
		File file = new File(fileStr);
		if (!file.exists()) 
			return new ValidateResult("The file " + fileStr + " doesn't exist");

		
		dataModel.setValue("/fom/search/filepath", fileStr);
		return ValidateResult.VALID;
	}
}
