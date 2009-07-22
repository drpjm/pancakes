package edu.gatech.grits.pancakes.lang;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;


public class SafeQueue extends ArrayBlockingQueue<Packet> {

	private static final long serialVersionUID = 3066706691279574271L;
	private final static int DEFAULT_CAPACITY = 25;
	private final static boolean FAIRNESS = true;
	
	public SafeQueue() {
		super(DEFAULT_CAPACITY, FAIRNESS);
	}
	
	public SafeQueue(int capacity) {
		super(capacity, FAIRNESS);
	}
	
	public SafeQueue(int capacity, boolean fairness) {
		super(capacity, fairness);
	}
	
	public void enqueue(Packet pkt) {
		if(!offer(pkt)) {
			System.err.println("Failed to put object into the queue.");
		}
	}
	
	public void enqueueAll(ArrayList<Packet> packets) {
		for(Packet pkt : packets) {
			if(!offer(pkt)) {
				System.err.println("Failed to put packet into the queue.");
			}
		}
	}
	
	public Packet dequeue() {
		Packet pkt = this.poll();
//		if(pkt == null) {
//			System.err.println("Failed to retrieve object from queue.");
//		}
		return pkt;
	}
	
	public ArrayList<Packet> drain() {
		ArrayList<Packet> pkts = new ArrayList<Packet>(size());
		drainTo(pkts);
		return pkts;
	}
	
}
