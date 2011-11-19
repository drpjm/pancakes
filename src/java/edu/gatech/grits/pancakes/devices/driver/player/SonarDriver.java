package edu.gatech.grits.pancakes.devices.driver.player;

import javaclient3.RangerInterface;
import javaclient3.structures.PlayerConstants;
import javaclient3.structures.ranger.PlayerRangerData;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.PlayerBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.SonarPacket;

public class SonarDriver implements HardwareDriver<SonarPacket> {

	private RangerInterface device;
	private PlayerBackend backend;
	
	public SonarDriver(Backend backend) {
		this.backend = (PlayerBackend) backend;
		while(!((PlayerBackend) backend).getHandle().isReadyRequestDevice()) {
			Kernel.getInstance().getSyslog().debug("Trying to get an interface for the SonarDevice.");
			device = ((PlayerBackend) backend).getHandle().requestInterfaceRanger(0, PlayerConstants.PLAYER_OPEN_MODE);
		}
		Kernel.getInstance().getSyslog().debug("Received an interface!");
//		if(device == null)
//			System.out.println("Driver failed to initialize!");
	}
	
	
	public void request(SonarPacket pkt) {
		// does nothing, since sonar is not an actuator
		return;
	}
	
	public SonarPacket query() {
		SonarPacket pkt = new SonarPacket(); //16);
		
		//manager.update();
		backend.update();
		
		if(device.isDataReady()) {
			//System.out.println("Sonar data ready!");
			PlayerRangerData data = device.getData();
			double[] ranges = data.getRanges();
			
			// time for a sanity check
			if(ranges.length != 16) {
				// pkt not ready
			} else {
				pkt.setSonarReadings(ranges);
			}
			
		} else {
			//System.out.println("Sonar data NOT ready!");
		}
		return pkt;
	}


	public void close() {
		// do nothing	
	}
}
