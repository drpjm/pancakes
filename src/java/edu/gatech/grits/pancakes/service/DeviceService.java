package edu.gatech.grits.pancakes.service;

import java.util.ArrayList;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.*;
import edu.gatech.grits.pancakes.devices.backend.*;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.util.Properties;

public class DeviceService extends Service {

	private Backend deviceBackend;
	
	public DeviceService(Properties props) {
		super("devices");
		
		String backend = props.getBackend();

		if(backend.equals("player"))
			deviceBackend = new PlayerBackend(props.getBackendPort());

		if(backend.equals("k3"))
			deviceBackend = new K3Backend();
		
		if(!backend.equals("none"))
			buildDeviceRegistry(props.getDevices());
		
		if(backend.equals("player"))
			((PlayerBackend) deviceBackend).finalize();

		for(String key : taskList()) {
			scheduleTask(key);
		}
		
	}

	public void buildDeviceRegistry(ArrayList<String> sensors) {
		
		for(String s : sensors){
			
			Kernel.syslog.debug("Adding sensor: " + s);

			if(deviceBackend.getBackendType().equals("player")) {
				((PlayerBackend) deviceBackend).update();
			}

			String currSensor = s.toLowerCase();
			if(currSensor.equals("sonar")){
				addTask("sonar", new SonarDevice(deviceBackend, 500l));
			}
			else if(currSensor.equals("ir")){
				addTask("ir", new IRDevice(deviceBackend, 250l));
			}
			else if(currSensor.equals("local")){
				addTask("local", new LocalPoseDevice(deviceBackend, 0l));
			}
			else if(currSensor.equals("battery")){
				addTask("battery", new BatteryDevice(deviceBackend, 1000l));
			}
			else if(currSensor.equals("motors")){
				addTask("motors", new MotorDevice(deviceBackend));
			}
		}
	}

	public void close() {
		deviceBackend.close();
		for(String key : taskList()) {
			removeTask(key);
		}
	}

	@Override
	public void process(Packet pkt) {
		// TODO Auto-generated method stub
		
	}

}
