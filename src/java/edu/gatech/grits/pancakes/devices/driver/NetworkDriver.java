package edu.gatech.grits.pancakes.devices.driver;

public interface NetworkDriver<T> {
	
	public void request(T pkt);
	public T query();

}
