package edu.gatech.grits.pancakes.lang;

public class ControlPacket extends Packet {
	
	private static final long serialVersionUID = 1761628700970106348L;
	private final String control;
	private final long delay;

	public ControlPacket(String dst, String ctrl) {
		super(dst);
		control = ctrl;
		delay = 0l;
	}
	
	public ControlPacket(String dst, String ctrl, long arg) {
		super(dst);
		control = ctrl;
		delay = arg;
	}
	
	public final String getControl() {
		return control;
	}
	
	public final long getDelay() {
		return delay;
	}

}
