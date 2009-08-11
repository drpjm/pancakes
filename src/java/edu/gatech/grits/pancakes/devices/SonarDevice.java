package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.SonarPacket;

public class SonarDevice implements Device, Runnable {

	private HardwareDriver<SonarPacket> driver;
	private final long delay = 500l;
	
	@SuppressWarnings("unchecked")
	public SonarDevice(Backend backend) {
		driver = (HardwareDriver<SonarPacket>) backend.getDriver("SonarDriver");
	}
	
	public final boolean isRunnable() {
		return true;
	}
	
	public final long delay() {
		return delay;
	}
	
	public Packet query() {
		return driver.query();
	}
	
	public void request(Packet pkt) {
		driver.request((SonarPacket) pkt);
	};
	
	public void debug() {

		SonarPacket pkt = driver.query();
		
		System.err.println("Sensor Readings...");
		for(int j=0; j<pkt.getSize(); j++) {
			System.err.println("Sensor #" + j + ":" + (Float) pkt.getSonarReading(j));
		}
		
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
