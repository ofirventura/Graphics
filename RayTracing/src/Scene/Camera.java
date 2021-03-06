package Scene;

import RayTracing.Matrix4;
import RayTracing.Ray;
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
	
	private Vector3 towards;
	private Vector3 right;
	private Vector3 up;
	//private Matrix4 m;
	
	private Vector3 p1;
	
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
		//m = new Matrix4();
		
		towards = lookAtPosition.sub(position);
		towards.normal();
		
		right = towards.crossProduct(upVector).mul(-1);
		right.normal();
		
		up = towards.crossProduct(right).mul(-1);
		up.normal();
		
		//m.setRow(0, towards.getX(), towards.getY(), towards.getZ(), (double)0.0);
		//m.setRow(1, right.getX(), right.getY(), right.getZ(), (double)0.0);
		//m.setRow(2, up.getX(), up.getY(), up.getZ(), (double)0.0);
		//m.setRow(3, (double)0.0, (double)0.0, (double)0.0, (double)1.0);
		
		//Matrix4 newMat = new Matrix4();
		
		//newMat.setRow(0, (double)1.0, (double)0.0, (double)0.0, (-1) * (double)position.getX());
		//newMat.setRow(1, (double)0.0, (double)1.0, (double)0.0, (-1) * (double)position.getY());
		//newMat.setRow(2, (double)0.0, (double)0.0, (double)1.0, (-1) * (double)position.getZ());
		//newMat.setRow(3, (double)0.0, (double)0.0, (double)0.0, (double)1.0);
		
		//m.setMatrix(m.multiply(newMat).getMatrix());
		
		computeP1();
	}

	public void computeP1()
	{
		double teta = getTeta();
		Vector3 u = right.mul(Math.tan(teta));
		Vector3 w = up.mul(Math.tan(teta));
		Vector3 v = towards.sub(u.add(w));
		p1 = position.add(v.mul(screenDist));
				
	}

	public double getTeta()
	{
		
		return Math.atan((screenWidth/2) / screenDist);
	}
	
	public Vector3 getP1()
	{
		return p1;
	}
	
	//public Matrix4 getM()
	//{
	//	return m;
	//}
	
	public Vector3 getTowards()
	{
		return towards;
	}
	
	public Vector3 getRight()
	{
		return right;
	}
	
	public Vector3 getUp()
	{
		return up;
	}
	
	public Ray createRay(int row, int col, int imageWidth)
	{
		
		Vector3 v = right.mul((row + 0.5)/imageWidth  * 2 * screenDist * Math.tan(getTeta()));
		Vector3 u = up.mul((col + 0.5)/imageWidth * 2 * screenDist * Math.tan(getTeta()));
		Vector3 p = p1.add(v.add(u));
		
		Vector3 V = p.sub(position);
		V.normal();
		
		return new Ray(position, V);
	}


}
