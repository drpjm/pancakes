package edu.gatech.grits.pancakes.client;

import javolution.util.FastList;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.*;
import edu.gatech.grits.pancakes.service.ClientService;
import edu.gatech.grits.pancakes.service.NetworkService;

/**
 * This scheduled task shares information with any neighbors on the network.
 * @author pmartin
 *
 */
public class LocalPoseShare extends Task {

	private FastList<String> neighborIds;
	private LocalPosePacket currLocalPose;
	private long time;
	private TaskState currentState;
	
	private int rollValue;
	
	public static final String ROLL = "roll";
	public static final String SWITCH = "switch";
	
	public LocalPoseShare(){
		setDelay(250l);
		
		currentState = TaskState.INIT;
		neighborIds = new FastList<String>();
		currLocalPose = new LocalPosePacket();
		// current die roll
		rollValue = (int) Math.floor(Math.random()*20);
		
		// handles current network neighborhood updates
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
						// while in INIT, send the message with roll value
						if(currentState == TaskState.INIT){
							Kernel.syslog.debug("I see " + n.getID() + ". Start playing...");
							NetworkPacket rollPkt = new NetworkPacket(Kernel.id, n.getID());
							Packet p = new Packet(ROLL);
							p.add("value", rollValue);
							rollPkt.addPacket(p);
							publish(CoreChannel.NETWORK, rollPkt);
							currentState = TaskState.PLAY;
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
		
		Callback<Packet> localCbk = new Callback<Packet>(){

			public void onMessage(Packet message) {
				
				if(message instanceof LocalPosePacket){
					currLocalPose = (LocalPosePacket)message;
				}
				else if(message instanceof NetworkPacket){
					
					NetworkPacket inNetPkt = (NetworkPacket) message;
					
					// PLAY handshaking!
					Packet firstPkt = inNetPkt.getPackets().getFirst();
					if(firstPkt.getPacketType().equals(ROLL) && currentState == TaskState.PLAY){
						int neighborRoll = Integer.valueOf(firstPkt.get("value"));
//						Kernel.syslog.debug("Received a " + firstPkt.getPacketType() + " packet: " + neighborRoll);
						
						if(neighborRoll > rollValue){
							Kernel.syslog.debug(Kernel.id + " is waiting.");
							currentState = TaskState.WAITING;
						}
						else if(neighborRoll < rollValue){
							Kernel.syslog.debug(Kernel.id + " is starting.");
							currentState = TaskState.SENDING;
						}
						else{
							// roll again
							Kernel.syslog.debug("Play again!");
							rollValue = (int) Math.floor(Math.random()*20);
						}
						NetworkPacket rollPkt = new NetworkPacket(Kernel.id, inNetPkt.getSource());
						Packet p = new Packet(ROLL);
						p.add("value", rollValue);
						rollPkt.addPacket(p);
						publish(CoreChannel.NETWORK, rollPkt);
					}
					
					// Exchange of information
					if(firstPkt.getPacketType().equals(PacketType.LOCAL_POSE)){
						if(currentState == TaskState.WAITING){
//							Kernel.syslog.debug("Receiving local pose from " + inNetPkt.getSource());
							synchronized(this){
								currentState = TaskState.SENDING;
							}							
						}
					}
					
				}
			}
			
		};
		subscribe(CoreChannel.SYSTEM, localCbk);
		
		Callback<Packet> batteryPkt = new  Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message instanceof ControlPacket){

					if(((ControlPacket)message).getControl().equals("MED")){
						// send message to my neighbor
						NetworkPacket switchOut = new NetworkPacket(Kernel.id, neighborIds.getFirst());
						Packet sw = new Packet(SWITCH);
						switchOut.addPacket(sw);
						publish(CoreChannel.NETWORK, switchOut);
					}

				}
			}

		};
		subscribe(ClientService.BATTERY_UPDATE, batteryPkt);

	}
	
	public void close() {
		// TODO Auto-generated method stub

	}

	public final void run() {
		
		if(currentState == TaskState.SENDING){
			NetworkPacket outNetPkt = new NetworkPacket(Kernel.id, neighborIds.getFirst());
			outNetPkt.addPacket(currLocalPose);
			synchronized(this){
				currentState = TaskState.WAITING;
			}
			publish(CoreChannel.NETWORK, outNetPkt);
		}
	}

	private enum TaskState {
		INIT,PLAY,SENDING,WAITING;
	}
}
