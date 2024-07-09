package model;

import controller.ImageController;
import controller.ImageControllerImplementation;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import view.ImageView;

/**
 * Implementation of the ImageModel interface providing functionality for image manipulation and
 * operations.
 */
public class ImageModelImplementation implements ImageModel {

  private final Map<String, Pixel> reference = new HashMap<String, Pixel>();

  private Integer maxVal(Pixel imageArray) {
    List<Integer>[][] img = imageArray.getPixel();
    int maximumValue = Collections.max(img[0][0]);
    for (int i = 0; i < img.length; i++) {
      for (int j = 0; j < img[0].length; j++) {
        maximumValue = Collections.max(img[i][j]);
        if (maximumValue == 255) {
          return maximumValue;
        }
      }
    }
    return maximumValue;
  }

  /**
   * Retrieves the list of reference names available in the system.
   *
   * @return A list containing the names of references available in the system.
   */
  @Override
  public List<String> getRefNames() {
    List<String> keys = new ArrayList<>();
    keys.addAll(reference.keySet());
    return keys;
  }

  /**
   * Used to test the mock model.
   *
   * @param imageName image passed as input.
   */
  @Override
  public void applyBlur(String imageName) { // used for mock testing.
  }

  /**
   * Checks and identifies the component type based on the provided string input.
   *
   * @param component The string representing a particular image component (e.g., "red-component",
   *     "blue-component").
   * @return The identified component type as a string ("Red", "Blue", "Green", "Intensity",
   *     "Value", "Luma"), or "Unrecognized Component" if the input does not match recognized
   *     component types.
   */
  @Override
  public String check(String component) {

    if (component.equals("red-component")) {
      return "Red";
    } else if (component.equals("blue-component")) {
      return "Blue";
    } else if (component.equals("green-component")) {
      return "Green";
    } else if (component.equals("intensity-component")) {
      return "Intensity";
    } else if (component.equals("value-component")) {
      return "Value";
    } else if (component.equals("luma-component")) {
      return "Luma";
    } else {
      return "Unrecognized Component";
    }
  }

