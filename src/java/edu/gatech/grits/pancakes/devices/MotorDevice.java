package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.MotorPacket;

public class MotorDevice extends Device<MotorPacket> {
	
	public MotorDevice(Backend backend) {
		super(backend, "Motor", CoreChannel.CTRL, 0l);
	}
}
