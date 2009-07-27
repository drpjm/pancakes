package edu.gatech.grits.pancakes.agent;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.util.Properties;

public abstract class PancakesAgent {

	private Kernel kernel;
	private Properties properties;

	/**
	 * This class is the primary functional block for high-level control of
	 * the Pancakes system.
	 * 
	 * @param properties
	 */	
	public PancakesAgent(Properties p) {
		properties = p;
		kernel = new Kernel(properties);
	}
	
	public final void pushRequest(Packet pkt) {
		try {
			Kernel.stream.publish("user", pkt);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public final void sendMessage(String target, Packet pkt) {
		NetworkPacket n = (NetworkPacket) pkt;
		n.setDestination(target);
		n.setSource(properties.getID());
		sendMessage(n);
	}
	
	private final void sendMessage(NetworkPacket pkt){
		try {
			Kernel.stream.publish("network", pkt);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
}
