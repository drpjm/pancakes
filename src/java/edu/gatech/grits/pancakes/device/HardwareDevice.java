package edu.gatech.grits.pancakes.device;

import edu.gatech.grits.pancakes.structures.Packet;

public interface HardwareDevice {
	
	public void debug();
	public Packet query();
	public void request(Packet pkt);
	
}
