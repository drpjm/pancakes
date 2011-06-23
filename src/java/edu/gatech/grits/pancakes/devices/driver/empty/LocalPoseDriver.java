package edu.gatech.grits.pancakes.devices.driver.empty;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.EmptyBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;

public class LocalPoseDriver implements HardwareDriver<LocalPosePacket> {
	
	private EmptyBackend backend;
	
	public LocalPoseDriver(Backend backend) {
		super();
		this.backend = (EmptyBackend) backend;
	}
	
	public void request(LocalPosePacket pkt) {
		// returns nothing, since it's not an actuator
		return;
	}
	
	public LocalPosePacket query() {
		LocalPosePacket pkt = new LocalPosePacket();
		
		pkt.setPose((float)Math.random(), (float)Math.random(), (float)Math.random());
		
		return pkt;
	}
	
	public void close() {
		//nothing to close
	}
	
	
}
