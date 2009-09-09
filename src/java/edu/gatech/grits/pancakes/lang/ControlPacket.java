package edu.gatech.grits.pancakes.lang;

public class ControlPacket extends Packet {
	
	private static final long serialVersionUID = 1761628700970106348L;
	private final String ctrl;

	public ControlPacket(String type, String ctrl) {
		super(type);
		this.ctrl = ctrl;
	}
	
	public final String getControl() {
		return ctrl;
	}

}
