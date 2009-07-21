package edu.gatech.grits.pancakes.net;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import javolution.util.FastList;
import edu.gatech.grits.pancakes.structures.SafeQueue;
import edu.gatech.grits.pancakes.structures.SyrupPacket;

public class NetworkServer extends Thread {

	private ServerSocket listener;
	//private ArrayList<SyrupPacket> packets = new ArrayList<SyrupPacket>();
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
				Socket client = listener.accept();
				//System.out.println("Client found!");
				InputStream in = client.getInputStream();
				ObjectInputStream oin = new ObjectInputStream(in);
				//System.out.println("Server ready to receive!");

				SyrupPacket pkt = (SyrupPacket) oin.readObject();
				//packets.add(pkt);
				packets.enqueue(pkt);
				
			} catch (IOException e) {
				System.out.println("Error receiving object");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Error receiving object");
				e.printStackTrace();
			}
		}
	}
	
//	public SyrupPacket getData() {
//		if (packets.size() > 0) {
//			return packets.remove(packets.size()-1);
//		} else {
//			return null;
//		}
//	}
	
	public ArrayList<SyrupPacket> getAllData() {
		return packets.drain();
	}
	
	public int getSize() {
		return packets.size();
	}
}
