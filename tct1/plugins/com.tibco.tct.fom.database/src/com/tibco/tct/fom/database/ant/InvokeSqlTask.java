package com.tibco.tct.fom.database.ant;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.InstanceAlreadyExistsException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;

import com.tibco.tct.fom.common.CommonUtils;
import com.tibco.tct.fom.common.EncrypterDecryptorUtil;

//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_HOST_NAME;
//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_PORT;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_USER_ENABLE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_ENABLE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_DRIVER_PATH;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.HAS_LASTESTOJDBCDRIVER;
//import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_SID;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_URL;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_USER;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_PASSWORD;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_AF_TIBCO_HOME;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_USER;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_PASSWORD;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_SCRIPT_DIR;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.TCT_PREFIX;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.ENCR_PASWD_REGEX;

public class InvokeSqlTask extends Task {
	
	public void execute() {
		Hashtable<?, ?> table = getProject().getProperties();
		boolean createUser = Boolean.parseBoolean(table.get(DB_CREATE_USER_ENABLE).toString());
		boolean createTablespace = Boolean.parseBoolean(table.get(DB_CREATE_TABLESPACE_ENABLE).toString());
		try{
			if(createTablespace){
				createTablespace(table);
			}
			if(createUser){
				createGeneralUser(table);
			}
			//if(createUser || createTablespace){
				runOtherScript(table, getScriptFile(table, TCT_PREFIX + "OMS_DDL.sql"));
				runOtherScript(table, getScriptFile(table, "OMS_SeedData.sql"));
			//}
		}catch(Exception e){
			if (e instanceof InstanceAlreadyExistsException) {
				//
			}else{
				throw new BuildException(e.getMessage());
			}
		}
	}
	
	private SQLExec getSysSQLExecuter(Project project, Hashtable<?, ?> table){
		String driverPath = null;
		if(Boolean.parseBoolean(table.get(HAS_LASTESTOJDBCDRIVER)+"")){
		    driverPath = table.get(DB_DRIVER_PATH) + "/ojdbc7.jar";
		}else{
			driverPath = table.get(DB_DRIVER_PATH) + "/ojdbc6.jar";
	    }
		Path path = new Path(project, driverPath);
		//String dbUrl = "jdbc:oracle:thin:@" + table.get(DB_HOST_NAME) + ":" + table.get(DB_PORT) + ":" + table.get(DB_SID);
		String dbUrl = table.get(DB_URL).toString();
		
		SQLExec sqlExec = new SQLExec();
		sqlExec.setClasspath(path);
		sqlExec.setDriver("oracle.jdbc.driver.OracleDriver");
		sqlExec.setUrl(dbUrl);
		String sysUser = table.get(DB_USER).toString();
		String denPassword = "";
		try {
			denPassword = EncrypterDecryptorUtil.getCipher().decrypt(table.get(DB_PASSWORD).toString());
		} catch (Exception e) {
			throw new BuildException(e.getMessage());
		} 
		sqlExec.setUserid(sysUser);
		sqlExec.setPassword(denPassword);
		return sqlExec;
	}
	
	private File getScriptFile(Hashtable<?, ?> table, String fileName){
		String scriptsDir = table.get(DB_AF_TIBCO_HOME) + DB_SCRIPT_DIR;
		File file = new File(scriptsDir, fileName);
		return file;
	}
	
	private void printLog(File file){
		System.out.println("Invoke " + file.getAbsolutePath() + " file successfully.");
	}
	
	private void createTablespace(Hashtable<?, ?> table){
		Project project = new Project();
		SQLExec sqlExec = getSysSQLExecuter(project, table);
		File file = getScriptFile(table, TCT_PREFIX + "CreateTableSpace.sql");
		sqlExec.setSrc(file);
		sqlExec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(SQLExec.OnError.class, "abort")));
		sqlExec.setPrint(true);
		sqlExec.setProject(project);
		try{
			sqlExec.execute();
			printLog(file);
		}catch(Exception e){
			throw new BuildException(e.getMessage());
		}
	}
	
	private static File DecrptFile(File file) throws Exception{
		List<String> contentList = CommonUtils.readFile(file);
		Pattern pwd = Pattern.compile(ENCR_PASWD_REGEX);
		for(int i=0; i<contentList.size(); i++){
			String tempLine = contentList.get(i);
			if(tempLine != null && tempLine.length() > 0){
				Matcher m = pwd.matcher(tempLine);
				if(m.find()){
					contentList.remove(i);
					String paswd = m.toMatchResult().group();
					String denPassword =  EncrypterDecryptorUtil.getCipher().decrypt(paswd);
					contentList.add(i, tempLine.replaceAll("\\Q" + paswd + "\\E", denPassword));
				}
			}
		}
		CommonUtils.writeFile(file, CommonUtils.listToString(contentList));
		return file;
	}
	
	private void createGeneralUser(Hashtable<?, ?> table){
		Project project = new Project();
		SQLExec sqlExec = getSysSQLExecuter(project, table);
		try{
			File file = DecrptFile(getScriptFile(table, TCT_PREFIX + "createOMSUser.sql"));
			sqlExec.setSrc(file);
			sqlExec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(SQLExec.OnError.class, "abort")));
			sqlExec.setPrint(true);
			sqlExec.setProject(project);
			sqlExec.execute();
			printLog(file);
		}catch(Exception e){
			throw new BuildException(e.getMessage());
		}
	}
	
	private void runOtherScript(Hashtable<?, ?> table, File scriptFile){
		Project project = new Project();
		SQLExec sqlExec = null;
		//if(Boolean.parseBoolean(table.get(DB_CREATE_USER_ENABLE).toString())){
			sqlExec = getGeneralSQLExecuter(project, table);
		//}else{
		//	sqlExec = getSysSQLExecuter(project, table);
		//}
		
		sqlExec.setSrc(scriptFile);
		sqlExec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(SQLExec.OnError.class, "continue")));
		sqlExec.setPrint(true);
		sqlExec.setProject(project);
		try{
			sqlExec.execute();
			printLog(scriptFile);
		}catch(Exception e){
			throw new BuildException(e.getMessage());
		}
	}
	
	private SQLExec getGeneralSQLExecuter(Project project, Hashtable<?, ?> table){
		String driverPath = null;
		if(Boolean.parseBoolean(table.get(HAS_LASTESTOJDBCDRIVER)+"")){
		    driverPath = table.get(DB_DRIVER_PATH) + "/ojdbc7.jar";
		}else{
			driverPath = table.get(DB_DRIVER_PATH) + "/ojdbc6.jar";
	    }
		Path path = new Path(project, driverPath);
		//String dbUrl = "jdbc:oracle:thin:@" + table.get(DB_HOST_NAME) + ":" + table.get(DB_PORT) + ":" + table.get(DB_SID);
		String dbUrl = table.get(DB_URL).toString();
		
		SQLExec sqlExec = new SQLExec();
		sqlExec.setClasspath(path);
		sqlExec.setDriver("oracle.jdbc.driver.OracleDriver");
		sqlExec.setUrl(dbUrl);
		String sysUser = table.get(DB_CREATE_USER).toString();
		String denPassword = "";
		try {
			denPassword = EncrypterDecryptorUtil.getCipher().decrypt(table.get(DB_CREATE_PASSWORD).toString());
		} catch (Exception e) {
			throw new BuildException(e);
		} 
		sqlExec.setUserid(sysUser);
		sqlExec.setPassword(denPassword);
		return sqlExec;
	}
}
