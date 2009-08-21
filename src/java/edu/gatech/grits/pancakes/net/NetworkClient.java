package edu.gatech.grits.pancakes.net;

import java.net.*;
import java.io.*;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.lang.NetworkNeighbor;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Subscription;
import edu.gatech.grits.pancakes.service.NetworkService;

public class NetworkClient {

	private final Subscription subscription;
	
	public NetworkClient() {
		Fiber fiber = Kernel.scheduler.newFiber();
		fiber.start();
		Callback<Packet> callback = new Callback<Packet>() {
			public void onMessage(Packet packet) {
				final NetworkNeighbor n = NetworkService.neighborhood.getNeighbor(((NetworkPacket) packet).getDestination());
				if(n != null) {
					try {	
						final Socket socket = new Socket(n.getHostname(), n.getNetworkPort());
						
						OutputStream out = socket.getOutputStream();
						ObjectOutputStream oout = new ObjectOutputStream(out);
						oout.writeObject(packet);
						oout.flush();
						socket.close();
					} catch(IOException e) {
						System.out.println("send failed!");
						e.printStackTrace();
					}	
				} else {
					System.err.println("Destination not in the reachable network neighborhood");
				}
			}
		};
		
		subscription = new Subscription("network", fiber, callback);
		
		try {
			Kernel.stream.subscribe(subscription);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		Kernel.stream.unsubscribe(subscription);
		subscription.getFiber().dispose();
	}
}
