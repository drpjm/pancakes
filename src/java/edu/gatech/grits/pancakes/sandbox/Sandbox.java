package edu.gatech.grits.pancakes.sandbox;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.Packet;
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
				pkt.debug();
			}
		};
		
		Kernel.stream.subscribe("system", fiber, callback);
		
		Runnable runnable = new Runnable() {
			public void run() {
				MotorPacket mp = new MotorPacket();
				mp.setVelocity(0.5f);
				mp.setRotationalVelocity(1.0f);
				try {
					Kernel.stream.publish("user", mp);
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Kernel.scheduler.exectute(runnable);
		
		while(true) {
			// do nothing
		}

	}
	
	

}
