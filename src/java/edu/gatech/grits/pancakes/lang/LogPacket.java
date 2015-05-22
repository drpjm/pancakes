package edu.gatech.grits.pancakes.lang;

import org.apache.log4j.Level;

public class LogPacket extends Packet {

	private static final long serialVersionUID = 2357367909093730373L;

	public LogPacket(String s, Level l, String m) {
		super(PacketType.LOG);
		add("message", m);
		add("source", s);
		add("level", l.toString());
	}
	
	public String getMessage() {
		return get("message");
	}
	
	public String getSource() {
		return get("source");
	}
	
	public String getLevel() {
		return get("level");
	}

}
