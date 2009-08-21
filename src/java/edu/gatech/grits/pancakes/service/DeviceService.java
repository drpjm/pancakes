package edu.gatech.grits.pancakes.service;

import java.util.ArrayList;

import javolution.util.FastMap;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.devices.*;
import edu.gatech.grits.pancakes.devices.backend.*;
import edu.gatech.grits.pancakes.util.Properties;

public class DeviceService {

	private FastMap<String, Device> deviceRegistry;
	private Backend deviceBackend;
	
	public DeviceService(Properties props) {
		String backend = props.getBackend();

		if(backend.equals("player"))
			deviceBackend = new PlayerBackend(props.getBackendPort());

		if(backend.equals("k3"))
			deviceBackend = new K3Backend();
		
		if(!backend.equals("none"))
			buildDeviceRegistry(props.getDevices());
		
		if(backend.equals("player"))
			((PlayerBackend) deviceBackend).finalize();

		for(String key : deviceRegistry.keySet()) {
			Device d = deviceRegistry.get(key);
			if(d.isRunnable()) {
				try {
					Kernel.scheduler.schedule((Runnable) d, d.delay());
				} catch (SchedulingException e) {
					Kernel.syslog.error("Unable to schedule " + key + ".");
				}
			}
		}
		
	}

	public void buildDeviceRegistry(ArrayList<String> sensors) {
		
		deviceRegistry = new FastMap<String, Device>();
		
		for(String s : sensors){
			
			Kernel.syslog.debug("Adding sensor: " + s);

			if(deviceBackend.getBackendType().equals("player")) {
				((PlayerBackend) deviceBackend).update();
			}

			String currSensor = s.toLowerCase();
			if(currSensor.equals("sonar")){
				deviceRegistry.put("sonar", new SonarDevice(deviceBackend));
			}
			else if(currSensor.equals("ir")){
				deviceRegistry.put("ir", new IRDevice(deviceBackend));
			}
			else if(currSensor.equals("local")){
				deviceRegistry.put("local", new LocalPoseDevice(deviceBackend));
			}
			else if(currSensor.equals("battery")){
				deviceRegistry.put("battery", new BatteryDevice(deviceBackend));
			}
			else if(currSensor.equals("motors")){
				deviceRegistry.put("motors", new MotorDevice(deviceBackend));
			}
		}
	}

	public void close() {
		((PlayerBackend) deviceBackend).close();
		for(String key : deviceRegistry.keySet()) {
			Device d = deviceRegistry.get(key);
			if(d.isRunnable()) {
				try {
					Kernel.scheduler.cancel((Runnable) d);
				} catch (SchedulingException e) {
					Kernel.syslog.warn("Unable to terminate " + key + ".");
				}
			}
			d.close();
		}
	}

}
