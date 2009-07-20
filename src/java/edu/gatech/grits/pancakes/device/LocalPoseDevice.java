/**
 * 
 */
package edu.gatech.grits.pancakes.device;

import edu.gatech.grits.pancakes.backend.Backend;
import edu.gatech.grits.pancakes.driver.HardwareDriver;
import edu.gatech.grits.pancakes.kernel.Kernel;
import edu.gatech.grits.pancakes.kernel.Stream.CommunicationException;
import edu.gatech.grits.pancakes.structures.LocalPosePacket;
import edu.gatech.grits.pancakes.structures.Packet;

/**
 * @author jean-pierre
 *
 */
public class LocalPoseDevice implements HardwareDevice, Runnable {

	private HardwareDriver<LocalPosePacket> driver;
	
	@SuppressWarnings("unchecked")
	public LocalPoseDevice(Backend backend) {
		driver = (HardwareDriver<LocalPosePacket>) backend.getDriver("LocalPoseDriver");
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

	@Override
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

