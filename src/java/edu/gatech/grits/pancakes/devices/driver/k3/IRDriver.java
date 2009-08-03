package edu.gatech.grits.pancakes.devices.driver.k3;


import org.swig.k3i.k3i;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.IRPacket;
import flanagan.interpolation.CubicSpline;

public class IRDriver implements HardwareDriver<IRPacket> {

	CubicSpline cinterp = null;
	
	public IRDriver(Backend backend) {
		
		double[] distance = { 0.0, 0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 25.0 };
		double[] measurement = { 4000.0, 3945.0, 1342.0, 163.0, 38.0, 22.0, 12.0, 0.0 };
		
		cinterp = new CubicSpline(measurement, distance);
	}
	
	public void request(IRPacket pkt) {
		// does nothing, since IR is not an actuator
		return;
	}
	
	public IRPacket query() {
		IRPacket pkt = new IRPacket(); //9);
		
		//k3ctrl.proxIR(2, null, null);
		//System.out.println(k3ctrl.getIrdata());
		
		float ranges[] = new float[9];
		
		for(int i=0; i<8; i++) {
			double reading = (double) k3i.queryIR(i);
			
			if(reading < 0.0 || reading > 4000.0) {
				ranges[i] = -1.0f;
			} else {
				//System.out.println(reading);
				ranges[i] = (float) cinterp.interpolate(reading);
				ranges[i] /= 100.0f;
			}
		}
		
		pkt.setIRReadings(ranges);
		
		//pkt.setDataReady(true);
		
		return pkt;
	}
}
