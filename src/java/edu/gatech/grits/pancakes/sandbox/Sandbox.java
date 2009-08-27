package edu.gatech.grits.pancakes.sandbox;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.BatteryPacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Subscription;
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
				//System.err.println("Incoming packet(s):");
				pkt.debug();
				//if(pkt.getPacketType().equals("battery")) {
//					//pkt.debug();
//					NetworkPacket n = new NetworkPacket("8", "9");
//					n.addPacket(pkt);
//					try {
//						Kernel.stream.publish("network", n);
//					} catch (CommunicationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				//	System.out.println(((BatteryPacket) pkt).getVoltage());
				//}
			}
		};
		
		Kernel.syslog.debug("Starting sandbox.");
		Subscription s = new Subscription("system", fiber, callback);
		Kernel.stream.subscribe(s);
		
		Runnable runnable = new Runnable() {
			public void run() {
				MotorPacket mp = new MotorPacket();
				mp.setVelocity(0.25f);
				mp.setRotationalVelocity(1.0f);
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
			//NetworkService.neighborhood.debug();
			//Kernel.stream.publish("network", new NetworkPacket("8", "8"));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	

}
