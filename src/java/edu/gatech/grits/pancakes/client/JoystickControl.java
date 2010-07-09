package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.JoystickPacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;

public class JoystickControl extends Task {
	
	private volatile JoystickPacket jsPkt = null;
	
	public JoystickControl() {
		setDelay(200);
		Callback<Packet> jsCbk = new Callback<Packet>() {
			
			public void onMessage(Packet message) {
				if(message instanceof JoystickPacket) {
					jsPkt = (JoystickPacket) message;
					//System.out.println("Got joystick!");
				}
			}
		};
		subscribe(CoreChannel.SYSTEM, jsCbk);
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Kernel.syslog.debug("Firing!");
		if(jsPkt != null) {
			int x = (int) jsPkt.getPositionX();
			int y = (int) jsPkt.getPositionY();
			int b1 = (int) jsPkt.getPushButton1();
			int b2 = (int) jsPkt.getPushButton2();
			
			if((x > -1 && x < 170) && (y > -1 && y < 170)) {
				
				MotorPacket pkt = new MotorPacket();
				pkt.setVelocity(-0.3f*((float) (x-67))/(160-67));
				pkt.setRotationalVelocity(-0.9f*((float) (y-71)/(165-71)));
				
				try {
					Kernel.stream.publish(CoreChannel.SYSTEM, pkt);
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
