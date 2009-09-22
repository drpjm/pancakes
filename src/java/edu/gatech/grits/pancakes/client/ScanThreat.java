package edu.gatech.grits.pancakes.client;

import java.awt.geom.Point2D;                                                                                                                                     
import java.awt.geom.Point2D.Float;                                                                                                                               

import javolution.util.FastMap;   

import edu.gatech.grits.pancakes.lang.LocalPosePacket;
import edu.gatech.grits.pancakes.lang.MotorPacket;
import edu.gatech.grits.pancakes.lang.Packet;
import edu.gatech.grits.pancakes.lang.Task;
                                                                                                                                                                  
public class ScanThreat extends Task {                                                                                                               
                                                                                                                                                                  
	public ScanThreat() {
		super("system", 0l);
		// TODO Auto-generated constructor stub
	}

	private final float RADIUS = 50.0f;                                                                                                                        
    private final float CIRC_GAIN = 1f;                                                                                                                       
                                                                                                                                                              
    private final float MAX_VEL = 0.1f;

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

	public void process(Packet pkt) {
		// TODO Auto-generated method stub
		if(pkt.getPacketType().equals("localpose")) {
			float k;
			
			LocalPosePacket localData = (LocalPosePacket) pkt;

            Point2D.Float r1 = new Point2D.Float(0,0);
            Point2D.Float r2 = new Point2D.Float(localData.getPositionX(), localData.getPositionY());
             
            float theta = ((LocalPosePacket) pkt).getTheta();

            Point2D.Float x2 = new Point2D.Float((float)Math.cos(theta), (float)Math.sin(theta));
            Point2D.Float y2 = rotate(x2, (float)-Math.PI / 2);
//		            System.out.println("Agent axes: " + x2 + "; " + y2);



            // vector and angle from target to agent
            Point2D.Float r = new Point2D.Float((float)(r2.getX() - r1.getX()), (float) (r2.getY() - r1.getY()));
            float beta = (float) Math.atan2(r.getY(), r.getX());

            Point2D.Float y1 = new Point2D.Float((float) Math.cos(beta), (float) Math.sin(beta));
            Point2D.Float x1 = rotate(y1, (float) Math.PI / 2);
//		            System.out.println("Target axes: " + x1 + "; " + y1);

            // check for when r is 0 ...
            if(norm(r) > 0){
                // if not, execute controller calculation!
            	float middleTerm = (float) ( CIRC_GAIN*(1-Math.pow( RADIUS / norm(r), 2)) ) * ( innerProduct(r,y2)/norm(r) );
                float lastTerm =  innerProduct(x1,x2)/norm(r);
                k = innerProduct(x1, y2) - middleTerm - lastTerm;
//	                    System.out.println("curvature: " + k);
            }
            else{
            	k = 0;
            }
         
            MotorPacket ctrl = new MotorPacket();
            ctrl.setVelocity(MAX_VEL);
            ctrl.setRotationalVelocity(k*MAX_VEL);
             
            publish("user", ctrl);
			}
		}

		public void run() {
			// TODO Auto-generated method stub
			
		}

}
