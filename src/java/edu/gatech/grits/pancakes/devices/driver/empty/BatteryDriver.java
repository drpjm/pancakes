package edu.gatech.grits.pancakes.devices.driver.empty;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.EmptyBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.BatteryPacket;

public class BatteryDriver implements HardwareDriver<BatteryPacket> {

	private EmptyBackend backend;
	
	private float voltage;
	private float delT = 0.001f;
	
	public BatteryDriver(Backend b){
		super();
		this.backend = (EmptyBackend) b;
		voltage = 8.4f;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BatteryPacket query() {
		
//		System.err.println(voltage);
		
		BatteryPacket bp = new BatteryPacket();
		
		voltage = voltage - delT*voltage;
		if(voltage < 6.6f){
			voltage = 6.6f;
		}
		bp.setVoltage(voltage);
		
		return bp;
	}

	@Override
	public void request(BatteryPacket pkt) {
		// TODO Auto-generated method stub
		
	}

}
