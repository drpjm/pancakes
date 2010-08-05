package edu.gatech.grits.pancakes.client;

import javolution.util.FastList;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.*;
import edu.gatech.grits.pancakes.service.ControlOption;
import edu.gatech.grits.pancakes.service.NetworkService;

public class DynamicControlTask extends Task {

	private BatteryLevel currBatteryLevel;
	private RunningState currRunningState = RunningState.IDLE;
	private final float MAX_VOLTAGE = 8.4f;
	private final float MIN_VOLTAGE = 6.6f;
	private float voltageRange;
	private boolean initBattLvlSet = false;
	private float initBattLvl;

	public DynamicControlTask(){
		setDelay(1000l);
		getRequiredDevices().add("battery");
		currBatteryLevel = BatteryLevel.NONE;
		
		// gets new battery data
		Callback<Packet> batteryCbk = new Callback<Packet>(){

			@Override
			public void onMessage(Packet message) {

				if(message instanceof BatteryPacket){
					final float volts = ((BatteryPacket)message).getVoltage();
//					Kernel.syslog.debug(Float.toString(volts));
					if(!initBattLvlSet){
						if(volts < MAX_VOLTAGE && volts > MIN_VOLTAGE){
							initBattLvl = volts;
							initBattLvlSet = true;
							voltageRange = initBattLvl - MIN_VOLTAGE;
							Kernel.syslog.debug(DynamicControlTask.class.getSimpleName() + ": battery levels set: " + initBattLvl + ", range: " + voltageRange);
						}
					}
					else{
						// calculate % power available - set current power mode
						float percentAvail = ((volts - MIN_VOLTAGE) / voltageRange) * 100 ;
						Kernel.syslog.debug(DynamicControlTask.class.getSimpleName() + ": battery @ " + percentAvail + " %");
						
						if(percentAvail > 90f){
							currBatteryLevel = BatteryLevel.HIGH;
						}
						else if(percentAvail < 90f && percentAvail > 65f){
							currBatteryLevel = BatteryLevel.MED; 
						}
						else {
							currBatteryLevel = BatteryLevel.LOW;
						}
						
					}
				}

			}

		};
		subscribe(CoreChannel.SYSTEM, batteryCbk);
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {

		Kernel.syslog.debug("running state = " + currRunningState);
//		Kernel.syslog.debug(DynamicControlTask.class.getSimpleName() + ": level = " + currBatteryLevel);
		
		switch(currBatteryLevel){

		case NONE:
			break;
			
		case LOW: 
			
			if(currRunningState == RunningState.GOING_HOME){
				ControlPacket ctrl = new ControlPacket(ControlOption.STOP, GoToGoal.class.getSimpleName(), this.getClass().getSimpleName());
				publish(CoreChannel.SYSCTRL, ctrl);
			}
			
			break;

		case MED: 
			
			if(currRunningState == RunningState.SCAN_THREAT){
				
				ControlPacket ctrl1 = new ControlPacket(ControlOption.STOP, ScanThreat.class.getSimpleName(), this.getClass().getSimpleName());
				publish(CoreChannel.SYSCTRL, ctrl1);
				
				// migrate to neighbors and stop ScanThreat
				MigrationPacket migrate = new MigrationPacket();
				migrate.setTaskName(ScanThreat.class.getSimpleName());
				FastList<String> reqDevices = new FastList<String>();
				reqDevices.add("localpose");
				reqDevices.add("motor");
				migrate.setRequiredDevices(reqDevices);
				publish(NetworkService.MIGRATE, migrate);
				
				// start to go home!
				currRunningState = RunningState.GOING_HOME;
				ControlPacket ctrl2 = new ControlPacket(ControlOption.START, GoToGoal.class.getSimpleName(), this.getClass().getSimpleName());
				publish(CoreChannel.SYSCTRL, ctrl2);
			}
			
			break;

		case HIGH: 

			if(currRunningState == RunningState.IDLE){
				// start ScanThreat
				ControlPacket ctrl = new ControlPacket(ControlOption.START, ScanThreat.class.getSimpleName(), this.getClass().getSimpleName());
				publish(CoreChannel.SYSCTRL, ctrl);
				currRunningState = RunningState.SCAN_THREAT;
			}

			break;
		}

	}

	private enum BatteryLevel {
		LOW,MED,HIGH,NONE;
	}

	private enum RunningState {
		SCAN_THREAT,MIGRATING,GOING_HOME,IDLE;
	}
}
