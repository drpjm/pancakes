package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.MotionPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;

public class MotionDetection extends Task {

	public void close() {
		// TODO Auto-generated method stub
		unsubscribe();
	}

	public void run() {
		// TODO Auto-generated method stub

	}
	
	public MotionDetection() {
		setDelay(0l);
		
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet message) {
				MotionPacket pkt = (MotionPacket) message;
				pkt.debug();
			}
		};
		
		subscribe(CoreChannel.SYSTEM, callback);
		
		Kernel.syslog.debug("Ready to detect motion!");
	}

}
