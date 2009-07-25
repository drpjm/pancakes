package edu.gatech.grits.pancakes.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.LogPacket;
import edu.gatech.grits.pancakes.lang.Packet;

public class Log4jService {

	private static final String CFG_FILE = "cfg/log4j.cfg";
	private final Fiber fiber = Kernel.scheduler.newFiber();
	private Callback<Packet> callback;
	
	public Log4jService() {
		PropertyConfigurator.configure(CFG_FILE);
		fiber.start();
		callback = new Callback<Packet>() {
			public void onMessage(Packet packet) {
				LogPacket p = (LogPacket) packet;
				Logger.getLogger(p.getSource()).log(Level.toLevel(p.getLevel()), p.getMessage());
			}
		};
		
		try {
			Kernel.stream.subscribe("log", fiber, callback);
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
	}
}
