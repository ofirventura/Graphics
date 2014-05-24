package Scene;



import java.util.Random;

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
	
	private Vector3 towards;
	private Vector3 right;
	private Vector3 up;
	private Vector3 p1;
	
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
	
	public Vector3 getTowards()
	{
		return towards;
	}
	
	public Vector3 getRight()
	{
		return right;
	}
	
	public Vector3 getUp()
	{
		return up;
	}
	
	public Vector3 getP1()
	{
		return p1;
	}
	
	public void computeLightArea(Vector3 point, int numOfShadowRays)
	{
		towards = point.sub(position);
		towards.normal();
		
		right = towards.crossProduct(new Vector3((double)0.0, (double)1.0, (double)0.0));//.mul(-1);
		right.normal();
		
		up = towards.crossProduct(right);//.mul(-1);
		up.normal();
		
		double cellSize = radius/numOfShadowRays;
		Vector3 u = right.mul((cellSize * numOfShadowRays)/2.0);
		Vector3 w = up.mul((cellSize * numOfShadowRays)/2.0);
		p1 = position.sub(u).sub(w);

	}
	
	public Ray createRay(Vector3 point, int numOfShadowRays, int i, int j)
	{
		
		double cellSize = radius/numOfShadowRays;
		
		Random rand = new Random();
		Vector3 v = right.mul((i + rand.nextDouble()) * cellSize);
		Vector3 u = up.mul((j + rand.nextDouble()) * cellSize);
		Vector3 p = p1.add(v.add(u));
		
		Vector3 V;
		if (radius == 0)
		{
			V = this.position.sub(point);
		}
		else
		{
			V = p.sub(point);
		
		}
		
		V.normal();
		return new Ray(point.add(V.mul(0.00001)), V);
		
	}
}
