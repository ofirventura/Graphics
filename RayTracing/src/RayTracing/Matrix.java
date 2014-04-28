package RayTracing;


public class Matrix
{

	double[][] matrix;
	
	public Matrix()
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
	
	public Vector mulVector(Vector v)
	{
		Vector w = new Vector(0,0,0); 
		double[] row;
		double val;
		for (int i = 0 ; i < 3; i++)
		{
			row = getRow(i);
			val = v.getX()*row[0] + v.getY()*row[1] + v.getZ()*row[2];
			switch (i){
				case 0:
					w.setX(val);
					break;
				case 1:
					w.setY(val);
					break;
				case 2:
					w.setZ(val);
					break;
			}
			
		}
		return w;
	}
	/*
	public Matrix mulConst(double c){
		double[][] m = new double[3][3];
		for (int i = 0; i < m.length; i++)
		{
			for (int j = 0; j < m[0].length; j++)
			{
				m[i][j] = c*getCell(i, j);
			}
		}
		
		Matrix M = new Matrix();
		M.setMatrix(m);
		return M;
	}*/
}
