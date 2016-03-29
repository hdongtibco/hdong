package com.tibco.tct.fom.common;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;


public class DecryptPaswdTask extends Task  {
	
	private String password;
    private String propertyName;

   public String getPassword() {
        return password;
    }

   public void setProp1(String password) {
        this.password = password;
    }

   public void setProperty(String propertyName) {
        this.propertyName = propertyName;
    }

   public void execute(){
	   try {
		   String denPassword = EncrypterDecryptorUtil.getCipher().decrypt(password);
		   setProp1(denPassword);
	   } catch (Exception e) {
		throw new BuildException(e);
	   }

       getProject().setNewProperty(this.propertyName, password);
    }
}
