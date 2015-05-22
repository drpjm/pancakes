/**
 * 
 */
package edu.gatech.grits.pancakes.devices;

import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;

/**
 * @author jean-pierre
 *
 */
public class LocalPoseDevice extends Device<LocalPosePacket> {

	public LocalPoseDevice(Backend backend, long delay) {
		super(backend, "LocalPose", null, delay);
	}
}

