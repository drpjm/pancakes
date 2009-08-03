package edu.gatech.grits.pancakes.devices.backend;

import org.swig.k3i.k3i;

public class K3Backend extends Backend {

	static {
		System.loadLibrary("k3i");
	}
	
	public K3Backend() {
		super("k3");
		
		k3i.initializeK3();
	}

	@Override
	public Object getHandle() {
		// since the K3 backend is really a swig library, do nothing
		return null;
	}

}
