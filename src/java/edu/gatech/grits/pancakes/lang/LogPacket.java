package edu.gatech.grits.pancakes.lang;

import org.apache.log4j.Level;

public class LogPacket extends Packet {

	private static final long serialVersionUID = 2357367909093730373L;

	public LogPacket(Object o, Level l, String m) {
		super("log");
		add("message", m);
		add("source", o.getClass());
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
