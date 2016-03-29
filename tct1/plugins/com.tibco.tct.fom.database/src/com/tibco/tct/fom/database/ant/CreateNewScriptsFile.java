package com.tibco.tct.fom.database.ant;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import com.tibco.tct.fom.common.CommonUtils;

import static com.tibco.tct.fom.database.ant.ConstantsAnt.TCT_PREFIX;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_SCRIPT_DIR;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_USER_ENABLE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_ENABLE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_AF_TIBCO_HOME;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_LOCATION;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_FILENAME;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_NAME;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_MIN_SIZE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_TABLESPACE_MAX_SIZE;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_USER;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_CREATE_PASSWORD;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_EXIST_SPACENAME;
import static com.tibco.tct.fom.database.ant.ConstantsAnt.DB_EXIST_SPACESIZE;

public class CreateNewScriptsFile {
	
	private static String getScriptDir(String fomHome){
		return fomHome + DB_SCRIPT_DIR;
	}
	
	private static File createTCTScript(File file){
		String tctFileName = TCT_PREFIX + file.getName();
		File tctScriptFile = new File(file.getParentFile(), tctFileName);
		if(tctScriptFile.exists()){
			tctScriptFile.delete();
		}
		return tctScriptFile;
	}
	
	public static void generateTCTScript(Hashtable<?, ?> table) throws Exception {
		String scriptDir = getScriptDir(table.get(DB_AF_TIBCO_HOME).toString());
		String dataFilePath = (table.get(DB_CREATE_TABLESPACE_LOCATION) + "/" + table.get(DB_CREATE_TABLESPACE_FILENAME)).replace('\\', '/');
		int minsize = Integer.parseInt(table.get(DB_CREATE_TABLESPACE_MIN_SIZE)+"");
		int maxSize = Integer.parseInt(table.get(DB_CREATE_TABLESPACE_MAX_SIZE)+"");
		if(Boolean.parseBoolean(table.get(DB_CREATE_TABLESPACE_ENABLE).toString())){
			createTablespaceTCT(scriptDir + "/CreateTableSpace.sql", table.get(DB_CREATE_TABLESPACE_NAME)+"", dataFilePath, minsize, maxSize);
		}
		
		createOMSUserTCT(scriptDir + "/createOMSUser.sql", table.get(DB_CREATE_USER)+"", table.get(DB_CREATE_PASSWORD)+"", table.get(DB_EXIST_SPACENAME)+"", table.get(DB_EXIST_SPACESIZE)+"");
		createOMSDDLTCT(scriptDir + "/OMS_DDL.sql", table.get(DB_EXIST_SPACENAME)+"");
		
//		if(Boolean.parseBoolean(table.get(DB_CREATE_USER_ENABLE).toString())){
//			createOMSUserTCT(scriptDir + "/createOMSUser.sql", table.get(DB_CREATE_USER)+"", table.get(DB_CREATE_PASSWORD)+"", table.get(DB_EXIST_SPACENAME)+"", table.get(DB_EXIST_SPACESIZE)+"");
//			createOMSDDLTCT(scriptDir + "/OMS_DDL.sql", table.get(DB_EXIST_SPACENAME)+"");
//		}else{
//			if(Boolean.parseBoolean(table.get(DB_CREATE_TABLESPACE_ENABLE).toString())){
//				createOMSDDLTCT(scriptDir + "/OMS_DDL.sql", table.get(DB_CREATE_TABLESPACE_NAME)+"");
//			}
//		}
	}
	
	private static void createOMSDDLTCT(String filePath, String newTablespace) throws Exception{
		File file = new File(filePath);
		String newFileContent = getNewFileContent(CommonUtils.readFile(file), newTablespace);
		CommonUtils.writeFile(createTCTScript(file), newFileContent);
	}
	
	private static void createOMSUserTCT(String filePath, String uewUser, String newPaswd, String exitSpace, String exitSpaceSize) throws Exception{
		File file = new File(filePath);
		String newFileContent = getNewFileContent(CommonUtils.readFile(file), uewUser, newPaswd, exitSpace, exitSpaceSize);
		CommonUtils.writeFile(createTCTScript(file), newFileContent);
	}
	
