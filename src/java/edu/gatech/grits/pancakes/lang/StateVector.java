package edu.gatech.grits.pancakes.lang;

import java.util.Date;

import javolution.util.FastMap;

public class StateVector {
	
	private FastMap<String, Packet> stateVector;
	private final Date timeStamp = new Date(System.currentTimeMillis());
	
	public StateVector() {
		stateVector.setShared(true);
	}
	
	public void add(String key, Packet pkt) {
		stateVector.put(key, pkt);
	}
	
	public Packet get(String key) {
		return stateVector.get(key);
	}
	
	public Date getTimestamp() {
		return timeStamp;
	}
}