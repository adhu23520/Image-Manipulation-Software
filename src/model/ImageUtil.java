package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * Utility class for handling image processing operations.
 */
public class ImageUtil {

  /**
   * Creates a PPM formatted string representing an image.
   *
   * @param command    The PPM command for the image.
   * @param width      The width of the image.
   * @param height     The height of the image.
   * @param maxValue   The maximum color value.
   * @param imageArray The pixel representation of the image.
   * @param filePath   The file path to save the image.
   * @return The formatted PPM string representing the image.
   * @throws IOException If an I/O error occurs during PPM creation.
   */
  public StringBuilder ppmCreate(
          String command, int width, int height, int maxValue, Pixel imageArray, String filePath)
          throws IOException {
    StringBuilder format = new StringBuilder();
    List<Integer>[][] img = imageArray.getPixel();
    format.append(command + '\n' + width + " " + height + '\n' + maxValue + '\n');

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        for (int k : img[i][j]) {
          format.append(k);
          format.append('\n');
        }
      }
      format.append('\n');
    }
    return format;
  }

  /**
   * Creates a BufferedImage from the provided Pixel object.
   *
   * @param imageArray The Pixel object containing the image data.
   * @return A BufferedImage representing the image.
   * @throws IOException If an I/O error occurs during the image creation process.
   */
  public BufferedImage createImageFile(Pixel imageArray) throws IOException {

    if (imageArray == null) {
      throw new IllegalArgumentException("imageArray cannot be null");
    }

    List<Integer>[][] pixelOfImage = imageArray.getPixel();
    int height = pixelOfImage.length;
    int width = pixelOfImage[0].length;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        List<Integer> pixel = pixelOfImage[y][x];
        int red = pixel.get(0);
        int green = pixel.get(1);
        int blue = pixel.get(2);
        int rgb = (red << 16) | (green << 8) | blue;
        image.setRGB(x, y, rgb);
      }
    }
    return image;
  }

  /**
   * Reads a PPM file using the provided scanner.
   *
   * @param scanner The scanner used to read the PPM file.
   * @return A Pixel object representing the image read from the PPM file. Returns null if the file
   *          format is invalid.
   * @throws IOException If an I/O error occurs during file reading.
   */
  public Pixel ppmRead(Scanner scanner) throws IOException {

    StringBuilder build = new StringBuilder();
    // read the file line by line, and populate a string. This will throw away any comment lines
    while (scanner.hasNextLine()) {
      String scan = scanner.nextLine();
      if (scan.length() != 0 && scan.charAt(0) != '#') {
        build.append(scan + System.lineSeparator());
      }
    }

    // now set up the scanner to read from the string we just built
    scanner = new Scanner(build.toString());

    String command;

    command = scanner.next();
    if (!command.equals("P3")) {
      // System.out.println("Invalid PPM file: plain RAW file should begin with P3");
      return null;
    }
    int width = scanner.nextInt();
    int height = scanner.nextInt();
    int maxValue = scanner.nextInt();

    List<Integer>[][] img = new List[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        List<Integer> rgb = new LinkedList<>();
        int red = scanner.nextInt();
        int green = scanner.nextInt();
        int blue = scanner.nextInt();
        rgb.add(red);
        rgb.add(green);
        rgb.add(blue);
        img[i][j] = rgb;
      }
    }
    return new PixelImplementation(img);
  }

  /**
   * Converts a BufferedImage to a Pixel representation.
   *
   * @param input The BufferedImage to be converted.
   * @return A Pixel object representing the converted image.
   * @throws IOException If an I/O error occurs during the conversion process.
   */
  public Pixel readOtherFormatsFile(BufferedImage input) throws IOException {

    int width = input.getWidth();
    int height = input.getHeight();

    List<Integer>[][] img = new List[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int pixel = input.getRGB(j, i);
        List<Integer> rgb = new LinkedList<>();
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = pixel & 0xff;
        rgb.add(red);
        rgb.add(green);
        rgb.add(blue);
        img[i][j] = rgb;
      }
    }
    return new PixelImplementation(img);
  }

  /**
   * Creates a BufferedImage from the provided Pixel representation.
   *
   * @param pixels   The Pixel object containing the image data.
   * @param filePath The file path where the created image will be saved.
   * @return A BufferedImage representing the image generated from the Pixel data.
   * @throws IOException If an I/O error occurs during the image creation process.
   */
  public BufferedImage createImage(Pixel pixels, String filePath) throws IOException {

    List<Integer>[][] img = pixels.getPixel();
    int height = img.length;
    int width = img[0].length;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        List<Integer> pixel = img[y][x];
        int red = pixel.get(0);
        int green = pixel.get(1);
        int blue = pixel.get(2);
        int rgb = (red << 16) | (green << 8) | blue;
        image.setRGB(x, y, rgb);
      }
    }
    return image;
  }

  /**
   * Converts the provided Pixel image into a greyscale representation based on the specified
   * command.
   *
   * @param command The type of greyscale transformation: "Red", "Green", "Blue", "Value",
   *                "Intensity", or "Luma".
   * @param width   The width of the image.
   * @param height  The height of the image.
   * @param pixel   The Pixel object containing the original image data.
   * @return A Pixel object representing the greyscaled image according to the specified command.
   * @throws IOException If an I/O error occurs during the greyscale conversion process.
   */
  public Pixel greyscaleImg(String command, int width, int height, Pixel pixel) throws IOException {
    List<Integer>[][] output = new List[height][width];
    List<Integer>[][] img = pixel.getPixel();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        List<Integer> appendToOutput = new ArrayList<>();
        if (command.equals("Red")) {
          int temp = img[i][j].get(0);
          appendToOutput.add(temp);
          appendToOutput.add(temp);
          appendToOutput.add(temp);
        } else if (command.equals("Green")) {
          int temp = img[i][j].get(1);
          appendToOutput.add(temp);
          appendToOutput.add(temp);
          appendToOutput.add(temp);
        } else if (command.equals("Blue")) {
          int temp = img[i][j].get(2);
          appendToOutput.add(temp);
          appendToOutput.add(temp);
          appendToOutput.add(temp);

        } else if (command.equals("Value")) {
          List<Integer> temp = img[i][j];
          int max = Collections.max(temp);
          appendToOutput.add(max);
          appendToOutput.add(max);
          appendToOutput.add(max);
        } else if (command.equals("Intensity")) {
          List<Integer> temp = img[i][j];
          int avg = (temp.get(0) + temp.get(1) + temp.get(2)) / 3;
          appendToOutput.add(avg);
          appendToOutput.add(avg);
          appendToOutput.add(avg);
        } else if (command.equals("Luma")) {
          List<Integer> temp = img[i][j];
          double weightedSum = 0.2126 * temp.get(0) + 0.7152 * temp.get(1) + 0.0722 * temp.get(2);
          appendToOutput.add((int) weightedSum);
          appendToOutput.add((int) weightedSum);
          appendToOutput.add((int) weightedSum);
        }
        output[i][j] = appendToOutput;
      }
    }
    return new PixelImplementation(output);
  }

  /**
   * Flips the provided Pixel image horizontally or vertically based on the specified command.
   *
   * @param command The type of flip operation: "Horizontal" or "Vertical".
   * @param width   The width of the image.
   * @param height  The height of the image.
   * @param pixel   The Pixel object containing the original image data.
   * @return A Pixel object representing the image flipped as per the specified command.
   * @throws IOException If an I/O error occurs during the flipping process.
   */
  public Pixel flip(String command, int width, int height, Pixel pixel) throws IOException {
    List<Integer>[][] outFlip = new List[height][width];
    List<Integer>[][] img = pixel.getPixel();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        outFlip[i][j] = img[i][j];
      }
    }
    if (command.equals("Horizontal")) {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width / 2; j++) {
          List<Integer> temp = outFlip[i][j];
          outFlip[i][j] = outFlip[i][width - j - 1];
          outFlip[i][width - j - 1] = temp;
        }
      }
    }

    if (command.equals("Vertical")) {
      for (int i = 0; i < height / 2; i++) {
        for (int j = 0; j < width; j++) {
          List<Integer> temp = outFlip[i][j];
          outFlip[i][j] = outFlip[height - i - 1][j];
          outFlip[height - i - 1][j] = temp;
        }
      }
    }
    return new PixelImplementation(outFlip);
  }

  /**
   * Adjusts the brightness of the provided image by adding or subtracting the specified value to
   * each pixel component.
   *
   * @param val    The value to be added to or subtracted from each pixel component.
   *               A positive value
   *               increases brightness, while a negative value decreases it.
   * @param width  The width of the image.
   * @param height The height of the image.
   * @param pixel  The Pixel object containing the original image data.
   * @return A Pixel object representing the adjusted brightness image.
   * @throws IOException If an I/O error occurs during the brightness adjustment process.
   */
  public Pixel brightDark(int val, int width, int height, Pixel pixel) throws IOException {
    List<Integer>[][] output = new List[height][width];
    List<Integer>[][] img = pixel.getPixel();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        List<Integer> appendToOutput = new ArrayList<>();
        if (val > 0) {
          int elementOne = Math.min(img[i][j].get(0) + val, 255);
          int elementTwo = Math.min(img[i][j].get(1) + val, 255);
          int elementThree = Math.min(img[i][j].get(2) + val, 255);
          appendToOutput.add(elementOne);
          appendToOutput.add(elementTwo);
          appendToOutput.add(elementThree);
          output[i][j] = appendToOutput;
        } else if (val < 0) {
          int elementOne = Math.max(img[i][j].get(0) + val, 0);
          int elementTwo = Math.max(img[i][j].get(1) + val, 0);
          int elementThree = Math.max(img[i][j].get(2) + val, 0);
          appendToOutput.add(elementOne);
          appendToOutput.add(elementTwo);
          appendToOutput.add(elementThree);
          output[i][j] = appendToOutput;
        }
      }
    }
    return new PixelImplementation(output);
  }

  /**
   * Applies a custom filter matrix to modify a portion of the provided image pixels.
   *
   * @param filter          The 3x3 filter matrix to apply to the image pixels.
   * @param pixel           The Pixel object containing the original image data.
   * @param width           The width of the image.
   * @param height          The height of the image.
   * @param splitPercentage The percentage of the width where the filter is applied; the rest
   *                        remains unchanged.
   * @return A Pixel object representing the modified image after applying the filter to the
   *          specified portion.
   */
  public Pixel filterMultiply(
          double[][] filter, Pixel pixel, int width, int height, double splitPercentage) {
    List<Integer>[][] outputImg = pixel.getPixel();
    List<Integer>[][] outputImg1 = new List[height][width];

    int splitPosition = (int) (width * (splitPercentage / 100));
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (j < splitPosition) {
          List<Integer> arr = new ArrayList<>();
          double redPixel =
                  filter[0][0] * outputImg[i][j].get(0)
                          + filter[0][1] * outputImg[i][j].get(1)
                          + filter[0][2] * outputImg[i][j].get(2);
          redPixel = Math.min(255, redPixel);

          double greenPixel =
                  filter[1][0] * outputImg[i][j].get(0)
                          + filter[1][1] * outputImg[i][j].get(1)
                          + filter[1][2] * outputImg[i][j].get(2);
          greenPixel = Math.min(255, greenPixel);

          double bluePixel =
                  filter[2][0] * outputImg[i][j].get(0)
                          + filter[2][1] * outputImg[i][j].get(1)
                          + filter[2][2] * outputImg[i][j].get(2);
          bluePixel = Math.min(255, bluePixel);

          arr.add((int) Math.round(redPixel));
          arr.add((int) Math.round(greenPixel));
          arr.add((int) Math.round(bluePixel));
          outputImg1[i][j] = arr;
        } else {
          List<Integer> arr = new ArrayList<>();
          double redPixel = outputImg[i][j].get(0);
          double greenPixel = outputImg[i][j].get(1);
          double bluePixel = outputImg[i][j].get(2);

          arr.add((int) Math.round(redPixel));
          arr.add((int) Math.round(greenPixel));
          arr.add((int) Math.round(bluePixel));
          outputImg1[i][j] = arr;
        }
      }
    }
    return new PixelImplementation(outputImg1);
  }

  /**
   * Applies a convolutional filter to modify a portion of the provided image pixels.
   *
   * @param pixel           The Pixel object containing the original image data.
   * @param filter          The 2D matrix representing the convolutional filter.
   * @param width           The width of the image.
   * @param height          The height of the image.
   * @param splitPercentage The percentage of the width where the filter is applied; the rest
   *                        remains unchanged.
   * @return A Pixel object representing the modified image after applying the convolutional filter
   *          to the specified portion.
   */
  public Pixel filter(
          Pixel pixel, double[][] filter, int width, int height, double splitPercentage) {

    List<Integer>[][] filterImg = new List[height][width];
    List<Integer>[][] image = pixel.getPixel();
    int middle = filter.length / 2;

    int splitPosition = (int) (width * (splitPercentage / 100));
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        if (j < splitPosition) {
          double red = 0.0;
          double green = 0.0;
          double blue = 0.0;
          for (int m = -middle; m <= middle; m++) {
            for (int n = -middle; n <= middle; n++) {
              int pixX = i + m;
              int pixY = j + n;
              if (pixX >= 0 && pixX < height && pixY >= 0 && pixY < width) {
                double weights = filter[m + middle][n + middle];
                red += image[pixX][pixY].get(0) * weights;
                green += image[pixX][pixY].get(1) * weights;
                blue += image[pixX][pixY].get(2) * weights;
              }
            }
          }
          int newRed = Math.min(Math.max((int) red, 0), 255);
          int newGreen = Math.min(Math.max((int) green, 0), 255);
          int newBlue = Math.min(Math.max((int) blue, 0), 255);

          List<Integer> filterPxl = new ArrayList<>();
          filterPxl.add(0, Math.round(newRed));
          filterPxl.add(1, Math.round(newGreen));
          filterPxl.add(2, Math.round(newBlue));
          filterImg[i][j] = filterPxl;
        } else {
          List<Integer> arr = new ArrayList<>();
          double redPixel = image[i][j].get(0);
          double greenPixel = image[i][j].get(1);
          double bluePixel = image[i][j].get(2);

          arr.add((int) Math.round(redPixel));
          arr.add((int) Math.round(greenPixel));
          arr.add((int) Math.round(bluePixel));
          filterImg[i][j] = arr;
        }
      }
    }
    return new PixelImplementation(filterImg);
  }

  /**
   * Draws a histogram line graph on the provided Graphics2D context based on the histogram values.
   *
   * @param g2d         The Graphics2D context to draw on.
   * @param histChannel An array representing the histogram values for a specific channel (e.g.,
   *                    Red, Green, or Blue).
   * @param color       The Color object specifying the color to use for drawing the histogram.
   * @param height      The height to which the histogram should be scaled when drawing.
   */
  public void drawHist(Graphics2D g2d, int[] histChannel, Color color, int height) {

    int max = getMaxVal(histChannel);
    double scale = (double) height / max;

    // Draw the histogram line graph
    g2d.setColor(color);
    for (int i = 0; i < 255; i++) {
      int x1 = i;
      int y1 = height - (int) (histChannel[i] * scale);
      int x2 = i + 1;
      int y2 = height - (int) (histChannel[i + 1] * scale);

      if (histChannel[i] != 0 && histChannel[i + 1] != 0) {
        g2d.drawLine(x1, y1, x2, y2);
      }
    }
  }

  private int getMaxVal(int[] array) {
    int max = array[0];
    for (int value : array) {
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

  /**
   * Adjusts the levels of an image for a specific color channel based on provided parameters.
   *
   * @param b    The minimum input level.
   * @param m    The middle input level.
   * @param w    The maximum input level.
   * @param pcnt The percentage value used to split the width of the image for adjustment.
   * @param img  The 2D list representing the image pixel values.
   * @return A Pixel object representing the adjusted image.
   */
  public Pixel levelAdjustImg(int b, int m, int w, double pcnt, List<Integer>[][] img) {
    int height = img.length;
    int width = img[0].length;

    int splitPos = (int) (width * (pcnt / 100.0));
    if (pcnt == 0.0) {
      splitPos = width;
    }

    for (int c = 0; c < 3; c++) {

      double a = computeA(b, m, w);
      double aA = computeAa(b, m, w);
      double bA = computeAb(b, m, w);
      double cA = computeAc(b, m, w);

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          if (j < splitPos) {
            List<Integer> rgb = img[i][j];
            int prevVal = rgb.get(c);
            int newVal = quadEqu(a, aA, bA, cA, prevVal);
            newVal = Math.max(0, Math.min(newVal, 255)); // Clipping the value to 0-255 range
            rgb.set(c, newVal);
          }
        }
      }
    }

    return new PixelImplementation(img);
  }

  /**
   * Performs color correction on an image based on calculated peak values for RGB channels and an
   * average peak value. Adjusts the image pixels according to the split percentage and calculated
   * peak values.
   *
   * @param imageName       The name of the original image file.
   * @param destImage       The destination image file name after correction.
   * @param splitPercentage The percentage value used to split the width of the image for
   *                        correction.
   * @param img             The 2D list representing the image pixel values to be corrected.
   * @return A Pixel object representing the color-corrected image.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Pixel colorCorrectionImg(
          String imageName, String destImage, double splitPercentage, List<Integer>[][] img)
          throws IOException {
    int height = img.length;
    int width = img[0].length;

    int[][] histImg = fetchHist(imageName, destImage, img);

    int[] rPeak = findPeak(histImg[0]);
    int[] gPeak = findPeak(histImg[1]);
    int[] bPeak = findPeak(histImg[2]);

    double avgRPeak = rPeak[1];
    double avgGPeak = gPeak[1];
    double avgBPeak = bPeak[1];

    double average = (avgRPeak + avgBPeak + avgGPeak) / 3;

    int splitPos = (int) (width * (splitPercentage / 100.0));
    if (splitPercentage == 0.0) {
      splitPos = width;
    }

    for (int k = 0; k < height; k++) {
      for (int l = 0; l < width; l++) {
        if (l < splitPos) {
          img[k][l] = offsetVal(img[k][l], 0, rPeak[1], average);
          img[k][l] = offsetVal(img[k][l], 1, gPeak[1], average);
          img[k][l] = offsetVal(img[k][l], 2, bPeak[1], average);
        }
      }
    }

    return new PixelImplementation(img);
  }

  // Helper method to offset values for a specific channel
  private List<Integer> offsetVal(List<Integer> img, int chan, int peak, double average) {
    double offset = average - peak;
    double newVal = img.get(chan) + offset;

    // Clamping newVal to the range [0, 255] and rounding it to the nearest integer
    newVal = Math.min(Math.max(newVal, 0), 255);
    img.set(chan, (int) Math.round(newVal));

    return img;
  }

  private int[] findPeak(int[] chanHist) {
    int peakVal = 0;
    int peakPos = 0;

    // Find the peak within the meaningful range (10 to 245)
    for (int i = 10; i < 245; i++) {
      if (chanHist[i] > peakVal) {
        peakVal = chanHist[i];
        peakPos = i;
      }
    }

    return new int[]{peakVal, peakPos};
  }

  private static double computeA(int b, int m, int w) {
    return (b * b * (m - w)) - b * ((m * m) - (w * w)) + (w * m * m) - (m * w * w);
  }

  private static double computeAa(int b, int m, int w) {
    return (-b * (128 - 255)) + (128 * w) - (255 * m);
  }

  private static double computeAb(int b, int m, int w) {
    return (b * b * (128 - 255)) + (255 * m * m) - (128 * w * w);
  }

  private static double computeAc(int b, int m, int w) {
    return (b * b * ((255 * m) - (128 * w))) - (b * ((255 * m * m) - (128 * w * w)));
  }

  private static int quadEqu(double a, double aA, double bA, double cA, int x) {
    double aa = computeA(0, 128, 255);
    double num = aA * x * x + bA * x + cA;
    double res = num / aa;

    // Ensure the res is within the valid range (0-255)
    res = Math.max(0, Math.min(255, res));

    // Round the res to the nearest integer
    int newValue = (int) Math.round(res);

    return newValue;
  }

  /**
   * Generates histograms for the Red, Green, and Blue color channels of the input image and saves
   * the histogram image.
   *
   * @param imgName     The name of the original image file.
   * @param destImgName The name for the destination histogram image file.
   * @param image       The 2D list representing the image pixel values to generate the histogram.
   * @return A 2D integer array representing the histograms for Red, Green, and Blue channels.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[][] fetchHist(String imgName, String destImgName, List<Integer>[][] image)
          throws IOException {
    int[][] histImg = new int[4][256];
    int height = image.length;
    int width = image[0].length;

    for (int k = 0; k < height; k++) {
      for (int l = 0; l < width; l++) {
        List<Integer> rgb = image[k][l];
        int redComp = rgb.get(0);
        int greenComp = rgb.get(1);
        int blueComp = rgb.get(2);
        histImg[0][redComp]++;
        histImg[1][greenComp]++;
        histImg[2][blueComp]++;
      }
    }

    int width1 = 256;
    int height1 = 256;
    BufferedImage img = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, width1, height1);

    g2d.setColor(Color.GRAY);
    for (int i = 0; i < width1; i += 32) {
      g2d.drawLine(i, 0, i, height1);
      g2d.drawLine(0, i, width1, i);
    }

    drawHist(g2d, histImg[0], Color.RED, height1);
    drawHist(g2d, histImg[1], Color.GREEN, height1);
    drawHist(g2d, histImg[2], Color.BLUE, height1);

    ImageIO.write(img, "png", new File(destImgName + ".png"));

    return histImg;
  }

  /**
   * Combines three separate images representing the Red, Green, and Blue channels into a single
   * image.
   *
   * @param redImage   The Pixel object representing the image for the Red channel.
   * @param greenImage The Pixel object representing the image for the Green channel.
   * @param blueImage  The Pixel object representing the image for the Blue channel.
   * @param width      The width of the resulting combined image.
   * @param height     The height of the resulting combined image.
   * @return A Pixel object representing the combined RGB image.
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Pixel rgbCombine(Pixel redImage, Pixel greenImage, Pixel blueImage, int width, int height)
          throws IOException {

    List<Integer>[][] red = redImage.getPixel();
    List<Integer>[][] green = greenImage.getPixel();
    List<Integer>[][] blue = blueImage.getPixel();

    List<Integer>[][] finalImg = new List[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        List<Integer> rgbVal = new ArrayList<>();
        int elementOne = red[i][j].get(0);
        rgbVal.add(elementOne);
        int elementTwo = green[i][j].get(1);
        rgbVal.add(elementTwo);
        int elementThree = blue[i][j].get(2);
        rgbVal.add(elementThree);
        finalImg[i][j] = rgbVal;
      }
    }
    return new PixelImplementation(finalImg);
  }

  private static BufferedImage padImage(BufferedImage image) {
    int originalWidth = image.getWidth();
    int originalHeight = image.getHeight();
    int newSize = Math.max(originalWidth, originalHeight);
    int paddedSize = Integer.highestOneBit(newSize - 1) << 1;

    BufferedImage paddedImage =
            new BufferedImage(paddedSize, paddedSize, BufferedImage.TYPE_INT_RGB);
    Graphics g = paddedImage.getGraphics();
    g.drawImage(image, (paddedSize - originalWidth) / 2, (paddedSize - originalHeight) / 2, null);
    g.dispose();

    return paddedImage;
  }

  private static BufferedImage transformImage(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    double[][][] channels = imageToDoubleArrays(image);

    for (int c = 0; c < 3; c++) {
      for (int size = Math.max(width, height); size > 1; size /= 2) {
        for (int i = 0; i < size; i++) {
          channels[c][i] = transform(channels[c][i], size);
        }

        for (int j = 0; j < size; j++) {
          double[] column = new double[size];
          for (int i = 0; i < size; i++) {
            column[i] = channels[c][i][j];
          }
          column = transform(column, size);
          for (int i = 0; i < size; i++) {
            channels[c][i][j] = column[i];
          }
        }
      }
    }

    return doubleArraysToImage(channels);
  }

  private static BufferedImage thresholdImage(BufferedImage image, double thresholdPercentage) {
    double[][][] channels = imageToDoubleArrays(image);
    double[] allValues = getAllValues(channels);

    Arrays.sort(allValues);

    System.out.println(thresholdPercentage);
    int thresholdIndex = (int) (allValues.length * thresholdPercentage);

    thresholdIndex = Math.min(thresholdIndex, allValues.length - 1);

    double thresholdValue = allValues[thresholdIndex];
    for (int c = 0; c < 3; c++) {
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          channels[c][i][j] =
                  (Math.abs(channels[c][i][j]) >= thresholdValue) ? channels[c][i][j] : 0.0;
        }
      }
    }

    return doubleArraysToImage(channels);
  }

  private static BufferedImage invertImage(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    double[][][] channels = imageToDoubleArrays(image);

    for (int c = 0; c < 3; c++) {
      for (int size = 2; size <= Math.max(width, height); size *= 2) {
        for (int j = 0; j < size; j++) {
          channels[c][j] = invert(channels[c][j], size);
        }

        for (int i = 0; i < size; i++) {
          double[] column = new double[size];
          for (int j = 0; j < size; j++) {
            column[j] = channels[c][j][i];
          }
          column = invert(column, size);
          for (int j = 0; j < size; j++) {
            channels[c][j][i] = column[j];
          }
        }
      }
    }

    return doubleArraysToImage(channels);
  }

  private static BufferedImage unpadImage(
          BufferedImage image, int originalWidth, int originalHeight) {
    int paddedSize = image.getWidth();
    int xOffset = (paddedSize - originalWidth) / 2;
    int yOffset = (paddedSize - originalHeight) / 2;

    return image.getSubimage(xOffset, yOffset, originalWidth, originalHeight);
  }

  private static double[] transform(double[] sequence, int size) {
    double[] avg = new double[size / 2];
    double[] diff = new double[size / 2];

    for (int i = 0, j = 0; i < size - 1; i += 2, j++) {
      double a = sequence[i];
      double b = sequence[i + 1];
      double av = (a + b) / Math.sqrt(2);
      double di = (a - b) / Math.sqrt(2);
      avg[j] = av;
      diff[j] = di;
    }

    double[] result = new double[size];
    for (int i = 0; i < size / 2; i++) {
      result[i] = avg[i];
      result[i + size / 2] = diff[i];
    }

    return result;
  }

  /**
   * to compress file.
   * @param percentage      Threshold value for compression.
   * @param inputImagePath  Path of image to be compressed.
   * @param outputImagePath Destination for the compressed image.
   * @return the compressed image.
   */
  public static Pixel compressImage(int percentage, String inputImagePath, String outputImagePath) {
    List<Integer>[][] pixels = new List[0][];
    try {
      BufferedImage originalImage = ImageIO.read(new File(inputImagePath));
      int newWidth = (int) (originalImage.getWidth() * (percentage / 100.0));
      int newHeight = (int) (originalImage.getHeight() * (percentage / 100.0));

      Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
      BufferedImage compressedImage =
              new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
      compressedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

      int width = compressedImage.getWidth();
      int height = compressedImage.getHeight();

      pixels = new List[height][width];
      for (int i = 0; i < height; i++) {

        for (int j = 0; j < width; j++) {

          int rgb = compressedImage.getRGB(j, i);

          int red = (rgb >> 16) & 0xFF;
          int green = (rgb >> 8) & 0xFF;
          int blue = rgb & 0xFF;

          List<Integer> rgbList = Arrays.asList(red, green, blue);

          pixels[i][j] = rgbList;
        }
      }

      ImageIO.write(compressedImage, "jpg", new File(outputImagePath));
      System.out.println("Image compression successful.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new PixelImplementation(pixels);
  }

  private static double[] invert(double[] sequence, int size) {
    double[] avg = new double[size / 2];
    double[] diff = new double[size / 2];

    for (int i = 0, j = size / 2; j < size; i++, j++) {
      if (i < sequence.length && j < sequence.length) {
        double av = sequence[i];
        double di = sequence[j];
        double a = (av + di) / Math.sqrt(2);
        double b = (av - di) / Math.sqrt(2);
        avg[i] = Math.max(0, Math.min(255, a));
        diff[i] = Math.max(0, Math.min(255, b));
      }
    }

    double[] result = new double[size];
    for (int i = 0, j = 0; i < size - 1; i += 2, j++) {
      if (j < avg.length) {
        result[i] = avg[j];
        result[i + 1] = diff[j];
      }
    }

    return result;
  }

  private static double[][][] imageToDoubleArrays(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    double[][][] channels = new double[3][height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Color color = new Color(image.getRGB(j, i));
        channels[0][i][j] = color.getRed();
        channels[1][i][j] = color.getGreen();
        channels[2][i][j] = color.getBlue();
      }
    }

    return channels;
  }

  private static BufferedImage doubleArraysToImage(double[][][] channels) {
    int height = channels[0].length;
    int width = channels[0][0].length;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int red = (int) channels[0][i][j];
        int green = (int) channels[1][i][j];
        int blue = (int) channels[2][i][j];
        int rgb = (red << 16) | (green << 8) | blue;
        image.setRGB(j, i, rgb);
      }
    }

    return image;
  }

  private static double[] getAllValues(double[][][] channels) {
    int height = channels[0].length;
    int width = channels[0][0].length;

    double[] values = new double[3 * height * width];
    int index = 0;

    for (int c = 0; c < 3; c++) {
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          values[index++] = channels[c][i][j];
        }
      }
    }

    return values;
  }
}
