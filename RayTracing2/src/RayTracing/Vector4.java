package RayTracing;


public class Vector4
{


	private double x;
	private double y;
	private double z;
	private double w;
	
	public Vector4(double x, double y, double z, double w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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

	public double getW()
	{
		return w;
	}

	public void setW(double w)
	{
		this.w = w;
	}

	public void normal()
	{
		double num = Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z + this.w*this.w);
		
		if (num != 0)
		{
			this.x /= num;
			this.y /= num;
			this.z /= num;
			this.w /= num;
		}
		
	}



	public Vector4 substract(Vector4 v)
	{
		Vector4 result = new Vector4(0, 0, 0, 0);
		
		result.setX(this.x - v.getX());
		result.setY(this.y - v.getY());
		result.setZ(this.z - v.getZ());
		result.setZ(this.w - v.getW());
		
		return result;
	}

}
