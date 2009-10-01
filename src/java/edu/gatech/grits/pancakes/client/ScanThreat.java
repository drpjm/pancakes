package edu.gatech.grits.pancakes.client;

import java.awt.geom.Point2D;                                                                                                                                     

import org.jetlang.core.Callback;

import javolution.util.*;   

import edu.gatech.grits.pancakes.client.util.TrackBoundary;
import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.*;
import edu.gatech.grits.pancakes.service.ClientService;

public class ScanThreat extends Task {                                                                                                               

	// Player
	private float radius = 4.0f;                                                                                                                        
	private float circGain = 3f;                                                                                                                       
	private float maxVel = 0.8f;
	
	// K3
//	private float radius = 35.0f;                                                                                                                        
//	private float circGain = 3f;                                                                                                                       
//	private float maxVel = 0.2f;

	
	private FastMap<String, Point2D.Float> neighborPoints;
	private TrackBoundary tracker;

	public ScanThreat() {
		
		setDelay(0l);
		neighborPoints = new FastMap<String, Point2D.Float>();
		tracker = new TrackBoundary(maxVel, circGain, radius, 0, 0);
		
		Callback<Packet> cbk = new Callback<Packet>(){

			public void onMessage(Packet pkt) {
				if(pkt.getPacketType().equals(PacketType.LOCAL_POSE)) {
					float k;

					LocalPosePacket localData = (LocalPosePacket) pkt;
//					Kernel.syslog.record(localData);
					localData.debug();
					
					MotorPacket ctrl = tracker.calculate(localData, neighborPoints);
					
//					Kernel.syslog.record(ctrl);
					publish(CoreChannel.COMMAND, ctrl);

				}
				else if(pkt instanceof NetworkPacket){
					NetworkPacket netPkt = (NetworkPacket) pkt;
					String src = netPkt.getSource();
					Packet p1 = netPkt.getPackets().get(0);
					if(p1 instanceof LocalPosePacket){
						Kernel.syslog.debug(this.getClass().getSimpleName() + ": add neighbor's local pose.");
					}
				}
			}

		};
		subscribe(CoreChannel.SYSTEM, cbk);
		
		Callback<Packet> batteryCbk = new  Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message instanceof ControlPacket){
					
					Kernel.syslog.debug("************* Changing params *************");
					radius = radius + 20f;
					maxVel = 0.5f * maxVel;
					Kernel.syslog.debug(" vel=" + maxVel + " rad=" + radius);
					
				}
			}
			
		};
		subscribe(ClientService.BATTERY_UPDATE, batteryCbk);
		
	}

	private final Point2D.Float rotate(Point2D.Float vec, float angle){

		float xp = (float) (vec.getX()*Math.cos(angle) + vec.getY()*Math.sin(angle));
		float yp = (float) (-vec.getX()*Math.sin(angle) + vec.getY()*Math.cos(angle));

		Point2D.Float ret = new Point2D.Float(xp, yp);

		return ret;
	}

	private final float norm(Point2D.Float vec){

		return (float) Math.sqrt( innerProduct(vec,vec) );

	}

	private final float innerProduct(Point2D.Float vec1, Point2D.Float vec2){

		return (float) (vec1.getX()*vec2.getX() + vec1.getY()*vec2.getY());

	}

	public void close() {
		// TODO Auto-generated method stub

	}


	public void run() {
		// TODO Auto-generated method stub

	}

}
