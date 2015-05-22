package edu.gatech.grits.pancakes.service;

import javolution.util.FastList;
import javolution.util.FastMap;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.*;
import edu.gatech.grits.pancakes.devices.backend.*;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.util.Properties;

public class DeviceService extends Service {

	private Backend deviceBackend;
	private FastMap<String, Long> deviceProps;

	public DeviceService(Properties props) {
		super(DeviceService.class.getSimpleName());

		String backend = props.getBackend();

		if(backend.equals("player"))
			deviceBackend = new PlayerBackend(props.getBackendPort());

		if(backend.equals("k3"))
			deviceBackend = new K3Backend();

		if(backend.equals("bug"))
			deviceBackend = new BugBackend();

		if(backend.equals("empty"))
			deviceBackend = new EmptyBackend();

		// initialize the device properties
		deviceProps = new FastMap<String,Long>();
		for(String s : props.getDevices()){
			deviceProps.put(s, props.getDelayOf(s.toLowerCase()));
		}
		buildDeviceRegistry();

		if(backend.equals("player"))
			((PlayerBackend) deviceBackend).complete();

		for(String key : taskList()) {
			scheduleTask(key);
		}

	}

	public void buildDeviceRegistry() {

		for(FastMap.Entry<String, Long> entry = deviceProps.head(), end = deviceProps.tail(); (entry = entry.getNext()) != end;){

			if(deviceBackend.getClass().getSimpleName().equals(PlayerBackend.class.getSimpleName())){
				((PlayerBackend) deviceBackend).update();
			}

			String currDevice = entry.getKey();
			Long deviceDelay = entry.getValue();
			Kernel.getInstance().getSyslog().debug("Adding device: " + currDevice + " with delay " + deviceDelay);

			// TODO: need to ditch hard coding the names here...
			if(currDevice.equals("sonar")){
				addTask(SonarDevice.class.getSimpleName(), new SonarDevice(deviceBackend, deviceDelay.longValue()));
			}
			else if(currDevice.equals("ir")){
				addTask(IRDevice.class.getSimpleName(), new IRDevice(deviceBackend, deviceDelay.longValue()));
			}
			else if(currDevice.equals("localpose")){
				addTask(LocalPoseDevice.class.getSimpleName(), new LocalPoseDevice(deviceBackend, deviceDelay.longValue()));
			}
			else if(currDevice.equals("battery")){
				addTask(BatteryDevice.class.getSimpleName(), new BatteryDevice(deviceBackend, deviceDelay.longValue()));
			}
			else if(currDevice.equals("motor")){
				addTask(MotorDevice.class.getSimpleName(), new MotorDevice(deviceBackend));
			}
			else if(currDevice.equals("motion")) {
				addTask(MotionDevice.class.getSimpleName(), new MotionDevice(deviceBackend));
			}
		}
	}

	// TODO: need to improve the closing interface - should be more like a system termination
	public void close() {
		deviceBackend.close();
		for(String key : taskList()) {
			removeTask(key);
		}
		unsubscribe();
	}

	@Override
	protected void restartService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stopService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startTask(String taskName) {
		// TODO Auto-generated method stub
		
	}


}
