package edu.gatech.grits.pancakes.sandbox;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Subscription;
import edu.gatech.grits.pancakes.util.Properties;

public class Monitor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Kernel kernel = new Kernel(new Properties("k3.props"));
		Fiber fiber = Kernel.scheduler.newFiber();
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				System.err.println("Incoming packet(s):");
				//pkt.debug();
				//if(pkt.getPacketType().equals("network"))
				if(pkt.getPacketType().equals("network")) {
					for(Packet p : ((NetworkPacket) pkt).getPackets()) {
						p.debug();
					}
				}
			}
		};
		
		Subscription subscription = new Subscription("system", fiber, callback);
		
		Kernel.syslog.debug("Starting Monitor.");
		
		try {
			Kernel.stream.subscribe(subscription);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true) {
			// do nothing
		}
	}

}
