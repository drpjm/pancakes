package edu.gatech.grits.pancakes.devices.driver.bug;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.devices.driver.net.BugMotion;
import edu.gatech.grits.pancakes.lang.MotionPacket;

public class MotionDriver implements HardwareDriver<MotionPacket> {

	private final BugMotion motion;
	
	public MotionDriver(Backend backend) {
		//System.out.println("Driver called!");
		motion = new BugMotion();
	}
	
	public void close() {
		motion.close();
	}

	public MotionPacket query() {
		// TODO Auto-generated method stub
		return null;
	}

	public void request(MotionPacket pkt) {
		// TODO Auto-generated method stub

	}

}
