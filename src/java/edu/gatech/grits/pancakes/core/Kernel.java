package edu.gatech.grits.pancakes.core;

import edu.gatech.grits.pancakes.service.*;
import edu.gatech.grits.pancakes.util.Properties;

public class Kernel {

	public static Scheduler scheduler;// = new Scheduler();
	public static Stream stream;// = new Stream();
	public static Syslog syslog;// = new Syslogp();
	private DeviceService ds;
	private NetworkService ns;
	private Log4jService l4j;
	private TwitterService twitter;
	
	public Kernel(Properties properties) {
		scheduler = new Scheduler();
		stream = new Stream();
		syslog = new Syslog();
		l4j = new Log4jService();
		ds = new DeviceService(properties);
		ns = new NetworkService(properties);
		twitter = new TwitterService(properties);
	}
}
