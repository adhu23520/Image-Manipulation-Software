package controller;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

import model.ImageModel;
import view.ImageView;
import view.UIInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This class contains unit tests that verify the functionality of the UIController.
 */
public class UIControllerTest {
  class ImageModelMock implements ImageModel {

    boolean levelAdjustCalled = false;
    boolean blurCalled = false;
    boolean brightenCalled = false;
    int brightenValue = 0;

    @Override
    public void brightenImg(int value, String imageName, String newImageName) throws IOException {
      brightenCalled = true;
      brightenValue = value;
    }

    @Override
    public void applyBlur(String imageName) {
      blurCalled = true;
    }

    /**
     * Checks if the provided component exists.
     *
     * @param component The component to check.
     * @return A string indicating the status of the component.
     */
    @Override
    public String check(String component) {
      return null;
    }

    /**
     * Retrieves a list of reference names.
     *
     * @return The list of reference names.
     */
    @Override
    public List<String> getRefNames() {
      return null;
    }

    /**
     * Loads an image from the provided Scanner object.
     *
     * @param sc            The Scanner object containing image data.
     * @param referenceName The reference name for the loaded image.
     * @return A boolean indicating if the image was loaded successfully.
     * @throws IOException If an I/O error occurs during image loading.
     */
    @Override
    public boolean loadImage(Scanner sc, String referenceName) throws IOException {
      return false;
    }

    /**
     * Loads an image from a BufferedImage object.
     *
     * @param inputImg   The BufferedImage to load.
     * @param references The reference name for the loaded image.
     * @throws IOException If an I/O error occurs during image loading.
     */
    @Override
    public void loadImage(BufferedImage inputImg, String references) throws IOException {
      // To mock the model.

    }

    /**
     * Saves an image referenced by the given name to a specified file path in PPM format.
     *
     * @param filePath      The file path to save the image.
     * @param referenceName The reference name for the image to be saved.
     * @return A StringBuilder containing the representation of the saved image in PPM format.
     * @throws IOException If an I/O error occurs during the image saving process.
     */
    @Override
    public StringBuilder saveImage(String filePath, String referenceName) throws IOException {
      return null;
    }

    /**
     * Saves an image referenced by the given name to a specified file path.
     *
     * @param filePath      The file path to save the image.
     * @param referenceName The reference name for the image to be saved.
     * @return The BufferedImage representation of the saved image.
     * @throws IOException If an I/O error occurs during the image saving process.
     */
    @Override
    public BufferedImage saveImage1(String filePath, String referenceName) throws IOException {
      return null;
    }

    /**
     * Performs a flipping operation (vertical or horizontal) on an image and saves the result under
     * a new reference name.
     *
     * @param flipOption   The type of flipping operation to be performed (e.g., "Vertical",
     *                     "Horizontal").
     * @param imageName    The reference name of the original image to be flipped.
     * @param newImageName The reference name for the flipped image after the operation.
     * @throws IOException If an I/O error occurs during the flipping operation.
     */
    @Override
    public void flipping(String flipOption, String imageName, String newImageName)
            throws IOException {
      // To mock the model.

    }

    /**
     * Converts the specified image to greyscale based on the given component option and saves the
     * result under a new reference name.
     *
     * @param componentOption The greyscale component option ("Red", "Green", "Blue", "Luma",
     *                        "Intensity") or the general "image-name" for luma-filter.
     * @param imageName       The reference name of the original image to be converted to greyscale.
     * @param newImageName    The reference name for the greyscaled image after the conversion.
     * @throws IOException If an I/O error occurs during the greyscale conversion operation.
     */
    @Override
    public void greyscaleImg(String componentOption, String imageName, String newImageName)
            throws IOException {
      // To mock the model.

    }

    /**
     * Combines separate red, green, and blue images into a single RGB image and saves it under a
     * new reference name.
     *
     * @param rComponent   The reference name of the red component image.
     * @param gComponent   The reference name of the green component image.
     * @param bComponent   The reference name of the blue component image.
     * @param newImageName The reference name for the combined RGB image.
     * @throws IOException If an I/O error occurs during the RGB image combination process.
     */
    @Override
    public void rgbImgsCombine(
            String rComponent, String gComponent, String bComponent, String newImageName)
            throws IOException {
      // To mock the model.

    }

    /**
     * Executes a series of commands read from a provided scanner, interpreting and executing them
     * within the application.
     *
     * @param sc    The scanner object used to read the commands from an input source.
     * @param model The ImageModel instance to operate on the commands.
     * @param in    The InputStream for input operations.
     * @param out   The OutputStream for output operations.
     * @param view  The ImageView instance for displaying output.
     * @throws IOException If an I/O error occurs while executing the commands.
     */
    @Override
    public void runScript(
            Scanner sc, ImageModel model, InputStream in, OutputStream out, ImageView view)
            throws IOException {
      // To mock the model.

    }

    /**
     * Applies a blur filter to the specified image, creating a new image with the blurred effect.
     *
     * @param imageName       The name of the image to apply the blur effect to.
     * @param newImageName    The name for the new image with the applied blur effect.
     * @param splitPercentage The percentage of split for the blur effect.
     * @throws IOException If an I/O error occurs during the blur effect application.
     */
    @Override
    public void imgblur(String imageName, String newImageName, double splitPercentage)
            throws IOException {
      // To mock the model.

    }

