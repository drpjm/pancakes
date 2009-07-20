package edu.gatech.grits.pancakes.structures;

public class SonarPacket extends Packet { /**
	 * 
	 */
	private static final long serialVersionUID = -1820788047016645217L;
	
	
	public SonarPacket() {
		super("sonar");
	}
	
	public final float getSonarReading(int index) {
		return Float.valueOf(this.get("sonar" + Integer.valueOf(index).toString())).floatValue();
	}
	
	@Deprecated
	public final String getSonarReading(String id) {
		return this.get(id);
	}

	public final float[] getSonarReadings() {
		float[] readings = new float[this.getSize()];
		
		for(int i=0; i<this.getSize(); i++) {
			readings[i] = Float.valueOf(this.get("sonar" + Integer.valueOf(i).toString())).floatValue();
		}
		
		return readings;
	}
	
	public final void setSonarReadings(float[] readings) {
		for(int i=0; i<readings.length; i++) {
			this.add("sonar" + Integer.valueOf(i).toString(), Float.valueOf(readings[i]));
		}
		
		return;
	}
	
}