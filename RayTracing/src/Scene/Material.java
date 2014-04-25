package Scene;

import java.awt.Color;

public class Material
{

	/*
	 * "mtl" = defines a new material 
 	 * 		params[0,1,2] = diffuse color (r, g, b) 
  	 * 		params[3,4,5] = specular color (r, g, b) 
  	 * 		params[6,7,8] = reflection color (r, g, b) 
  	 * 		params[9] = phong specularity coefficient (shininess) 
  	 * 		params[10] = transparency value between 0 and 1 
	 */
	
	private Color diffuse;
	private Color specular;
	private Color reflection;
	private double phong;
	private double transparency;
	
	public Color getDiffuse()
	{
		return diffuse;
	}
	
	public void setDiffuse(Color diffuse)
	{
		this.diffuse = diffuse;
	}
	
	public Color getSpecular()
	{
		return specular;
	}
	
	public void setSpecular(Color specular)
	{
		this.specular = specular;
	}
	
	public Color getReflection()
	{
		return reflection;
	}
	
	public void setReflection(Color reflection)
	{
		this.reflection = reflection;
	}
	
	public double getPhong()
	{
		return phong;
	}
	
	public void setPhong(double phong)
	{
		this.phong = phong;
	}
	
	public double getTransparency()
	{
		return transparency;
	}
	
	public void setTransparency(double transparency)
	{
		this.transparency = transparency;
	}
	
	
}
