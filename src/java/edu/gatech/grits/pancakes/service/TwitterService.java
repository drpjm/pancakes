package edu.gatech.grits.pancakes.service;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LogPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Subscription;
import edu.gatech.grits.pancakes.social.twitter.Tweeter;
import edu.gatech.grits.pancakes.util.Properties;

public class TwitterService extends Service {

	private Tweeter tweeter;
	private final Subscription subscription;
	
	
	public TwitterService(Properties properties) {
		super(TwitterService.class.getSimpleName());
		tweeter = new Tweeter(properties);
		Fiber fiber = Kernel.getInstance().getScheduler().newFiber();
		fiber.start();
		
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				LogPacket l = (LogPacket) pkt;
				if(l.getSource().equals("twitter")) {
					tweeter.tweet(l.getMessage());
				}
			}
		};
		
		subscription = new Subscription(CoreChannel.LOG, fiber, callback);
		
		try {
			Kernel.getInstance().getStream().subscribe(subscription);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		Kernel.getInstance().getStream().unsubscribe(subscription);
		subscription.getFiber().dispose();
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
