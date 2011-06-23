package edu.gatech.grits.pancakes.devices.backend;

import javaclient3.PlayerClient;
import javaclient3.structures.PlayerConstants;

public class PlayerBackend extends Backend {

	private PlayerClient handle;
	
	public PlayerBackend(int port) {
		super("player");
		
		handle = new PlayerClient("127.0.0.1", port);
		
		handle.requestDataDeliveryMode(PlayerConstants.PLAYER_DATAMODE_PULL);
		handle.requestAddReplaceRule(-1, -1, PlayerConstants.PLAYER_MSGTYPE_DATA, -1, 1);
	}
	
	@Override
	public PlayerClient getHandle() {
		return handle;
	}
	
	public void update() {
		handle.readAll();
	}
	
	public void complete() {
		handle.runThreaded(50, 0);
	}
	
	public void close() {
		handle.close();
	}

}
