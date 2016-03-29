package com.tibco.tct.fom.common;

import java.io.File;

import com.tibco.customwizard.action.ActionException;
import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.tct.framework.support.TCTContext;
import com.tibco.tct.framework.utils.TCTHelper;

public class GetFOMHomeAction implements ICustomAction{

	public void execute(IActionContext actionContext) throws Exception {
		actionContext.getDataModel().setValue("/fom/CommonConfiguration/tibcofomhome", getFOMHome(actionContext));
	}
	
	private String getFOMHome(IActionContext actionContext) throws Exception{
		String currentProductVersion = actionContext.getWizardInstance().getWizardConfig().getProductVersion();
		String TIBCOhome = TCTContext.getInstance().getTibcoHome();
		String AFHomePath = TCTHelper.toUnixPath(TIBCOhome + "/af");
		String FOMHomePath = TCTHelper.toUnixPath(TIBCOhome + "/af/" + currentProductVersion);
		return isValidFomHome(AFHomePath,FOMHomePath, actionContext);
	}
	
	private String isValidFomHome(String AFHomePath,String FOMHomePath, IActionContext actionContext) throws Exception{
		
		if (FOMHomePath == null || FOMHomePath.length() == 0) {
			throw new ActionException(TCTHelper.getMessageProvider(actionContext.getWizardInstance()).getMessage("fom.home.null"));
		}
		
		File folder = new File(FOMHomePath);
		//String correctVersion = isCorrectFormat(AFHomePath);
		String correctVersion = FOMHomePath.substring(FOMHomePath.lastIndexOf("/")+1);
		if (!(folder.exists() && folder.isDirectory()) || correctVersion==null) {
			throw new ActionException(TCTHelper.getMessageProvider(actionContext.getWizardInstance()).getMessage("fom.home.not.exist", FOMHomePath));
		}
		else
		{
			folder = new File(AFHomePath+"/"+correctVersion);
		}

		File afEMSDir = new File(folder,"ems");
		if(!afEMSDir.exists() || !afEMSDir.isDirectory()){
			throw new ActionException(TCTHelper.getMessageProvider(actionContext.getWizardInstance()).getMessage("fom.ems.not.exist", FOMHomePath));
		}
		
		File afConfigDir = new File(folder,"config");
		if(!afConfigDir.exists() || !afConfigDir.isDirectory()){
			throw new ActionException(TCTHelper.getMessageProvider(actionContext.getWizardInstance()).getMessage("fom.config.not.exist", FOMHomePath));
		}
		
		File afDBDir = new File(folder,"db/oracle/oms");
		if(!afDBDir.exists() || !afDBDir.isDirectory()){
			throw new ActionException(TCTHelper.getMessageProvider(actionContext.getWizardInstance()).getMessage("fom.db.not.exist", FOMHomePath));
		}
		
		return folder.getAbsolutePath();
	}

	/*private String isCorrectFormat(String aFHomePath) {
		String correctFileName = null;
		File file = new File(aFHomePath);
		String[] fileNameArray = file.list();
		for(String fileName : fileNameArray)
		{
			if(isCorrect(fileName))
			{
				correctFileName = fileName;
			    break;
			}
		}
		return correctFileName;
	}

	private boolean isCorrect(String fileName) {
		return fileName.matches("\\d.\\d");
	}*/
}
