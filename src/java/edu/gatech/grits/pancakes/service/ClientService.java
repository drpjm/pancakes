package edu.gatech.grits.pancakes.service;

import java.io.File;

import javolution.util.FastList;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.util.ClassFinder;
import edu.gatech.grits.pancakes.util.Properties;

public class ClientService extends Service {

	private final String CLIENT_CLASSPATH = "edu.gatech.grits.pancakes.client";
//	private final ClassFinder classFinder;

	public ClientService(Properties properties) {
		super(ClientService.class.getSimpleName());

		FastList<String> tasks = properties.getClientTasks();
//		classFinder = new ClassFinder(CLIENT_CLASSPATH);

		
		// TODO: get client task prop files and load their details

		// search in file name list
		for(String taskName : tasks){
			if(!taskName.isEmpty()){
				startTask(taskName);
			}
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void restartService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stopService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startTask(String taskName) {
		Task t;
		try {
//			String qualifiedName = classFinder.findQualifiedClassname(taskName);
			
			t = (Task) Class.forName(CLIENT_CLASSPATH + "." + taskName).newInstance();
			// TODO: new constructor calls using task properties
			addTask(t.getClass().getSimpleName(), t);
			scheduleTask(t.getClass().getSimpleName());
			Kernel.syslog.debug(taskName + " task started.");
			
		} catch (InstantiationException e) {
			Kernel.syslog.error(taskName + " not instantiated!");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Kernel.syslog.error(taskName + " task not found! Skipping.");
		}
	}
	

}
