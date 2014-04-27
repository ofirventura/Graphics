package RayTracing;


public class Matrix3
{

	double[][] matrix;
	
	public Matrix3()
	{
		this.matrix = new double[3][3];
	}
	
	public void setMatrix(double[][] m)
	{
		this.matrix = m;
	}
	
	public void setRow(int row, double one, double two, double three)
	{
		this.matrix[row][0] = one;
		this.matrix[row][1] = two;
		this.matrix[row][2] = three;
	}
	
	public void setCell(int row, int col, double member)
	{
		this.matrix[row][col] = member;
	}
	
	public double[][] getMatrix()
	{
		return this.matrix;
	}
	
	public double[] getRow(int row)
	{
		return this.matrix[row];
	}
	
	public double getCell(int row, int col)
	{
		return this.matrix[row][col];
	}
}
