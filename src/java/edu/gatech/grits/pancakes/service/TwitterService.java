package edu.gatech.grits.pancakes.service;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.LogPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.social.twitter.Tweeter;
import edu.gatech.grits.pancakes.util.Properties;

public class TwitterService {

	private Tweeter tweeter;
	private final Fiber fiber = Kernel.scheduler.newFiber();
	
	
	public TwitterService(Properties properties) {
		tweeter = new Tweeter(properties);
		fiber.start();
		
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				LogPacket l = (LogPacket) pkt;
				if(l.getSource().equals("twitter")) {
					tweeter.tweet(l.getMessage());
				}
			}
		};
		
		try {
			Kernel.stream.subscribe("log", fiber, callback);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
