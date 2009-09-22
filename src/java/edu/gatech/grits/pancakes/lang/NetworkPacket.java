package edu.gatech.grits.pancakes.lang;

import javolution.util.FastList;

public class NetworkPacket extends Packet {

	private static final long serialVersionUID = 7163598483472214897L;
	private FastList<Packet> packets = new FastList<Packet>(3);
	
	public NetworkPacket(String src, String dst) {
		super(PacketType.NETWORK);
		setDestination(dst);
		setSource(src);
	}
	
	public final void setDestination(String dst) {
		add("dst", dst);
	}
	
	public final String getDestination() {
		return get("dst");
	}
	
	public final void setSource(String src) {
		add("src", src);
	}
	
	public final String getSource(String src) {
		return get("src");
	}
	
	public final void addPacket(Packet p) {
		packets.add(p);
	}
	
	public final FastList<Packet> getPackets() {
		return packets;
	}

	
}
