package edu.gatech.grits.pancakes.devices.backend;

import java.lang.reflect.Constructor;

public abstract class Backend {

	private String backendType = null;
	
	public Backend(String type) {
		backendType = type;
	}
	
	public String getBackendType() {
		return backendType;
	}
	
	public abstract Object getHandle();
	
	@SuppressWarnings("unchecked")
	public Object getDriver(String driverName) {
		try {
			Class cls = Class.forName("edu.gatech.grits.pancakes.driver." + backendType + "." + driverName);
			Class partypes = Backend.class;
	        Constructor ct = cls.getConstructor(partypes);
	        Object arglist = this;
	        return ct.newInstance(arglist);
		} catch (Throwable e) {
			System.err.println(e);
			return null;
		}
	}
}
