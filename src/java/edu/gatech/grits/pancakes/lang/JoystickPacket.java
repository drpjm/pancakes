package edu.gatech.grits.pancakes.lang;

public class JoystickPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2816220927258062911L;

	public JoystickPacket() {
		super(PacketType.JOYSTICK);
	}
	
	public final void setPushButton1(int state) {
		this.add("pushbutton1", new Float(state));
	}
	
	public final void setPushButton2(int state) {
		this.add("pushbutton2", new Float(state));
	}
	
	public final float getPushButton1() {
		return Float.valueOf(this.get("pushbutton1"));
	}
	
	public final float getPushButton2() {
		return Float.valueOf(this.get("pushbutton2"));
	}
	
	public final void setPositionX(int position) {
		this.add("position-x", new Float(position));
	}
	
	public final void setPositionY(int position) {
		this.add("position-y", new Float(position));
	}
	
	public final float getPositionX() {
		return Float.valueOf(this.get("position-x"));
	}
	
	public final float getPositionY() {
		return Float.valueOf(this.get("position-y"));
	}
}
