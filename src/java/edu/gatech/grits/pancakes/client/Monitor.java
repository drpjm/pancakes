package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.NetworkNeighborPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.SonarPacket;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.NetworkService;
import edu.gatech.grits.pancakes.util.Properties;

public class Monitor extends Task {

	private long startTime;
	private boolean rescheduled;
	private final String id;
	
	public Monitor() {
		id = Kernel.id;
		startTime = System.currentTimeMillis();
		rescheduled = false;
		setDelay(0l);
		// looks like Java Swing listeners!
		Callback<Packet> data = new Callback<Packet>(){

			public void onMessage(Packet message) {
				Kernel.syslog.record(message);
				
				if(message.getPacketType().equals(PacketType.LOCAL_POSE)){
					LocalPosePacket local = (LocalPosePacket) message;
					local.debug();
				}
				
				if(message.getPacketType().equals(PacketType.NETWORK)){
					Kernel.syslog.debug("Received message from: " + ((NetworkPacket)message).getSource());
				}
				
				if(System.currentTimeMillis() - startTime > 10000 && !rescheduled){
//					publish(CoreChannel.CONTROL, new ControlPacket("devices", ControlPacket.RESCHEDULE, "localpose", 1500));
					publish(CoreChannel.CONTROL, new ControlPacket("network", ControlPacket.RESCHEDULE, "speaker", 3000));
					rescheduled = true;
				}
			}
			
		};
		
		Callback<Packet> neighbors = new Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message.getPacketType().equals(PacketType.NEIGHBOR)){
					NetworkNeighborPacket npkt = (NetworkNeighborPacket) message;
					if(!npkt.isExpired()){
						Kernel.syslog.debug("Monitor: got a new neighbor!");
						
						//send a reply to the neighbor
						String target = npkt.getNeighbor().getID();
						if(!target.equals(id)){
							NetworkPacket np = new NetworkPacket(Kernel.id, target);
							publish(CoreChannel.NETWORK, np);
						}
					}
					
				}
			}
			
		};
		
		subscribe(CoreChannel.SYSTEM, data);
		subscribe(NetworkService.NEIGHBORHOOD, neighbors);
	}

	public void close() {
		// do nothing
	}

	public void run() {
		// do nothing
	}

}
