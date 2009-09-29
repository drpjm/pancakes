package edu.gatech.grits.pancakes.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.Task;

public class DiscoverySpeaker extends Task {

	private final String MCAST_ADDR = "224.224.224.224";
	private final int DEST_PORT = 1337;
	private String BROADCAST;
	private DatagramSocket socket;
	
	public DiscoverySpeaker(String hostname, int network_port, String id) {
		setDelay(1000l);
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		BROADCAST = hostname + ":" + id + ":" + network_port;
	}
	
	public void run() {
		sendDiscovery();
	}
	
	public void sendDiscovery() {		
//		Kernel.syslog.debug(this.getClass().getSimpleName() + " sending discovery...");
		Charset charSet = Charset.forName("US-ASCII");
		
		byte[] b = BROADCAST.getBytes(charSet);
		DatagramPacket dgram = null;
		
		try {
			dgram = new DatagramPacket(b, b.length, InetAddress.getByName(MCAST_ADDR), DEST_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			socket.send(dgram);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		socket.close();
	}
	
}
