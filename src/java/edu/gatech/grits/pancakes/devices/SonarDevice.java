package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.lang.SonarPacket;

public class SonarDevice extends Device<SonarPacket> {

	public SonarDevice(Backend backend, long delay) {
		super(backend, "Sonar", null, delay);
	}
}
