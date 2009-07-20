package edu.gatech.grits.pancakes.device;

import org.jetlang.core.Callback;
import org.jetlang.fibers.Fiber;

import edu.gatech.grits.pancakes.structures.MotorPacket;
import edu.gatech.grits.pancakes.structures.Packet;
import edu.gatech.grits.pancakes.backend.Backend;
import edu.gatech.grits.pancakes.driver.HardwareDriver;
import edu.gatech.grits.pancakes.kernel.Kernel;
import edu.gatech.grits.pancakes.kernel.Stream.CommunicationException;

public class MotorDevice implements HardwareDevice {
	
	private HardwareDriver<MotorPacket> driver;
	private final Fiber fiber = Kernel.scheduler.newFiber();
	private Callback<Packet> callback;
	
	@SuppressWarnings("unchecked")
	public MotorDevice(Backend backend) {
		driver = (HardwareDriver<MotorPacket>) backend.getDriver("MotorDriver");
		fiber.start();
		callback = new Callback<Packet>() {
			public void onMessage(Packet pkt) {
				driver.request((MotorPacket) pkt);
			}
		};
		
		try {
			Kernel.stream.subscribe("user", fiber, callback);
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}
	
	public Packet query() {
		return driver.query();
	}
	
	public void request(Packet pkt) {
		driver.request((MotorPacket) pkt);
	};
	
	public void debug() {
		MotorPacket pkt = driver.query();
		
		System.err.println("Player Velocity pose: (" + pkt.getVelocity() + "," + pkt.getRotationalVelocity() + ")");
	}
}
