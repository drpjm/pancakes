package edu.gatech.grits.pancakes.devices.driver.k3;

import swig.korebot.k3.k3ctrl;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.MotorPacket;

public class MotorDriver implements HardwareDriver<MotorPacket> {
		
//	static {
//		System.loadLibrary("k3ctrl");
//	}
	
	private final static float L = 0.0889f; // k3
	//private static float r = 0.0127f; // k3
	
	public MotorDriver(Backend backend) {
		
	}
	
	public void request(MotorPacket pkt) {
		float vel = pkt.getVelocity();
		float rad = pkt.getRotationalVelocity();
		
		float vel_l = vel - (rad * L)/2.0f;
		float vel_r = vel + (rad * L)/2.0f;
		
		int vel_l_k3 = (int) (vel_l * 1000.0f * 144.01f);
		int vel_r_k3 = (int) (vel_r * 1000.0f * 144.01f);
		
		k3ctrl.motSpeed(vel_r_k3, vel_l_k3);
	}
	
	public MotorPacket query() {
		// actuators return nothing here
		return null;
	}
}
