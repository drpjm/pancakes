package edu.gatech.grits.pancakes.client;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;

public class Monitor extends Task {

	public Monitor() {
		super("system", 0l);
		//Kernel.syslog.debug("Starting Monitor");
	}

	public void close() {
		// do nothing
	}

	public void process(Packet pkt) {
		Kernel.syslog.debug(pkt.getPacketType());
		if(pkt.getPacketType().equals("localpose")) {
			pkt.debug();
		}
	}

	public void run() {
		// do nothing
	}

}
