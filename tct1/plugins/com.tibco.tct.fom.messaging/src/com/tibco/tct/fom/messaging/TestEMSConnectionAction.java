package com.tibco.tct.fom.messaging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import org.nuxeo.xforms.xforms.model.XForm;

import com.tibco.customwizard.action.ActionException;
import com.tibco.customwizard.action.IActionContext;
import com.tibco.customwizard.action.ICustomAction;
import com.tibco.customwizard.support.IBackgroundAction;
import com.tibco.customwizard.util.WizardHelper;
import com.tibco.customwizard.xforms.IXFormActionContext;
import com.tibco.tct.framework.support.IMessageProvider;
import com.tibco.tct.framework.utils.TCTHelper;

public class TestEMSConnectionAction implements ICustomAction, IBackgroundAction{

	private static URL configFile;
	
	public void execute(IActionContext actionContext) throws Exception {
		IXFormActionContext xformActionContext = (IXFormActionContext) actionContext;
		XForm form = xformActionContext.getForm();
		configFile = actionContext.getWizardInstance().getWizardConfig().getConfigFile();
		IMessageProvider messageProvider = TCTHelper.getMessageProvider(actionContext.getWizardInstance());
		try {
			testTibcoEMSConnection(form, messageProvider);
			WizardHelper.openMessage(actionContext.getWizardInstance(), messageProvider.getMessage("testconnection.success"));
			actionContext.getWizardInstance().setAttribute("testResult", "success");
			TCTHelper.revalidateCurrentPage(actionContext.getWizardInstance());
		} catch (Exception e) {
			actionContext.getWizardInstance().setAttribute("testResult", "faild");
			TCTHelper.revalidateCurrentPage(actionContext.getWizardInstance());
			WizardHelper.openErrorDialog(actionContext.getWizardInstance(), e);
		}
	}

	protected void testTibcoEMSConnection(XForm form, IMessageProvider messageProvider) throws Exception {
		String username = form.getUI().getControl("user").getXMLValue();
		String password = form.getUI().getControl("password").getXMLValue();
		String hostportlist = form.getUI().getControl("emsurls").getXMLValue();
		String[] hostPorts = hostportlist.split(",");
		for (int i = 0; i < hostPorts.length; i++) {
			String url = hostPorts[i];
			testTibcoEMSConnection(url, username, password, messageProvider);
		}
	}

	protected boolean testTibcoEMSConnection(String url, String username, String password, IMessageProvider messageProvider) throws Exception {
		URL emsDriverUrl = new URL(configFile, "tibjmsadmin.jar");
		URL jmsDriverUrl = new URL(configFile, "jms.jar");
		URL tibjmsDriverUrl = new URL(configFile, "tibjms.jar");
		ClassLoader emsClassLoader = new URLClassLoader(new URL[]{emsDriverUrl,jmsDriverUrl,tibjmsDriverUrl});
		
		// Check user
		if (!checkEMSUser(emsClassLoader, url, username, password)) {
			checkQueueTopic(emsClassLoader, url, username, password, messageProvider);
		}
		return true;
	}


	private boolean checkEMSUser(ClassLoader emsClassLoader, String url, String username, String password) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> queueClass = emsClassLoader.loadClass("com.tibco.tibjms.admin.QueueInfo");
		Object queue = queueClass.getConstructor(String.class).newInstance("testEmsNoneAdminUserQueuePrivilege");
		
		Class<?> topicClass = emsClassLoader.loadClass("com.tibco.tibjms.admin.TopicInfo");
		Object topic = topicClass.getConstructor(String.class).newInstance("testEmsNoneAdminUserTopicPrivilege ");
		
		Class<?>tibjmsAdmin = emsClassLoader.loadClass("com.tibco.tibjms.admin.TibjmsAdmin");
		Constructor<?> emsConstructor = tibjmsAdmin.getConstructor(String.class,String.class,String.class);
		Object emsAdmin = emsConstructor.newInstance(url, username, password);
		try {
			String queueName = (String) queueClass.getMethod("getName").invoke(queue);
			tibjmsAdmin.getMethod("destroyQueue", String.class).invoke(emsAdmin, queueName);
			
			String topicName = (String) topicClass.getMethod("getName").invoke(topic);
			tibjmsAdmin.getMethod("destroyTopic", String.class).invoke(emsAdmin, topicName);
			
			tibjmsAdmin.getMethod("createTopic", topicClass).invoke(emsAdmin, topic);
			tibjmsAdmin.getMethod("createQueue", queueClass).invoke(emsAdmin, queue);
		} catch (Exception e) {
			return false;
		} 
		return true;
	}

	private boolean checkQueueTopic(ClassLoader emsClassLoader, String url, String username, String password, IMessageProvider messageProvider) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Class<?>tibjmsAdmin = emsClassLoader.loadClass("com.tibco.tibjms.admin.TibjmsAdmin");
		Constructor<?> emsConstructor = tibjmsAdmin.getConstructor(String.class,String.class,String.class);
		Object emsAdmin = emsConstructor.newInstance(url, username, password);
		
		try {
			tibjmsAdmin.getMethod("getQueue", String.class).invoke(emsAdmin, "AMX_SV.>");
			tibjmsAdmin.getMethod("getQueue", String.class).invoke(emsAdmin, "com.tibco.amf.admin.deploymentServerQueue");
			tibjmsAdmin.getMethod("getTopic", String.class).invoke(emsAdmin, "EMSGMS.>");
		} catch (Exception e) {
			throw new ActionException(messageProvider.getMessage("TestEMSConnectionAction.error.user", username), e);
		} 
		return true;
	}
}
