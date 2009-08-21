package edu.gatech.grits.pancakes.service;

import edu.gatech.grits.pancakes.net.*;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.util.Properties;

public class NetworkService {

	private NetworkServer server;
	private NetworkClient client = new NetworkClient();
	private DiscoveryListener listener = new DiscoveryListener();
	private DiscoverySpeaker speaker;
	public static NetworkNeighborhood neighborhood = new NetworkNeighborhood();
	
	public NetworkService(Properties properties) {
		server = new NetworkServer(properties.getNetworkPort());
		System.out.println("Network Port: " + properties.getNetworkPort());
		speaker = new DiscoverySpeaker(properties.getNetworkAddress(), properties.getNetworkPort(), properties.getID());		
		try {
			Kernel.scheduler.schedule(speaker, 1000);
			Kernel.scheduler.schedule(neighborhood, 10000);
		} catch (SchedulingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		server.close();
		client.close();
		listener.close();
		
		try {
			Kernel.scheduler.cancel(speaker);
			Kernel.scheduler.cancel(neighborhood);
		} catch (SchedulingException e) {
			System.err.println("Unable to cancel runnables.");
		}
		
		speaker.close();
	}
}
