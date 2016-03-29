package com.tibco.tct.fom.database.ant;

import java.util.Hashtable;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_USER_ENABLE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_ENABLE;

public class UpdateScriptFiles extends Task{

	public void execute(){
		Hashtable<?, ?> table = getProject().getProperties();
		try {
			//if(Boolean.parseBoolean(table.get(DB_CREATE_USER_ENABLE).toString()) || Boolean.parseBoolean(table.get(DB_CREATE_TABLESPACE_ENABLE).toString())){
				CreateNewScriptsFile.generateTCTScript(table);
			//}
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
