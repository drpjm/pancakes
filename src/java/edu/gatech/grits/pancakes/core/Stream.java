package edu.gatech.grits.pancakes.core;

import javolution.util.FastMap;

import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;
import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.lang.LogPacket;
import edu.gatech.grits.pancakes.lang.Packet;

public class Stream {

	private FastMap<String, Channel<Packet>> channels = new FastMap<String, Channel<Packet>>();

	public Stream() {
		channels.put("system", new MemoryChannel<Packet>());
		channels.put("network", new MemoryChannel<Packet>());
		channels.put("user", new MemoryChannel<Packet>());
		channels.put("log", new MemoryChannel<Packet>());
	}
	
	
	public final void publish(String channel, Packet pkt) throws CommunicationException {
		Channel<Packet> chl = null;
		
		synchronized(this) {
			chl = channels.get(channel);
		}
		if(chl != null) {
			chl.publish(pkt);
		} else {
			throw new CommunicationException("Channel does not exist.");
		}
	}
	
	public final void subscribe(String channel, Fiber fiber, Callback<Packet> callback) throws CommunicationException {
		Channel<Packet> chl = null;
		
		synchronized(this) {
			chl = channels.get(channel);
		}
		
		if(chl != null) {
			chl.subscribe(fiber, callback);
		} else {
			throw new CommunicationException("Channel does not exist.");
		}
	}
	
	public class CommunicationException extends Exception {
		
		private static final long serialVersionUID = -7489495710491502682L;

		public CommunicationException(String desc) {
			super(desc);
		}
	}
}
