/**
 * 
 */
package edu.gatech.grits.pancakes.lang;

/**
 * @author jean-pierre
 *
 */
public class MotorPacket extends Packet { /**
	 * 
	 */
	private static final long serialVersionUID = -7369752263853901700L;
	
	public MotorPacket() {
		super(PacketType.MOTOR);
	}
	
	public final float getVelocity() {
		return Float.valueOf(this.get("velocity")).floatValue();
	}
	
	public final void setVelocity(float velocity) {
		this.add("velocity", Float.valueOf(velocity)); //  (0, Float.valueOf(velocity));
	}
	
	public final float getRotationalVelocity() {
		return Float.valueOf(this.get("rotationalVelocity")).floatValue();
	}
	
	public final void setRotationalVelocity(float rotationalVelocity) {
		this.add("rotationalVelocity", Float.valueOf(rotationalVelocity));
	}
	
	public final float getStatus() {
		return Float.valueOf(this.get("status")).floatValue();
	}
	
	public final void setStatus(float status) {
		this.add("status", Float.valueOf(status));
	}
	
	public final void setParameter(String parameter, Float value) {
		this.add(parameter, value);
	}

}
