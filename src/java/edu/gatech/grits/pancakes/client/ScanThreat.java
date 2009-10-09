package edu.gatech.grits.pancakes.client;

import java.awt.geom.Point2D;                                                                                                                                     
import java.awt.geom.Point2D.Float;                                                                                                                               

import org.jetlang.core.Callback;

import javolution.util.*;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.ControlPacket;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.NetworkPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;
import edu.gatech.grits.pancakes.service.ClientService;
import edu.gatech.grits.pancakes.util.Properties;

public class ScanThreat extends Task {                                                                                                               

//	private float radius = 55.0f;	// k3
	private float radius = 4f;	// player
	private float circGain = 2f;   
	private float formGain = 1f;

	private float maxVel = 0.7f;	// player
//	private float maxVel = 0.13f;	// k3
	private float maxRotation = maxVel * 0.8f;

	private boolean switched;
	private Point2D.Float goal = new Point2D.Float(160.0f,(-80.0f + (float)(Math.random()*30)));
//	private Point2D.Float center = new Point2D.Float(10.0f,-9.0f);	// k3
	private Point2D.Float center = new Point2D.Float(0,0);	// player
	
	private FastMap<String, Point2D.Float> neighborPoints;

	public ScanThreat() {
		setDelay(0l);
		switched = false;
		neighborPoints = new FastMap<String, Point2D.Float>();
		
		Callback<Packet> cbk = new Callback<Packet>(){

			public void onMessage(Packet pkt) {
				
//				Kernel.syslog.debug("Received message: ");
				pkt.debug();
				// new neighbor update!
				if(pkt.getPacketType().equals(PacketType.NETWORK)){
					NetworkPacket np = (NetworkPacket) pkt;
					Kernel.syslog.debug(np.getSource());
					if(np.getPackets().getFirst() instanceof LocalPosePacket){
						LocalPosePacket lpp = (LocalPosePacket) np.getPackets().getFirst();
						Point2D.Float point = new Point2D.Float();
						point.setLocation(lpp.getPositionX(), lpp.getPositionY());
						neighborPoints.put(np.getSource(), point);
					}
				}
				// execute algorithm
				else if(pkt.getPacketType().equals(PacketType.LOCAL_POSE)) {

					LocalPosePacket localData = (LocalPosePacket) pkt;
					if(localData.getPositionX() != 0 && localData.getPositionY() != 0 && localData.getTheta() != 0){

						if(!switched){

							float k;
							float v = maxVel;
							// hard wired target point
							Point2D.Float r1 = center;
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
								float middleTerm = (float) ( circGain*(1-Math.pow( radius / norm(r), 2)) ) * ( innerProduct(r,y2)/norm(r) );
								float lastTerm =  innerProduct(x1,x2)/norm(r);
								k = innerProduct(x1, y2) - middleTerm - lastTerm;
							}
							else{
								k = 0;
							}

							v = spacingController(v, localData, neighborPoints);

							MotorPacket ctrl = new MotorPacket();
							ctrl.setVelocity(v);
							ctrl.setRotationalVelocity(k*v);

							publish(CoreChannel.COMMAND, ctrl);
						}
						// GoToGoal
						else{
							MotorPacket ctrl = goToGoal(localData);
							Kernel.syslog.record(ctrl);
							publish(CoreChannel.COMMAND, ctrl);
						}

					}
				}
			}

		};
		subscribe(CoreChannel.SYSTEM, cbk);

		Callback<Packet> batteryPkt = new  Callback<Packet>(){

			public void onMessage(Packet message) {
				if(message instanceof ControlPacket){

					if(((ControlPacket)message).getControl().equals("MED")){
						Kernel.syslog.debug("************* Changing params *************");
						radius = 35.0f;
						maxVel = 0.06f;
						circGain = 3f;
					}
					else if(((ControlPacket)message).getControl().equals("LOW")){
						switched = true;
						Kernel.syslog.debug("************* Go Home! *************");
					}

				}
			}

		};
		subscribe(ClientService.BATTERY_UPDATE, batteryPkt);
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

	private final MotorPacket goToGoal(LocalPosePacket localPose){

		MotorPacket mp = new MotorPacket();

		float xPos = localPose.getPositionX();
		float yPos = localPose.getPositionY();
		float theta = localPose.getTheta();

		float diffX = (float) (goal.getX() - xPos);
		float diffY = (float) (goal.getY() - yPos);
		float distance = (float) Math.sqrt(diffX*diffX + diffY*diffY);

		float targetTheta = (float) Math.atan2(diffY, diffX);
		float omega = targetTheta - theta;

		float vel = 0;
		float rotation = omega;

		if( Math.abs(omega) <  (float) (Math.PI/8) ){
			float velCalc = distance / 100f;
			if( Math.abs(velCalc) < maxVel ){
				vel = velCalc;
			}
			else {
				vel = maxVel;
			}
		}

		if( omega < -maxRotation){
			omega = -maxRotation;
		}
		else if(omega > maxRotation){
			omega = maxRotation;
		}

		mp.setVelocity(vel);
		mp.setRotationalVelocity(rotation);
		return mp;

	}

	private final float spacingController(float currVel, LocalPosePacket localPose, FastMap<String, Point2D.Float> neighborPoints){
		float newVel = currVel;
		int numOfNeighbors = neighborPoints.size();
		if(numOfNeighbors > 0){
			float desiredArcSeparation = (float) (2 * Math.PI * radius / (numOfNeighbors + 1));

			float closestNeighborDist = desiredArcSeparation / radius;

			float myAngle = (float) Math.atan2(localPose.getPositionY() - center.getY(), 
					localPose.getPositionX() - center.getX());

			boolean isFirst = true;
			for(FastMap.Entry<String, Point2D.Float> curr = neighborPoints.head(), end = neighborPoints.tail(); (curr = curr.getNext()) != end;){

				Point2D.Float neighbor = curr.getValue();
				Point2D.Float neighborToCenter = new Point2D.Float();
				neighborToCenter.setLocation(center.getX() - neighbor.getX(),
						center.getY() - neighbor.getY());

				float thresholdRadius = radius + 1f;
				if( norm(neighborToCenter) < thresholdRadius ){

					float neighborAngle = (float) Math.atan2(neighbor.getY() - center.getY(), neighbor.getX() - center.getX());
					float angleDiff = neighborAngle - myAngle;
					if(angleDiff < 0){
						angleDiff = (float) (angleDiff + 2*Math.PI);
					}else if(angleDiff >= 2*Math.PI){
						angleDiff = (float) (angleDiff - 2*Math.PI);
					}

					// Save this agent distance temporarily if it is currently the closest
					if(isFirst || angleDiff <= closestNeighborDist){
						closestNeighborDist = angleDiff;
						isFirst = false;
					}

				}

			}
			newVel = maxVel + formGain * (desiredArcSeparation - radius * closestNeighborDist);
			if(newVel > maxVel){
				newVel = maxVel;
			}
			else if (newVel < -maxVel){
				newVel = -maxVel;
			}
//			Kernel.syslog.debug("Linear speed: " + newVel);
		}
		return newVel;
	}

	public void close() {
		// TODO Auto-generated method stub

	}


	public void run() {
		// TODO Auto-generated method stub

	}

}
