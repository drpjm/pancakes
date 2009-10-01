package edu.gatech.grits.pancakes.client.util;

import java.awt.geom.Point2D;

import javolution.util.*;

import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;

public class TrackBoundary {

	private float radius = 35.0f;                                                                                                                        
	private float circGain = 3f; 
	private float formGain = 1f;
	private float maxVel = 0.2f;
	private Point2D.Float targetPt;

	public TrackBoundary(float vel, float gain, float rad, float targx, float targy){
		this.radius = rad;
		this.circGain = gain;
		this.maxVel = vel;
		targetPt = new Point2D.Float(targx, targy);
	}

	public final MotorPacket calculate(LocalPosePacket localPose, FastMap<String, Point2D.Float> neighborPoints){
		
		float k;
		float v;
		
		// hard wired target point
		Point2D.Float r1 = targetPt;
		Point2D.Float r2 = new Point2D.Float(localPose.getPositionX(), localPose.getPositionY());

		float theta = localPose.getTheta();

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
		v = maxVel;
		
		// spacing control adjust
		int numOfNeighbors = neighborPoints.size();
		if(numOfNeighbors > 0){
			float desiredArcSeparation = (float) (2 * Math.PI * radius / (numOfNeighbors + 1));
			
			float closestNeighborDist = desiredArcSeparation / radius;
			
			float myAngle = (float) Math.atan2(localPose.getPositionY() - targetPt.getY(), 
					localPose.getPositionX() - targetPt.getX());
			
			boolean isFirst = true;
			for(FastMap.Entry<String, Point2D.Float> curr = neighborPoints.head(), end = neighborPoints.tail(); (curr = curr.getNext()) != end;){
				
				Point2D.Float neighbor = curr.getValue();
				Point2D.Float neighborToCenter = new Point2D.Float();
				neighborToCenter.setLocation(targetPt.getX() - neighbor.getX(),
						targetPt.getY() - neighbor.getY());
				
				float thresholdRadius = radius + 5f;
				if( norm(neighborToCenter) < thresholdRadius ){
					
					float neighborAngle = (float) Math.atan2(neighbor.getY() - targetPt.getY(), neighbor.getX() - targetPt.getX());
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
			v = maxVel + formGain * (desiredArcSeparation - radius * closestNeighborDist);
			if(v > maxVel){
				v = maxVel;
			}
			else if (v < -maxVel){
				v = -maxVel;
			}
			
		}
		
		MotorPacket ctrl = new MotorPacket();
		ctrl.setVelocity(v);
		ctrl.setRotationalVelocity(k*v);

		return ctrl;
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
	public final float getRadius() {
		return radius;
	}


	public final void setRadius(float radius) {
		this.radius = radius;
	}


	public final float getCircGain() {
		return circGain;
	}


	public final void setCircGain(float circGain) {
		this.circGain = circGain;
	}


	public final float getMaxVel() {
		return maxVel;
	}


	public final void setMaxVel(float maxVel) {
		this.maxVel = maxVel;
	}
	
	
}