	private static void createTablespaceTCT(String scriptFilePath, String tablespaceName, String dataFilePath, int minsize, int maxSize) throws Exception{
		File file = new File(scriptFilePath);
		String newFileContent =  "CREATE TABLESPACE " + tablespaceName + " DATAFILE '" + dataFilePath + "' SIZE " + minsize + "M AUTOEXTEND ON MAXSIZE " + maxSize + "M;";
		CommonUtils.writeFile(createTCTScript(file), newFileContent);
	}
	
	private static String getNewFileContent(List<String>fileContenList, String tablespaceName ){
		return CommonUtils.listToString(deleteInvalidChar(fileContenList)).replaceAll("&1", tablespaceName);
	}
	
	private static String getNewFileContent(List<String>fileContenList, String uewUser, String newPaswd, String exitSpace, String exitSpaceSize){
		String oldUser = getOldUserName(fileContenList);
		fileContenList = deleteInvalidChar(replaceUserPassword(fileContenList, newPaswd));
		return CommonUtils.listToString(replaceSpaceNameSize(fileContenList, exitSpaceSize, uewUser, exitSpace)).replaceAll(oldUser, uewUser);
	}
	
	private static List<String> deleteInvalidChar(List<String> fileContenList){
		for(int i=0; i<fileContenList.size(); i++){
			String tempLine = fileContenList.get(i).replaceAll(" +", " ").trim().toUpperCase();
			if(tempLine.startsWith("SET") || tempLine.startsWith("EXIT")){
				fileContenList.remove(i);
				fileContenList.add(i,"");
			}
		}
		return fileContenList;
	}
	
	private static List<String> replaceSpaceNameSize(List<String>fileContenList, String newSize, String uewUser, String exitSpace){
		if(fileContenList != null && !fileContenList.isEmpty()){
			for(int i=0; i<fileContenList.size(); i++){
				String tempLine = fileContenList.get(i).replaceAll(" +", " ").trim().toUpperCase();
				if(tempLine.contains("QUOTA") && tempLine.contains("ON")){
					String newLine = "ALTER USER " + uewUser + " QUOTA " + newSize + "M" + " ON " + exitSpace + ";";
					fileContenList.remove(i);
					fileContenList.add(i,newLine);
					break;
				}else if(tempLine.contains("DEFAULT")){
					String newLine = "ALTER USER " + uewUser + " DEFAULT TABLESPACE " + exitSpace + ";";
					fileContenList.remove(i);
					fileContenList.add(i,newLine);
				}
			}
		}
		return fileContenList;
	}
	
	private static String getOldUserName(List<String>fileContenList){
		if(fileContenList != null && !fileContenList.isEmpty()){
			for(int i=0; i<fileContenList.size(); i++){
				String line = fileContenList.get(i).replaceAll(" +", " ").trim();
				String tempLine = fileContenList.get(i).replaceAll(" +", " ").trim().toUpperCase();
				if(tempLine.contains("CREATE USER")){
					return line.split(" ")[2];
				}
			}
		}
		return null;
	}
	
	private static List<String> replaceUserPassword(List<String>fileContenList, String newPaswd){
		if(fileContenList != null && !fileContenList.isEmpty()){
			for(int i=0; i<fileContenList.size(); i++){
				String line = fileContenList.get(i).replaceAll(" +", " ").trim();
				String tempLine = fileContenList.get(i).replaceAll(" +", " ").trim().toUpperCase();
				if(tempLine.contains("IDENTIFIED BY")){
					int end = tempLine.indexOf("IDENTIFIED BY") + "IDENTIFIED BY".length() + 1;
					String tem = line.substring(0, end);
					line = tem + " " + newPaswd + ";";
					fileContenList.remove(i);
					fileContenList.add(i,line);
					break;
				}
			}
		}
		return fileContenList;
	}
}
