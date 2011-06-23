package edu.gatech.grits.pancakes.service;

import java.util.Set;

import javolution.util.FastMap;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.ControlPacket;
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
		Fiber fiber = Kernel.getInstance().getScheduler().newFiber();
		fiber.start();
		Callback<Packet> sysCtrlCallback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				if(pkt instanceof ControlPacket){
					processSysCtrl((ControlPacket) pkt);
				}
			}
		};
		subscription = new Subscription(CoreChannel.SYSCTRL, fiber, sysCtrlCallback);
		
		try {
			Kernel.getInstance().getStream().subscribe(subscription);
		} catch (CommunicationException e) {
			System.err.println(e.getMessage());
		}
		
		taskRegistry = new FastMap<String, Taskable>();
	}
	
	public final Taskable getTask(String key) {
		return taskRegistry.get(key);
	}
	
	protected final Set<String> taskList() {
		return taskRegistry.keySet();
	}
	
	public final void addTask(String key, Taskable task) {
		taskRegistry.put(key, task);
	}
	
	public final void scheduleTask(String key) {
		Taskable task = taskRegistry.get(key);
		if(task.isTimeDriven()) {
			try {
				Kernel.getInstance().getScheduler().schedule(task, task.delay());
			} catch (SchedulingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public final void removeTask(String key) {
		Taskable t = taskRegistry.remove(key);
		if(t.isTimeDriven()) {
			try {
				Kernel.getInstance().getScheduler().cancel(t);
			} catch (SchedulingException e) {
				Kernel.getInstance().getSyslog().error("Unable to cancel task.");
			}
		}
		t.close();			//closes any necessary resources
		t.unsubscribe();	//un-subscribes from channels
	}
	
	public final void rescheduleTask(String key, long delay) {
		Taskable t = taskRegistry.get(key);
		if(t.isTimeDriven()) {
			try {
				Kernel.getInstance().getScheduler().reschedule(t, delay);
			} catch (SchedulingException e) {
				Kernel.getInstance().getSyslog().error("Unable to reschedule task with a delay of " + delay + ".");
			}
		}
	}
	
	protected final void unsubscribe() {
		Kernel.getInstance().getStream().unsubscribe(subscription);
		subscription.getFiber().dispose();
	}
	
	/**
	 * Returns if the queried task name is currently in this service.
	 * @param taskName
	 * @return
	 */
	protected final boolean hasTask(String taskName){
		if(taskList().contains(taskName)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * This method stops the Service. It first removes all of its currently executing tasks, then
	 * unsubscribes from the system control channel. Note: the Service does not die fully! It still listens
	 * to be restarted.
	 */
	protected final void stop(){
		// kill all tasks
		for(FastMap.Entry<String, Taskable> entry = taskRegistry.head(), end = taskRegistry.tail(); (entry = entry.getNext())!= end; ){
			removeTask(entry.getKey());
		}
	}
	
	protected abstract void restartService();
	protected abstract void stopService();
	
	protected void processSysCtrl(ControlPacket ctrlPkt){
		String targetComponent = ctrlPkt.getComponentToControl();

		if(targetComponent.equals(this.serviceName)){
			System.out.println(targetComponent + " requested to " + ctrlPkt.getControl() 
					+ " by " + ctrlPkt.getReqComponent());
			// services can only RESTART or STOP
			switch(ctrlPkt.getControl()){
			case RESCHEDULE: 
				Kernel.getInstance().getSyslog().error(this.getClass().getSimpleName() + " cannot " + ctrlPkt.getControl());
				break;
			case STOP:
				Kernel.getInstance().getSyslog().debug("Stopping " + this.getClass().getSimpleName());
				stopService();
				break;
			case RESTART:
				restartService();
				break;
			}
		}
		else{
			// check if a device task needs to be controlled
			if(this.hasTask(targetComponent)){
				switch(ctrlPkt.getControl()){
				case RESCHEDULE: 
					Kernel.getInstance().getSyslog().debug("Reschedule " + targetComponent + " with delay "
							+ ctrlPkt.getDelay());
					rescheduleTask(targetComponent, ctrlPkt.getDelay());
					break;
				case STOP:
					Kernel.getInstance().getSyslog().debug("Stopping " + targetComponent);
					removeTask(targetComponent);
					break;
				case RESTART:
					Kernel.getInstance().getSyslog().debug("Restart " + targetComponent);
					//TODO: implement the restarting of a task
					break;
				}
			}
			// new Task -- from migration!
			if(ctrlPkt.getControl() == ControlOption.START){
				startTask(targetComponent);
			}
		}

	}
	
	public abstract void close();
	public abstract void startTask(String taskName);
	
}
