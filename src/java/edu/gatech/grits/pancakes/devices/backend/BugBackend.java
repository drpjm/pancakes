package edu.gatech.grits.pancakes.devices.backend;

import edu.gatech.grits.pancakes.core.Kernel;

public class BugBackend extends Backend {

	public BugBackend() {
		super("bug");
		Kernel.getInstance().getStream().createChannel("bug");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getHandle() {
		// TODO Auto-generated method stub
		return null;
	}

}
