package edu.gatech.grits.pancakes.core;

import javolution.util.FastMap;

import org.jetlang.channels.Channel;
import org.jetlang.channels.MemoryChannel;

import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Subscription;

public class Stream {

	private FastMap<String, Channel<Packet>> channels = new FastMap<String, Channel<Packet>>();
	
	public Stream() {
		channels.put("system", new MemoryChannel<Packet>());
		channels.put("network", new MemoryChannel<Packet>());
		channels.put("user", new MemoryChannel<Packet>());
		channels.put("log", new MemoryChannel<Packet>());
		channels.put("ctrl", new MemoryChannel<Packet>());
	}
	
	public final void createChannel(String channel) {
		synchronized(this) {
			channels.put(channel, new MemoryChannel<Packet>());
		}
	}
	
	public final void destoryChannel(String channel) {
		Channel<Packet> chl = null;
		
		synchronized(this) {
			chl = channels.remove(channel);
		}
		
		if(chl != null) {
			((MemoryChannel<Packet>) chl).clearSubscribers();
		}
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
	
	public final void subscribe(Subscription s) throws CommunicationException {
		Channel<Packet> chl = null;
		
		synchronized(this) {
			chl = channels.get(s.getChannel());
		}
		
		if(chl != null) {
			s.setDisposable(chl.subscribe(s.getFiber(), s.getCallback()));
		} else {
			throw new CommunicationException("Channel does not exist.");
		}
	}
	
	public final void unsubscribe(Subscription s) {
		s.getDisposable().dispose();
	}
	
	public class CommunicationException extends Exception {
		
		private static final long serialVersionUID = -7489495710491502682L;

		public CommunicationException(String desc) {
			super(desc);
		}
	}
}
