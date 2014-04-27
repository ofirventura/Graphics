package Scene.Surface;

import RayTracing.Ray;
import RayTracing.Vector3;


public class Sphere extends Surface
{

	/*
	 * "sph" = defines a new sphere 
 	 * 		params[0,1,2] = position of the sphere center (x, y, z) 
  	 * 		params[3] = radius 
  	 * 		params[4] = material index (integer). each defined material gets an 
  	 * 		automatic material index starting from 1, 2 and so on
	 */

	private Vector3 center;
	private double radius;
	
	public Vector3 getCenter()
	{
		return center;
	}
	
	public void setCenter(Vector3 center)
	{
		this.center = center;
	}

	public double getRadius()
	{
		return radius;
	}

	public void setRadius(double radius)
	{
		this.radius = radius;
	}

    public Vector3 intersect(Ray ray) {
    	Vector3 v = ray.getV();
    	Vector3 p0 = ray.getP0();
        Vector3 a = center.sub(p0);
        double vDotProA = v.dotProduct(a);
        if (vDotProA < 0)
            return null;
        double l = v.length();    
        Vector3 R = v.mul(vDotProA/(l*l));
        double r_l = R.length();
        double q = Math.sqrt(a.length() - r_l*r_l);
        if (q > radius)
            return null;
        double h = Math.sqrt(radius*radius - q*q);
        double RsubHdivV = (r_l-h)/l;
        return v.mul(RsubHdivV).add(p0);
    }	
		
}
