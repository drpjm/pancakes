package edu.gatech.grits.pancakes.devices.driver.k3;

import swig.korebot.k3.*;

@Deprecated
public class K3Driver {
	
	static {
		System.loadLibrary("k3ctrl");
	}
	
	
	public K3Driver() {
				
		if( k3ctrl.initKH3() == 0) {
			System.out.println("K3 is alive");
		} else {
			System.out.println("K3 is dead");
		}
	}

}
