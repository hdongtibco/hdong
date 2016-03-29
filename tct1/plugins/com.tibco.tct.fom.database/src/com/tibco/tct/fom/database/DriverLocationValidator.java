package com.tibco.tct.fom.database;

import java.io.File;

import org.nuxeo.xforms.ui.UIControl;

import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.validator.ICustomValidator;
import com.tibco.customwizard.validator.IValidationContext;
import com.tibco.customwizard.validator.ValidateResult;
import com.tibco.customwizard.xforms.IXFormValidationContext;

public class DriverLocationValidator implements ICustomValidator {
	public ValidateResult validate(IValidationContext validationContext) {
		return validate(((IXFormValidationContext) validationContext).getControl(), validationContext.getWizardInstance()
				.getDataModel());
	}

	protected ValidateResult validate(UIControl control, IDataModel dataModel) {
		String folderStr = control.getXMLValue();
		String fieldName = control.getName();
		if (folderStr == null || folderStr.length() == 0) {
			return new ValidateResult(fieldName + " The JDBC Driver Folder Path can't be empty.");
		}
		File folder = new File(folderStr);
		if(!folder.exists() || !folder.isDirectory()){
			return new ValidateResult(fieldName + " There is no such directory.");
		}
		if(!(new File(folder,"ojdbc6.jar").exists()||new File(folder,"ojdbc7.jar").exists())){
			return new ValidateResult(fieldName + " Can't find the 'ojdbc6.jar' or 'ojdbc7.jar' file in this directory.");
		}
		if(new File(folder,"ojdbc7.jar").exists()){
			dataModel.setValue("/fom/databaseConfig/hasLastestOjdbcDriver","true");
		}else{
			dataModel.setValue("/fom/databaseConfig/hasLastestOjdbcDriver","false");
		}
		return ValidateResult.VALID;
	}
}
