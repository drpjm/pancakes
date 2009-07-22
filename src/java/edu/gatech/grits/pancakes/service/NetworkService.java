package edu.gatech.grits.pancakes.service;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javolution.util.FastList;
import javolution.util.FastMap;
import edu.gatech.grits.pancakes.net.*;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.lang.NetworkNeighbor;
import edu.gatech.grits.pancakes.lang.SafeQueue;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.util.Properties;

public class NetworkService {

	private int port;
	private NetworkServer server;
	private NetworkClient client;
	private DiscoveryListener listener;
	private DiscoverySpeaker speaker;
	private Properties properties;
	private SafeQueue sendQueue = new SafeQueue();
	
	public NetworkService(Properties properties) {
		this.properties = properties;
		port = properties.getNetworkPort(); //network_port;
		server = new NetworkServer(port);
		listener = new DiscoveryListener();
		speaker = new DiscoverySpeaker(properties.getNetworkAddress(), port, properties.getID());
		listener.start();
		
		try {
			Kernel.scheduler.schedule(speaker, 1000);
		} catch (SchedulingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
