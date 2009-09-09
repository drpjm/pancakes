package edu.gatech.grits.pancakes.lang;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;

public abstract class Task implements Taskable {

	private Subscription subscription = null;
	private boolean isEventDriven = false;
	private boolean isTimeDriven = false;
	private long timeDelay = 0l;
	
	public Task(String channel, long delay) {
		if(channel != null) {
			isEventDriven = true;
			subscribe(channel);
		}
		
		if(delay > 0l) {
			isTimeDriven = true;
			setDelay(delay);
		}
	}
	
	public final void setDelay(long delay) {
		timeDelay = delay;
	}
	
	public final long delay() {
		return timeDelay;
	}
	
	public final boolean isEventDriven() {
		return isEventDriven;
	}
	
	public final boolean isTimeDriven() {
		return isTimeDriven;
	}
	
	public final void subscribe(String chl) {
		Fiber fiber = Kernel.scheduler.newFiber();
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				process(pkt);
			}
		};
		
		subscription = new Subscription(chl, fiber, callback);
		
		try {
			Kernel.stream.subscribe(subscription);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}
	
	public final void unsubscribe() {
		if(subscription != null) {
			Kernel.stream.unsubscribe(subscription);
			subscription.getFiber().dispose();
			subscription = null;
		}
	}
	
	public final void publish(String channel, Packet packet) {
		try {
			Kernel.stream.publish(channel, packet);
		} catch (CommunicationException e) {
			Kernel.syslog.error("Unable to publish packet to " + channel + ".");
		}
	}
}
