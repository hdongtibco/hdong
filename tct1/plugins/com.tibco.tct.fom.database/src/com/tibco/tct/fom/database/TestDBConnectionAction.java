package com.tibco.tct.fom.database;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.ActionException;
import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.config.IDataModel;
import com.tibco.customwizard.instance.PageInstance;
import com.tibco.customwizard.instance.WizardInstance;
import com.tibco.customwizard.support.IBackgroundAction;
import com.tibco.customwizard.util.WizardHelper;
import com.tibco.customwizard.util.XFormUtils;
import com.tibco.customwizard.xforms.IXFormActionContext;
import com.tibco.tct.framework.support.IMessageProvider;
import com.tibco.tct.framework.support.TCTContext;
import com.tibco.tct.framework.utils.TCTHelper;


public class TestDBConnectionAction implements ICustomAction, IBackgroundAction {

	private static Method dbConnectionTester;
	
	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		WizardInstance wizardInstance = actionContext.getWizardInstance();
		IMessageProvider messageProvider = TCTHelper.getMessageProvider(wizardInstance);
		IDataModel dataModel = actionContext.getDataModel();
		try {
			testDBConnection(dataModel, form, messageProvider);
			WizardHelper.openMessage(wizardInstance, messageProvider.getMessage("testconnection.success"));
		} catch (Exception e) {
			WizardHelper.openErrorDialog(wizardInstance, e);
		}
	}

	public void testConnection(WizardInstance wizardInstance, String pageId) throws Exception {
		PageInstance pageInstance = WizardHelper.getPageInstanceById(wizardInstance, pageId);
		if(pageInstance!=null){
			XForm form = XFormUtils.getXForm(pageInstance);
			IMessageProvider messageProvider = TCTHelper.getMessageProvider(wizardInstance);
			//testDBConnection(form, messageProvider);
		}		
	}

	private void testDBConnection(IDataModel dataModel, XForm form, IMessageProvider messageProvider) throws Exception {
		String url = form.getUI().getControl("dburl").getXMLValue();
		String username = form.getUI().getControl("user").getXMLValue();
		String password = form.getUI().getControl("password").getXMLValue();
		String driver = "oracle.jdbc.OracleDriver";
		Properties info = new Properties();
		info.put("user", username);
		info.put("password", password);
		File driverFile = null;
		if(Boolean.parseBoolean(dataModel.getValue("/fom/databaseConfig/hasLastestOjdbcDriver"))){
			driverFile = new File(form.getUI().getControl("driverpath").getXMLValue(),"ojdbc7.jar");
		}else{
			driverFile = new File(form.getUI().getControl("driverpath").getXMLValue(),"ojdbc6.jar");
		}
		String path = driverFile.getAbsolutePath().replace('\\', '/');
		URL driverUrl = new URL("file", "", path);
		testDBConnection(driver, url, info, driverUrl);
	}
	
	private boolean testDBConnection(String driver, String url, Properties info, URL driverUrl) throws Exception {
		if (dbConnectionTester == null) {
			List<URL> urlList = new ArrayList<URL>();
			urlList.add(driverUrl);
			urlList.add(TCTContext.getInstance().getTctJarUrl());
			ClassLoader dbDriverClassLoader = new URLClassLoader(urlList.toArray(new URL[urlList.size()]));
			Class<?> testerClass = dbDriverClassLoader.loadClass("com.tibco.tct.framework.internal.support.DBConnectionTester");
			dbConnectionTester = testerClass.getMethod("getConnection",new Class[] { String.class, String.class, Properties.class });
		}

		Connection conn = null;
		try {
			conn = (Connection) dbConnectionTester.invoke(null, driver, url, info);
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				e = (Exception) e.getCause();
			}
			String errorMsg = e.getMessage();
			if (e instanceof ClassNotFoundException) {
				errorMsg = e.toString();
			}
			throw new ActionException("Could not establish connection to the database: " + errorMsg, e);
		}finally {
			if(conn != null){
				conn.close();
			}
		}
		return true;
	}

	public static void resetDBDrivers() {
		dbConnectionTester = null;
	}
}
