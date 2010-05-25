package edu.gatech.grits.pancakes.service;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.net.DiscoveryListener;
import edu.gatech.grits.pancakes.net.DiscoverySpeaker;
import edu.gatech.grits.pancakes.net.NetworkClient;
import edu.gatech.grits.pancakes.net.NetworkServer;
import edu.gatech.grits.pancakes.util.Properties;

public class NetworkService extends Service {

	private NetworkServer server;
	private NetworkClient client;
	private DiscoveryListener listener;
	private DiscoverySpeaker speaker;
	
	public static final String NEIGHBORHOOD = "neighborhood";
	
	public NetworkService(Properties properties) {
		super("network");
		
		// make channel for passing neighbor information
		Kernel.stream.createChannel(NEIGHBORHOOD);
		
		client = new NetworkClient();
		server = new NetworkServer(properties.getNetworkPort());
		Kernel.syslog.debug("Network Port: " + properties.getNetworkPort());
		
		listener = new DiscoveryListener();
		speaker = new DiscoverySpeaker(properties.getNetworkAddress(), properties.getNetworkPort(), properties.getID());		

		addTask("speaker", speaker);
		addTask("listener", listener);
		addTask("client", client);

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
	public void process(Packet pkt) {
		
		ControlPacket ctrlPkt = (ControlPacket) pkt;
		Task t = (Task) getTask(ctrlPkt.getTaskName());
		
		if(t != null){
			if(ctrlPkt.getControl().equals(ControlPacket.RESCHEDULE)) {
				Kernel.syslog.debug("Reschedule " + t.getClass().getSimpleName() + ": " + ctrlPkt.getDelay());
				rescheduleTask(ctrlPkt.getTaskName(), ctrlPkt.getDelay());
			}
		}
		
	}
}
