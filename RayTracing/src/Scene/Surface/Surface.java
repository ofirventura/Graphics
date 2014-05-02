package Scene.Surface;
import RayTracing.Ray;
import RayTracing.Vector3;


public abstract class Surface
{

	private int material;

	public int getMaterial()
	{
		return material;
	}

	public void setMaterial(int material)
	{
		this.material = material;
	}
	
	public abstract Double intersect(Ray ray);
	public abstract Vector3 getNormal(Vector3 p);
}
