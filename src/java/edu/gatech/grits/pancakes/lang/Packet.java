package edu.gatech.grits.pancakes.lang;

import java.io.Serializable;
import java.util.Map.Entry;

import javolution.util.FastMap;

public class Packet implements Serializable {	

	private static final long serialVersionUID = -5572583233040310877L;

	private String typeOfPacket;
	private FastMap<String, String> dataMap = new FastMap<String, String>();
	
	public Packet(String type) {
		typeOfPacket = type;
	}
	
	public final String getPacketType() {
		return typeOfPacket;
	}
	
	public final void add(String name, Object element) {
		dataMap.put(name, element.toString());
	}
	
	public final String get(String name) {
		return dataMap.get(name);
	}
	
	public final void debug() {
		System.out.print("<" + typeOfPacket + ">");
		for(Entry<String,String> e : dataMap.entrySet()) {
			System.out.print("<" + e.getKey() + ">" + e.getValue() + "</" + e.getKey() + ">");
		}
		System.out.println("</" + typeOfPacket + ">");
	}
	
	public final String toString() {
		String out = "<" + typeOfPacket + ">";
		for(Entry<String,String> e : dataMap.entrySet()) {
			out += "<" + e.getKey() + ">" + e.getValue() + "</" + e.getKey() + ">";
		}
		out += "</" + typeOfPacket + ">";
		return out;
	}
	
	public final int getSize() {
		return dataMap.size();
	}
}
