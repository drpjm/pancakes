package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.lang.IRPacket;

public class IRDevice extends Device<IRPacket> {

	public IRDevice(Backend backend, long delay) {
		super(backend, "IR", null, delay);
	}
}
