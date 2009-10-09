package edu.gatech.grits.pancakes.service;

import javolution.util.FastList;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.util.Properties;

public class ClientService extends Service {

	private final String CLASSPATH = "edu.gatech.grits.pancakes.client";
	public final static String BATTERY_UPDATE = "battery_update";


	public ClientService(Properties properties) {
		super("Client");

		Kernel.stream.createChannel(BATTERY_UPDATE);
		
		FastList<String> tasks = properties.getClientTasks();

		// search in file name list
		for(String taskName : tasks){
			try {
				Kernel.syslog.debug(CLASSPATH + "." + taskName);
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

		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(Packet pkt) {
		if(pkt instanceof ControlPacket){
			ControlPacket ctrlPkt = (ControlPacket)pkt;
			if(ctrlPkt.getPacketType().equals("client") && ctrlPkt.getControl().equals(ControlPacket.RESCHEDULE)){
				
				Task t = (Task) getTask(ctrlPkt.getTaskName());
				if(t != null){
					
				}
				
			}
		}

	}

}
