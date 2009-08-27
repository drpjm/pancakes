package edu.gatech.grits.pancakes.devices.driver.k3;

import org.swig.k3i.k3i;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.K3Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.SonarPacket;

public class SonarDriver implements HardwareDriver<SonarPacket> {
	
	private K3Backend backend;
	
	public SonarDriver(Backend backend) {
		this.backend = (K3Backend) backend;
		//Kernel.scheduler.execute("ultrasound_enable --mask 31");
		k3i.sonarEnable();
	}
	
	public void request(SonarPacket pkt) {
		// does nothing, since sonar is not an actuator
		return;
	}
	

//	public SonarPacket query() {
//		for(Packet p : backend.update()) {
//			if(p.getPacketType().equals("sonar"))
//				return (SonarPacket) p;
//		}
//		
//		return new SonarPacket();
//	}
	
	public SonarPacket query() {
		backend.update();
		
		SonarPacket pkt = new SonarPacket();
		
		float[] readings = new float[5];
		
		for(int i=0; i<5; i++) {
			readings[i] = (float) k3i.sonarDistance(i) * 0.01f;
		}
		
		pkt.setSonarReadings(readings);
		return pkt;
	}
	
	public void close() {
		//Kernel.scheduler.execute("ultrasound_enable --mask 0");
		k3i.sonarDisable();
	}
}
