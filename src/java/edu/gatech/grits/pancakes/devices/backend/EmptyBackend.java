package edu.gatech.grits.pancakes.devices.backend;

public class EmptyBackend extends Backend {

	public EmptyBackend() {
		super("empty");
		
	}

	@Override
	public void close() {

	}

	@Override
	public Object getHandle() {
		return null;
	}

}
