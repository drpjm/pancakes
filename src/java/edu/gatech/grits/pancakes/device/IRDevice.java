package edu.gatech.grits.pancakes.device;

import edu.gatech.grits.pancakes.backend.Backend;
import edu.gatech.grits.pancakes.driver.HardwareDriver;
import edu.gatech.grits.pancakes.structures.IRPacket;
import edu.gatech.grits.pancakes.structures.Packet;
import edu.gatech.grits.pancakes.kernel.Kernel;
import edu.gatech.grits.pancakes.kernel.Stream.CommunicationException;

public class IRDevice implements HardwareDevice, Runnable {

	private HardwareDriver<IRPacket> driver;
	
	@SuppressWarnings("unchecked")
	public IRDevice(Backend backend) {
		driver = (HardwareDriver<IRPacket>) backend.getDriver("IRDriver");
	}
	
	public Packet query() {
		return driver.query();
	}
	
	public void request(Packet pkt) {
		driver.request((IRPacket) pkt);
	};
	
	public void debug() {

		IRPacket pkt = driver.query();
		
		System.err.println("IR Readings...");
		for(int j=0; j<pkt.getSize(); j++) {
			System.err.println("Sensor #" + j + ":" + (Float) pkt.getIRReading(j));
		}
	}

	public void run() {
		try {
			Kernel.stream.publish("system", driver.query());
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}
}
