package Scene;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

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
	
	public boolean isMaterialNumMatch()
	{
		int max = 0;
		for (Surface surface : surfaces)
		{
			if (surface.getMaterial() > max)
			{
				max = surface.getMaterial();
			}
		}
		
		if (max + 1 == materials.size())
		{
			return true;
		}
		
		return false;
	}
	
	public Color getColorForPixel(int row, int col, int imageWidth)
	{
		Ray ray = camera.createRay(row, col, imageWidth);
		Stack<Surface> objectStack = new Stack<Surface>();

		Vector3 pixelColor = trace(ray, 1, objectStack);
		Color color = new Color(pixelColor.getX(), pixelColor.getY(), pixelColor.getZ());
		return color.getColorInByte();
	}

	private Vector3 trace(Ray ray, int recursionDepth, Stack<Surface> stack) {
		
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
		
		if (Double.isInfinite(minDist) || minSurface == null)
		{
			// no intersection
			return new Vector3(background.getR(), background.getG(), background.getB());
		}
		Vector3 v = ray.getV();
		v.normal();
		
		Vector3 point = ray.getP0().add(v.mul(minDist));
		Vector3 normal = minSurface.getNormal(point); 
		Vector3 reflectionColor = new Vector3(0, 0, 0);
		Vector3 transparentColor = new Vector3(0, 0, 0);
		Material material = materials.get(minSurface.getMaterial());

		
		if (!stack.isEmpty() && stack.peek().equals(minSurface))
		{
			Ray newRay = new Ray(point.add(v.mul(0.00009)), v);
			stack.pop();
			return trace(newRay, recursionDepth, stack);
		}
		
		// Lights
		Vector3 color = illuminati(normal, point, material, ray);
		
		// Reflection
		if (getMaxNumOfRecursions() >= recursionDepth && isObjReflective(minSurface))
		{
			double c1 = -1.0 * normal.dotProduct(v);
			Vector3 Rl = v.add(normal.mul(2.0 * c1)); 
			Rl.normal();
			Ray newRay = new Ray(point.add(Rl.mul(0.001)), Rl);
			reflectionColor =  trace(newRay, recursionDepth + 1, stack);
		}
		
		// Transparency
		if (material.getTransparency() > 0.0)
		{
			stack.push(minSurface);
			Ray newRay = new Ray(point.add(v.mul(0.00009)), v);
			transparentColor = trace(newRay, recursionDepth, stack);
			if (!stack.isEmpty())
				stack.pop();
		}
		
		return outputColor(color, reflectionColor, transparentColor, material);
		
		
	}
	
	
	/*
	 * output color = (background color) * transparency + (diffuse + specular) * (1 - transparency) + (reflection color)
	 * */
	private Vector3 outputColor(Vector3 color, Vector3 reflectionColor, Vector3 transparentColor, Material material) {
		Color reflection = material.getReflection();
		Vector3 reflectionMulOrigin = new Vector3(reflection.getR()*reflectionColor.getX(), 
				reflection.getG()*reflectionColor.getY(),
				reflection.getB()*reflectionColor.getZ()); 
		double transparency = material.getTransparency();
		Vector3 output = transparentColor.mul(transparency).add(color.mul(1 - transparency)).add(reflectionMulOrigin);
		return output;
	}

	private boolean isObjReflective(Surface surface) {
		int index = surface.getMaterial();
		Color refCol = materials.get(index).getReflection();
		if (refCol.getR() > 0 || refCol.getG() > 0 || refCol.getB() > 0) return true;
		else return false;
	}

	private Vector3 illuminati(Vector3 normal, Vector3 point, Material material, Ray ray)
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
			
			Vector3 r = normal.mul(2.0 * l.dotProduct(normal)).sub(l);
			r.normal();
			
			double l_dot_r = r.dotProduct(ray.getV().mul(-1.0));
			
			if (l_dot_r >= 0.0) {
				
				if (l_dot_r > 1.0)
					l_dot_r = 1.0;
				
				double rv = Math.pow(l_dot_r, material.getPhong());
				
				red = material.getSpecular().getR() * light.getColor().getR() * light.getSpecularIntensity() * rv;
				green = material.getSpecular().getG() * light.getColor().getG() * light.getSpecularIntensity() * rv;
				blue = material.getSpecular().getB() * light.getColor().getB() * light.getSpecularIntensity() * rv;
				pixelColor = pixelColor.add(new Vector3(red,green,blue));

			}
		}
		
		return pixelColor;
	}
}
