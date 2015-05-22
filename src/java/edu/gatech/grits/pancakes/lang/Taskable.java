package edu.gatech.grits.pancakes.lang;

import org.jetlang.core.Callback;

public interface Taskable extends Runnable {
	
	public void close();
//	public void subscribe(String chl);
	public void subscribe(String chl, Callback<Packet> cbk);
	public void publish(String chl, Packet pkt);
	public void unsubscribe();
//	public void process(Packet pkt);
	public long delay();
	public void setDelay(long delay);
	public boolean isEventDriven();
	public boolean isTimeDriven();

}
