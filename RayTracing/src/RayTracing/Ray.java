package RayTracing;

public class Ray {

    private Vector p0;
    private Vector v;
       
    public Ray(Vector p0, Vector v) {
		this.p0 = p0;
		this.v = v;
	}
	public Vector getP0() {
		return p0;
	}
	public Vector getV() {
		return v;
	}
	
	
}
