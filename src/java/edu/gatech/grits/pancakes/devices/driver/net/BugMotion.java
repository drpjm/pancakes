package edu.gatech.grits.pancakes.devices.driver.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.MotionPacket;
import edu.gatech.grits.pancakes.net.NetworkServer;

public class BugMotion extends NetworkServer implements HardwareDriver<MotionPacket> {

	public BugMotion() {
		super(4324);
		// TODO Auto-generated constructor stub
	}

	public MotionPacket query() {
		// TODO Auto-generated method stub
		return null;
	}

	public void request(MotionPacket pkt) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleConnection(Socket client) {
		InputStream in;
		try {
			//System.out.println("Receiving a packet!");
			in = client.getInputStream();
			ObjectInputStream oin = new ObjectInputStream(in);
			String msg = (String) oin.readObject();
//			oin.close();
//			in.close();
			MotionPacket pkt = new MotionPacket();
			pkt.setMotionDetected(true);
			Kernel.getInstance().getStream().publish(CoreChannel.SYSTEM, pkt);
		} catch (IOException e) {
			System.err.println("Error receiving object.");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
