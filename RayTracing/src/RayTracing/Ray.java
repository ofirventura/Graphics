package RayTracing;

public class Ray {

    private Vector3 p0;
    private Vector3 v;
       
    public Ray(Vector3 p0, Vector3 v) {
		this.p0 = p0;
		this.v = v;
	}
	public Vector3 getP0() {
		return p0;
	}
	public Vector3 getV() {
		return v;
	}
	
	
}
