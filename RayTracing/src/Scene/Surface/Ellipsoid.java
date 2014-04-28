package Scene.Surface;


import RayTracing.Matrix3;
import RayTracing.Vector3;
import RayTracing.Ray;

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
	
	public Vector3 getNormal(Vector3 p)
	{
		Vector3 result = matrix.mulVector(p).sub(matrix.mulVector(center));
		result.normal();
		return result;
	}
	
	public Vector3 intersect(Ray ray)
	{
		Vector3 v = ray.getV();
		Vector3 p0 = ray.getP0();
		Vector3 v1 = matrix.mulVector(v);
		Vector3 p1 = matrix.mulVector(p0).sub(matrix.mulVector(center));
		double x = (-2)*p1.dotProduct(v1);
		double v1Len = v1.length();
		double p1Len = p1.length();
		double delta = x*x - 4*v1Len*v1Len*(p1Len*p1Len-1);
		
		// no intersection
		if (delta < 0 )
		{
			return null;
		}
		// one point of intersection
		else if (delta == 0)
		{
			double t= x/(2*v1Len*v1Len);
			return p0.add(v.mul(t));
		}
		// two points
		else 
		{
			double t1 = (x+Math.sqrt(delta))/(2*v1Len*v1Len);
			Vector3 result1 = p0.add(v.mul(t1));
			Vector3 dist1 = p0.sub(result1);
			
			double t2 = (x-Math.sqrt(delta))/(2*v1Len*v1Len);
			Vector3 result2 = p0.add(v.mul(t2));
			Vector3 dist2 = p0.sub(result2);
			
			return dist1.length() > dist2.length() ? result2 : result1;
		}	
	}
}
