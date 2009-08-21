package edu.gatech.grits.pancakes.lang;

import org.jetlang.core.Callback;
import org.jetlang.core.Disposable;
import org.jetlang.fibers.Fiber;

public class Subscription {

	private final Fiber fiber;
	private final Callback<Packet> callback;
	private Disposable disposable;
	private final String channel;
	
	public Subscription(String chl, Fiber f, Callback<Packet> c) {
		fiber = f;
		callback = c;
		channel = chl;
	}
	
	public final Fiber getFiber() {
		return fiber;
	}
	
	public final Callback<Packet> getCallback() {
		return callback;
	}
	
	public final void setDisposable(Disposable d) {
		disposable = d;
	}
	
	public final Disposable getDisposable() {
		return disposable;
	}
	
	public final String getChannel() {
		return channel;
	}
}
