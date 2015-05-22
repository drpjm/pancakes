package edu.gatech.grits.pancakes.devices.driver.empty;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.EmptyBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.IRPacket;

public class IRDriver implements HardwareDriver<IRPacket> {

	private EmptyBackend backend;
	
	public IRDriver(Backend b){
		super();
		this.backend = (EmptyBackend) b;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IRPacket query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(IRPacket pkt) {
		// TODO Auto-generated method stub
		
	}

}
