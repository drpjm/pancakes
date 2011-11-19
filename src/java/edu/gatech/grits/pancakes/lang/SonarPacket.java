package edu.gatech.grits.pancakes.lang;

public class SonarPacket extends Packet { /**
	 * 
	 */
	private static final long serialVersionUID = -1820788047016645217L;
	
	
	public SonarPacket() {
		super(PacketType.SONAR);
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
	
	public final void setSonarReadings(double[] ranges) {
		for(int i=0; i<ranges.length; i++) {
			this.add("sonar" + Integer.valueOf(i).toString(), Float.valueOf(ranges[i]));
		}
		
		return;
	}
	
}