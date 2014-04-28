package Scene;

import RayTracing.Matrix4;
import RayTracing.Vector3;


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
	
	private Vector3 position;
	private Vector3 lookAtPosition;
	private Vector3 upVector;
	private double screenDist;
	private double screenWidth;
	
	private Vector3 w;
	private Vector3 u;
	private Vector3 v;
	private Matrix4 m;
	
	public Vector3 getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector3 position)
	{
		this.position = position;
	}

	public Vector3 getLookAtPosition()
	{
		return lookAtPosition;
	}

	public void setLookAtPosition(Vector3 lookAtPosition)
	{
		this.lookAtPosition = lookAtPosition;
	}

	public Vector3 getUpVector()
	{
		return upVector;
	}

	public void setUpVector(Vector3 upVector)
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
	
	public void calcTransMatrix()
	{
		this.m = new Matrix4();
		
		this.w = lookAtPosition.sub(position);
		this.w.normal();
		
		this.u = upVector.crossProduct(this.w);
		this.u.normal();
		
		this.v = this.w.crossProduct(this.u);
		this.v.normal();
		
		this.m.setRow(0, this.w.getX(), this.w.getY(), this.w.getZ(), (double)0.0);
		this.m.setRow(1, this.u.getX(), this.u.getY(), this.u.getZ(), (double)0.0);
		this.m.setRow(2, this.v.getX(), this.v.getY(), this.v.getZ(), (double)0.0);
		this.m.setRow(3, (double)0.0, (double)0.0, (double)0.0, (double)1.0);
		
		Matrix4 newMat = new Matrix4();
		
		newMat.setRow(0, (double)1.0, (double)0.0, (double)0.0, (-1) * (double)v.getX());
		newMat.setRow(1, (double)0.0, (double)1.0, (double)0.0, (-1) * (double)v.getY());
		newMat.setRow(2, (double)0.0, (double)0.0, (double)1.0, (-1) * (double)v.getZ());
		newMat.setRow(3, (double)0.0, (double)0.0, (double)0.0, (double)1.0);
		
		this.m.setMatrix(this.m.multiply(newMat).getMatrix());
		
	}
	



}
