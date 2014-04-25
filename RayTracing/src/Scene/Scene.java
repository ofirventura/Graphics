package Scene;

import java.awt.Color;
import java.util.List;

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
}
