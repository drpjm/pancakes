package edu.gatech.grits.pancakes.client;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.Task;

public class HelloPancakes extends Task {

	public HelloPancakes() {
		// TODO Auto-generated constructor stub
		setDelay(0l);
		Kernel.syslog.info("Hello, Pancakes!");
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void run() {
		// TODO Auto-generated method stub

	}

}
