package model;

import java.awt.image.BufferedImage;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

import view.ImageView;

/**
 * The ImageModel interface defines the contract for manipulating images.
 * It provides various image processing operations.
 */
public interface ImageModel {

  void applyBlur(String imageName);

  /**
   * Checks if the provided component exists.
   *
   * @param component The component to check.
   * @return A string indicating the status of the component.
   */
  String check(String component);

  /**
   * Retrieves a list of reference names.
   *
   * @return The list of reference names.
   */
  List<String> getRefNames();

  /**
   * Loads an image from the provided Scanner object.
   *
   * @param sc            The Scanner object containing image data.
   * @param referenceName The reference name for the loaded image.
   * @return A boolean indicating if the image was loaded successfully.
   * @throws IOException If an I/O error occurs during image loading.
   */
  boolean loadImage(Scanner sc, String referenceName) throws IOException;

  /**
   * Loads an image from a BufferedImage object.
   *
   * @param inputImg   The BufferedImage to load.
   * @param references The reference name for the loaded image.
   * @throws IOException If an I/O error occurs during image loading.
   */
  void loadImage(BufferedImage inputImg, String references) throws IOException;

  /**
   * Saves an image referenced by the given name to a specified file path in PPM format.
   *
   * @param filePath      The file path to save the image.
   * @param referenceName The reference name for the image to be saved.
   * @return A StringBuilder containing the representation of the saved image in PPM format.
   * @throws IOException If an I/O error occurs during the image saving process.
   */
  StringBuilder saveImage(String filePath, String referenceName) throws IOException;


  /**
   * Saves an image referenced by the given name to a specified file path.
   *
   * @param filePath      The file path to save the image.
   * @param referenceName The reference name for the image to be saved.
   * @return The BufferedImage representation of the saved image.
   * @throws IOException If an I/O error occurs during the image saving process.
   */
  BufferedImage saveImage1(String filePath, String referenceName) throws IOException;


  /**
   * Performs a flipping operation (vertical or horizontal)
   * on an image and saves the result under a new reference name.
   *
   * @param flipOption   The type of flipping operation to be performed
   *                     (e.g., "Vertical", "Horizontal").
   * @param imageName    The reference name of the original image to be flipped.
   * @param newImageName The reference name for the flipped image after the operation.
   * @throws IOException If an I/O error occurs during the flipping operation.
   */
  void flipping(String flipOption, String imageName, String newImageName) throws IOException;


  /**
   * Converts the specified image to greyscale based on the given
   * component option and saves the result under a new reference name.
   *
   * @param componentOption The greyscale component option
   *                        ("Red", "Green", "Blue", "Luma", "Intensity") or the general
   *                        "image-name" for luma-filter.
   * @param imageName       The reference name of the original image to be converted to greyscale.
   * @param newImageName    The reference name for the greyscaled image after the conversion.
   * @throws IOException If an I/O error occurs during the greyscale conversion operation.
   */
  void greyscaleImg(String componentOption, String imageName,
                    String newImageName) throws IOException;


  /**
   * Adjusts the brightness of the specified image by increasing or decreasing its brightness value
   * and saves the modified image under a new reference name.
   *
   * @param value        The value by which the brightness should be adjusted
   *                     (positive for brightening, negative for darkening).
   * @param imageName    The reference name of the original image to be brightened or darkened.
   * @param newImageName The reference name for the modified image after adjusting the brightness.
   * @throws IOException If an I/O error occurs during the brightness adjustment operation.
   */
  void brightenImg(int value, String imageName, String newImageName) throws IOException;


  /**
   * Combines separate red, green, and blue images into a single RGB image
   * and saves it under a new reference name.
   *
   * @param rComponent   The reference name of the red component image.
   * @param gComponent   The reference name of the green component image.
   * @param bComponent   The reference name of the blue component image.
   * @param newImageName The reference name for the combined RGB image.
   * @throws IOException If an I/O error occurs during the RGB image combination process.
   */
  void rgbImgsCombine(String rComponent, String gComponent,
                      String bComponent, String newImageName) throws IOException;


