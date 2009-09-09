package edu.gatech.grits.pancakes.core;

import javolution.util.FastMap;
import edu.gatech.grits.pancakes.service.*;
import edu.gatech.grits.pancakes.util.Properties;

public class Kernel {

	public static Scheduler scheduler;// = new Scheduler();
	public static Stream stream;// = new Stream();
	public static Syslog syslog;// = new Syslogp();
	
//	private DeviceService ds;
//	private NetworkService ns;
//	private Log4jService l4j;
//	private TwitterService twitter;
	
	FastMap<String, Service> serviceList = new FastMap<String, Service>();
	
	public Kernel(Properties properties) {
		scheduler = new Scheduler();
		stream = new Stream();
		syslog = new Syslog();
		
		startServices(properties);
	}
	
	public void shutdown() {
		stopServices();
	}
	
	public void startServices(Properties properties) {
		if(properties.isDevicesEnabled()) {
			serviceList.put("devices", new DeviceService(properties));
		}
		if(properties.isNetworkEnabled()) {
			serviceList.put("network", new NetworkService(properties));
		}
		if(properties.isTwitterEnabled()) {
			serviceList.put("twitter", new TwitterService(properties));
		}
		if(properties.isLog4jEnabled()) {
			serviceList.put("log4j", new Log4jService());
		}
	}
	
	public void stopServices() {
		for(String key : serviceList.keySet()) {
			serviceList.get(key).close();
		}
	}
	
//	public Service getService(String service) {
//		return serviceList.get(service);
//	}
}
