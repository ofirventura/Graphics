import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Main {

	public static void main(String[] args) throws IOException {
		String input = args[0];
		BufferedImage img = ImageIO.read(new File(input));
		SeamCarving s = new SeamCarving(img);
		int[][] a = s.calcMapEntropy();
		int n = a.length;
		int m = a[a.length-1].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				//System.out.print(a[i][j] + " ");
				img.setRGB(i, j, (a[i][j]) | (a[i][j] << 8) | (a[i][j] << 16));
			}
			//System.out.println("\n");
		}
		File outputfile = new File("image_entropy.jpg");
		ImageIO.write(img, "jpg", outputfile);
		
		BufferedImage img2 = ImageIO.read(new File(input));
		SeamCarving s2 = new SeamCarving(img2);
		int[][] b = s2.calcMapGradient();
		int k = a.length;
		int l = a[a.length-1].length;
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < l; j++) {
				//System.out.print(a[i][j] + " ");
				img2.setRGB(i, j, (b[i][j]) | (b[i][j] << 8) | (b[i][j] << 16));
			}
			//System.out.println("\n");
		}
		File outputfile2 = new File("image_gradiant.jpg");
		ImageIO.write(img2, "jpg", outputfile2);
	}
	
}
