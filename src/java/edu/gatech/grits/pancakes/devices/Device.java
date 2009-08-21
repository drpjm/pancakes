package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.lang.Packet;

public interface Device {
	
	public void debug();
	public Packet query();
	public void request(Packet pkt);
	public boolean isRunnable();
	public long delay();
	public void close();
}
