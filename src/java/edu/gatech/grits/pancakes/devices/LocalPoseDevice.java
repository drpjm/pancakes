/**
 * 
 */
package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.Packet;

/**
 * @author jean-pierre
 *
 */
public class LocalPoseDevice implements Device, Runnable {

	private HardwareDriver<LocalPosePacket> driver;
	
	@SuppressWarnings("unchecked")
	public LocalPoseDevice(Backend backend) {
		driver = (HardwareDriver<LocalPosePacket>) backend.getDriver("LocalPoseDriver");
	}
	
	public final boolean isRunnable() {
		return true;
	}
	
	public Packet query() {
		return driver.query();
	}
	
	public void request(Packet pkt) {
		driver.request((LocalPosePacket) pkt);
	};
	
	public void debug() {
		
		LocalPosePacket pkt = driver.query();
		
		System.err.println("Robot pose: (" + pkt.getPositionX() + "," + pkt.getPositionY() + "," + pkt.getTheta() + ")");
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			Kernel.stream.publish("system", driver.query());
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}
}

