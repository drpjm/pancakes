package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.IRPacket;
import edu.gatech.grits.pancakes.lang.Packet;

public class IRDevice implements Device, Runnable {

	private HardwareDriver<IRPacket> driver;
	private final long delay = 250l;
	
	@SuppressWarnings("unchecked")
	public IRDevice(Backend backend) {
		driver = (HardwareDriver<IRPacket>) backend.getDriver("IRDriver");
	}
	
	public long delay() {
		return delay;
	}
	
	public final boolean isRunnable() {
		return true;
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