    /**
     * Applies a sharpening filter to the specified image, creating a new image with a sharpened
     * effect.
     *
     * @param imageName       The name of the image to apply the sharpening effect to.
     * @param newImageName    The name for the new image with the applied sharpening effect.
     * @param splitPercentage The percentage of split for the sharpening effect.
     * @throws IOException If an I/O error occurs during the sharpening effect application.
     */
    @Override
    public void imgSharpen(String imageName, String newImageName, double splitPercentage)
            throws IOException {
      // To mock the model.

    }

    /**
     * Creates a new greyscale image from the specified image using a greyscale filter.
     *
     * @param imageName    The name of the original image to convert to greyscale.
     * @param newImageName The name for the new greyscale image.
     * @throws IOException If an I/O error occurs during the greyscale image creation.
     */
    @Override
    public void newGreyscaleImg(String imageName, String newImageName) throws IOException {
      // To mock the model.

    }

    /**
     * Applies a sepia filter to the specified image and creates a new sepia-toned image.
     *
     * @param imageName       The name of the original image to apply the sepia filter.
     * @param newImageName    The name for the new sepia-toned image.
     * @param splitPercentage The percentage to adjust the intensity of the sepia effect.
     * @throws IOException If an I/O error occurs during the sepia filter application.
     */
    @Override
    public void imgSepia(String imageName, String newImageName, double splitPercentage)
            throws IOException {
      // To mock the model.

    }

    /**
     * Checks if the provided option is the command to quit the application.
     *
     * @param checkCommand The option/command to be checked for quitting the application.
     * @return True if the provided option is "quit"; false otherwise.
     */
    @Override
    public boolean quit(String checkCommand) {
      return false;
    }

    /**
     * Applies a histogram transformation to an image.
     *
     * @param imageName     The name of the source image.
     * @param destImageName The name of the destination image.
     * @throws IOException If an I/O error occurs during histogram creation.
     */
    @Override
    public void histogram(String imageName, String destImageName) throws IOException {
      // To mock the model.

    }

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
    @Override
    public void levelAdj(
            String imgName, String newImgName, int b, int m, int w, double splitPercent)
            throws IOException {
      // To mock the model.

    }

    /**
     * Performs color correction on the specified image and saves it as a new image.
     *
     * @param imgName      The name of the image to be corrected.
     * @param newImgName   The name for the new image after applying color correction.
     * @param splitPercent The split percentage for color correction.
     * @throws IOException If an error occurs while performing the color correction.
     */
    @Override
    public void imageCorrection(String imgName, String newImgName, double splitPercent)
            throws IOException {
      // To mock the model.

    }

    @Override
    public BufferedImage viewImage(String referenceName) throws IOException {
      return null;
    }

    @Override
    public boolean checkImageLoaded() {
      return false;
    }

    @Override
    public void compress(String fileName, String destFileName, int threshold) {
      // To mock the model.

    }

    @Override
    public void imgRedComp(String fileName, String destName) {
      // To mock the model.
    }
  }

  class UIInterfaceMock implements UIInterface {
    public int brightenInput;
    BufferedImage displayedImage;

    /**
     * Gets the filepath for loading or saving.
     *
     * @param option Open or Save
     * @return String filepath
     */
    @Override
    public String getFilePath(String option) {
      return null;
    }

    @Override
    public void setImage(BufferedImage image) {
      displayedImage = image;
    }

    /**
     * Gets the brighten value through the slider.
     *
     * @return Brighten value
     */
    @Override
    public Integer inputBrighten() {
      return null;
    }

    /**
     * Gets three files for executing RGB combine function.
     *
     * @return List of three filepath
     */
    @Override
    public List<String> rgbFileRead() {
      return null;
    }

    /**
     * Displays all three images and asks the user whether the images are to be saved.
     *
     * @param image     Split images
     * @param imageType Red, green, blue
     * @return True or False
     */
    @Override
    public boolean rgbSplitPopUp(BufferedImage image, String imageType) {
      return false;
    }

    /**
     * Error thrown when the user tries to save without loading any image.
     */
    @Override
    public void noImageErrorMessage() {
      // To mock the model.

    }

    /**
     * Warning displayed when the user tries to load a new image when already an image is in the
     * GUI.
     */
    @Override
    public void newLoadSaveWarning() {
      // To mock the model.

    }

    /**
     * Error thrown when file extension is invalid.
     */
    @Override
    public void invalidExtensionError() {
      // To mock the model.

    }

    /**
     * Pop-up asking whether the user wants to quit the program.
     *
     * @return True or False
     */
    @Override
    public boolean quitGUI() {
      return false;
    }

    /**
     * Adds features to the GUI.
     *
     * @param features New feature
     * @throws IOException throws if file not found
     */
    @Override
    public void addFeatures(Features features) throws IOException {
      // To mock the model.

    }
  }

  private ImageModelMock modelMock;
  private UIInterfaceMock viewMock;
  private UIController controller;

  @Before
  public void setUp() throws IOException {
    modelMock = new ImageModelMock();
    viewMock = new UIInterfaceMock();
    controller = new UIController(modelMock, viewMock);
  }

  @Test
  public void testLevelsAdjustment() throws IOException {
    int b = 10;
    int m = 50;
    int w = 100;
    double splitPercentage = 20.0;

    controller.levelsAdjustment(b, m, w, splitPercentage);

    assertTrue(modelMock.levelAdjustCalled);

    assertNotNull(viewMock.displayedImage);
  }

  @Test
  public void testBrightenImage() throws IOException {
    int expectedBrightenValue = 20;
    viewMock.brightenInput = expectedBrightenValue;

    controller.brightenImage();

    assertTrue("brightenImg was not called", modelMock.brightenCalled);
    assertEquals("Incorrect brighten value", expectedBrightenValue, modelMock.brightenValue);
  }
}
