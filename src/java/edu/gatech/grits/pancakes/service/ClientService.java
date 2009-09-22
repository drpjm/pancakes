package edu.gatech.grits.pancakes.service;

import edu.gatech.grits.pancakes.client.Monitor;
import edu.gatech.grits.pancakes.client.ScanThreat;
import edu.gatech.grits.pancakes.lang.Packet;

public class ClientService extends Service {

	public ClientService() {
		super("Client");
		// TODO Auto-generated constructor stub
		addTask("monitor", new Monitor());
		addTask("scanThreat", new ScanThreat());
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(Packet pkt) {
		// TODO Auto-generated method stub

	}

}
