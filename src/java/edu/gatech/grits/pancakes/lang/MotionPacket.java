package edu.gatech.grits.pancakes.lang;

public class MotionPacket extends Packet {

	private static final long serialVersionUID = 1880496113858108646L;

	public MotionPacket() {
		super(PacketType.MOTION);
		// TODO Auto-generated constructor stub
	}
	
	public void setMotionDetected(boolean value) {
		add("detected", new Boolean(value));
	}
	
	public boolean getMotionDetected() {
		return (new Boolean(get("detected")).booleanValue());
	}

}
