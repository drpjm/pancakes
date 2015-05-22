package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.lang.MotionPacket;

public class MotionDevice extends Device<MotionPacket> {

	public MotionDevice(Backend backend) {
		super(backend, "Motion", null, 0l);
		//System.out.println("Setting up Motion device!");
		// TODO Auto-generated constructor stub
	}

}
