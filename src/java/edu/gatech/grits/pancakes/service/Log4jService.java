package edu.gatech.grits.pancakes.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.CoreChannel;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.LogPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Subscription;

public class Log4jService extends Service {

	private static final String CFG_FILE = "cfg/log4j.cfg";
	private final Subscription subscription;
	
	public Log4jService() {
		super("log4j");
		PropertyConfigurator.configure(CFG_FILE);
		Fiber fiber = Kernel.scheduler.newFiber();
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet packet) {
				LogPacket p = (LogPacket) packet;
				Logger.getLogger(p.getSource()).log(Level.toLevel(p.getLevel()), p.getMessage());
			}
		};
		
		subscription = new Subscription(CoreChannel.LOG, fiber, callback);
		
		try {
			Kernel.stream.subscribe(subscription);
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		Kernel.stream.unsubscribe(subscription);
	}

	@Override
	public void process(Packet pkt) {
		// TODO Auto-generated method stub
		
	}
}
