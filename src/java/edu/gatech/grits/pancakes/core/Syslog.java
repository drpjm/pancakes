package edu.gatech.grits.pancakes.core;

import org.apache.log4j.Level;

import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LogPacket;
import edu.gatech.grits.pancakes.lang.Packet;

public class Syslog {
	
	private final void log(LogPacket pkt) {
		try {
			Kernel.getInstance().getStream().publish(CoreChannel.LOG, pkt);
//			Kernel.getInstance().getStream().publish("log", pkt);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final void debug(String m) {
		//String src = (new Throwable()).getStackTrace()[1].getClassName();
		log(new LogPacket("syslog", Level.DEBUG, m));
	}
	
	public final void warn(String m) {
		log(new LogPacket("syslog", Level.WARN, m));
	}
	
	public final void info(String m) {
		log(new LogPacket("syslog", Level.INFO, m));
	}
	
	public final void fatal(String m) {
		log(new LogPacket("syslog", Level.FATAL, m));
	}
	
	public final void error(String m) {
		log(new LogPacket("syslog", Level.ERROR, m));
	}
	
	public final void tweet(String m) {
		log(new LogPacket("twitter", Level.INFO, m));
	}
	
	public final void record(Packet p) {
		log(new LogPacket("data", Level.INFO, Kernel.getInstance().getId() + ":" + p.toString()));
	}
	
	public final void record(Packet p, String id){
		log(new LogPacket("data", Level.INFO, id + ":" + p.toString()));
	}
}
