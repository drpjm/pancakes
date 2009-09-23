package edu.gatech.grits.pancakes.service;

import java.util.ArrayList;

import javolution.util.FastList;

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
			buildDeviceRegistry(props);
		
		if(backend.equals("player"))
			((PlayerBackend) deviceBackend).finalize();

		for(String key : taskList()) {
			scheduleTask(key);
		}
		
	}

//	public void buildDeviceRegistry(FastList<String> sensors) {
	public void buildDeviceRegistry(Properties props) {
		
		FastList<String> sensors = props.getDevices();
		for(String s : sensors){
			
			if(deviceBackend.getBackendType().equals("player")) {
				((PlayerBackend) deviceBackend).update();
			}

			String currSensor = s.toLowerCase();
			Long sensorDelay = props.getDelayOf(currSensor);
			Kernel.syslog.debug("Adding sensor: " + s + " with delay " + sensorDelay);

			if(currSensor.equals("sonar")){
				addTask("sonar", new SonarDevice(deviceBackend, sensorDelay.longValue()));
			}
			else if(currSensor.equals("ir")){
				addTask("ir", new IRDevice(deviceBackend, sensorDelay.longValue()));
			}
			else if(currSensor.equals("localpose")){
				addTask("localpose", new LocalPoseDevice(deviceBackend, sensorDelay.longValue()));
			}
			else if(currSensor.equals("battery")){
				addTask("battery", new BatteryDevice(deviceBackend, sensorDelay.longValue()));
			}
			else if(currSensor.equals("motor")){
				addTask("motor", new MotorDevice(deviceBackend));
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
