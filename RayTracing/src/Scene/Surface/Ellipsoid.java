package Scene.Surface;

import RayTracing.Matrix3;
import RayTracing.Vector3;


public class Ellipsoid extends Surface
{

	/*
	 * "elp" = defines a new ellipsoid 
 	 * 		params[0,1,2] = position of the ellipsoid center (x, y, z) 
  	 * 		params[3,4,5] = first row of transformation matrix 
  	 * 		params[6,7,8] = second row of transformation matrix 
  	 * 		params[9,10,11] = third row of transformation matrix 
  	 * 		params[12] = material index 
	 */
	
	private Vector3 center;
	private Matrix3 matrix;
	
	public Vector3 getCenter()
	{
		return center;
	}
	
	public void setCenter(Vector3 center)
	{
		this.center = center;
	}

	public Matrix3 getMatrix()
	{
		return matrix;
	}

	public void setMatrix(Matrix3 matrix)
	{
		this.matrix = matrix;
	}
}
