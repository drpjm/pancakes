package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.CoreChannel;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.SonarPacket;
import edu.gatech.grits.pancakes.lang.Task;

public class Monitor extends Task {

	public Monitor() {
		this.setDelay(0l);
		// looks like Java Swing listeners!
		Callback<Packet> cbk = new Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message.getPacketType().equals(PacketType.LOCAL_POSE)){
					LocalPosePacket local = (LocalPosePacket) message;
					local.debug();
				}
			}
			
		};
		subscribe(CoreChannel.SYSTEM, cbk);
	}

	public void close() {
		// do nothing
	}

	public void run() {
		// do nothing
	}

}
