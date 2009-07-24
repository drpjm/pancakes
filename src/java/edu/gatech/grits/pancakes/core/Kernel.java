package edu.gatech.grits.pancakes.core;

import edu.gatech.grits.pancakes.service.DeviceService;
import edu.gatech.grits.pancakes.service.NetworkService;
import edu.gatech.grits.pancakes.util.Properties;

public class Kernel {

	public static Scheduler scheduler;// = new Scheduler();
	public static Stream stream;// = new Stream();
	public static Syslog syslog;// = new Syslogp();
	private DeviceService ds;
	private NetworkService ns;
	
	public Kernel(Properties properties) {
		scheduler = new Scheduler();
		stream = new Stream();
		syslog = new Syslog();
		ds = new DeviceService(properties);
		//ns = new NetworkService(properties);
	}
}
