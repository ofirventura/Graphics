import java.awt.*;
import java.awt.image.*;

/**
 * Seam Carving algorithm
 * Creators: Dekel Auster, Liza Perelroyzen
 * Date: 20/03/13
 * Time: 13:11
 */
public class SeamCarving {
    // Fields
    private int width;
    private int height;
    private int[] pixels;
    private int[] energy;
    private boolean[] transportMap;
    private boolean[] alreadyUsed;
    private boolean[] alreadyUsedRotated;
    private int outWidth;
    private int outHeight;
    private boolean forwardEnergy;
    private boolean isImageRotated;

    // Constructor
    public SeamCarving(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
    }

    // Methods
    public BufferedImage seamAll(int outWidth, int outHeight, boolean energyType) {
        this.outWidth = outWidth;
        this.outHeight = outHeight;
        this.forwardEnergy = energyType;
        int origWidth = width;
        int origHeight = height;

        // if width/height >= 200%, make it less than 200% and then just duplicate each pixel at the end
        if (this.outWidth >= 2 * width)
            this.outWidth = width + this.outWidth % width;
        if (this.outHeight >= 2 * height)
            this.outHeight = height + this.outHeight % height;

        // calculate energy and map before the seaming
        calcEnergy();
        buildTransportMap();

        // seam
        int r = Math.abs(height - this.outHeight) + 1;
        int c = Math.abs(width - this.outWidth) + 1;
        int i = r - 1;
        int j = c - 1;
        alreadyUsed = new boolean[height * width];
        alreadyUsedRotated = new boolean[height * width];
        while (i > 0 || j > 0) {
            boolean direction = transportMap[i*c+j];

            if (isImageRotated != direction)
                rotateImage();

            seam();
            if (!direction) // vertical
                j--;
            else // horizontal
                i--;
        }

        // if original out width/height were >= 200%, duplicate each pixel
        if (outWidth >= 2 * origWidth) {
            if (isImageRotated)
                rotateImage();
            duplicate(origWidth, outWidth / origWidth);
        }
        if (outHeight >= 2 * origHeight) {
            if (!isImageRotated)
                rotateImage();
            duplicate(origHeight, outHeight / origHeight);
        }

        // return image to original direction
        if (isImageRotated) {
            rotateImage();
        }

        // build returned image
        BufferedImage image = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, outWidth, outHeight, pixels, 0, outWidth);
        return image;
    }

    // Helping Methods
    private void duplicate(int orig, int times) {
        int newWidth = (times - 1) * orig + width;
        int[] newPixels = new int[newWidth * height];
        boolean[] newAlreadyUsed = new boolean[newWidth * height];
        boolean[] newAlreadyUsedRotate = new boolean[newWidth * height];
        int[] newEnergy = new int[newWidth * height];
        // duplicate only pixels that aren't already duplicates
        for (int i=0; i<height; i++) {
            int jDelta = 0;
            for (int j=0; j<orig; j++) {
                int oldPlace = i * width + j+jDelta;
                int oldPixel = pixels[oldPlace];
                boolean oldAlreadyUsed = alreadyUsed[oldPlace];
                boolean oldAlreadyUsedRotate = alreadyUsedRotated[oldPlace];
                int oldEnergy = energy[oldPlace];
                for (int k=0; k < times; k++) {
                    int newPlace = i * newWidth + times*j+jDelta+k;
                    newPixels[newPlace] = oldPixel;
                    newAlreadyUsed[newPlace] = oldAlreadyUsed;
                    newAlreadyUsedRotate[newPlace] = oldAlreadyUsedRotate;
                    newEnergy[newPlace] = oldEnergy;
                }
                // if the pixel was already duplicated, duplicate once,
                // and skip the next one (its duplicate)
                if (alreadyUsed[i * width + j+jDelta]) {
                    int newPlace = i*newWidth + times*j+jDelta+times;
                    newPixels[newPlace] = oldPixel;
                    newAlreadyUsedRotate[newPlace] = oldAlreadyUsedRotate;
                    newAlreadyUsed[newPlace] = oldAlreadyUsed;
                    newEnergy[newPlace] = oldEnergy;
                    jDelta++;
                }
            }
        }

        pixels = newPixels;
        width = newWidth;
        alreadyUsed = newAlreadyUsed;
        alreadyUsedRotated = newAlreadyUsedRotate;
        energy = newEnergy;
    }

    private void rotateImage() {
        pixels = transpose(pixels, width, height);
        energy = transpose(energy, width, height);
        boolean[] tempAlreadyUsed = alreadyUsed;
        alreadyUsed = transpose(alreadyUsedRotated, width, height);
        alreadyUsedRotated = transpose(tempAlreadyUsed, width, height);
        int temp = width;
        width = height;
        height = temp;
        temp = outWidth;
        outWidth = outHeight;
        outHeight = temp;
        isImageRotated = !isImageRotated;
    }

    private void buildTransportMap() {
        int r = Math.abs(height - outHeight) + 1;
        int c = Math.abs(width - outWidth) + 1;
        boolean[] directions = new boolean[r*c]; // false = vertical, true = horizontal
        int[] weights = new int[r*c];
        weights[0] = 0;
        directions[0] = false;
        // case that i=0
        for (int j=1; j<c; j++) {
            weights[j] = weights[j-1] + energy[height * width - (j-1) - 1];
            directions[j] = false; // vertical
        }
        // case that j=0
        for (int i=1; i<r; i++) {
            weights[i*c] = weights[(i-1)*c] + energy[(height - (i-1)) * width - 1];
            directions[i*c] = true; // horizontal
        }

        for (int i=1; i<r; i++)
            for (int j=1; j<c; j++) {
                int horizontalWeight = weights[(i-1)*c+j] + energy[(height - (i-1)) * width - j - 1];
                int verticalWeight = weights[i*c + j-1] + energy[(height - i) * width - (j-1) - 1];
                if (horizontalWeight < verticalWeight) { // choose horizontal
                    directions[i*c+j] = true;
                    weights[i*c+j] = horizontalWeight;
                } else { // choose vertical
                    directions[i*c+j] = false;
                    weights[i*c+j] = verticalWeight;
                }
            }

        transportMap = directions;
    }

    private void seam() {
        boolean toEnlarge = width < outWidth;
        int newWidthDifference = !toEnlarge ? -1 : 1;
        int[] dynMap;
        if (!forwardEnergy)
            dynMap = calcBackwardMap();
        else
            dynMap = calcForwardMap();

        // get the columns in each row with the least energy, from the last to the first
        int[] columns = getMinColumns(dynMap);

        // build new pixel+energy maps, without that column
        int newWidth = width + newWidthDifference;
        int[] newPixels = new int[height * newWidth];
        int[] newEnergy = new int[height * newWidth];
        boolean [] newAlreadyUsed = new boolean[height * newWidth];
        boolean[] newAlreadyUsedRotated = new boolean[height * newWidth];
        for (int r=0; r < height; r++) {
            int cutPlace = columns[r];
            for (int c=0; c < cutPlace; c++) {
                int newLocation = r * newWidth + c;
                int oldLocation = r * width + c;
                newPixels[newLocation] = pixels[oldLocation];
                newEnergy[newLocation] = energy[oldLocation];
                newAlreadyUsed[newLocation] = alreadyUsed[oldLocation];
                newAlreadyUsedRotated[newLocation] = alreadyUsedRotated[oldLocation];
            }
            if (toEnlarge) {
                int newLocation1 = r * newWidth + cutPlace;
                int newLocation2 = newLocation1 + newWidthDifference;
                int oldLocation = r * width + cutPlace;
                newPixels[newLocation1] = pixels[oldLocation];
                newPixels[newLocation2] = pixels[oldLocation];
                newEnergy[newLocation1] = energy[oldLocation];
                newEnergy[newLocation2] = energy[oldLocation];
                newAlreadyUsed[newLocation1] = true;
                newAlreadyUsed[newLocation2] = true;
                newAlreadyUsedRotated[newLocation1] = alreadyUsedRotated[oldLocation];
                newAlreadyUsedRotated[newLocation2] = alreadyUsedRotated[oldLocation];
            }
            for (int c = cutPlace + 1; c < width; c++) {
                int newLocation = r * newWidth + c + newWidthDifference;
                int oldLocation = r * width + c;
                newPixels[newLocation] = pixels[oldLocation];
                newEnergy[newLocation] = energy[oldLocation];
                newAlreadyUsed[newLocation] = alreadyUsed[oldLocation];
                newAlreadyUsedRotated[newLocation] = alreadyUsedRotated[oldLocation];
            }
        }

        // update arrays
        pixels = newPixels;
        energy = newEnergy;
        alreadyUsed = newAlreadyUsed;
        alreadyUsedRotated = newAlreadyUsedRotated;

        // set new width/height
        width += newWidthDifference;

        // fix energy surrounding the place removed
        if (!forwardEnergy)
            for (int r=0; r<height; r++) {
                int c = columns[r];
                for (int i = Math.max(0, r-1); i<=Math.min(height-1, r+1); i++)
                    for (int j = Math.max(0, c-1); j<=Math.min(width-1, c+1+newWidthDifference); j++)
                        calcSingleEnergy(i, j);
            }
    }

    // backtrack the map using the lowest energy in each row, around the column in the row below, from end to start
    private int[] getMinColumns(int[] dynMap) {
        int[] columns = new int[height];
        int minColumn = getMinEnergyColumnInLastLine(dynMap);
        columns[height - 1] = minColumn;
        // find the column out of (i-1, j-1),(i-1,j),(i-1,j+1) with the least amount of energy
        for (int r = height - 2; r >= 0; r--) {
            int middleC = columns[r+1];
            int chosenC = (!alreadyUsed[r*width + middleC]) ? middleC : -1;
            int chosenVal = dynMap[r*width + middleC];
            // check if left val has the least amount of energy; skip already-used spots
            int leftC = middleC - 1;
            while (leftC >= 0 && alreadyUsed[r*width + leftC])
                leftC--;
            if (leftC >= 0) {
                int leftVal = dynMap[r * width + leftC];
                if (chosenC == -1 || leftVal < chosenVal) {
                    chosenVal = leftVal;
                    chosenC = leftC;
                }
            }
            // check if right val has least amount of energy; skip already-used spots
            int rightC = middleC + 1;
            while (rightC < width && alreadyUsed[r * width + rightC])
                rightC++;
            if (rightC < width) {
                int rightVal = dynMap[r * width + rightC];
                if (chosenC == -1 || rightVal < chosenVal)
                    chosenC = rightC;
            }
            columns[r] = chosenC;
        }
        return columns;
    }

    private int getMinEnergyColumnInLastLine(int[] dynMap) {
        int minValue = -1;
        int minColumn = -1;
        for (int c=0; c < width; c++) {
            if (alreadyUsed[(height-1)*width + c])
                continue;
            int currValue = dynMap[(height-1)*width + c];
            if (minValue == -1 || currValue < minValue) {
                minValue = currValue;
                minColumn = c;
            }
        }
        return minColumn;
    }

    private void calcEnergy() {
        energy = new int[width * height];
        for(int r = 0; r < height; r++)
            for (int c = 0; c < width; c++)
                calcSingleEnergy(r, c);
    }

    private void calcSingleEnergy(int r, int c) {
        int sum = 0;
        int numCols = 0;
        int sourcePixel = pixels[r * width + c];
        Color sourceCol = new Color(sourcePixel);
        for (int i=Math.max(0, r - 1); i<=Math.min(height - 1, r + 1); i++)
            for (int j=Math.max(0, c - 1); j<=Math.min(width - 1, c + 1); j++) {
                if (r == i && c == j)
                    continue;
                int currPixel = pixels[i * width + j];
                Color currCol = new Color(currPixel);
                sum += calcDifference(currCol, sourceCol);
                numCols++;
            }

        energy[r * width + c] = (sum * 8) / numCols; // *8 to add accuracy before dividing
    }

    private int[] calcBackwardMap() {
        int[] dynMap = new int[width * height];
        // fill first row only from energy
        for (int c=0; c < width; c++) {
            dynMap[c] = energy[c];
        }

        // fill 2nd-last rows with energy+min(map(line above))
        for (int r=1; r < height; r++)
            for (int c=0; c < width; c++) {
                if (alreadyUsed[r * width + c]) // this location was already used for duplication, cannot reuse it
                    continue;

                // find the minimal dynamic map addition to this spot
                int minAddition = (!alreadyUsed[(r-1) * width + c])
                        ? dynMap[(r-1) * width + c] : -1;
                // find the first unused spot to the left, use it if it's minimal
                int leftSpot = c-1;
                while (leftSpot >= 0 && alreadyUsed[(r-1) * width + leftSpot])
                    leftSpot--;
                if (leftSpot >= 0) {
                    int currVal = dynMap[(r-1) * width + leftSpot];
                    if (minAddition == -1 || currVal < minAddition)
                        minAddition = currVal;
                }

                // find the first unused spot to the right, use it if it's minimal
                int rightSpot = c + 1;
                while(rightSpot <= width - 1 && alreadyUsed[(r-1) * width + rightSpot])
                    rightSpot++;
                if (rightSpot <= width - 1) {
                    int currVal = dynMap[(r-1) * width + rightSpot];
                    if (minAddition == -1 || currVal < minAddition)
                        minAddition = currVal;
                }
                dynMap[r * width + c] = energy[r * width + c] + minAddition;
            }
        return dynMap;
    }

    private int[] calcForwardMap() {
        int[] cl = new int[width * height];
        int[] cu = new int[width * height];
        int[] cr = new int[width * height];
        int upperCol, rightCol, leftCol, currLocation;
        int upperPixel, rightPixel, leftPixel;
        int currPixelNum;
        int[] prevRowPixelCols = new int[width];
        int[] currRowPixelCols = new int[width];
        Color upperPixelColor = new Color(0,0,0), rightPixelColor, leftPixelColor;
        // Calculate Cl, Cu, Cr
        for (int i = 0; i < height; i++)  {
            currPixelNum = 0;
            for (int j=0; j < width; j++) {
                currLocation = i * width + j;
                if (alreadyUsed[currLocation])
                    continue;
                currRowPixelCols[currPixelNum] = j;
                // look up
                if (i > 0) {
                    upperCol = prevRowPixelCols[currPixelNum];
                    upperPixel = pixels[(i-1)*width + upperCol];
                    upperPixelColor = new Color(upperPixel);
                }
                // look right
                rightCol = j + 1;
                while (rightCol < width && alreadyUsed[i*width + rightCol])
                    rightCol++;
                if (rightCol < width)
                    rightPixel = pixels[i*width + j+1];
                else
                    rightPixel = pixels[currLocation];
                // look left
                leftCol = j - 1;
                while (leftCol >= 0 && alreadyUsed[i*width + leftCol])
                    leftCol--;
                if (leftCol >= 0)
                    leftPixel = pixels[i * width + leftCol];
                else
                    leftPixel = pixels[currLocation];
                rightPixelColor = new Color(rightPixel);
                leftPixelColor = new Color(leftPixel);
                cu[currLocation] = calcDifference(rightPixelColor, leftPixelColor);
                if (i > 0) {
                    if (leftCol >= 0)
                        cl[currLocation] = cu[currLocation] + calcDifference(upperPixelColor, leftPixelColor);
                    if (rightCol < width)
                        cr[currLocation] = cu[currLocation] + calcDifference(upperPixelColor, rightPixelColor);
                }
                currPixelNum++;
            }
            prevRowPixelCols = currRowPixelCols;
            currRowPixelCols = new int[width];
        }

        // Calculate dynamic map
        int dynMap[] = new int[height*width];
        currPixelNum = 0;
        // i = 0
        for (int j=0; j<width; j++) {
            if (alreadyUsed[j])
                continue;
            currRowPixelCols[currPixelNum] = j;
            dynMap[j] = cu[j];
            currPixelNum++;
        }

        // 1 <= i < height
        for (int i=1; i < height; i++) {
            currPixelNum = 0;
            prevRowPixelCols = currRowPixelCols;
            currRowPixelCols = new int[width];
            for (int j=0; j < width; j++) {
                currLocation = i*width + j;
                if (alreadyUsed[currLocation])
                    continue;
                int minAddition = dynMap[(i-1)*width + prevRowPixelCols[currPixelNum]] + cu[currLocation];
                leftCol = j - 1;
                while (leftCol >= 0 && alreadyUsed[(i-1)*width+leftCol])
                    leftCol--;
                if (leftCol >= 0) {
                    int l = dynMap[(i-1)*width + leftCol] + cl[currLocation];
                    minAddition = Math.min(minAddition, l);
                }
                rightCol = j + 1;
                while (rightCol < width && alreadyUsed[(i-1)*rightCol])
                    rightCol++;
                if (rightCol < width) {
                    int r = dynMap[(i-1)*width + rightCol] + cr[currLocation];
                    minAddition = Math.min(minAddition, r);
                }
                dynMap[currLocation] = minAddition;
            }
        }

        return dynMap;
    }

    private int calcDifference(Color a, Color b) {
        return Math.abs(a.getRed() - b.getRed())
                + Math.abs(a.getGreen() - b.getGreen())
                + Math.abs(a.getBlue() - b.getBlue());
    }

    // transpose pixels
    private static int[] transpose(int[] orig, int cols, int rows) {
        int[] res = new int[rows*cols];
        for (int i=0; i < rows; i++)
            for (int j=0; j<cols; j++)
                res[j*rows+i] = orig[i*cols+j];
        return res;
    }

    // transpose boolean
    private static boolean[] transpose(boolean[] orig, int cols, int rows) {
        boolean[] res = new boolean[rows*cols];
        for (int i=0; i < rows; i++)
            for (int j=0; j<cols; j++)
                res[j*rows+i] = orig[i*cols+j];
        return res;
    }
}
