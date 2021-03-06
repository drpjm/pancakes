package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.*;
import edu.gatech.grits.pancakes.lang.BatteryPacket;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.ClientService;

public class BatteryWatchdog extends Task {
	
	private final float HIGH_LEVEL = 7.9f;
	private final float MED_LEVEL = 7.65f;
	private final float LOW_LEVEL = 7.4f;
	private long localPoseDelay = 250l;
//	private boolean switched;
	private boolean isMed = false;
	private boolean isLow = false;
		
	public BatteryWatchdog() {
//		switched = false;
		setDelay(0l);
		
		Callback<Packet> data = new Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message.getPacketType().equals(PacketType.BATTERY)){
					BatteryPacket battery = (BatteryPacket) message;
					
					//battery.debug();
//					Kernel.getInstance().getSyslog().record(battery);
					float level = battery.getVoltage();
					if(!isMed && level < HIGH_LEVEL && level > MED_LEVEL ) {
						isMed = true;
						Kernel.getInstance().getSyslog().debug("Battery threshold: MED");
//						HIGH_LEVEL -= 0.5f;
						localPoseDelay = 500l;
//						publish(CoreChannel.SYSCTRL, new ControlPacket("device", ControlPacket.RESCHEDULE, 
//									"localpose", localPoseDelay));
//						// slow down neighbor updates
//						publish(CoreChannel.SYSCTRL, new ControlPacket("client", ControlPacket.RESCHEDULE, 
//									LocalPoseShare.class.getSimpleName().toLowerCase(), 500l));
//						
//						publish(ClientService.BATTERY_UPDATE, new ControlPacket(getClass().getSimpleName(), "MED", ""));
//						switched = true;
					}
					else if(!isLow && level < MED_LEVEL && level > LOW_LEVEL ){
						isLow = true;
						Kernel.getInstance().getSyslog().debug("Battery threshold: LOW");
//						publish(ClientService.BATTERY_UPDATE, new ControlPacket(getClass().getSimpleName(), "LOW", ""));
					}
//					else{
//						Kernel.getInstance().getSyslog().debug("Battery is WAY LOW!");
//					}
					
				}
			}
			
		};
		
		subscribe(CoreChannel.SYSTEM, data);
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
