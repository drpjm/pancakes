package edu.gatech.grits.pancakes.net;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.SafeQueue;
import edu.gatech.grits.pancakes.lang.Packet;

public class NetworkServer extends Thread {

	private ServerSocket listener;
	private SafeQueue packets = new SafeQueue();
	
	
	public NetworkServer(int port) {
		try {
			listener = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error!");
		}
		this.start();
	}
	
	public void run() {
		while(true) {
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
	
	public void handleConnection(Socket client) {
		InputStream in;
		try {
			in = client.getInputStream();
			ObjectInputStream oin = new ObjectInputStream(in);
			Packet pkt = (Packet) oin.readObject();
			packets.enqueue(pkt);
		} catch (IOException e) {
			System.err.println("Error receiving object.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public ArrayList<Packet> getAllData() {
		return packets.drain();
	}
	
	public int getSize() {
		return packets.size();
	}
}
