package RayTracing;


public class Vector3
{


	private double x;
	private double y;
	private double z;
	
	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX()
	{
		return x;
	}
	
	public void setX(double x)
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY(double y)
	{
		this.y = y;
	}

	public double getZ()
	{
		return z;
	}

	public void setZ(double z)
	{
		this.z = z;
	}


	public Vector3 crossProduct(Vector3 v)
	{

		Vector3 result = new Vector3(0, 0, 0);
		
		result.setX((this.y * v.getZ()) - (this.z * v.getY()));
		result.setY((this.z * v.getX()) - (this.x * v.getZ()));
		result.setZ((this.x * v.getY()) - (this.y * v.getX()));
		
		return result;
	}

	public double length() 
    {
        return Math.sqrt(dotProduct(this));
    }

	public double dotProduct(Vector3 v) {
		return this.x*v.x + this.y*v.y + this.z*v.z; 
	}
	
    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public Vector3 mul(double c) {
        return new Vector3(c * x, c * y, c * z);
    }

    public Vector3 sub(Vector3 v) {
        return add(v.mul(-1));
    }
    
    public void normal()
    {
    	double l = length();
    	if (l == 0) return;
    	x = x/l;
    	y = y/l;
    	z = z/l;
    }

}
