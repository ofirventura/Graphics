import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Seam Carving main class
 * Creators: Dekel Auster, Liza Perelroyzen
 * Date: 20/03/13
 * Time: 13:11
 */
public class Program {
    public static void main(String[] args) {
        if (args.length != 5) {
            printUsage();
            return;
        }
        String inFile = args[0];
        int outWidth = Integer.parseInt(args[1]);
        int outHeight= Integer.parseInt(args[2]);
        int energyTypeInt = Integer.parseInt(args[3]);

        if (energyTypeInt < 0 || energyTypeInt > 1) {
            System.out.println("Error: energy type must be between 0 and 1");
            printUsage();
            return;
        }
        boolean energyType = (energyTypeInt == 1);
        String outFile = args[4];

        BufferedImage image;
        try {
            image = ImageIO.read(new File(inFile));
        } catch (IOException exception) {
            System.out.println("Could not read input file: "+ exception.getMessage());
            return;
        }
        SeamCarving seam = new SeamCarving(image);
        BufferedImage outImage = seam.seamAll(outWidth, outHeight, energyType);
        try {
            String extension = outFile.substring(outFile.lastIndexOf('.') + 1);
            ImageIO.write(outImage, extension, new File(outFile));
        } catch (IOException exception) {
            System.out.println("Could not write to output file: " + exception.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Usage: seamCarving <input_file> <output_cols> <output_rows> <energy_type> <output_file>");
    }
}
