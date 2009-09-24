package edu.gatech.grits.pancakes.core;

import javolution.util.FastMap;
import edu.gatech.grits.pancakes.service.*;
import edu.gatech.grits.pancakes.util.Properties;

public class Kernel {

	public static Scheduler scheduler;// = new Scheduler();
	public static Stream stream;// = new Stream();
	public static Syslog syslog;// = new Syslogp();
	
	public static String id;
	
	FastMap<String, Service> serviceList = new FastMap<String, Service>();
	
	public Kernel(Properties properties) {
		scheduler = new Scheduler();
		stream = new Stream();
		syslog = new Syslog();
		
		id = properties.getID();
		
		startServices(properties);
	}
	
	public void shutdown() {
		stopServices();
	}
	
	public void startServices(Properties properties) {
		
		// TODO: consider always starting the network and device services...
		
		// logging should be started first since we use it in all other services!		
		if(properties.getServices().contains("log4j")){
			int logIndex = properties.getServices().indexOf("log4j");
			String logging = properties.getServices().remove(logIndex);
			serviceList.put(logging, new Log4jService());
		}
		// next, get the network service up
		if(properties.getServices().contains("network")){
			int netIndex = properties.getServices().indexOf("network");
			String net = properties.getServices().remove(netIndex);
			serviceList.put(net, new NetworkService(properties));
		}
		
		for(String service : properties.getServices()) {
			if(service.equals("devices")) {
				serviceList.put("devices", new DeviceService(properties));
			}
			else if(service.equals("twitter")) {
				serviceList.put("twitter", new TwitterService(properties));
			}
			else if(service.equals("client")) {
				serviceList.put("client", new ClientService(properties));
			}
		}
	}
	
	public void stopServices() {
		for(String key : serviceList.keySet()) {
			serviceList.get(key).close();
		}
	}
}
