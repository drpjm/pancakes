package edu.gatech.grits.pancakes.service;

import javolution.util.FastList;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.client.ScanThreat;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.util.Properties;

public class ClientService extends Service {

	private final String CLASSPATH = "edu.gatech.grits.pancakes.client";

	public ClientService(Properties properties) {
		super("Client");

		FastList<String> tasks = properties.getClientTasks();

		// search in file name list
		for(String taskName : tasks){
			try {
				Task t = (Task) Class.forName(CLASSPATH + "." + taskName).newInstance();
				
				
				
				addTask(taskName.toLowerCase(), t);
				scheduleTask(taskName.toLowerCase());
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

			//			}
		}
//		addTask("scanThreat", new ScanThreat());
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(Packet pkt) {
		// TODO Auto-generated method stub

	}

}
