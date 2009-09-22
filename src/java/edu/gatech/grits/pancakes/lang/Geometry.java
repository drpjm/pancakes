package edu.gatech.grits.pancakes.lang;

public class Geometry {

	public static float PI_2 = (float)(Math.PI / 2);
	public static float THREEPI_2 = (float)(3 * Math.PI / 2);
	
	/**
	 * This method calculates the angle and distance to a target using the coordinates:
	 * y-axis = 0 rad
	 * x-axis = -pi/2 rad
	 * Returns an array: { angle, distance }
	 * 
	 * @param targetX
	 * @param targetY
	 * @param currX
	 * @param currY
	 * @return
	 */
	public static final float[] calcDestination(float targetX, float targetY, float currX, float currY){
		
		float destinationAngle = 0f;
		
		//computer translated coordinates
		float x_tran = targetX-currX;
		float y_tran = targetY-currY;

		//compute euclidean distance from inital position to final position

		float distance = (float)Math.sqrt(Math.pow(x_tran, 2) + Math.pow(y_tran, 2));

		float temp1=(float)Math.abs(x_tran / distance);
		float theta_temp = (float)Math.acos(temp1);

		if(x_tran>=0 && y_tran>=0){
			//Quadrant I
			destinationAngle= THREEPI_2 + theta_temp;
		}
		else if(x_tran<=0 && y_tran>=0){
			// Quadrant II
			destinationAngle = PI_2 - theta_temp;
		}
		else if(x_tran<=0 && y_tran <=0){
			// Quadrant III
			destinationAngle = PI_2+theta_temp;
		}
		else if(x_tran>=0 && y_tran <=0){
			// Quadrant IV
			destinationAngle = THREEPI_2-theta_temp;
		}
		else{
			destinationAngle = -1;
		}

		float[] data = {destinationAngle, distance};
		return data;
		
	}
}
