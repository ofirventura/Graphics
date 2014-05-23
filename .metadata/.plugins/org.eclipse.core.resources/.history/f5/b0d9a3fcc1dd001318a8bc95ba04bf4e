package RayTracing;


public class Color
{
	private double r;
	private double g;
	private double b;

	public Color()
	{
		this.r = (double)0.0;
		this.g = (double)0.0;
		this.b = (double)0.0;
	}
	
	public Color(double red, double green, double blue)
	{
		this.r = red;
		this.g = green;
		this.b = blue;
	}

	public double getR()
	{
		return r;
	}

	public void setR(double r)
	{
		this.r = r;
	}

	public double getG()
	{
		return g;
	}

	public void setG(double g)
	{
		this.g = g;
	}

	public double getB()
	{
		return b;
	}

	public void setB(double b)
	{
		this.b = b;
	}
	
	public Color getColorInByte()
	{
		this.r = this.r > 1 ? (double)1.0 : this.r;
		this.g = this.g > 1 ? (double)1.0 : this.g;
		this.b = this.b > 1 ? (double)1.0 : this.b;
		
		return new Color((double)Math.floor(this.r*255), (double)Math.floor(this.g*255), (double)Math.floor(this.b*255));
	}
	
	

}
