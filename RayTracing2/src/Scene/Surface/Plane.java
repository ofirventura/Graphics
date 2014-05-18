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
	

	public Vector3 getNormal(Vector3 p)
	{
		normal.normal();
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
	

    public Double intersect(Ray ray)
    {
        Vector3 p0 = ray.getP0();
        Vector3 v = ray.getV();
        v.normal();
        double nDotP0 = normal.dotProduct(p0);
        double nDotv = normal.dotProduct(v);
        double t = (offset - nDotP0) / nDotv ;
        if (t <= 0)
    		return Double.POSITIVE_INFINITY;
        return t;
     }
    
    @Override
    public String toString() {
    	return "Plane";
    }
}
