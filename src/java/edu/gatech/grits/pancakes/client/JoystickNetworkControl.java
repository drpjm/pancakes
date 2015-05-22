package edu.gatech.grits.pancakes.client;

import javolution.util.FastList;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.JoystickPacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.NetworkNeighbor;
import edu.gatech.grits.pancakes.lang.NetworkNeighborPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.NetworkService;

public class JoystickNetworkControl extends Task {
	
	private volatile JoystickPacket jsPkt = null;
	private volatile FastList<String> neighborIDs = new FastList<String>();
	
	public JoystickNetworkControl() {
		setDelay(500l);
		
		Callback<Packet> neighborCbk = new Callback<Packet>() {
			
			public void onMessage(Packet message) {
				if(message instanceof NetworkNeighborPacket) {
					NetworkNeighborPacket pkt = (NetworkNeighborPacket) message;
					NetworkNeighbor n = pkt.getNeighbor();
					if(!pkt.isExpired()) {
						if(!neighborIDs.contains(n.getID())) {
							neighborIDs.add(n.getID());
							Kernel.getInstance().getSyslog().debug("Detected agent (" + n.getID() + ") on network.");
						}
					} else {
						neighborIDs.remove(n.getID());
						//jsPkt = null;
						Kernel.getInstance().getSyslog().debug("No longer detecting agent (" + n.getID() + ") on network.");
					}
				}
			}
		};
		subscribe(NetworkService.NEIGHBORHOOD, neighborCbk);
		
		
		Callback<Packet> jsCbk = new Callback<Packet>() {
			
			public void onMessage(Packet message) {
				if(message instanceof JoystickPacket) {
					jsPkt = (JoystickPacket) message;
					//System.out.println("Got joystick!");
				} else if(message instanceof NetworkPacket) {
					jsPkt = (JoystickPacket) ((NetworkPacket) message).getPayloadPackets().getFirst();
					control();
				}
			}
		};
		subscribe(CoreChannel.SYSTEM, jsCbk);
		
		
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Kernel.getInstance().getSyslog().debug("Firing!");
		for(String id : neighborIDs) {
			NetworkPacket pkt = new NetworkPacket(Kernel.getInstance().getId(), id);
			pkt.addPayloadPacket(packit());
			try {
				Kernel.getInstance().getStream().publish(CoreChannel.NETWORK, pkt);
			} catch (CommunicationException e) {
				Kernel.getInstance().getSyslog().error("Unable to send packet to agent (" + id + ").");
			}
		}
	}
	
	private MotorPacket packit() {
		MotorPacket pkt = new MotorPacket();
		if(jsPkt != null) {
			int x = (int) jsPkt.getPositionX();
			int y = (int) jsPkt.getPositionY();
			int b1 = (int) jsPkt.getPushButton1();
			int b2 = (int) jsPkt.getPushButton2();
			
			if((x > -1 && x < 170) && (y > -1 && y < 170)) {
				
				//pkt = new MotorPacket();
				pkt.setVelocity(-0.3f*((float) (x-67))/(160-67));
				pkt.setRotationalVelocity(-0.9f*((float) (y-71)/(165-71)));
			}
		}
		return pkt;
	}
	
	public void control() {
		if(jsPkt != null) {
			int x = (int) jsPkt.getPositionX();
			int y = (int) jsPkt.getPositionY();
			int b1 = (int) jsPkt.getPushButton1();
			int b2 = (int) jsPkt.getPushButton2();
			
			if((x > -1 && x < 170) && (y > -1 && y < 170)) {
				
				MotorPacket pkt = new MotorPacket();
				pkt.setVelocity(-0.3f*((float) (x-67))/(160-67));
				pkt.setRotationalVelocity(-0.9f*((float) (y-71)/(165-71)));
				
				try {
					Kernel.getInstance().getStream().publish(CoreChannel.CTRL, pkt);
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
