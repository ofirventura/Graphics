package RayTracing;


public class Vector
{


	private double x;
	private double y;
	private double z;
	
	public Vector(double x, double y, double z)
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

    public double length() 
    {
        return Math.sqrt(dotProduct(this));
    }

	public double dotProduct(Vector v) {
		return this.x*v.x + this.y*v.y + this.z*v.z; 
	}
	
    public Vector add(Vector v) {
        return new Vector(x+v.x, y+v.y, z+v.z);
    }

    public Vector mul(double c) {
        return new Vector(c*x, c*y, c*z);
    }

    public Vector sub(Vector v) {
        return add(v.mul(-1));
    }
	
}
