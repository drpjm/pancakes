package edu.gatech.grits.pancakes.devices.driver.empty;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.MotorPacket;

public class MotorDriver implements HardwareDriver<MotorPacket> {

	public MotorDriver(Backend b){
		
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MotorPacket query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(MotorPacket pkt) {
		
		Kernel.getInstance().getSyslog().debug("Issuing " + pkt + " to motors.");
		
	}

}
