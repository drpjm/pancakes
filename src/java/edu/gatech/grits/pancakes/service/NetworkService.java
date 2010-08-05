package edu.gatech.grits.pancakes.service;

import edu.gatech.grits.pancakes.net.*;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.util.Properties;

public class NetworkService extends Service {

	private NetworkServer server;
	private NetworkClient client;
	private DiscoveryListener listener;
	private DiscoverySpeaker speaker;
	//TODO: make the migrator an optional task...from property file
	private TaskMigrator taskMigrator;
	
	public static final String NEIGHBORHOOD = "neighborhood";
	public static final String MIGRATE = "migrate";
	
	public NetworkService(Properties properties) {
		super(NetworkService.class.getSimpleName());
		
		// make channel for passing neighbor information
		Kernel.stream.createChannel(NEIGHBORHOOD);
		// make channel for initiating task migration
		Kernel.stream.createChannel(MIGRATE);
		
		client = new NetworkClient();
		server = new NetworkServer(properties.getNetworkPort());
		Kernel.syslog.debug("Network Port: " + properties.getNetworkPort());
		
		listener = new DiscoveryListener();
		speaker = new DiscoverySpeaker(properties.getNetworkAddress(), properties.getNetworkPort(), properties.getID());		
		
		// TODO: add migrator only if it is in property file
		taskMigrator = new TaskMigrator();
		
		addTask(speaker.getClass().getSimpleName(), speaker);
		addTask(listener.getClass().getSimpleName(), listener);
		addTask(client.getClass().getSimpleName(), client);
		
		addTask(taskMigrator.getClass().getSimpleName(), taskMigrator);

		for(String key : taskList()) {
			scheduleTask(key);
		}

	}
	
	public void close() {
		
		server.close();
		client.close();
		listener.close();

		try {
			Kernel.scheduler.cancel(speaker);
//			Kernel.scheduler.cancel(listener);
		} catch (SchedulingException e) {
			System.err.println("Unable to cancel runnables.");
		}
		
		speaker.close();
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
		// TODO Auto-generated method stub
		
	}

}
