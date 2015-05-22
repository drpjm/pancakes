package edu.gatech.grits.pancakes.util;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * A class that broadcasts localization data to motion capture objects.
 * @author pmartin
 *
 */
public class GritsLabGPS {

	private final int GPS_PORT = 26;
	
	private ViconClient viconClient;
	
	public GritsLabGPS(String viconHostname, Integer viconPort){
		
		try {
			viconClient = new ViconClient(viconHostname, viconPort);
		} catch (UnknownHostException e) {
			System.err.println("Error: failed to connect to Vicon: " + viconHostname + ":" + viconPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
}
