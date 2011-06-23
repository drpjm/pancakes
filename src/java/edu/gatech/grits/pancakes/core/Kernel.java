package edu.gatech.grits.pancakes.core;

import javolution.util.FastList;
import javolution.util.FastMap;
import edu.gatech.grits.pancakes.service.ClientService;
import edu.gatech.grits.pancakes.service.DeviceService;
import edu.gatech.grits.pancakes.service.Log4jService;
import edu.gatech.grits.pancakes.service.NetworkService;
import edu.gatech.grits.pancakes.service.Service;
import edu.gatech.grits.pancakes.service.TwitterService;
import edu.gatech.grits.pancakes.util.Properties;

public class Kernel {

	private Scheduler scheduler;
	private Stream stream;
	private Syslog syslog;
	
	private Properties props;
	
	private String id;
	private FastList<String> devices;
	
	private FastMap<String, Service> serviceList = new FastMap<String, Service>();
	
	private static Kernel KERNEL;
	
	private Kernel(Properties properties) {
		
		scheduler = new Scheduler();
		stream = new Stream();
		syslog = new Syslog();
		
		id = properties.getId();		
		devices = properties.getDevices();	
		
	}
	
	public static Kernel getInstance(){
		return KERNEL;
	}
	
	public static Kernel newInstance(String fileName){
		if(KERNEL == null){
			Properties props = new Properties(fileName);
			KERNEL = new Kernel(props);
			KERNEL.props = props;
		}
		return KERNEL;
	}
	
	public void shutdown() {
		KERNEL.stopServices();
	}
	
	public void startUp(){
		KERNEL.startServices(KERNEL.props);
	}
	
	private void startServices(Properties properties) {
		
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
		
		System.err.println("Pancakes system " + id + " created. \n" + KERNEL.getId());
	}
	
	private void stopServices() {
		for(String key : serviceList.keySet()) {
			serviceList.get(key).close();
		}
	}

	public String getId(){
		return KERNEL.id;
	}

	private void setId(String id){
		this.id = id;
	}
	
	private void setDevices(FastList<String> devices) {
		this.devices = devices;
	}

	public FastList<String> getDevices() {
		return KERNEL.devices;
	}

	public FastMap<String, Service> getServiceList(){
		return KERNEL.serviceList;
	}
	
	public Scheduler getScheduler() {
		return KERNEL.scheduler;
	}

	public Stream getStream() {
		return KERNEL.stream;
	}

	public Syslog getSyslog() {
		return KERNEL.syslog;
	}

	
}
