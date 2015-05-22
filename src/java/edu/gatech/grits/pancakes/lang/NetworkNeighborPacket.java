package edu.gatech.grits.pancakes.lang;

import java.util.Date;

public class NetworkNeighborPacket extends Packet {

	private static final long serialVersionUID = -1732234663974191834L;

	public NetworkNeighborPacket() {
		super(PacketType.NEIGHBOR);
	}

	public NetworkNeighborPacket(Boolean expired, NetworkNeighbor n) {
		this();
		addNeighbor(expired, n);
	}

	public final void addNeighbor(Boolean expired, NetworkNeighbor n){
		this.add("expired", expired.toString());
		this.add("hostname", n.getHostname());
		this.add("id", n.getID());
		this.add("netport", Integer.valueOf(n.getNetworkPort()));
	}
	
	public final Boolean isExpired(){
		return Boolean.valueOf(this.get("expired"));
	}
	
	public final NetworkNeighbor getNeighbor(){
		String id = this.get("id");
		String hostname = this.get("hostname");
		int port = Integer.valueOf(this.get("netport")).intValue();
		Date d = new Date(0);
		
		return new NetworkNeighbor(id, hostname, port, d);
	}
	
	public static void main(String[] args){
		
		long timeout  = 5000;
		NetworkNeighbor n = new NetworkNeighbor("8", "awesome", 89, new Date(System.currentTimeMillis()-timeout));
		NetworkNeighborPacket pkt = new NetworkNeighborPacket();
		pkt.addNeighbor(true, n);
		pkt.debug();
		
	}
}
