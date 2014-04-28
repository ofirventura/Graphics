package Scene.Surface;

import RayTracing.Ray;
import RayTracing.Vector;


public class Sphere extends Surface
{

	/*
	 * "sph" = defines a new sphere 
 	 * 		params[0,1,2] = position of the sphere center (x, y, z) 
  	 * 		params[3] = radius 
  	 * 		params[4] = material index (integer). each defined material gets an 
  	 * 		automatic material index starting from 1, 2 and so on
	 */

	private Vector center;
	private double radius;
	
	public Vector getCenter()
	{
		return center;
	}
	
	public void setCenter(Vector center)
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

    public Vector intersect(Ray ray) {
    	Vector v = ray.getV();
    	v.normal();
    	Vector p0 = ray.getP0();
        Vector a = center.sub(p0);
        double vDotProA = v.dotProduct(a);
        if (vDotProA < 0)
            return null;
        double l = v.length();    
        Vector R = v.mul(vDotProA/(l*l));
        double r_l = R.length();
        double q = Math.sqrt(a.length() - r_l*r_l);
        if (q > radius)
            return null;
        double h = Math.sqrt(radius*radius - q*q);
        
        double t1 = (r_l-h)/l;
        Vector p1 = v.mul(t1).add(p0);
        Vector dist1 = p0.sub(p1);
        
        double t2 = (r_l+h)/l;
        Vector p2 = v.mul(t2).add(p0);
        Vector dist2 = p0.sub(p2);
        
        return dist1.length() > dist2.length() ? p2 : p1; 
    }	
    
    public Vector getNormal(Vector p) {
    	Vector result = p.sub(center);
    	result.normal();
        return result;
    }
		
}
