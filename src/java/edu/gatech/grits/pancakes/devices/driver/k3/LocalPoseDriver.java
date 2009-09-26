package edu.gatech.grits.pancakes.devices.driver.k3;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.devices.driver.net.ViconRTEDriver;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;

public class LocalPoseDriver implements HardwareDriver<LocalPosePacket> {
	
	private ViconRTEDriver device;
	
	public LocalPoseDriver(Backend backend) {
		device = new ViconRTEDriver(1026, 30); // TODO ID needs to be properly passed here
	}
	
	public void request(LocalPosePacket pkt) {
		// returns nothing, since it's not an actuator
		return;
	}
	
	public LocalPosePacket query() {
		return device.query();
	}
	
	public void close() {
		device.close();
	}
	
	
}
