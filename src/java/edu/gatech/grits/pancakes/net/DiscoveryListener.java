package edu.gatech.grits.pancakes.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javolution.util.FastList;
import javolution.util.FastMap;
import edu.gatech.grits.pancakes.core.*;
import edu.gatech.grits.pancakes.lang.*;
import edu.gatech.grits.pancakes.service.NetworkService;

public class DiscoveryListener extends Task {

	private final String MCAST_ADDR = "224.224.224.224";
	private final int DEST_PORT = 1337;
	private final int BUFFER_LENGTH = 19;
	private MulticastSocket socket;
	private boolean isRunning = false;
	Thread mainThread;

	private final long TIMEOUT = 10000; // 10s
	private FastMap<String, NetworkNeighbor> neighbors = new FastMap<String, NetworkNeighbor>(10);

	public DiscoveryListener() {
		setDelay(0l);
		try {
			socket = new MulticastSocket(DEST_PORT);
		} catch (IOException e) {
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

						Date d = new Date(System.currentTimeMillis()-TIMEOUT);

						FastList<String> expired;

						synchronized(this) {
							expired = new FastList<String>(neighbors.size());
						}

						for(String key : neighbors.keySet()) {
							NetworkNeighbor n;

							synchronized(this) {
								n = neighbors.get(key);
							}

							if(n.getTimestamp().before(d)) {
								expired.add(key);
							}
						}

						for(String key : expired) {
							Kernel.syslog.debug("Remove expired neighbor " + key);
							NetworkNeighbor expiredNeighbor;
							expiredNeighbor = neighbors.remove(key);
							NetworkNeighborPacket np = new NetworkNeighborPacket(new Boolean(true), expiredNeighbor);
							publish(NetworkService.NEIGHBORHOOD, np);
						}
					} catch (IOException e) {
						System.err.println("Unable to receive datagram or interrupted.");
						return;
					}
				}
			}
		};

		mainThread = new Thread(task);
		mainThread.start();

	}

	public void close() {
		unsubscribe();
		isRunning = false;
		socket.close();
	}

	public void run() {
		// empty.
	}

	private final void addNetworkNeighbor(DatagramPacket dgram) {
		ArrayList<String> p = parse(dgram.getData());
		if(p != null) {
			if(!p.get(1).equals(Kernel.id)){
				if(!neighbors.containsKey(p.get(1))){
					Kernel.syslog.debug("Adding neighbor: " + p);
					NetworkNeighbor n = new NetworkNeighbor(p.get(1), p.get(0), Integer.valueOf(p.get(2)), new Date(System.currentTimeMillis()));
					neighbors.put(p.get(1), n);
					publish(NetworkService.NEIGHBORHOOD, new NetworkNeighborPacket(new Boolean(false), n));
				}
				else{
					neighbors.get(p.get(1)).setTimestamp(new Date(System.currentTimeMillis()));
				}
			}
		}
		return;
	}

	private final ArrayList<String> parse(byte[] data) {
		ArrayList<String> parameters = new ArrayList<String>(3);

		try {
			String message = new String(data, "US-ASCII");
			//System.out.println("Message :" + message);
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
