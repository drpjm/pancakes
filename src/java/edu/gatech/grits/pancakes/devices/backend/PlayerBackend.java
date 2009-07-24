package edu.gatech.grits.pancakes.devices.backend;

import java.util.logging.Level;

import edu.gatech.grits.pancakes.core.Kernel;
import javaclient2.PlayerClient;
import javaclient2.structures.PlayerConstants;

public class PlayerBackend extends Backend {

	private PlayerClient handle;
	
	public PlayerBackend(int port) {
		super("player");
		
		handle = new PlayerClient("127.0.0.1", port);
		//handle.runThreaded(-1, -1);
		
		
		handle.requestDataDeliveryMode(PlayerConstants.PLAYER_DATAMODE_PULL);
		handle.readAll();
		handle.requestAddReplaceRule(-1, -1, PlayerConstants.PLAYER_MSGTYPE_DATA, -1, 1);
		handle.readAll();
		
		//handle.start();
		//handle.runThreaded(-1, -1);
		//handle.run();
		//handle.readAll();
		
		
		//handle.run();
		//Kernel.syslog.log(new LogPacket(this, Level.INFO, "PlayerBackend has started."));
//		int retry = 1;
//		
//		while(!handle.isReadyRequestDevice()) {
//			Kernel.syslog.debug("Starting retry " + retry + " of 3.");
//			Kernel.syslog.debug("Waiting for 1s to authenticate...");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			retry++;
//			if(retry > 3) {
//				Kernel.syslog.debug("Exiting application.");
//				System.exit(0);
//			}
//		}
	}
	
	@Override
	public PlayerClient getHandle() {
		return handle;
	}
	
	public void update() {
		handle.readAll();
	}
	
	public void finalize() {
		handle.runThreaded(-1, -1);
	}

}
