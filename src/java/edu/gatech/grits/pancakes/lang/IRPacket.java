package edu.gatech.grits.pancakes.lang;

public class IRPacket extends Packet { /**
	 * 
	 */
	private static final long serialVersionUID = -2583593406130700951L;

// HardwarePacket<Float> {
	
	
	public IRPacket() {
		super(PacketType.IR);
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
	
	public final void setIRReadings(double[] ranges) {
		for(int i=0; i<ranges.length; i++) {
			this.add("ir" + Integer.valueOf(i).toString(), Double.valueOf(ranges[i]));
		}
		
		return;
	}
	
}