package edu.gatech.grits.pancakes.devices.driver.k3;


import org.swig.k3i.k3i;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.backend.K3Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.IRPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import flanagan.interpolation.CubicSpline;

public class IRDriver implements HardwareDriver<IRPacket> {

	private K3Backend backend;
	private CubicSpline cinterp;
	
	public IRDriver(Backend backend) {
		this.backend = (K3Backend) backend;
		
		double[] distance = { 0.0, 0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 25.0 };
		double[] measurement = { 4000.0, 3945.0, 1342.0, 163.0, 38.0, 22.0, 12.0, 0.0 };
		
		cinterp = new CubicSpline(measurement, distance);
	}
	
	public void request(IRPacket pkt) {
		// does nothing, since IR is not an actuator
		return;
	}
	
//	public IRPacket query() {
//		for(Packet p : backend.update()) {
//			if(p.getPacketType().equals("ir"))
//				return (IRPacket) p;
//		}
//		
//		return new IRPacket();
//	}
	
	public IRPacket query() {
		backend.update();
		
		IRPacket pkt = new IRPacket();
		
		float[] readings = new float[9];
		
		for(int i=0; i<9; i++) {
			readings[i] = (float) cinterp.interpolate((double) k3i.irDistance(i));
		}
		
		pkt.setIRReadings(readings);
		
		return pkt;	
	}
	
	public void close() {
		// do nothing
	}
}
