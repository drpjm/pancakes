package edu.gatech.grits.pancakes.service;


import edu.gatech.grits.pancakes.net.*;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.util.Properties;

public class NetworkService {

	private NetworkServer server;
	private NetworkClient client;
	private DiscoveryListener listener;
	private DiscoverySpeaker speaker;
	public static NetworkNeighborhood neighborhood = new NetworkNeighborhood();
	
	public NetworkService(Properties properties) {
		server = new NetworkServer(properties.getNetworkPort());
		client = new NetworkClient();
		listener = new DiscoveryListener();
		speaker = new DiscoverySpeaker(properties.getNetworkAddress(), properties.getNetworkPort(), properties.getID());
		listener.start();
		
		try {
			Kernel.scheduler.schedule(speaker, 1000);
			Kernel.scheduler.schedule(neighborhood, 10000);
		} catch (SchedulingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
