package edu.gatech.grits.pancakes.core;

import org.apache.log4j.Level;

import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.LogPacket;

public class Syslog {
	
	private final void log(LogPacket pkt) {
		try {
			Kernel.stream.publish("log", pkt);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final void debug(String m) {
		//String src = (new Throwable()).getStackTrace()[1].getClassName();
		log(new LogPacket("pancakes", Level.DEBUG, m));
	}
	
	public final void warn(String m) {
		log(new LogPacket("pancakes", Level.WARN, m));
	}
	
	public final void info(String m) {
		log(new LogPacket("pancakes", Level.INFO, m));
	}
	
	public final void fatal(String m) {
		log(new LogPacket("pancakes", Level.FATAL, m));
	}
	
	public final void error(String m) {
		log(new LogPacket("pancakes", Level.ERROR, m));
	}
}
