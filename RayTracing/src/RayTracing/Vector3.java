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

	public void normal()
	{
		double num = Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
		
		if (num != 0)
		{
			this.x /= num;
			this.y /= num;
			this.z /= num;
		}
		
	}

	public Vector3 crossProduct(Vector3 v)
	{

		Vector3 result = new Vector3(0, 0, 0);
		
		result.setX((this.y * v.getZ()) - (this.z * v.getY()));
		result.setY((this.x * v.getZ()) - (this.z * v.getX()));
		result.setZ((this.y * v.getX()) - (this.x * v.getY()));
		
		return null;
	}

	public Vector3 substract(Vector3 v)
	{
		Vector3 result = new Vector3(0, 0, 0);
		
		result.setX(this.x - v.getX());
		result.setY(this.y - v.getY());
		result.setZ(this.z - v.getZ());
		
		return result;
	}

}
