package edu.gatech.grits.pancakes.client;

import java.awt.geom.Point2D;                                                                                                                                     
import java.awt.geom.Point2D.Float;                                                                                                                               

import org.jetlang.core.Callback;

import javolution.util.FastMap;   

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.util.Properties;

public class ScanThreat extends Task {                                                                                                               

	private final float RADIUS = 30.0f;                                                                                                                        
	private final float CIRC_GAIN = 4f;                                                                                                                       

	private final float MAX_VEL = 0.12f;

	public ScanThreat() {
		setDelay(0l);
		Callback<Packet> cbk = new Callback<Packet>(){

			public void onMessage(Packet pkt) {
//				Kernel.syslog.debug("I hear something...");
				if(pkt.getPacketType().equals(PacketType.LOCAL_POSE)) {
					float k;

					LocalPosePacket localData = (LocalPosePacket) pkt;
//					localData.debug();
					
					// hard wired target point
					Point2D.Float r1 = new Point2D.Float(1.8f,-9.0f);
					Point2D.Float r2 = new Point2D.Float(localData.getPositionX(), localData.getPositionY());

					float theta = ((LocalPosePacket) pkt).getTheta();

					Point2D.Float x2 = new Point2D.Float((float)Math.cos(theta), (float)Math.sin(theta));
					Point2D.Float y2 = rotate(x2, (float)-Math.PI / 2);

					// vector and angle from target to agent
					Point2D.Float r = new Point2D.Float((float)(r2.getX() - r1.getX()), (float) (r2.getY() - r1.getY()));
					float beta = (float) Math.atan2(r.getY(), r.getX());

					Point2D.Float y1 = new Point2D.Float((float) Math.cos(beta), (float) Math.sin(beta));
					Point2D.Float x1 = rotate(y1, (float) Math.PI / 2);

					// check for when r is 0 ...
					if(norm(r) > 0){
						// if not, execute controller calculation!
						float middleTerm = (float) ( CIRC_GAIN*(1-Math.pow( RADIUS / norm(r), 2)) ) * ( innerProduct(r,y2)/norm(r) );
						float lastTerm =  innerProduct(x1,x2)/norm(r);
						k = innerProduct(x1, y2) - middleTerm - lastTerm;
					}
					else{
						k = 0;
					}

					MotorPacket ctrl = new MotorPacket();
					ctrl.setVelocity(MAX_VEL);
					ctrl.setRotationalVelocity(k*MAX_VEL);

//					ctrl.debug();
					
					publish("user", ctrl);

				}
			}

		};
		subscribe(CoreChannel.SYSTEM, cbk);
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
