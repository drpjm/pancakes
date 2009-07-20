package edu.gatech.grits.pancakes.kernel;

import edu.gatech.grits.pancakes.service.DeviceService;
import edu.gatech.grits.pancakes.util.Properties;

public class Kernel {

	public static Scheduler scheduler = new Scheduler();
	public static Stream stream = new Stream();
	private DeviceService ds;
	
	public Kernel(Properties properties) {
		ds = new DeviceService(properties);
	}
}
