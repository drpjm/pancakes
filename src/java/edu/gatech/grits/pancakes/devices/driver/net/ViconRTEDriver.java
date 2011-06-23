package edu.gatech.grits.pancakes.devices.driver.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.devices.driver.NetworkDriver;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;


public class ViconRTEDriver implements NetworkDriver<LocalPosePacket> {

	/**
	 * @param args
	 * 
	 */
	private int BUFFER_LENGTH = 200;
	private int DGRAM_PORT = 95;
	private DatagramSocket socket = null;
	private int ID = 0;
	private String data = null;
	
	private Thread mainThread;
	private volatile boolean stopRequested = false;
	
	//private byte[] data = null;
	
	public ViconRTEDriver(int port, int id) {
		DGRAM_PORT = port;
		ID = id;
		
		try {
			socket = new DatagramSocket(DGRAM_PORT);
			socket.setBroadcast(true);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Runnable task = new Runnable() {
			public void run() {
				while(!stopRequested) {
					listen();
				}
			}
			
		};
		
		mainThread = new Thread(task);
		mainThread.start();
		
		System.out.println("I'M A POORLY WRITTEN DRIVER!");
	}
	
	public LocalPosePacket query() {
		return processData(data);
	}
	
	public void close() {
		socket.close();
	}
	
	private void listen() {
		// TODO Auto-generated method stub
		byte[] b = new byte[BUFFER_LENGTH];
		DatagramPacket dgram = new DatagramPacket(b, b.length);
		
		try {
			socket.receive(dgram);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		String raw = new String(dgram.getData());
		String msg = raw.substring(0, raw.indexOf('\0'));
			
		//System.out.println(msg);
		
		msg = msg.replace("<(", "");
		msg = msg.replace(")>", "");
		msg = msg.replaceAll("<.>", "");
			
		//System.out.println(msg);
			
		String[] tokens = msg.split(",");
		
//		for(int i = 0; i<tokens.length; i++) {
//			System.out.println(tokens[i]);
//		}
		
//		System.out.println("My ID: " + ID);
//		System.out.println("Received ID: " + tokens[0]);
		
		if( (new Integer(ID)).equals(Integer.valueOf(tokens[0])) ) {
//			Kernel.getInstance().getSyslog().debug("Received '" + msg +"' from Vicon.");
			data = msg;
		}
	}
	
	public LocalPosePacket processData(String data) {
		LocalPosePacket pkt = new LocalPosePacket();
		
		String[] tokens = data.split(",");
		
		float yaw = (Float.valueOf(tokens[3])).floatValue();
		
//		if (yaw < 0.0f) {
//			yaw += 2.0f * ((float) Math.PI);
//		}
		
		float x = (Float.valueOf(tokens[1])).floatValue();
		float y = (Float.valueOf(tokens[2])).floatValue();
		
		pkt.setPose(x, y, yaw);
		
		return pkt;
	}

	public void request(LocalPosePacket pkt) {
		// TODO Auto-generated method stub
		// do nothing
	}
}
