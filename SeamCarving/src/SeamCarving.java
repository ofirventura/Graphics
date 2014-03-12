import java.awt.image.BufferedImage;


public class SeamCarving {
	
	int width;  // m
	int height; // n
	BufferedImage img;
	
	public SeamCarving(BufferedImage img)
	{
		this.img = img;
		width = img.getWidth();
		height = img.getHeight();
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
	
	public int[][] calcMapGradient()
	{
		int[][] map = new int[height][width];
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				map[i][j] = calcSinglePixelGradient(i,j);
			}
		}
		return map;
	}

	private int calcSinglePixelGradient(int row_i, int col_j) {
		int numOfNeighbours = 0; // TODO: update initialize to 0
		int energy = 0;
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
	private int diffRGB(int row_i, int col_j, int i, int j) {
		System.out.println(" " +  i + " " + j);
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
	
	private int[][] calcMapEntropy()
	{
		int[][] map = new int[height][width];
		int[][] energyMap = calcMapGradient();
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				map[i][j] = (calcSinglePixelEntropy(i,j) + energyMap[i][j]) / 2;
			}
		}
		
		return map;
	}

	private int calcSinglePixelEntropy(int row_i, int col_j) {
		
		int entropy = 0;
		
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
				entropy -= p_mn(i,j) * Math.log(p_mn(i,j));
			}
		}
		return entropy;
	}

	private int calcGreyPixel(int i, int j) {
		
		int pixel = img.getRGB(i, j);
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
		
	    return (red + green + blue) / 3;
	}

	private int p_mn(int i, int j) {
		
		int img_grey = calcGreyNeighbour(i,j);
		
		return calcGreyPixel(i, j) / img_grey;
	}

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
	}

}
