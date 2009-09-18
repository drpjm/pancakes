package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;

public abstract class Device<T> extends Task {
	
	private final HardwareDriver<T> driver;
	private final String device;
	
	@SuppressWarnings("unchecked")
	public Device(Backend backend, String type, String channel, long delay) {
		super(channel, delay);
		driver = (HardwareDriver<T>) backend.getDriver(type + "Driver");		
		device = type.toLowerCase();
	}
	
	public final void run() {
		try {
			Kernel.stream.publish("system", (Packet) driver.query());
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public final void process(Packet pkt) {
		if(pkt.getPacketType().equals(device))
			driver.request((T) pkt);
	}
	
	public final void close() {
		unsubscribe();
		driver.close();
	}
	
}
