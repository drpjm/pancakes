package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.lang.JoystickPacket;
import edu.gatech.grits.pancakes.lang.PacketType;

public class JoystickDevice extends Device<JoystickPacket> {

	public JoystickDevice(Backend backend) {
		super(backend, "Joystick", null, 0l);
		// TODO Auto-generated constructor stub
	}

}
