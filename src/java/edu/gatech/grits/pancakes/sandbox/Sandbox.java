package edu.gatech.grits.pancakes.sandbox;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.service.NetworkService;
import edu.gatech.grits.pancakes.util.Properties;

public class Sandbox {

	/**
	 * @param args
	 * @throws CommunicationException 
	 */
	public static void main(String[] args) throws CommunicationException {
		// TODO Auto-generated method stub
		
		Kernel kernel = new Kernel(new Properties("k3.props"));
		Fiber fiber = Kernel.scheduler.newFiber();
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				System.err.println("Incoming packet(s):");
				//pkt.debug();
				//if(pkt.getPacketType().equals("network"))
					pkt.debug();
			}
		};
		
		Kernel.syslog.debug("Starting sandbox.");
		
		Kernel.stream.subscribe("system", fiber, callback);
		
		Runnable runnable = new Runnable() {
			public void run() {
				MotorPacket mp = new MotorPacket();
				mp.setVelocity(0.0f);
				mp.setRotationalVelocity(0.0f);
				try {
					Kernel.stream.publish("user", mp);
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Kernel.scheduler.execute(runnable);
		
		while(true) {
			// do nothing
			NetworkService.neighborhood.debug();
			Kernel.stream.publish("network", new NetworkPacket("8", "8"));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	

}
