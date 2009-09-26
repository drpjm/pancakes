package edu.gatech.grits.pancakes.client.util;

import java.awt.geom.Point2D;

import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;

public class TrackBoundary {

	private float radius = 30.0f;                                                                                                                        
	private float circGain = 4f;                                                                                                                       

	private float maxVel = 0.12f;


	public TrackBoundary(float vel, float gain, float rad){
		this.radius = rad;
		this.circGain = gain;
		this.maxVel = vel;
	}

	public final MotorPacket calculate(LocalPosePacket localPose){
		float k;

		// hard wired target point
		Point2D.Float r1 = new Point2D.Float(1.8f,-9.0f);
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

		MotorPacket ctrl = new MotorPacket();
		ctrl.setVelocity(maxVel);
		ctrl.setRotationalVelocity(k*maxVel);

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