  /**
   * Performs a flipping operation (vertical or horizontal) on an image and saves the result under a
   * new reference name.
   *
   * @param flipType The type of flipping operation to be performed (e.g., "Vertical",
   *     "Horizontal").
   * @param img The reference name of the original image to be flipped.
   * @param newImgName The reference name for the flipped image after the operation.
   * @throws IOException If an I/O error occurs during the flipping operation.
   */
  @Override
  public void flipping(String flipType, String img, String newImgName) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(img);
    Pixel newPixel =
        image.flip(flipType, pixel.getPixel()[0].length, pixel.getPixel().length, pixel);
    reference.put(newImgName, newPixel);
  }

  /**
   * Combines separate red, green, and blue images into a single RGB image and saves it under a new
   * reference name.
   *
   * @param redComp The reference name of the red component image.
   * @param greenComp The reference name of the green component image.
   * @param blueComp The reference name of the blue component image.
   * @param newImgName The reference name for the combined RGB image.
   * @throws IOException If an I/O error occurs during the RGB image combination process.
   */
  @Override
  public void rgbImgsCombine(String redComp, String greenComp, String blueComp, String newImgName)
      throws IOException {
    ImageUtil img = new ImageUtil();
    Pixel redImg = reference.get(redComp);
    Pixel greenImg = reference.get(greenComp);
    Pixel blueImg = reference.get(blueComp);
    Pixel newPixel =
        img.rgbCombine(
            redImg, greenImg, blueImg, redImg.getPixel()[0].length, redImg.getPixel().length);
    reference.put(newImgName, newPixel);
  }

  @Override
  public void compress(String fileName, String destFileName, int threshold) {
    ImageUtil img = new ImageUtil();
    compressed(threshold, fileName, destFileName);
  }

  /**
   * Used to compress an image.
   *
   * @param percentage Threshhold value.
   * @param fileName Path to file.
   * @param outputPath Destination of the file.
   */
  public void compressed(int percentage, String fileName, String outputPath) {
    ArrayList<Pixel> splittedImages = getAllComponentImages(fileName);
    Pixel originalImage = reference.get(fileName);
    CompressionHelper imageCompression =
        new CompressionHelper(originalImage.getPixel().length, originalImage.getPixel()[0].length);
    imageCompression.compThreshold(splittedImages.get(0));
    imageCompression.compThreshold(splittedImages.get(1));
    imageCompression.compThreshold(splittedImages.get(2));
    imageCompression.retrieveThresholdValue(percentage);
    imageCompression.afterThresh();
    ArrayList<Pixel> results = imageCompression.unpadInverse();
    Pixel result = combineAllComponents(results.get(0), results.get(1), results.get(2));
    reference.put(outputPath, result);
  }

  Pixel combineAllComponents(Pixel image1, Pixel image2, Pixel image3) {

    int referenceWidth = image1.getPixel().length;
    int referenceHeight = image1.getPixel()[0].length;
    List<Integer>[][] resultImagePixels =
        new List[image1.getPixel().length][image1.getPixel()[0].length];

    int red;
    int blue;
    int green;
    for (int x = 0; x < referenceWidth; x++) {
      for (int y = 0; y < referenceHeight; y++) {
        List<Integer> appendToOutput = new ArrayList<>();

        red =
            image1.getPixel()[x][y].get(0)
                + image2.getPixel()[x][y].get(0)
                + image3.getPixel()[x][y].get(0);
        green =
            image1.getPixel()[x][y].get(1)
                + image2.getPixel()[x][y].get(1)
                + image3.getPixel()[x][y].get(1);
        blue =
            image1.getPixel()[x][y].get(2)
                + image2.getPixel()[x][y].get(2)
                + image3.getPixel()[x][y].get(2);
        appendToOutput.add(0, red);
        appendToOutput.add(1, green);
        appendToOutput.add(2, blue);
        resultImagePixels[x][y] = appendToOutput;
      }
    }
    return new PixelImplementation(resultImagePixels);
  }

  private ArrayList<Pixel> getAllComponentImages(String imageName) {
    ArrayList<Pixel> images = new ArrayList<>();
    Pixel image1 = applyOperationToAllPixels(pixel -> Arrays.asList(pixel.get(0), 0, 0), imageName);
    Pixel image2 = applyOperationToAllPixels(pixel -> Arrays.asList(0, pixel.get(1), 0), imageName);
    Pixel image3 = applyOperationToAllPixels(pixel -> Arrays.asList(0, 0, pixel.get(2)), imageName);
    images.add(image1);
    images.add(image2);
    images.add(image3);
    return images;
  }

  private Pixel applyOperationToAllPixels(
      Function<List<Integer>, List<Integer>> operation, String imageName) {
    Pixel image = this.reference.get(imageName);
    if (image == null) {
      throw new IllegalStateException("Image Not found or name entered in wrong syntax");
    }
    List<Integer>[][] resultImagePixels =
        new List[image.getPixel().length][image.getPixel()[0].length];

    for (int x = 0; x < image.getPixel().length; x++) {
      for (int y = 0; y < image.getPixel()[0].length; y++) {
        resultImagePixels[x][y] = operation.apply(image.getPixel()[x][y]);
      }
    }
    return new PixelImplementation(resultImagePixels);
  }

  /**
   * Adjusts the brightness of the specified image by increasing or decreasing its brightness value
   * and saves the modified image under a new reference name.
   *
   * @param value The value by which the brightness should be adjusted (positive for brightening,
   *     negative for darkening).
   * @param img The reference name of the original image to be brightened or darkened.
   * @param newImgName The reference name for the modified image after adjusting the brightness.
   * @throws IOException If an I/O error occurs during the brightness adjustment operation.
   */
  @Override
  public void brightenImg(int value, String img, String newImgName) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(img);
    Pixel newPixel =
        image.brightDark(value, pixel.getPixel()[0].length, pixel.getPixel().length, pixel);
    reference.put(newImgName, newPixel);
  }

  /**
   * Converts the specified image to greyscale based on the given component option and saves the
   * result under a new reference name.
   *
   * @param option The greyscale component option ("Red", "Green", "Blue", "Luma", "Intensity") or
   *     the general "image-name" for luma-filter.
   * @param imgName The reference name of the original image to be converted to greyscale.
   * @param newImgName The reference name for the greyscaled image after the conversion.
   * @throws IOException If an I/O error occurs during the greyscale conversion operation.
   */
  @Override
  public void greyscaleImg(String option, String imgName, String newImgName) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(imgName);
    Pixel newPixel =
        image.greyscaleImg(option, pixel.getPixel()[0].length, pixel.getPixel().length, pixel);
    reference.put(newImgName, newPixel);
  }

  public static final double[][] SEPIAFILTER = {
    {0.393, 0.769, 0.189},
    {0.349, 0.686, 0.168},
    {0.272, 0.534, 0.131}
  };

  /**
   * Applies a sepia filter to the specified image and creates a new sepia-toned image.
   *
   * @param imgName The name of the original image to apply the sepia filter.
   * @param newImgName The name for the new sepia-toned image.
   * @param splitPercentage The percentage to adjust the intensity of the sepia effect.
   * @throws IOException If an I/O error occurs during the sepia filter application.
   */
  @Override
  public void imgSepia(String imgName, String newImgName, double splitPercentage)
      throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(imgName);
    Pixel newPixel =
        image.filterMultiply(
            SEPIAFILTER,
            pixel,
            pixel.getPixel()[0].length,
            pixel.getPixel().length,
            splitPercentage);
    reference.put(newImgName, newPixel);
  }

  public static final double[][] GREYSCALEFILTER = {
    {0.2126, 0.7152, 0.0722},
    {0.2126, 0.7152, 0.0722},
    {0.2126, 0.7152, 0.0722}
  };

  /**
   * Creates a new greyscale image from the specified image using a greyscale filter.
   *
   * @param imgName The name of the original image to convert to greyscale.
   * @param newImgName The name for the new greyscale image.
   * @throws IOException If an I/O error occurs during the greyscale image creation.
   */
  @Override
  public void newGreyscaleImg(String imgName, String newImgName) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(imgName);
    Pixel newPixel =
        image.filterMultiply(
            GREYSCALEFILTER, pixel, pixel.getPixel()[0].length, pixel.getPixel().length, 0);
    reference.put(newImgName, newPixel);
  }

  public static final double[][] SHARPERFILTER = {
    {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
    {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
    {-1.0 / 8, 1.0 / 4, 1.0, 1.0 / 4, -1.0 / 8},
    {1.0 / 4, 1.0 / 4, 1.0 / 4, 1.0 / 8, -1.0 / 8},
    {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}
  };

  /**
   * Applies a sharpening filter to the specified image, creating a new image with a sharpened
   * effect.
   *
   * @param imgName The name of the image to apply the sharpening effect to.
   * @param newImgName The name for the new image with the applied sharpening effect.
   * @param splitPercentage The percentage of split for the sharpening effect.
   * @throws IOException If an I/O error occurs during the sharpening effect application.
   */
  @Override
  public void imgSharpen(String imgName, String newImgName, double splitPercentage)
      throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(imgName);
    Pixel newPixel =
        image.filter(
            pixel,
            SHARPERFILTER,
            pixel.getPixel()[0].length,
            pixel.getPixel().length,
            splitPercentage);
    reference.put(newImgName, newPixel);
  }

  public static final double[][] BLURFILTER = {
    {1.0 / 16, 1.0 / 8, 1.0 / 16},
    {1.0 / 8, 1.0 / 4, 1.0 / 8},
    {1.0 / 16, 1.0 / 8, 1.0 / 16}
  };

  /**
   * Applies a blur filter to the specified image, creating a new image with the blurred effect.
   *
   * @param imgName The name of the image to apply the blur effect to.
   * @param newImgName The name for the new image with the applied blur effect.
   * @param splitPercentage The percentage of split for the blur effect.
   * @throws IOException If an I/O error occurs during the blur effect application.
   */
  @Override
  public void imgblur(String imgName, String newImgName, double splitPercentage)
      throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(imgName);
    Pixel newPixel =
        image.filter(
            pixel,
            BLURFILTER,
            pixel.getPixel()[0].length,
            pixel.getPixel().length,
            splitPercentage);
    reference.put(newImgName, newPixel);
  }

  /**
   * Executes a series of commands read from a provided scanner, interpreting and executing them
   * within the application.
   *
   * @param scanner The scanner object used to read the commands from an input source.
   * @param model The ImageModel instance to operate on the commands.
   * @param input The InputStream for input operations.
   * @param output The OutputStream for output operations.
   * @param view The ImageView instance for displaying output.
   * @throws IOException If an I/O error occurs while executing the commands.
   */
  @Override
  public void runScript(
      Scanner scanner, ImageModel model, InputStream input, OutputStream output, ImageView view)
      throws IOException {
    ImageController inst = new ImageControllerImplementation(model, input, output, view);

    while (scanner.hasNextLine()) {
      String scan = scanner.nextLine();
      if (scan.charAt(0) != '#') {
        view.viewOutput(inst.commandExecute(model, scan), output);
        System.out.println();
      }
    }
    scanner.close();
  }

  /**
   * Loads an image from the provided Scanner object and stores it with a given reference name.
   *
   * @param sc The Scanner object used to read the image data.
   * @param referenceName The reference name to associate with the loaded image.
   * @return True if the image is successfully loaded and stored; false otherwise.
   * @throws IOException If an I/O error occurs during the image loading process.
   */
  @Override
  public boolean loadImage(Scanner sc, String referenceName) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = image.ppmRead(sc);
    if (pixel != null) {
      reference.put(referenceName, pixel);
      return true;
    }
    return false;
  }

  /**
   * Loads an image from a BufferedImage object and associates it with a given reference name.
   *
   * @param inputImg The BufferedImage object containing the image data to be loaded.
   * @param references The reference name to associate with the loaded image.
   * @throws IOException If an I/O error occurs during the image loading process.
   */
  @Override
  public void loadImage(BufferedImage inputImg, String references) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = image.readOtherFormatsFile(inputImg);
    reference.put(references, pixel);
  }

  /**
   * Saves an image referenced by the given name to a specified file path in PPM format.
   *
   * @param path The file path to save the image.
   * @param references The reference name for the image to be saved.
   * @return A StringBuilder containing the representation of the saved image in PPM format.
   * @throws IOException If an I/O error occurs during the image saving process.
   */
  @Override
  public StringBuilder saveImage(String path, String references) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(references);
    return image.ppmCreate(
        "P3", pixel.getPixel()[0].length, pixel.getPixel().length, maxVal(pixel), pixel, path);
  }

  /**
   * Saves an image referenced by the given name to a specified file path.
   *
   * @param path The file path to save the image.
   * @param references The reference name for the image to be saved.
   * @return The BufferedImage representation of the saved image.
   * @throws IOException If an I/O error occurs during the image saving process.
   */
  @Override
  public BufferedImage saveImage1(String path, String references) throws IOException {
    ImageUtil image = new ImageUtil();
    Pixel pixel = reference.get(references);
    return image.createImage(pixel, path);
  }

  /**
   * Checks if the provided option is the command to quit the application.
   *
   * @param checkOption The option/command to be checked for quitting the application.
   * @return True if the provided option is "quit"; false otherwise.
   */
  @Override
  public boolean quit(String checkOption) {
    return (checkOption.equals("quit"));
  }

  /**
   * Generates a histogram for the specified image and saves it as a new image under a given
   * reference name.
   *
   * @param imageName The reference name of the image for which the histogram needs to be generated.
   * @param newImageName The reference name for the new image containing the histogram.
   * @throws IOException If an I/O error occurs while generating or saving the histogram image.
   */
  @Override
  public void histogram(String imageName, String newImageName) throws IOException {
    ImageUtil image = new ImageUtil();
    List<Integer>[][] imagePixel = reference.get(imageName).getPixel();
    image.fetchHist(imageName, newImageName, imagePixel);
  }

  /**
   * Adjusts the color curves of an image based on provided parameters and saves it under a new
   * reference name.
   *
   * @param imgName The reference name of the original image to adjust the levels.
   * @param newImgName The reference name for the modified image after adjusting the levels.
   * @param b The black point for adjusting the levels.
   * @param m The mid-point for adjusting the levels.
   * @param w The white point for adjusting the levels.
   * @param splitPercent The percentage of split for the level adjustment.
   * @throws IOException If an I/O error occurs during the level adjustment process.
   */
  @Override
  public void levelAdj(String imgName, String newImgName, int b, int m, int w, double splitPercent)
      throws IOException {
    ImageUtil image = new ImageUtil();
    List<Integer>[][] pixel = reference.get(imgName).getPixel();
    Pixel newPixel = image.levelAdjustImg(b, m, w, splitPercent, pixel);
    reference.put(newImgName, newPixel);
  }

  /**
   * Performs color correction on the specified image and saves it as a new image.
   *
   * @param imgName The name of the image to be corrected.
   * @param newImgName The name for the new image after applying color correction.
   * @param splitPercent The split percentage for color correction.
   * @throws IOException If an error occurs while performing the color correction.
   */
  @Override
  public void imageCorrection(String imgName, String newImgName, double splitPercent)
      throws IOException {
    ImageUtil image = new ImageUtil();
    List<Integer>[][] pixel = reference.get(imgName).getPixel();
    Pixel newPixel = image.colorCorrectionImg(imgName, newImgName, splitPercent, pixel);
    reference.put(newImgName, newPixel);
  }

  @Override
  public BufferedImage viewImage(String referenceName) throws IOException {
    ImageUtil img = new ImageUtil();
    return img.createImageFile(reference.get(referenceName));
  }

  @Override
  public boolean checkImageLoaded() {
    return reference.containsKey("image");
  }

  @Override
  public void imgRedComp(String s, String d) {

    Pixel pixel = reference.get(s);
    Pixel pixelRed = buildRedPixelImage(pixel);
    this.reference.put(d, pixelRed);
  }

  @Override
  public void imgGreenComp(String s, String d) {

    Pixel pixel = reference.get(s);
    Pixel pixelGreen = buildGreenPixelImage(pixel);
    this.reference.put(d, pixelGreen);
  }

  @Override
  public void imgBlueComp(String s, String d) {

    Pixel pixel = reference.get(s);
    Pixel pixelBlue = buildBluePixelImage(pixel);
    this.reference.put(d, pixelBlue);
  }

  private Pixel buildRedPixelImage(Pixel orgImage) {
    int imageWidth = orgImage.getPixel().length;
    int imageHeight = orgImage.getPixel()[0].length;
    int rComponent = 0;
    List<Integer>[][] redImage = null;
    redImage = new List[imageHeight][imageWidth];
    for (int k = 0; k < imageHeight; k++) {
      for (int l = 0; l < imageWidth; l++) {
        List<Integer> addcomp = new ArrayList<>();
        rComponent = orgImage.getPixel()[k][l].get(0);
        addcomp = Arrays.asList(rComponent, 0, 0);
        redImage[k][l] = addcomp;
      }
    }
    return new PixelImplementation(redImage);
  }

  private Pixel buildGreenPixelImage(Pixel orgImage) {
    int imageWidth = orgImage.getPixel().length;
    int imageHeight = orgImage.getPixel()[0].length;
    int gComponent = 0;
    List<Integer>[][] greenImage = null;
    greenImage = new List[imageHeight][imageWidth];
    for (int k = 0; k < imageHeight; k++) {
      for (int l = 0; l < imageWidth; l++) {
        List<Integer> addcomp = new ArrayList<>();
        gComponent = orgImage.getPixel()[k][l].get(1);
        addcomp = Arrays.asList(0, gComponent, 0);
        greenImage[k][l] = addcomp;
      }
    }
    return new PixelImplementation(greenImage);
  }

  private Pixel buildBluePixelImage(Pixel orgImage) {
    int imageWidth = orgImage.getPixel().length;
    int imageHeight = orgImage.getPixel()[0].length;
    int bComponent = 0;
    List<Integer>[][] blueImage = null;
    blueImage = new List[imageHeight][imageWidth];
    for (int k = 0; k < imageHeight; k++) {
      for (int l = 0; l < imageWidth; l++) {
        List<Integer> addcomp = new ArrayList<>();
        bComponent = orgImage.getPixel()[k][l].get(2);
        addcomp = Arrays.asList(0, 0, bComponent);
        blueImage[k][l] = addcomp;
      }
    }
    return new PixelImplementation(blueImage);
  }
}
