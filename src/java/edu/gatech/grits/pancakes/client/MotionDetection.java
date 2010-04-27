package edu.gatech.grits.pancakes.client;

import javolution.util.FastList;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotionPacket;
import edu.gatech.grits.pancakes.lang.NetworkNeighbor;
import edu.gatech.grits.pancakes.lang.NetworkNeighborPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.NetworkService;

public class MotionDetection extends Task {

	private FastList<String> neighborIds = new FastList<String>();
	private long lastUpdate = System.currentTimeMillis();
	private long timeout = 2000l;
	private LocalPosePacket currLocation = null;
	
	public void close() {
		// TODO Auto-generated method stub
		unsubscribe();
	}

	public void run() {
		// TODO Auto-generated method stub

	}
	
	public MotionDetection() {
		setDelay(0l);
		
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet message) {
				if(message instanceof MotionPacket) {
				
					MotionPacket pkt = (MotionPacket) message;
					pkt.debug();
					if(System.currentTimeMillis() > lastUpdate + timeout) {
						synchronized(this) {
							if(currLocation != null) {
								for(String id : neighborIds) {
									NetworkPacket net = new NetworkPacket(Kernel.id, id);
									net.addPacket(currLocation);
									publish(CoreChannel.NETWORK, net);
									System.out.println("*** Sent motion update! ***");
								}
							}
						}
						lastUpdate = System.currentTimeMillis();
					}
				} else if(message instanceof LocalPosePacket) {
					LocalPosePacket pkt = (LocalPosePacket) message;
					synchronized(this) {
						currLocation = pkt;
						System.out.println("*** I have a new location! ***");
					}
				}
			}
		};
		
		Callback<Packet> neighborCbk = new Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message instanceof NetworkNeighborPacket){
					NetworkNeighborPacket pkt = (NetworkNeighborPacket) message;
					NetworkNeighbor n = pkt.getNeighbor();
					if(!pkt.isExpired()){
						// add
						synchronized(this) {
							if(!neighborIds.contains(n.getID())){
								neighborIds.add(n.getID());
							}
						}
					}
					else{
						// remove
						synchronized(this) {
							neighborIds.remove(n.getID());
						}
					}
				}
				
			}
		};
		
		
		subscribe(NetworkService.NEIGHBORHOOD, neighborCbk);
		subscribe(CoreChannel.SYSTEM, callback);
		
		System.out.println("Ready to detect motion!");
	}

}
