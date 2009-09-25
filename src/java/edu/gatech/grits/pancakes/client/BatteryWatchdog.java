package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.BatteryPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;

public class BatteryWatchdog extends Task {
	
	public BatteryWatchdog() {
		setDelay(0l);
		
		Callback<Packet> data = new Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message.getPacketType().equals(PacketType.BATTERY)){
					BatteryPacket battery = (BatteryPacket) message;
					
					battery.debug();
					
					if(battery.getVoltage() > 7.7f) {
						// battery high
						
					} else if(battery.getVoltage() > 7.2) {
						// battery medium
						
					} else {
						// battery low
						
					}
					
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
