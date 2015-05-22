package edu.gatech.grits.pancakes.devices.backend;

import java.lang.reflect.Constructor;

import edu.gatech.grits.pancakes.core.Kernel;

public abstract class Backend {

	private String backendType = null;
	
	public Backend(String type) {
		backendType = type;
	}
	
	public String getBackendType() {
		return backendType;
	}
	
	public abstract Object getHandle();
	public abstract void close();
	
	@SuppressWarnings("unchecked")
	public Object getDriver(String driverName) {
		try {
			Kernel.getInstance().getSyslog().debug("edu.gatech.grits.pancakes.devices.driver." + backendType + "." + driverName);
			Class cls = Class.forName("edu.gatech.grits.pancakes.devices.driver." + backendType + "." + driverName);
			Class partypes = Backend.class;
	        Constructor ct = cls.getConstructor(partypes);
	        Object arglist = this;
	        return ct.newInstance(arglist);
		} catch (Throwable e) {
			//System.err.println(e);
			Kernel.getInstance().getSyslog().error("Unable to find driver.");
			return null;
		}
	}
}
