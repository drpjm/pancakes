package edu.gatech.grits.pancakes.service;

public enum ControlOption {

	STOP(0),
	RESTART(1),
	RESCHEDULE(2),
	START(3);
	
	private int type;
	private ControlOption(int i){
		type = i;
	}

}
