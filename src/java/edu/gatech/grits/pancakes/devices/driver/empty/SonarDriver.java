package edu.gatech.grits.pancakes.devices.driver.empty;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.EmptyBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.SonarPacket;

public class SonarDriver implements HardwareDriver<SonarPacket> {

	private EmptyBackend backend;
	
	public SonarDriver(Backend backend) {
		super();
		this.backend = (EmptyBackend)backend;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SonarPacket query() {
		SonarPacket sonarPkt = new SonarPacket();
		float[] readings = {0,0,0,0};
		sonarPkt.setSonarReadings(readings);
		return sonarPkt;
	}

	@Override
	public void request(SonarPacket pkt) {
		// TODO Auto-generated method stub
		
	}

}
