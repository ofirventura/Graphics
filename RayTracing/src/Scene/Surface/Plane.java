package Scene.Surface;

import RayTracing.Ray;
import RayTracing.Vector3;


public class Plane extends Surface
{

	/*
	 * "pln" = defines a new plane 
 	 * 		params[0,1,2] = normal (x, y, z) 
 	 * 		params[3] = offset 
  	 * 		params[4] = material index
	 */
	
	private Vector3 normal;
	private double offset;
	
	public Vector3 getNormal()
	{
		return normal;
	}
	
	public void setNormal(Vector3 normal)
	{
		this.normal = normal;
	}

	public double getOffset()
	{
		return offset;
	}

	public void setOffset(double offset)
	{
		this.offset = offset;
	}
	
    public Vector3 intersect(Ray ray) {
        Vector3 p0 = ray.getP0();
        Vector3 v = ray.getV();
        double nDotP0 = normal.dotProduct(p0);
        double nDotv = normal.dotProduct(v);
        if (nDotv == 0)
        		return null;
        double t = (offset - nDotP0) / nDotv ;
        return p0.add(v.mul(t));
    }
	
}
