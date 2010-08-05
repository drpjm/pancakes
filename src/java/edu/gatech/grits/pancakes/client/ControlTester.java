package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.LocalPoseDevice;
import edu.gatech.grits.pancakes.devices.SonarDevice;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.ControlOption;
import edu.gatech.grits.pancakes.service.DeviceService;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.net.DiscoverySpeaker;


public class ControlTester extends Task {

	private int counter;
	private boolean slowSpeaker = false;

	public ControlTester(){
		counter = 0;
		setDelay(1000l);
		getRequiredDevices().add("localpose");

		Callback<Packet> myCbk = new Callback<Packet>(){

			@Override
			public void onMessage(Packet message) {
				if(message instanceof LocalPosePacket){
					Kernel.syslog.debug(this.getClass().getSimpleName() + " received localpose data.");
				}

			}

		};
		subscribe(CoreChannel.SYSTEM, myCbk);
	}

	@Override
	public void close() {
		
	}

	@Override
	public void run() {
		
		counter++;
		//		ControlPacket ctrlPkt = new ControlPacket(ControlOption.RESCHEDULE, DeviceService.class.getSimpleName(), this.getClass().getSimpleName());
		//		try {
		//			Kernel.stream.publish(CoreChannel.SYSCTRL, ctrlPkt);
		//		} catch (CommunicationException e) {
		//			e.printStackTrace();
		//		}

		if(counter > 10 && !slowSpeaker){
			ControlPacket discSpeakerCtrl = new ControlPacket(ControlOption.RESCHEDULE, DiscoverySpeaker.class.getSimpleName(), this.getClass().getSimpleName(), 2000);
			try {
				Kernel.stream.publish(CoreChannel.SYSCTRL, discSpeakerCtrl);
				slowSpeaker = true;
			} catch (CommunicationException e1) {
				e1.printStackTrace();
			}
		}

//		if(counter % 5 == 0){
//			long delay = 100;
//			delay = (long)counter * delay;
//			ControlPacket lpCtrlPkt = new ControlPacket(ControlOption.RESCHEDULE, LocalPoseDevice.class.getSimpleName(), this.getClass().getSimpleName(), delay);
//			ControlPacket sonarCtrlPkt = new ControlPacket(ControlOption.RESCHEDULE, SonarDevice.class.getSimpleName(), this.getClass().getSimpleName(), delay);
//			// try to send a sonar control packet
//			publish(CoreChannel.SYSCTRL, sonarCtrlPkt);
//			publish(CoreChannel.SYSCTRL, lpCtrlPkt);
//		}

	}

}
