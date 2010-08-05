package edu.gatech.grits.pancakes.lang;

import javolution.util.FastList;

public class NetworkPacket extends Packet {

	private static final long serialVersionUID = 7163598483472214897L;
	private FastList<Packet> payloadPkts = new FastList<Packet>(3);
	
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
	
	public final String getSource() {
		return get("src");
	}
	
	public final void addPayloadPacket(Packet p) {
		payloadPkts.add(p);
	}
	
	public final FastList<Packet> getPayloadPackets() {
		return payloadPkts;
	}

	
	
}
