package edu.gatech.grits.pancakes.lang;

public class NetworkPacket extends Packet {

	private static final long serialVersionUID = 7163598483472214897L;
	
	public NetworkPacket(String src, String dst) {
		super("network");
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

	
}
