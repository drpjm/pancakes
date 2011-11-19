package edu.gatech.grits.pancakes.devices.driver.player;

import javaclient3.RangerInterface;
import javaclient3.structures.PlayerConstants;
import javaclient3.structures.ranger.PlayerRangerData;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.PlayerBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.IRPacket;

public class IRDriver implements HardwareDriver<IRPacket> {

	private RangerInterface device;
	private PlayerBackend backend;
	
	public IRDriver(Backend backend) {
		this.backend = (PlayerBackend) backend;
		device = ((PlayerBackend) backend).getHandle().requestInterfaceRanger(0, PlayerConstants.PLAYER_OPEN_MODE);
	}
	
	public void request(IRPacket pkt) {
		// does nothing, since IR is not an actuator
		return;
	}
	
	public IRPacket query() {
		IRPacket pkt = new IRPacket(); //16);
		
		backend.update();
		
		if(device.isDataReady()) {
			PlayerRangerData data = device.getData();
			double[] ranges = data.getRanges();
			
			pkt.setIRReadings(ranges);
			
		} else {
			//System.out.println("IR data NOT ready!");
		}
		return pkt;
	}
	
	public void close() {
		// do nothing
	}
}
