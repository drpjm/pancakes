package edu.gatech.grits.pancakes.device;

import edu.gatech.grits.pancakes.backend.Backend;
import edu.gatech.grits.pancakes.driver.HardwareDriver;
import edu.gatech.grits.pancakes.structures.SonarPacket;
import edu.gatech.grits.pancakes.structures.Packet;
import edu.gatech.grits.pancakes.kernel.Kernel;
import edu.gatech.grits.pancakes.kernel.Stream.CommunicationException;

public class SonarDevice implements HardwareDevice, Runnable {

	private HardwareDriver<SonarPacket> driver;
	
	@SuppressWarnings("unchecked")
	public SonarDevice(Backend backend) {
		driver = (HardwareDriver<SonarPacket>) backend.getDriver("SonarDriver");
	}
	
	public Packet query() {
		return driver.query();
	}
	
	public void request(Packet pkt) {
		driver.request((SonarPacket) pkt);
	};
	
	public void debug() {

		SonarPacket pkt = driver.query();
		
		System.err.println("Sensor Readings...");
		for(int j=0; j<pkt.getSize(); j++) {
			System.err.println("Sensor #" + j + ":" + (Float) pkt.getSonarReading(j));
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Kernel.stream.publish("system", driver.query());
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}
}
