package edu.gatech.grits.pancakes.client;

import org.jetlang.core.Callback;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.PacketType;
import edu.gatech.grits.pancakes.lang.Task;

public class GoToGoal extends Task {


	private final float SIGHT_ANGLE = (float) Math.PI / 8;

	private final float MAX_TRANS = 0.1f;	// m/s
	private final float MAX_ROT = 0.22f;	// rad/s

	private final float goalX; // cm
	private final float goalY;	// cm


	public GoToGoal(){
		setDelay(1000l);

		goalX = 0;//Kernel.homeX;
		goalY = 0;//Kernel.homeY;

		Kernel.getInstance().getSyslog().debug("Going home: " + goalX + ", " + goalY);

		Callback<Packet> localPoseCbk = new Callback<Packet>(){

			@Override
			public void onMessage(Packet pkt) {

//				Kernel.getInstance().getSyslog().debug("Got message: " + pkt.getPacketType());

				if(pkt.getPacketType().equals(PacketType.LOCAL_POSE)){
					try{
						float xPos = ((LocalPosePacket) pkt).getPositionX();
						float yPos = ((LocalPosePacket) pkt).getPositionY();
						float theta = ((LocalPosePacket) pkt).getTheta();

						MotorPacket ctrl = calculateControl(xPos, yPos, theta);
						publish(CoreChannel.CTRL, ctrl);
					} catch(Exception e){
						e.printStackTrace();
					}
				}

			}

		};
		subscribe(CoreChannel.SYSTEM, localPoseCbk);
		// zero out motors
		publish(CoreChannel.CTRL, new MotorPacket());
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		Kernel.getInstance().getSyslog().debug(this.getClass().getSimpleName() + " running.");

	}

	private final MotorPacket calculateControl(float xPos, float yPos, float theta){

		float dx = goalX - xPos;
		float dy = goalY - yPos;
		float distance = (float) Math.sqrt( Math.pow(dx, 2) + Math.pow(dy, 2) );
		float theta_targ = (float) Math.atan2(dy, dx);
		
		// handle the [-pi, 0] coordinate system of K3s
		if(theta < 0){
			theta = (float) (theta + 2*Math.PI);
		}
		
		// rotational speed in rad/sec
		float omega = theta_targ - theta;

		// translational speed
		float vel = 0;

		//		Kernel.getInstance().getSyslog().debug("Angle diff: " + angleDiff);
		
		// can we "see" the goal?
		if(omega < SIGHT_ANGLE && omega > -SIGHT_ANGLE){

			float velCalc = distance / 100;	// scale to m/s

			// if so, drive towards it
			if(velCalc > MAX_TRANS){
				vel = MAX_TRANS;
			}
			else if(velCalc < -MAX_TRANS){
				vel = -MAX_TRANS;
			} 
			else {
				vel = velCalc;
			}

		} 
		else {

			if(omega > MAX_ROT){
				omega = MAX_ROT;
			}

			if(omega < -MAX_ROT){
				omega = -MAX_ROT;
			}

		}

//		Kernel.getInstance().getSyslog().debug("Vel: " + vel + ", Rotation: " + omega);

		return new MotorPacket(vel, omega);

	}

//	private final float[] calcDestination(float targetX, float targetY, float currX, float currY){
//
//		float destinationAngle = 0f;
//
//		//computer translated coordinates
//		float x_tran = targetX-currX;
//		float y_tran = targetY-currY;
//
//		//compute euclidean distance from inital position to final position
//
//		float distance = (float)Math.sqrt(Math.pow(x_tran, 2) + Math.pow(y_tran, 2));
//
//		float side = (float)Math.abs(x_tran / distance);
//		float theta_tran = (float)Math.acos(side);
//
//		if(x_tran>=0 && y_tran>=0){
//			//Quadrant I
//			destinationAngle= THREEPI_2 + theta_tran;
//		}
//		else if(x_tran<=0 && y_tran>=0){
//			// Quadrant II
//			destinationAngle = PI_2 - theta_tran;
//		}
//		else if(x_tran<=0 && y_tran <=0){
//			// Quadrant III
//			destinationAngle = PI_2+theta_tran;
//		}
//		else if(x_tran>=0 && y_tran <=0){
//			// Quadrant IV
//			destinationAngle = THREEPI_2-theta_tran;
//		}
//		else{
//			destinationAngle = -1;
//		}
//
//		float[] data = {destinationAngle, distance};
//		return data;
//
//	}


}
