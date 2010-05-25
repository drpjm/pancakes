package edu.gatech.grits.pancakes.devices.driver.k3;

import org.swig.k3i.k3i;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.K3Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.BatteryPacket;

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
	
//	public BatteryPacket query() {		
//		for(Packet p : backend.update()) {
//			if(p.getPacketType().equals("battery"))
//				return (BatteryPacket) p;
//		}
//		
//		return new BatteryPacket();
//	}
	
	public BatteryPacket query() {
		backend.update();
		
		BatteryPacket pkt = new BatteryPacket();
		
		//System.err.println("Voltage (int): " + k3i.batteryVoltage());
		//System.err.println("Voltage (float): " + ((float) k3i.batteryVoltage())/10000);
		
		pkt.setCurrent(( (float) k3i.batteryCurrent())/10000 );
		pkt.setVoltage(( (float) k3i.batteryVoltage())/10000 );
//		pkt.setAvgCurrent(( (float) k3i.batteryCurrentAverage()) / 10000 );
		
		
		return pkt;
	}
	
	public void close() {
		// do nothing
	}
}
