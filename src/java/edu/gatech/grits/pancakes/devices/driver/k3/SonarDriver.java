package edu.gatech.grits.pancakes.devices.driver.k3;

import swig.korebot.k3.k3ctrl;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.SonarPacket;

public class SonarDriver implements HardwareDriver<SonarPacket> {
	
	public SonarDriver(Backend backend) {
		// do nothing
	}
	
	public void request(SonarPacket pkt) {
		// does nothing, since sonar is not an actuator
		return;
	}
	
	public SonarPacket query() {
		SonarPacket pkt = new SonarPacket();
		
		float ranges[] = new float[5];
		
		for(int i=0; i<5; i++) {
			ranges[i] = (float) k3ctrl.measureUS(i+1);
			//System.out.println("Range #" + (i+1) + ": " + ranges[i]);
		}
		
		pkt.setSonarReadings(ranges);
		//pkt.setDataReady(true);

		return pkt;
	}
}
