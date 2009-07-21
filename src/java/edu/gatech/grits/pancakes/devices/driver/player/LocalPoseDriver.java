package edu.gatech.grits.pancakes.devices.driver.player;

import javaclient2.Position2DInterface;
import javaclient2.structures.PlayerConstants;
import javaclient2.structures.PlayerPose;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.PlayerBackend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;

public class LocalPoseDriver implements HardwareDriver<LocalPosePacket> {
	
	private Position2DInterface device;
	private PlayerBackend backend;
	
	public LocalPoseDriver(Backend backend) {
		this.backend = (PlayerBackend) backend;
		device = ((PlayerBackend) backend).getHandle().requestInterfacePosition2D(0, PlayerConstants.PLAYER_OPEN_MODE);
		device.setControlMode(PlayerConstants.PLAYER_POSITION2D_REQ_VELOCITY_MODE);
	}
	
	public void request(LocalPosePacket pkt) {
		return;
	}
	
	public LocalPosePacket query() {
		LocalPosePacket pkt = new LocalPosePacket();
		
		//manager.update();
		
		backend.update();
		
		if(device.isDataReady()) {
			PlayerPose pose = device.getData().getPos();
			pkt.setPose(pose.getPx(), pose.getPy(), pose.getPa());
		} else {
			//System.out.println("NOT READY!");
		}
		
		return pkt;
	}
}