  /**
   * Executes a series of commands read from a provided scanner,
   * interpreting and executing them within the application.
   *
   * @param sc    The scanner object used to read the commands from an input source.
   * @param model The ImageModel instance to operate on the commands.
   * @param in    The InputStream for input operations.
   * @param out   The OutputStream for output operations.
   * @param view  The ImageView instance for displaying output.
   * @throws IOException If an I/O error occurs while executing the commands.
   */
  void runScript(Scanner sc, ImageModel model, InputStream in,
                 OutputStream out, ImageView view) throws IOException;


  /**
   * Applies a blur filter to the specified image, creating a new image with the blurred effect.
   *
   * @param imageName       The name of the image to apply the blur effect to.
   * @param newImageName    The name for the new image with the applied blur effect.
   * @param splitPercentage The percentage of split for the blur effect.
   * @throws IOException If an I/O error occurs during the blur effect application.
   */
  void imgblur(String imageName, String newImageName, double splitPercentage) throws IOException;

  /**
   * Applies a sharpening filter to the specified image,
   * creating a new image with a sharpened effect.
   *
   * @param imageName       The name of the image to apply the sharpening effect to.
   * @param newImageName    The name for the new image with the applied sharpening effect.
   * @param splitPercentage The percentage of split for the sharpening effect.
   * @throws IOException If an I/O error occurs during the sharpening effect application.
   */
  void imgSharpen(String imageName, String newImageName, double splitPercentage) throws IOException;

  /**
   * Creates a new greyscale image from the specified image using a greyscale filter.
   *
   * @param imageName    The name of the original image to convert to greyscale.
   * @param newImageName The name for the new greyscale image.
   * @throws IOException If an I/O error occurs during the greyscale image creation.
   */
  void newGreyscaleImg(String imageName, String newImageName) throws IOException;

  /**
   * Applies a sepia filter to the specified image and creates a new sepia-toned image.
   *
   * @param imageName       The name of the original image to apply the sepia filter.
   * @param newImageName    The name for the new sepia-toned image.
   * @param splitPercentage The percentage to adjust the intensity of the sepia effect.
   * @throws IOException If an I/O error occurs during the sepia filter application.
   */
  void imgSepia(String imageName, String newImageName, double splitPercentage) throws IOException;

  /**
   * Checks if the provided option is the command to quit the application.
   *
   * @param checkCommand The option/command to be checked for quitting the application.
   * @return True if the provided option is "quit"; false otherwise.
   */
  boolean quit(String checkCommand);

  /**
   * Applies a histogram transformation to an image.
   *
   * @param imageName     The name of the source image.
   * @param destImageName The name of the destination image.
   * @throws IOException If an I/O error occurs during histogram creation.
   */
  public void histogram(String imageName, String destImageName) throws IOException;

  /**
   * Adjusts the levels of an image.
   *
   * @param imgName      The name of the source image.
   * @param newImgName   The name of the destination image.
   * @param b            The value for the black point.
   * @param m            The value for the mid-point.
   * @param w            The value for the white point.
   * @param splitPercent The percentage value for splitting.
   * @throws IOException If an I/O error occurs during level adjustment.
   */
  public void levelAdj(String imgName, String newImgName,
                       int b, int m, int w, double splitPercent) throws IOException;

  /**
   * Performs color correction on the specified image and saves it as a new image.
   *
   * @param imgName      The name of the image to be corrected.
   * @param newImgName   The name for the new image after applying color correction.
   * @param splitPercent The split percentage for color correction.
   * @throws IOException If an error occurs while performing the color correction.
   */
  void imageCorrection(String imgName, String newImgName, double splitPercent) throws IOException;


  BufferedImage viewImage(String referenceName) throws IOException;

  boolean checkImageLoaded();

  void compress(String fileName, String destFileName, int threshold);

  void imgRedComp(String fileName, String destName);

  void imgGreenComp(String fileName, String destName);

  void imgBlueComp(String fileName, String destName);
}
