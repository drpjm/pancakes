package edu.gatech.grits.pancakes.lang;

import java.util.Date;

public class NetworkNeighbor {
	
	private String HOSTNAME;
	private int NETWORK_PORT;
	private String ID;
	private Date TIMESTAMP;
	
	public NetworkNeighbor(String id, String hostname, int network_port, Date timestamp) {
		ID = id;
		HOSTNAME = hostname;
		NETWORK_PORT = network_port;
		TIMESTAMP = timestamp;
	}
	
	public String getHostname() {
		return HOSTNAME;
	}
	
	public String getID() {
		return ID;
	}
	
	public int getNetworkPort() {
		return NETWORK_PORT;
	}
	
	public Date getTimestamp() {
		return TIMESTAMP;
	}
	
	public void setHostname(String hostname) {
		HOSTNAME = hostname;
		return;
	}
	
	public void setID(String id) {
		ID = id;
		return;
	}
	
	public void setNetworkPort(int network_port) {
		NETWORK_PORT = network_port;
		return;
	}
}
