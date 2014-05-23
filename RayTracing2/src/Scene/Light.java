package Scene;



import RayTracing.Color;
import RayTracing.Ray;
import RayTracing.Vector3;


public class Light
{

	/*
	 * "lgt" = defines a new light 
 	 * 		params[0,1,2] = position of the light (x, y, z) 
  	 * 		params[3,4,5] = light color (r, g, b) 
  	 * 		params[6] = specular intensity 
  	 * 		params[7] = shadow intensity 
  	 * 		params[8] = light width / radius (used for soft shadows)
	 */
	
	private Vector3 position;
	private Color color;
	private double specularIntensity;
	private double shadowIntensity;
	private double radius;
	
	public Vector3 getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector3 position)
	{
		this.position = position;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public double getSpecularIntensity()
	{
		return specularIntensity;
	}

	public void setSpecularIntensity(double specularIntensity)
	{
		this.specularIntensity = specularIntensity;
	}

	public double getShadowIntensity()
	{
		return shadowIntensity;
	}

	public void setShadowIntensity(double shdowIntensity)
	{
		this.shadowIntensity = shdowIntensity;
	}

	public double getRadius()
	{
		return radius;
	}

	public void setRadius(double radius)
	{
		this.radius = radius;
	}
	
	public Ray createRay(Vector3 point, int numOfShadowRays, int i, int j)
	{
		Vector3 V = this.position.sub(point);
		V.normal();
		return new Ray(point.add(V.mul(0.001)), V);
	}
}
