package edu.gatech.grits.pancakes.devices.driver.player;

import javaclient2.IRInterface;
import javaclient2.structures.PlayerConstants;
import javaclient2.structures.ir.PlayerIrData;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.PlayerBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.IRPacket;

public class IRDriver implements HardwareDriver<IRPacket> {

	private IRInterface device;
	private PlayerBackend backend;
	
	public IRDriver(Backend backend) {
		this.backend = (PlayerBackend) backend;
		device = ((PlayerBackend) backend).getHandle().requestInterfaceIR(0, PlayerConstants.PLAYER_OPEN_MODE);
	}
	
	public void request(IRPacket pkt) {
		// does nothing, since IR is not an actuator
		return;
	}
	
	public IRPacket query() {
		IRPacket pkt = new IRPacket(); //16);
		
		backend.update();
		
		if(device.isDataReady()) {
			PlayerIrData data = device.getData();
			float[] ranges = data.getRanges();
			
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
