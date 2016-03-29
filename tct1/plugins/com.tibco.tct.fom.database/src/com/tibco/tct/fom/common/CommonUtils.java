package com.tibco.tct.fom.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class CommonUtils {

	public static String listToString(List<String> list){
		StringBuffer strBuf = new StringBuffer();
		if(list != null && !list.isEmpty()){
			for(int i=0; i<list.size(); i++){
				if(list.get(i) != null && !"".equals(list.get(i))){
					strBuf.append(list.get(i)).append(getSysLineSep());
				}
			}
		}
		return strBuf.toString();
	}
	
	public static String getSysLineSep(){
		Properties props=System.getProperties();
		String OSName = props.getProperty("os.name");
		if(OSName.indexOf("Windows") != -1)
			return "\r\n";
		else
			return "\n";
	}
	
	public static byte[] readFile(String filePath) throws Exception{
		File file = new File(filePath);
		InputStream in = new FileInputStream(file);
		byte[] buff = new byte[(int) file.length()];
		in.read(buff);
		in.close();
		return buff;
	}
	
	public static void writeFile(byte[] buff, String filePath) throws Exception{
		OutputStream out = new FileOutputStream(filePath);
		out.write(buff);
		out.close();
	}
	
	public static void writeFile(File file, String fileContent) throws Exception{
		BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file));
		bufWriter.write(fileContent);
		bufWriter.close();
	}
	
	public static List<String> readFile(File file) throws Exception{
		List<String> fileList = new LinkedList<String>();
		BufferedReader bufReader = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = bufReader.readLine()) != null){
			fileList.add(line);
		}
		bufReader.close();
		return fileList;
	}
}
