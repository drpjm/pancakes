package edu.gatech.grits.pancakes.lang;

public class BatteryPacket extends Packet {

	private static final long serialVersionUID = 5646193809509209402L;

	public BatteryPacket() {
		super("battery");
		// TODO Auto-generated constructor stub
	}
	
	public float getVoltage() {
		return new Float(get("voltage"));
	}

	public void setVoltage(float voltage) {
		add("voltage", voltage);
	}
	
	public float getCurrent() {
		return new Float(get("current"));
	}
	
	public void setCurrent(float current) {
		add("current", current);
	}

}