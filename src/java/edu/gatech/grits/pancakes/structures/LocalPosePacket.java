/**
 * 
 */
package edu.gatech.grits.pancakes.structures;

/**
 * @author jean-pierre
 *
 */
public class LocalPosePacket extends Packet { /**
	 * 
	 */
	private static final long serialVersionUID = 2590379254201931069L;


	public LocalPosePacket() {
		super("local");
	}
	
	public final void setPose(float x, float y, float theta) {
		this.add("x", Float.valueOf(x));
		this.add("y", Float.valueOf(y));
		this.add("theta", Float.valueOf(theta));
		
		return;
	}
	
	public final float getPositionX() {
		return Float.valueOf(this.get("x")).floatValue();
	}
	
	public final float getPositionY() {
		return Float.valueOf(this.get("y")).floatValue();
	}
	
	public final float getTheta() {
		return Float.valueOf(this.get("theta")).floatValue();
	}
	
}
