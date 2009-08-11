package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.BatteryPacket;
import edu.gatech.grits.pancakes.lang.Packet;

public class BatteryDevice implements Device, Runnable {

	private HardwareDriver<BatteryPacket> driver;
	private final long delay = 1000l;
	
	@SuppressWarnings("unchecked")
	public BatteryDevice(Backend backend) {
		driver = (HardwareDriver<BatteryPacket>) backend.getDriver("BatteryDriver");
	}
	
	public void debug() {
		
	}

	public boolean isRunnable() {
		return true;
	}

	public Packet query() {
		return driver.query();
	}

	public void request(Packet pkt) {
		driver.request((BatteryPacket) pkt);
	}

	public void run() {
		try {
			Kernel.stream.publish("system", driver.query());
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}

	public long delay() {
		// TODO Auto-generated method stub
		return delay;
	}

}
