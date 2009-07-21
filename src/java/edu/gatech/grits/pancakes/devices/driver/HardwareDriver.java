/**
 * 
 */
package edu.gatech.grits.pancakes.devices.driver;

/**
 * @author jean-pierre
 *
 */
public interface HardwareDriver<T> {
	public T query();
	public void request(T pkt);
}
