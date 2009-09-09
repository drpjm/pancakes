package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.lang.BatteryPacket;

public class BatteryDevice extends Device<BatteryPacket> {

	public BatteryDevice(Backend backend, long delay) {
		super(backend, "Battery", null, delay);
	}
	
}
