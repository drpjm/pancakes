package edu.gatech.grits.pancakes.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A class that connects to the Vicon motion capture system using TCP/IP.
 * @author pmartin
 *
 */
public class ViconClient {

	private int viconPort = 800;
	private Socket viconSocket;
	
	private Thread mainThread;
	private boolean isRunning;
	
	public ViconClient(String address, Integer port) throws UnknownHostException, IOException {
		
		// connect to Vicon
		viconPort = port.intValue();
		
		viconSocket = new Socket(address, viconPort);
	
		
		Runnable viconRun = new Runnable(){

			public void run() {
				
				if(isRunning){
					
				}
				
			}
			
		};
		
		mainThread = new Thread(viconRun);
//		mainThread.start();
		isRunning = true;
		
	}

	public final void requestStop(){
		if(isRunning){
			mainThread.interrupt();
		}
	}
	
}
