package edu.gatech.grits.pancakes.net;

import java.net.*;
import java.io.*;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.Packet;

public class NetworkServer {

	private ServerSocket listener;
	private Thread mainThread;
	private volatile boolean stopRequested = false;
	
	public NetworkServer(int port) {
		try {
			listener = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error!");
		}
		
		Runnable task = new Runnable() {
			public void run() {
				while(!stopRequested) {
					try {
						//System.out.println("Server waiting for client!");
						final Socket client = listener.accept();
						//System.out.println("Client found!");
						Runnable task = new Runnable() {
							public void run() {
								handleConnection(client);
							}
						};
						Kernel.scheduler.execute(task);		
					} catch (IOException e) {
						System.out.println("Error receiving object");
						e.printStackTrace();
					}
				}
			}
		};
		mainThread = new Thread(task);
		mainThread.start();
	}
	
	public void handleConnection(Socket client) {
		InputStream in;
		try {
			in = client.getInputStream();
			ObjectInputStream oin = new ObjectInputStream(in);
			Packet pkt = (Packet) oin.readObject();
			Kernel.stream.publish("system", pkt);
		} catch (IOException e) {
			System.err.println("Error receiving object.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final void close() {
		stopRequested = true;
	}
}
