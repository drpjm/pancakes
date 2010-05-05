package edu.gatech.grits.pancakes.devices.driver.empty;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.EmptyBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;

public class LocalPoseDriver implements HardwareDriver<LocalPosePacket> {

	private EmptyBackend backend;
	
	public LocalPoseDriver(Backend backend){
		this.backend = (EmptyBackend)backend;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocalPosePacket query() {
		LocalPosePacket newPkt = new LocalPosePacket();
		newPkt.setPose(0, 0, 0);
		return newPkt;
	}

	@Override
	public void request(LocalPosePacket pkt) {
		// TODO Auto-generated method stub
		
	}

}
