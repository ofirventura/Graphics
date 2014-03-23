import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Main {

	public static void main(String[] args) throws IOException {
		String input = args[0];
		BufferedImage img = ImageIO.read(new File(input));
		SeamCarving s = new SeamCarving(img);
		double[][] a = s.calcMapEntropy();
		int n = a.length;
		int m = a[a.length-1].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				//System.out.print(a[i][j] + " ");
				img.setRGB(i, j, ((int)a[i][j]) | ((int)a[i][j] << 8) | ((int)a[i][j] << 16));
			}
			//System.out.println("\n");
		}
		File outputfile = new File("image_entropy.bmp");
		ImageIO.write(img, "BMP", outputfile);
		
		BufferedImage img2 = ImageIO.read(new File(input));
		SeamCarving s2 = new SeamCarving(img2);
		double[][] b = s2.calcMapGradient();
		int k = a.length;
		int l = a[a.length-1].length;
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < l; j++) {
				//System.out.print(a[i][j] + " ");
				img2.setRGB(i, j, ((int)b[i][j]) | ((int)b[i][j] << 8) | ((int)b[i][j] << 16));
			}
			//System.out.println("\n");
		}
		File outputfile2 = new File("image_gradiant.bmp");
		ImageIO.write(img2, "BMP", outputfile2);
	}
	
}
