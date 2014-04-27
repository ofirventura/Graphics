package RayTracing;


public class Matrix4
{

	double[][] matrix;
	
	public Matrix4()
	{
		this.matrix = new double[4][4];
	}
	
	public void setMatrix(double[][] m)
	{
		this.matrix = m;
	}
	
	public void setRow(int row, double one, double two, double three, double four)
	{
		this.matrix[row][0] = one;
		this.matrix[row][1] = two;
		this.matrix[row][2] = three;
		this.matrix[row][3] = four;
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

	public Matrix4 multiply(Matrix4 newMat)
	{
		Matrix4 result = new Matrix4();
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				double cell = 0;
				for (int k = 0; k < 4; k++)
				{
					cell += this.matrix[i][k]*newMat.getCell(k, j);
				}
				result.setCell(i, j, cell);
			}
			
		}
		return result;
	}

	public Vector4 multiply(Vector3 v)
	{
		Vector4 result = new Vector4(0,0,0,0);
		
		for (int i = 0; i < 4; i++)
		{
			double cell = 0;
			for (int j = 0; j < 4; j++)
			{
				double member = j == 0 ? v.getX() : (j == 1 ? v.getY() : (j == 2 ? v.getZ() : (double)0.0));
				cell += this.matrix[i][j] * member;
			}
			
			if (i == 0)
			{
				result.setX(cell);
			}
			else if (i == 1)
			{
				result.setY(cell);
			}
			else if (i == 2)
			{
				result.setZ(cell);
			}
			else if (i == 3)
			{
				result.setW(cell);
			}
			
		}
		
		return result;
	}
}
