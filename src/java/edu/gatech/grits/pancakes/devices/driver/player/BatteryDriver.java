package edu.gatech.grits.pancakes.devices.driver.player;

import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.BatteryPacket;

public class BatteryDriver implements HardwareDriver<BatteryPacket>{

	public BatteryDriver(){
		System.currentTimeMillis();
	}
	
	public void close() {
		// TODO Auto-generated method stub
		
	}

	public BatteryPacket query() {
//		BatteryPacket newPkt = new BatteryPacket();
		
		return null;
	}

	public void request(BatteryPacket pkt) {
		// not an actuator
		return;
	}
	
}
