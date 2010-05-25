package edu.gatech.grits.pancakes.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javolution.util.FastMap;

import org.jetlang.fibers.Fiber;
import org.jetlang.fibers.PoolFiberFactory;

public class Scheduler {
	
	private final int CORE_POOL_SIZE = 16;
	public static final TimeUnit stdTimeUnit = TimeUnit.MILLISECONDS;
	private final ExecutorService executor = Executors.newCachedThreadPool();	
	private final PoolFiberFactory factory = new PoolFiberFactory(executor);
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
	
	private FastMap<Runnable, ScheduledFuture<?>> taskList = new FastMap<Runnable, ScheduledFuture<?>>();	
	
	public synchronized String execute(String command) {

		String s = null, result = null;
		
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // read the output from the command
            
			
            while ((s = stdInput.readLine()) != null && result == null) {
            	result = s;
            }
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
        
        
        return result;
	}
	
	public final void execute(Runnable r) {
		executor.execute(r);
	}
	
	public final Fiber newFiber() {
		return factory.create();
	}
	
	public final synchronized void schedule(Runnable r, long delay) throws SchedulingException {
		if(!taskList.keySet().contains(r)) {
			taskList.put(r, scheduler.scheduleAtFixedRate(r, 0, delay, stdTimeUnit));
		} else {
			throw new SchedulingException("Task has already been previously scheduled.");
		}
	}
	
	public final void reschedule(Runnable r, long delay) throws SchedulingException{
		cancel(r);
		schedule(r, delay);
	}
	
	public final long delay(Runnable r) throws SchedulingException {
		ScheduledFuture<?> sf = null;
		
		synchronized(this) {
			sf = taskList.get(r);
		}
		
		if(sf != null) {
			return sf.getDelay(stdTimeUnit);
		} else {
			throw new SchedulingException("Task has not been previously scheduled.");
		}
	}
	
	public final void cancel(Runnable r) throws SchedulingException {
		ScheduledFuture<?> sf = null;
		
		synchronized(this) {
			sf = taskList.get(r);
		}
		
		if(sf != null) {
			sf.cancel(false);
			synchronized(this) {
				taskList.remove(r);
			}
		} else {
			throw new SchedulingException("Task has not been previously scheduled.");
		}
	}
	
	public final void quit() {
		factory.dispose();
		executor.shutdown();
		scheduler.shutdown();
	}
	
	public class SchedulingException extends Exception {
		
		private static final long serialVersionUID = 1564933062044694001L;

		public SchedulingException(String desc) {
			super(desc);
		}
	}
	
}