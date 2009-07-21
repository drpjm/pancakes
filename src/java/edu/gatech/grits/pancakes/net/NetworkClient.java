package edu.gatech.grits.pancakes.net;

import java.net.*;
import java.io.*;

import edu.gatech.grits.pancakes.lang.Packet;

public class NetworkClient implements Runnable {

	private Socket socket;
	private Packet packet;
	
	public NetworkClient(String hostname, int port, Packet pkt) {
		
		try {
//			System.out.println("Sending to " + hostname);
			socket = new Socket(hostname, port);
			//System.out.println("Client is ready to send:");
		} catch(UnknownHostException e) {
			System.out.println("Host not found.");
		} catch(IOException e) {
			System.out.println("Error connecting to host: " + hostname);
		}
		packet = pkt;
	}
	
	@Override
	public void run() {
		try {
			//delay(0.5);
			OutputStream out = socket.getOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(packet);
			oout.flush();
			socket.close();
		} catch(IOException e) {
			System.out.println("send failed!");
			e.printStackTrace();
		}	
	}
}
