package edu.gatech.grits.pancakes.devices.driver.k3;

import org.swig.k3i.k3i;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.SonarPacket;

public class SonarDriver implements HardwareDriver<SonarPacket> {
	
	public SonarDriver(Backend backend) {
		// do nothing
		k3i.enableUSs();
	}
	
	public void request(SonarPacket pkt) {
		// does nothing, since sonar is not an actuator
		return;
	}
	
	public SonarPacket query() {
		SonarPacket pkt = new SonarPacket();
		
		float ranges[] = new float[5];
		
		for(int i=0; i<5; i++) {
			ranges[i] = (float) k3i.queryUS(i);
			//System.out.println("Range #" + (i+1) + ": " + ranges[i]);
		}
		
		pkt.setSonarReadings(ranges);
		//pkt.setDataReady(true);

		return pkt;
	}
}
