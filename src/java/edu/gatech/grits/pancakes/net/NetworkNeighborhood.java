package edu.gatech.grits.pancakes.net;

import java.util.Date;
import javolution.util.FastList;
import javolution.util.FastMap;
import edu.gatech.grits.pancakes.lang.NetworkNeighbor;

public class NetworkNeighborhood implements Runnable {

	private final long TIMEOUT = 10000; // 10s
	private FastMap<String, NetworkNeighbor> neighbors = new FastMap<String, NetworkNeighbor>(10);

	public synchronized void addNeighbor(String id, NetworkNeighbor n) {
		neighbors.put(id, n);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Date d = new Date(System.currentTimeMillis()-TIMEOUT);
		FastList<String> expired = new FastList<String>(neighbors.size());
		
		for(String key : neighbors.keySet()) {
			NetworkNeighbor n;
			
			synchronized(this) {
				n = neighbors.get(key);
			}
			
			if(n.getTimestamp().before(d)) {
				expired.add(key);
			}
		}
		
		synchronized(this) {
			for(String key : expired) {
				neighbors.remove(key);
			}
		}
	}

}
