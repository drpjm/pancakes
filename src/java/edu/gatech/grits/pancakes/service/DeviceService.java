package edu.gatech.grits.pancakes.service;

import java.util.ArrayList;

import javolution.util.FastMap;

import edu.gatech.grits.pancakes.backend.*;
import edu.gatech.grits.pancakes.device.*;
import edu.gatech.grits.pancakes.kernel.Kernel;
import edu.gatech.grits.pancakes.kernel.Scheduler.SchedulingException;
import edu.gatech.grits.pancakes.util.Properties;

public class DeviceService {

	private FastMap<String, HardwareDevice> deviceRegistry;
	private Backend deviceBackend;
	
	public DeviceService(Properties props) {
		String backend = props.getBackend();
		int port = props.getBackendPort();

		if(backend.equals("player")) {
			deviceBackend = new PlayerBackend(port);
//			try {
//				Kernel.scheduler.schedule((PlayerBackend) deviceBackend, 100);
//			} catch (SchedulingException e) {
//				// TODO Auto-generated catch block
//				System.out.println(e.getMessage());
//			}
		}
//		if(backend.equals("k3"))
//			deviceBackend = new K3Backend();
//		if(backend.equals("simbad"))
//			deviceBackend = new SimbadBackend();
		
		if(!backend.equals("none"))
			buildDeviceRegistry(props.getDevices());

	}

	public void buildDeviceRegistry(ArrayList<String> sensors) {
		deviceRegistry = FastMap.newInstance();
		// load sensors
		System.out.println(sensors.toString());
		for(String s : sensors){

			if(deviceBackend.getBackendType().equals("player")) {
				((PlayerBackend) deviceBackend).update();
			}

			String currSensor = s.toLowerCase();
			if(currSensor.equals("sonar")){
				deviceRegistry.put("sonar", new SonarDevice(deviceBackend));
				try {
					Kernel.scheduler.schedule((Runnable) deviceRegistry.get("sonar"), 250);
				} catch (SchedulingException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
				}
			}
			else if(currSensor.equals("ir")){
				deviceRegistry.put("ir", new IRDevice(deviceBackend));
				try {
					Kernel.scheduler.schedule((Runnable) deviceRegistry.get("ir"), 250);
				} catch (SchedulingException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
				}
			}
			else if(currSensor.equals("local")){
				deviceRegistry.put("local", new LocalPoseDevice(deviceBackend));
				try {
					Kernel.scheduler.schedule((Runnable) deviceRegistry.get("local"), 1000);
				} catch (SchedulingException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
				}
			}
			else if(currSensor.equals("motors")){
				deviceRegistry.put("motors", new MotorDevice(deviceBackend));
			}
		}
	}

	// should be moved into the Player backend
	public void close() {
		((PlayerBackend) deviceBackend).getHandle().close();
	}

}
