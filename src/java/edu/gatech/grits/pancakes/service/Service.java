package edu.gatech.grits.pancakes.service;

import java.util.Set;

import javolution.util.FastMap;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Subscription;
import edu.gatech.grits.pancakes.lang.Taskable;

public abstract class Service {
	
	private final Subscription subscription;
	private final String serviceName;
	private FastMap<String, Taskable> taskRegistry;
	
	public Service(String name) {
		serviceName = name;
		Fiber fiber = Kernel.scheduler.newFiber();
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				if(pkt.getPacketType().equals(serviceName))
					process(pkt);
			}
		};
		subscription = new Subscription(CoreChannel.SYSCTRL, fiber, callback);
		
		try {
			Kernel.stream.subscribe(subscription);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		
		taskRegistry = new FastMap<String, Taskable>();
	}
	
	public final Taskable getTask(String key) {
		return taskRegistry.get(key);
	}
	
	public final Set<String> taskList() {
		return taskRegistry.keySet();
	}
	
	public final void addTask(String key, Taskable task) {
		taskRegistry.put(key, task);
	}
	
	public final void scheduleTask(String key) {
		Taskable task = taskRegistry.get(key);
		if(task.isTimeDriven()) {
			try {
				Kernel.scheduler.schedule(task, task.delay());
			} catch (SchedulingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public final void removeTask(String key) {
		Taskable t = taskRegistry.remove(key);
		if(t.isTimeDriven()) {
			try {
				Kernel.scheduler.cancel(t);
			} catch (SchedulingException e) {
				Kernel.syslog.error("Unable to cancel task.");
			}
		}
		t.close();
	}
	
	public final void rescheduleTask(String key, long delay) {
		Taskable t = taskRegistry.get(key);
		if(t.isTimeDriven()) {
			try {
				Kernel.scheduler.reschedule(t, delay);
			} catch (SchedulingException e) {
				Kernel.syslog.error("Unable to reschedule task with a delay of " + delay + ".");
			}
		}
	}
	
	public void unsubscribe() {
		Kernel.stream.unsubscribe(subscription);
		subscription.getFiber().dispose();
	}
	
	public abstract void process(Packet pkt);
	public abstract void close();
	
	
}
