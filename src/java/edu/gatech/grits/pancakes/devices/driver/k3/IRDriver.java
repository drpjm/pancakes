package edu.gatech.grits.pancakes.devices.driver.k3;


import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.K3Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.IRPacket;
import edu.gatech.grits.pancakes.lang.Packet;

public class IRDriver implements HardwareDriver<IRPacket> {

	private K3Backend backend;
	
	public IRDriver(Backend backend) {
		this.backend = (K3Backend) backend;
	}
	
	public void request(IRPacket pkt) {
		// does nothing, since IR is not an actuator
		return;
	}
	
	public IRPacket query() {
		for(Packet p : backend.update()) {
			if(p.getPacketType().equals("ir"))
				return (IRPacket) p;
		}
		
		return new IRPacket();
	}
	
	public void close() {
		// do nothing
	}
}
