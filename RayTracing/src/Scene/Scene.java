package Scene;


import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;







import java.util.Map.Entry;

import RayTracing.Color;
import RayTracing.Ray;
import RayTracing.Vector3;
import Scene.Surface.Surface;


public class Scene
{
	
	/*
	 * "set" = general settings for the scene (once per scene file) 
 	 * 		params[0,1,2] = background color (r, g, b) 
  	 * 		params[3] = root number of shadow rays (N^2 rays will be shot) 
  	 * 		params[4] = maximum number of recursions 
	 */

	private Camera camera;
	private List<Light> lights;
	private List<Material> materials;
	private List<Surface> surfaces;
	private Color background;
	private int numOfShadowRays;
	private int maxNumOfRecursions;
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}
	
	public List<Light> getLights()
	{
		return lights;
	}
	
	public void setLights(List<Light> lights2)
	{
		this.lights = lights2;
	}
	
	public List<Material> getMaterials()
	{
		return materials;
	}
	
	public void setMaterials(List<Material> materials2)
	{
		this.materials = materials2;
	}
	
	public List<Surface> getSurfaces()
	{
		return surfaces;
	}
	
	public void setSurfaces(List<Surface> surfaces2)
	{
		this.surfaces = surfaces2;
	}
	
	public Color getBackground()
	{
		return background;
	}
	
	public void setBackground(Color background)
	{
		this.background = background;
	}
	
	public int getNumOfShadowRays()
	{
		return numOfShadowRays;
	}
	
	public void setNumOfShadowRays(int numOfShadowRays)
	{
		this.numOfShadowRays = numOfShadowRays;
	}
	
	public int getMaxNumOfRecursions()
	{
		return maxNumOfRecursions;
	}
	
	public void setMaxNumOfRecursions(int maxNumOfRecursions)
	{
		this.maxNumOfRecursions = maxNumOfRecursions;
	}
	
	public Color getColorForPixel(int row, int col, int imageWidth)
	{
		Color color = new Color();
		
		Ray ray = camera.createRay(row, col, imageWidth);
		HashMap<Surface, Double> distMap = new LinkedHashMap<Surface, Double>(); 
		
		for (Surface object : surfaces)
		{
			distMap.put(object, object.intersect(ray));
		}
		
		double minDist = Double.POSITIVE_INFINITY;
		Surface minSurface = null;
		
		for ( Entry<Surface, Double> entry : distMap.entrySet())
		{
			if (entry.getValue() < minDist)
			{
				minDist = entry.getValue();
				minSurface = entry.getKey();
			}
		}
		//System.out.println(minDist);
		//if (minSurface.toString().equals("Sphere")){
			//System.out.println(minSurface);
		//}
		color = getPixelColor(row, col, minSurface, minDist, ray);
		
		return color.getColorInByte();
	}

	
	// this function calculates the color of each pixel, but first i want to check it without the lights...
	private Color getPixelColor(int row, int col, Surface surface, double dist, Ray ray)
	{
		if (Double.isInfinite(dist) || surface == null)
		{
			// no intersection
			return this.background;
		}
		
		Vector3 point = ray.getP0().add(ray.getV().mul(dist));
		Vector3 normal = surface.getNormal(point); 
		Material material = this.materials.get(surface.getMaterial());
		
		return illuminati(normal, point, material, ray);   //this gets the color with all the light, should first test without it
		//return material.getDiffuse();
	}
	
	private Color illuminati(Vector3 normal, Vector3 point, Material material, Ray ray)
	{
		Vector3 pixelColor = new Vector3(0,0,0);
		
		for (Light light : this.lights)
		{
			//calc diffuse
			Vector3 l = light.getPosition().sub(point);
			l.normal();
			
			double nl = normal.dotProduct(l);
			
			if (nl < 0)
			{
				continue;
			}
		
			double red = material.getDiffuse().getR() * light.getColor().getR() * nl; 
			double green = material.getDiffuse().getG() * light.getColor().getG() * nl;
			double blue = material.getDiffuse().getB() * light.getColor().getB() * nl;
			pixelColor = pixelColor.add(new Vector3(red, green, blue));
			
			//calc specular
			
			Vector3 r = normal.mul(2 * l.dotProduct(normal)).sub(l);
			r.normal();
			
			double rv = Math.pow(r.dotProduct(ray.getV().mul(-1)), material.getPhong());
			
			red = material.getSpecular().getR() * light.getColor().getR() * light.getSpecularIntensity() * rv;
			green = material.getSpecular().getG() * light.getColor().getG() * light.getSpecularIntensity() * rv;
			blue = material.getSpecular().getB() * light.getColor().getB() * light.getSpecularIntensity() * rv;
			pixelColor = pixelColor.add(new Vector3(red,blue,green));
			
		}
		
		return new Color(pixelColor.getX(), pixelColor.getY(), pixelColor.getZ());
	}
}
