package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Syslog;
import edu.gatech.grits.pancakes.lang.BatteryPacket;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.ClientService;

public class BatteryWatchdog extends Task {
	
	private float BATTERY_LEVEL = 7.7f;
	private long DELAY = 250l;
		
	public BatteryWatchdog() {
		
		setDelay(0l);
		
		
		Callback<Packet> data = new Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message.getPacketType().equals(PacketType.BATTERY)){
					BatteryPacket battery = (BatteryPacket) message;
					
					//battery.debug();
					Kernel.syslog.record(battery);
					
					if(battery.getVoltage() < BATTERY_LEVEL) {
						Kernel.syslog.debug("Battery below threshold -- RESCHEDULE!");
						BATTERY_LEVEL -= 0.5f;
						DELAY += 750l;
						publish(CoreChannel.CONTROL, new ControlPacket("devices", ControlPacket.RESCHEDULE, "localpose", DELAY));
						publish(ClientService.BATTERY_UPDATE, new ControlPacket(getClass().getSimpleName(), "new", "duh"));
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
