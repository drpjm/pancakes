package edu.gatech.grits.pancakes.structures;

public class IRPacket extends Packet { /**
	 * 
	 */
	private static final long serialVersionUID = -2583593406130700951L;

// HardwarePacket<Float> {
	
	
	public IRPacket() {
		super("ir");
	}
	
	public final float getIRReading(int index) {
		return Float.valueOf(this.get("ir" + Integer.valueOf(index).toString())).floatValue();
	}

	public final float[] getIRReadings() {
		float[] readings = new float[this.getSize()];
		
		for(int i=0; i<this.getSize(); i++) {
			readings[i] = Float.valueOf(this.get("ir" + Integer.valueOf(i).toString())).floatValue();
		}
		
		return readings;
	}
	
	public final void setIRReadings(float[] readings) {
		for(int i=0; i<readings.length; i++) {
			this.add("ir" + Integer.valueOf(i).toString(), Float.valueOf(readings[i]));
		}
		
		return;
	}
	
}