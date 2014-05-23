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


    public Double intersect(Ray ray) {
    	Vector3 v = ray.getV();
    	v.normal();
    	Vector3 p0 = ray.getP0();
        Vector3 a = center.sub(p0);
        double vDotProA = v.dotProduct(a);
        if (vDotProA < 0)
            return Double.POSITIVE_INFINITY;
        double l = v.length();    
        Vector3 R = v.mul(vDotProA/(l*l));   
        double r_l = R.length();
        double q = Math.sqrt(a.length()*a.length() - r_l*r_l);
        if (q > radius)
            return Double.POSITIVE_INFINITY;
        double h = Math.sqrt(radius*radius - q*q);
        
        double t1 = vDotProA - h;
        Vector3 p1 = v.mul(t1).add(p0);
        Vector3 dist1 = p0.sub(p1);
        
        double t2 = vDotProA + h;
        Vector3 p2 = v.mul(t2).add(p0);
        Vector3 dist2 = p0.sub(p2);
        
		if (t1 < 0 && t2 < 0)
			return Double.POSITIVE_INFINITY;
        if (t1 < 0)
        	return t2;
        if (t2 < 0)
        	return t1;
        
        return dist1.length() > dist2.length() ? t2 : t1; 
    }	
    
    public Vector3 getNormal(Vector3 p) {
    	Vector3 result = p.sub(center);
    	result.normal();
        return result;
    }
    
    @Override
    public String toString() {
    	return "Sphere";
    }
		
}
