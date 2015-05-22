package edu.gatech.grits.pancakes.client;

import javolution.util.FastList;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.MigrationPacket;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.ControlOption;
import edu.gatech.grits.pancakes.service.NetworkService;

public class MigrationTester extends Task {

	private int countDown = 10;
	
	public MigrationTester(){
		setDelay(500l);

	}
	
	@Override
	public void run() {
		countDown--;
		Kernel.getInstance().getSyslog().debug(this.getClass().getSimpleName() + " count: " + countDown);
		if(countDown == 0){
			MigrationPacket mp = new MigrationPacket();
			mp.setTaskName(ControlTester.class.getSimpleName());
			FastList<String> reqDevices = new FastList<String>();
			reqDevices.add("localpose");
			mp.setRequiredDevices(reqDevices);
			publish(NetworkService.MIGRATE, mp);
		}
		
		if(countDown < 0){
			ControlPacket cp = new ControlPacket(ControlOption.STOP, MigrationTester.class.getSimpleName(), MigrationTester.class.getSimpleName());
			publish(CoreChannel.SYSCTRL, cp);
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
