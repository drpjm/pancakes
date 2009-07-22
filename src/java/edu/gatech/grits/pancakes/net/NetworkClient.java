package edu.gatech.grits.pancakes.net;

import java.net.*;
import java.io.*;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.Packet;

public class NetworkClient {

	private Socket socket;
	private Fiber fiber = Kernel.scheduler.newFiber();
	
	public NetworkClient(String hostname, int port) {
		try {
//			System.out.println("Sending to " + hostname);
			socket = new Socket(hostname, port);
			//System.out.println("Client is ready to send:");
		} catch(UnknownHostException e) {
			System.out.println("Host not found.");
		} catch(IOException e) {
			System.out.println("Error connecting to host: " + hostname);
		}
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet packet) {
				try {
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
		};
		
		try {
			Kernel.stream.subscribe("network", fiber, callback);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
