package edu.gatech.grits.pancakes.core;

import javolution.util.FastMap;
import edu.gatech.grits.pancakes.service.*;
import edu.gatech.grits.pancakes.util.Properties;

public class Kernel {

	public static Scheduler scheduler;// = new Scheduler();
	public static Stream stream;// = new Stream();
	public static Syslog syslog;// = new Syslogp();
	
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
		
		for(String service : properties.getServices()) {
			if(service.equals("devices")) {
				serviceList.put("devices", new DeviceService(properties));
			}
			else if(service.equals("network")) {
				serviceList.put("network", new NetworkService(properties));
			}
			else if(service.equals("twitter")) {
				serviceList.put("twitter", new TwitterService(properties));
			}
			else if(service.equals("log4j")) {
				serviceList.put("log4j", new Log4jService());
			}
			else if(service.equals("client")) {
				serviceList.put("client", new ClientService());
			}
		}
	}
	
	public void stopServices() {
		for(String key : serviceList.keySet()) {
			serviceList.get(key).close();
		}
	}
}
