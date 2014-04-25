package Scene;

import RayTracing.Vector;


public class Camera
{
	/*
	 * "cam" = camera settings (there will be only one per scene file) 
	 * 		params[0,1,2] = position (x, y, z) of the camera 
	 * 		params[3,4,5] = look-at position (x, y, z) of the camera 
	 * 		params[6,7,8] = up vector (x, y, z) of the camera 
	 * 		params[9] = screen distance from camera 
	 * 		params[10] = screen width from camera 
	 */
	
	private Vector position;
	private Vector lookAtPosition;
	private Vector upVector;
	private double screenDist;
	private double screenWidth;
	
	public Vector getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector position)
	{
		this.position = position;
	}

	public Vector getLookAtPosition()
	{
		return lookAtPosition;
	}

	public void setLookAtPosition(Vector lookAtPosition)
	{
		this.lookAtPosition = lookAtPosition;
	}

	public Vector getUpVector()
	{
		return upVector;
	}

	public void setUpVector(Vector upVector)
	{
		this.upVector = upVector;
	}

	public double getScreenDist()
	{
		return screenDist;
	}

	public void setScreenDist(double screenDist)
	{
		this.screenDist = screenDist;
	}

	public double getScreenWidth()
	{
		return screenWidth;
	}

	public void setScreenWidth(double screenWidth)
	{
		this.screenWidth = screenWidth;
	}
}
