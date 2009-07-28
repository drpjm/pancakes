package edu.gatech.grits.pancakes.agent.mode;

import javolution.util.FastList;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.StateVector;

public class StateVectorMode {
	
	private Fiber fiber = Kernel.scheduler.newFiber();
	private long delay = 1000l;
	
	private FastList<StateVector> stateVectors;
	private StateVector currentState = new StateVector();
		
		
	public StateVectorMode(int history) {
		fiber.start();
		
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				currentState.add(pkt.getPacketType(), pkt);
			}
		};
		
		try {
			Kernel.stream.subscribe("system", fiber, callback);
		} catch (CommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		stateVectors = new FastList<StateVector>(history);
		
		Runnable task = new Runnable() {
			public void run() {
				stateVectors.add(currentState);
			}
		};
		
		try {
			Kernel.scheduler.schedule(task, delay);
		} catch (SchedulingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final void setDelay(long d) {
		delay = d;
	}
	
	

}
