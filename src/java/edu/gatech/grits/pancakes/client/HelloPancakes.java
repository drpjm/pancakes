package edu.gatech.grits.pancakes.client;

import java.awt.geom.Point2D;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;

public class HelloPancakes extends Task {

	public HelloPancakes() {
		// TODO Auto-generated constructor stub
		setDelay(1000l);
		
		Callback<Packet> cbk = new Callback<Packet>(){

			public void onMessage(Packet pkt) {

				Kernel.syslog.debug("Got message: " + pkt.getPacketType());
				
			}
		};
		subscribe(CoreChannel.SYSTEM, cbk);
		
		publish(CoreChannel.CTRL, new MotorPacket());
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void run() {
		Kernel.syslog.info("Hello, Pancakes!");

	}

}
