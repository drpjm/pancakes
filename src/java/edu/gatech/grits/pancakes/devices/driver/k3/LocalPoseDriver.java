package edu.gatech.grits.pancakes.devices.driver.k3;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.devices.driver.net.ViconRTEDriver;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;

public class LocalPoseDriver implements HardwareDriver<LocalPosePacket> {
	
	private ViconRTEDriver device;
	
	public LocalPoseDriver(Backend backend) {
		device = new ViconRTEDriver(1026, 8); // TODO ID needs to be properly passed here
		Thread thread = new Thread(device);
		thread.start();
	}
	
	public void request(LocalPosePacket pkt) {
		// returns nothing, since it's not an actuator
		return;
	}
	
	public LocalPosePacket query() {
		LocalPosePacket pkt = new LocalPosePacket();
		
		String data = device.getData();
		
		if(data != null) {
			pkt = processData(data);
			//pkt.setDataReady(true);
		} else {
			//pkt.setDataReady(false);
		}
		
		return pkt;
	}
	
	public LocalPosePacket processData(String data) {
		LocalPosePacket pkt = new LocalPosePacket();
		
		String[] tokens = data.split(",");
		
		float yaw = (Float.valueOf(tokens[3])).floatValue();
		
//		if (yaw < 0.0f) {
//			yaw += 2.0f * ((float) Math.PI);
//		}
		
		float x = (Float.valueOf(tokens[1])).floatValue();
		float y = (Float.valueOf(tokens[2])).floatValue();
		
		pkt.setPose(x, y, yaw);
		
		return pkt;
	}
}
