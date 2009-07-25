package edu.gatech.grits.pancakes.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
//import edu.gatech.grits.pancakes.log.Syslogp;
import edu.gatech.grits.pancakes.lang.NetworkNeighbor;
import edu.gatech.grits.pancakes.service.NetworkService;

public class DiscoveryListener {
	
	private final String MCAST_ADDR = "224.224.224.224";
	private final int DEST_PORT = 1337;
	private final int BUFFER_LENGTH = 16;
	private MulticastSocket socket;
	private volatile boolean isRunning;	
	private Thread mainThread;
	
	public DiscoveryListener() {
		try {
			socket = new MulticastSocket(DEST_PORT);
		} catch (IOException e) {
			//Syslogp.ref(this).fatal("Unable to create multicast socket.");
			System.exit(-1);
		} // must bind receive side
		
		try {
			socket.joinGroup(InetAddress.getByName(MCAST_ADDR));
		} catch (UnknownHostException e) {
			//Syslogp.ref(this).fatal("Unknown host.");
			System.exit(-1);
		} catch (IOException e) {
			//Syslogp.ref(this).fatal("Unable to join multicast group.");
			System.exit(-1);
		}
		
		isRunning = true;
		
		Runnable task = new Runnable() {
			public void run() {
				byte[] b = new byte[BUFFER_LENGTH];
				DatagramPacket dgram = new DatagramPacket(b, b.length);
				
				while(isRunning) {
					try {
						socket.receive(dgram);
						addNetworkNeighbor(dgram);
					
					} catch (IOException e) {
						//Syslogp.ref(this).error("Unable to receive datagram.");
					} // blocks until a datagram is received
					
//					System.err.println("Received " + dgram.getLength() +
//							" bytes from " + dgram.getAddress());
//							dgram.setLength(b.length); // must reset length field!
				}
			}
		};
		
		mainThread = new Thread(task);
		mainThread.start();
		
	}
	
	public void close() {
		isRunning = false;
	}
	
	private void addNetworkNeighbor(DatagramPacket dgram) {
		ArrayList<String> p = parse(dgram.getData());
		
		if(p != null) {
			NetworkNeighbor n = new NetworkNeighbor(p.get(1), p.get(0), Integer.valueOf(p.get(2)), new Date(System.currentTimeMillis()));
			NetworkService.neighborhood.addNeighbor(p.get(1), n);
		}
		return;
	}
	
	private final ArrayList<String> parse(byte[] data) {
		ArrayList<String> parameters = new ArrayList<String>(3);
		
		try {
			String message = new String(data, "US-ASCII");
			StringTokenizer st = new StringTokenizer(message, ":");
			while(st.hasMoreTokens()) {
				parameters.add(st.nextToken().trim());
			}
			
		} catch (UnsupportedEncodingException e) {
			//Syslogp.ref(this).error("Message was incorrectly encoded.");
			parameters = null;
		}
		
		return parameters;
	}
	
}
