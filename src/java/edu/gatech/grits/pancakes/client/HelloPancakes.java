package edu.gatech.grits.pancakes.client;

import java.awt.geom.Point2D;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;

public class HelloPancakes extends Task {

	public HelloPancakes() {

		setDelay(1000l);
		
		Callback<Packet> cbk = new Callback<Packet>(){

			public void onMessage(Packet pkt) {

				Kernel.getInstance().getSyslog().debug("Got message: " + pkt.getPacketType());
				Kernel.getInstance().getSyslog().debug(pkt.toString());
				
			}
		};
		subscribe(CoreChannel.SYSTEM, cbk);
		
		publish(CoreChannel.CTRL, new MotorPacket());
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void run() {
		
		Kernel.getInstance().getSyslog().info("Hello, Pancakes!");

	}

}
