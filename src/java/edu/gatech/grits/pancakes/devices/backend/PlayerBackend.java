package edu.gatech.grits.pancakes.devices.backend;

import javaclient2.PlayerClient;
import javaclient2.structures.PlayerConstants;

public class PlayerBackend extends Backend {

	private PlayerClient handle;
	
	public PlayerBackend(int port) {
		super("player");
		
		handle = new PlayerClient("127.0.0.1", port);

		handle.runThreaded(-1, -1);
		
		handle.requestDataDeliveryMode(PlayerConstants.PLAYER_DATAMODE_PULL);
		handle.readAll();
		handle.requestAddReplaceRule(-1, -1, PlayerConstants.PLAYER_MSGTYPE_DATA, -1, 1);
		handle.readAll();
		
	}
	
	@Override
	public PlayerClient getHandle() {
		return handle;
	}
	
	public void update() {
		handle.readAll();
	}

}
