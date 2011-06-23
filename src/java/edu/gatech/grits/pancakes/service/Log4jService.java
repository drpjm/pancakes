package edu.gatech.grits.pancakes.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.*;

public class Log4jService extends Service {

	private static final String CFG_FILE = "cfg/log4j.cfg";
	private final Subscription subscription;
	
	public Log4jService() {
		super(Log4jService.class.getSimpleName());
		PropertyConfigurator.configure(CFG_FILE);
		Fiber fiber = Kernel.getInstance().getScheduler().newFiber();
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet packet) {
				LogPacket p = (LogPacket) packet;
				Logger.getLogger(p.getSource()).log(Level.toLevel(p.getLevel()), p.getMessage());
			}
		};
		
		subscription = new Subscription(CoreChannel.LOG, fiber, callback);
		
		try {
			Kernel.getInstance().getStream().subscribe(subscription);
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		Kernel.getInstance().getStream().unsubscribe(subscription);
	}

	@Override
	protected void restartService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stopService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startTask(String taskName) {
		// TODO Auto-generated method stub
		
	}

}
