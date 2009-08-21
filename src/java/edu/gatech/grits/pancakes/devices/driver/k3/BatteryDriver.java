package edu.gatech.grits.pancakes.devices.driver.k3;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.K3Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.BatteryPacket;
import edu.gatech.grits.pancakes.lang.Packet;

public class BatteryDriver implements HardwareDriver<BatteryPacket> {

	private K3Backend backend;
	
	public BatteryDriver(Backend backend) {
		// do nothing
		this.backend = (K3Backend) backend;
	}
	
	public void request(BatteryPacket pkt) {
		// does nothing, since IR is not an actuator
		return;
	}
	
	public BatteryPacket query() {		
		for(Packet p : backend.update()) {
			if(p.getPacketType().equals("battery"))
				return (BatteryPacket) p;
		}
		
		return new BatteryPacket();
	}
	
	public void close() {
		// do nothing
	}
}
