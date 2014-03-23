import java.awt.image.BufferedImage;


public class SeamCarving {
	
	int width;  // m
	int height; // n
	BufferedImage img;
	
	public SeamCarving(BufferedImage img)
	{
		this.img = img;
		width = img.getHeight();
		height = img.getWidth();
		// rgb = img.getRGB(0, 0, width, height, null, 0, width); 
	}
	
	/*
	 * i - row
	 * j - column
	 */
	private int convertIndex(int i, int j)
	{
		return i*width + j;
	}
	
	public double[][] calcMapGradient()
	{
		double[][] map = new double[height][width];
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				map[i][j] = calcSinglePixelGradient(i,j);
			}
		}
		return map;
	}

	private double calcSinglePixelGradient(int row_i, int col_j) {
		int numOfNeighbours = 0; // TODO: update initialize to 0
		double energy = 0;
		// this is only for a pixel with 8 neighbours
		// need to update the conditions of lower bound and upper bound
		// int upperBound = ... ; int lowerBound = ...;
		
		int row_lower = row_i-1;
		int row_upper = row_i+1;
		int column_lower = col_j-1;
		int column_upper = col_j+1;
		
		if (row_i == 0)
			row_lower = 0;
		if (row_i == height-1)
			row_upper = height-1;
		if (col_j == 0)
			column_lower = 0;
		if (col_j == width-1)
			column_upper = width-1;
		
		for (int i = row_lower; i <= row_upper; i++)
		{
			for (int j = column_lower; j <= column_upper; j++)
			{
				if (i == row_i && j == col_j)
					continue;
				
				energy += diffRGB(row_i, col_j, i, j);
				numOfNeighbours++;
			}
		}
		return energy / numOfNeighbours;
	}

	/*
	 * val = abs(Ri-R1)+abs(Gi-G1)+abs(Bi-B1) / 3
	 * */
	private double diffRGB(int row_i, int col_j, int i, int j) {
		//System.out.println(" " +  i + " " + j);
		int pixel = img.getRGB(i, j);
	    int red_i   = (pixel >> 16) & 0xff;
	    int green_i = (pixel >> 8) & 0xff;
	    int blue_i  = (pixel) & 0xff;
		int neighbourPixel = img.getRGB(row_i, col_j);
	    int neighbourRed = (neighbourPixel >> 16) & 0xff;
	    int neighbourGreen = (neighbourPixel >> 8) & 0xff;
	    int neighbourBlue  = (neighbourPixel) & 0xff;
	    
		return (Math.abs(red_i-neighbourRed) + Math.abs(green_i-neighbourGreen) + Math.abs(blue_i-neighbourBlue)) / 3;
	}
	
	public double[][] calcMapEntropy()
	{
		double[][] map = new double[height][width];
		double[][] energyMap = calcMapGradient();
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				map[i][j] = (( (double)energyMap[i][j] - calcSinglePixelEntropy(i,j)) / 2.0);
			}
		}
		
		return map;
	}

	private double calcSinglePixelEntropy(int row_i, int col_j) {
		
		double entropy = 0;
		
		int row_lower = row_i-4;
		int row_upper = row_i+4;
		int column_lower = col_j-4;
		int column_upper = col_j+4;
		int neighbour_grey = 0;
		
		if (row_i <= 3)
			row_lower = 0;
		if (row_i >= height-4)
			row_upper = height-1;
		if (col_j <= 3)
			column_lower = 0;
		if (col_j >= width-4)
			column_upper = width-1;
	
		//System.out.println(row_i + " " + col_j);
		//System.out.println("row upper" + row_upper);
		//System.out.println("row lower" + row_lower);
		//System.out.println("col upper" + column_upper);
		//System.out.println("col lower" + column_lower);
		
		for (int i = row_lower; i <= row_upper; i++)
		{
			for (int j = column_lower; j <= column_upper; j++)
			{
				neighbour_grey += calcGreyPixel(i,j);
			}
		}
		
		for (int i = row_lower; i <= row_upper; i++)
		{
			for (int j = column_lower; j <= column_upper; j++)
			{
				double p = p_mn(i,j,neighbour_grey);
				if (p != 0)
					entropy += p * Math.log(p);
			}
		}
		return entropy;
	}

	private int calcGreyPixel(int i, int j) {
		
		//System.out.println(i + " " + j);
		int pixel = img.getRGB(i, j);
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
		
	    return (red + green + blue) / 3;
	}

	private double p_mn(int i, int j, int neighbour_grey) {
				
		return ((double)calcGreyPixel(i, j)) / ((double)neighbour_grey);
	}

	/*
	private int calcGreyNeighbour(int row_i, int col_j) {
		int grey_neighbour = 0;
		
		int row_lower = row_i-2;
		int row_upper = row_i+2;
		int column_lower = col_j-2;
		int column_upper = col_j+2;
		
		if (row_i == 0)
			row_lower = 0;
		if (row_i == height-1)
			row_upper = height-1;
		if (col_j == 0)
			column_lower = 0;
		if (col_j == width-1)
			column_upper = width-1;
		
		for (int i = row_lower; i <= row_upper; i++)
		{
			for (int j = column_lower; j <= column_upper; j++)
			{
				grey_neighbour += calcGreyPixel(i, j);
			}
		}
		return grey_neighbour;
	}*/

}
