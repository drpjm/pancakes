package edu.gatech.grits.pancakes.service;

//import java.util.Timer;
//import java.util.TimerTask;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javolution.util.FastList;
import javolution.util.FastMap;
import edu.gatech.grits.pancakes.net.*;
import edu.gatech.grits.pancakes.structures.NetworkNeighbor;
import edu.gatech.grits.pancakes.structures.SafeQueue;
import edu.gatech.grits.pancakes.structures.SyrupPacket;
import edu.gatech.grits.pancakes.util.Properties;

public class NetworkManager {

	private int port;
	private NetworkServer server;
	private DiscoveryListener listener;
	private DiscoverySpeaker speaker;
	
	private Properties properties;
	
	//private FastList<SyrupPacket> sendQueue = new FastList<SyrupPacket>(5);
	private SafeQueue sendQueue = new SafeQueue();
	//private Timer timer = null;	
	private volatile boolean stopRequested;
	private Thread mainThread;
	
	public NetworkManager(Properties properties) {
		this.properties = properties;
		port = properties.getNetworkPort(); //network_port;
		server = new NetworkServer(port);
		listener = new DiscoveryListener();
		speaker = new DiscoverySpeaker(properties.getNetworkAddress(), port, properties.getID());
		listener.start();
		speaker.start();
		
		stopRequested = false;
		
		Runnable netRun = new Runnable(){

			public void run() {
				while(!stopRequested) {
					//System.out.println("Size: " + sendQueue.size());
					
					//while(!sendQueue.isEmpty()) {
//						System.out.println("Size of queue: " + sendQueue.size());
						
						SyrupPacket pkt;
						try {
							pkt = sendQueue.take();
//							System.out.println("Something is to be sent to: " + pkt.getRecipientID());
							NetworkNeighbor n = (NetworkNeighbor) getNetworkNeighborhood().get(pkt.getRecipientID());
							if(n != null) {
//								System.out.println("Sent!");
								(new NetworkClient(n.getHostname(), n.getNetworkPort())).send(pkt);
							}
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					//}
				}
				
			}
			
		};
		mainThread = new Thread(netRun);
		mainThread.start();
		//setupTimer();
	}
	
	public void queuePacket(SyrupPacket pkt) {
		NetworkNeighbor n = (NetworkNeighbor) this.getNetworkNeighborhood().get(pkt.getRecipientID());
		if(n != null) {
			sendQueue.enqueue(pkt);
		}
	}
	
	public ArrayList<SyrupPacket> getPackets() {
		return server.getAllData();
	}
	
	public FastMap<String, NetworkNeighbor> getNetworkNeighborhood() {
		return listener.getNeighbors();
	}
	
	public FastList<String> getNeighbors() {
		FastMap<String, NetworkNeighbor> neighbors = listener.getNeighbors();
		FastList<String> ns = new FastList<String>();
		for (FastMap.Entry<String,NetworkNeighbor> n = neighbors.head(), end = neighbors.tail(); (n = n.getNext()) != end;) {
	        String i = n.getKey(); 
			ns.add(i);
	    }
		return ns;
	}
	
	public final void stopRequest(){
		stopRequested = true;
		Thread.currentThread().interrupt();
	}
}
