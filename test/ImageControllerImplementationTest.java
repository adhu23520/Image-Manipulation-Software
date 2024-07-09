import static org.junit.Assert.assertEquals;

import controller.ImageController;
import controller.ImageControllerImplementation;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import model.ImageModel;
import model.ImageModelImplementation;
import model.ImageUtil;
import model.Pixel;
import org.junit.Test;
import view.ImageView;
import view.ImageViewImplementation;

/** Class to test functionalities of IME. */
public class ImageControllerImplementationTest {

  /**
   * Testing with a wrong command.
   *
   * @throws IOException if file not accessible.
   */
  @Test
  public void testCommandFormat() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input = "crop src/Koala.ppm Koala";
    in = new ByteArrayInputStream(input.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input), out);
    assertEquals(
            "Invalid Command \nAvailable commands:\n"
                    + "load\nsave\nvertical-flip\n"
                    + "horizontal-flip\ngreyscale\nrgb-split\nbrighten\n"
                    + "rgb-combine\nrun-script",
            out.toString());
  }

  /**
   * Load command test - wrong command format.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void loadCommand() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input = "load src/Koala.ppm";
    in = new ByteArrayInputStream(input.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input), out);
    assertEquals(
            "load command format is not correct \n " + "Format: load image-path image-name",
            out.toString());
  }

  /**
   * Load Command Test - same image-name.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void loadCommandTest2() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input = "load src/Koala.ppm Koala";
    String input2 = "load src/Koala.ppm Koala";
    in = new ByteArrayInputStream(input.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    assertEquals(
            "Command Executed Successfully\nimage-name already used by another image", out.toString());
  }

  /**
   * Save Command Test - same image-name.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void saveCommandTest1() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input = "save src/Koala.ppm Koala";
    in = new ByteArrayInputStream(input.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input), out);
    assertEquals("image-name not present", out.toString());
  }

  /**
   * Command Test - Loading, saving and accessing the same image.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void loadSaveLoadCommandTest1() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load images.png Koala";
    String input2 = "save Koala.png Koala-img";
    // String input3 = "load images.png Koala1";
    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    // view.viewOutput(controller.commandExecute(model, input3), out);
    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/Koala.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);
    Scanner sc2 = new Scanner(new FileInputStream("src/Koala1.ppm"));
    Pixel image2 = imageUtil.ppmRead(sc2);
    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Converts ppm image to jpeg and checks whether the pixels are same.
   *
   * @throws IOException throws exception if file not found
   */
  @Test
  public void loadSaveLoadCommandTest2() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "save src/Koala1.jpeg Koala";
    String input3 = "load src/Koala1.jpeg Koala1";
    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);
    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/Koala.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);
    File file = new File("src/Koala1.jpeg");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput);
    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Converts ppm image to png and checks whether the pixels are same.
   *
   * @throws IOException throws exception if file not found
   */
  @Test
  public void loadSaveLoadCommandTest3() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "save src/Koala2.png Koala";
    String input3 = "load src/Koala2.png Koala2";
    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);
    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/Koala.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);
    File file = new File("src/Koala2.png");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput);
    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Converts ppm image to bmp and checks whether the pixels are same.
   *
   * @throws IOException throws exception if file not found
   */
  @Test
  public void loadSaveLoadCommandTest4() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "save src/Koala3.bmp Koala";
    String input3 = "load src/Koala3.bmp Koala3";
    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);
    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/Koala.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);
    File file = new File("src/Koala3.bmp");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput);
    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Command Test - Checks whether horizontal flip is right.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void flipCommandTest1() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "horizontal-flip Fruits hFruits";
    String input3 = "save src/hFruits.ppm hFruits";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/actualoutput/hFruits.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);
    Scanner sc1 = new Scanner(new FileInputStream("res/hfFruits.ppm"));
    Pixel image2 = imageUtil.ppmRead(sc1);
    assertEquals(
            Arrays.stream(image1.getPixel()).toArray(), Arrays.stream(image2.getPixel()).toArray());
  }

  /**
   * Command Test - Checks whether vertical flip is right.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void flipCommandTest2() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "vertical-flip Fruits vFruits";
    String input3 = "save src/vFruits.ppm vFruits";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/vFruits.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);
    Scanner sc1 = new Scanner(new FileInputStream("res/vfFruits.ppm"));
    Pixel image2 = imageUtil.ppmRead(sc1);
    assertEquals(
            Arrays.stream(image1.getPixel()).toArray(), Arrays.stream(image2.getPixel()).toArray());
  }

  /**
   * Command Test - Horizontal flip, Vertical flip an image where command given is wrong.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void flipCommandTest3() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "horizontal-flip Fruits";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);

    assertEquals(
            "Command Executed Successfullyhorizontal-flip command format "
                    + "is not correct \n"
                    + "Format: horizontal-flip image-name dest-image-name",
            out.toString());
  }

  /**
   * Command Test - Horizontal flip, Vertical flip an image where command given is wrong.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void flipCommandTest4() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "vertical-flip Koala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);

    assertEquals(
            "Command Executed Successfullyvertical-flip command format "
                    + "is not correct \n"
                    + "Format: vertical-flip image-name dest-image-name",
            out.toString());
  }

  /**
   * Command Test -Apply greyscale to an image. (Wrong component)
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void greyscaleCommandTest1() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "greyscale grey-component Fruits greyFruits";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);

    assertEquals("Command Executed SuccessfullyUnrecognized Component", out.toString());
  }

  /**
   * Command Test -Apply greyscale to a ppm image and save as ppm image.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void greyscale() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "greyscale Fruits greyFruits";
    String input3 = "save src/greyFruits.ppm greyFruits";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();
    Scanner sc1 = new Scanner(new FileInputStream("src/greyFruits.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc1);
    Scanner sc = new Scanner(new FileInputStream("src/greyFruits.ppm"));
    Pixel image2 = imageUtil.ppmRead(sc);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Command Test - Apply greyscale to a ppm image and save it as jpeg.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void greyscaleChange() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "greyscale Fruits greyFruitsjpeg";
    String input3 = "save src/greyFruitsjpeg.jpeg greyFruitsjpeg";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();

    File file = new File("src/greyFruitsjpeg.jpeg");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image1 = imageUtil.readOtherFormatsFile(imageInput);
    File file2 = new File("src/greyscaleFruitsjpeg.jpeg");
    BufferedImage imageInput2 = ImageIO.read(file2);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Command Test - Apply greyscale to a ppm image and save it as png.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void greyscalepng() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "greyscale Fruits greyFruitspng";
    String input3 = "save src/greyFruitspng.png greyFruitspng";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();

    File file = new File("src/greyFruitspng.png");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image1 = imageUtil.readOtherFormatsFile(imageInput);
    File file2 = new File("src/greyscaleFruitspng.png");
    BufferedImage imageInput2 = ImageIO.read(file2);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Command Test - Apply greyscale to a ppm image and save it as bmp.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void greyscalebmp() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "greyscale Fruits greyFruitsbmp";
    String input3 = "save src/greyFruitsbmp.bmp greyFruitsbmp";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();

    File file = new File("src/greyFruitsbmp.bmp");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image1 = imageUtil.readOtherFormatsFile(imageInput);
    File file2 = new File("src/greyscaleFruitsbmp.bmp");
    BufferedImage imageInput2 = ImageIO.read(file2);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Command Test - Apply greyscale to image that doesn't exist.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void greyscaleCommandTest6() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruit.ppm Fruit";
    String input2 = "greyscale Fruit greyFruitsbmp";
    String input3 = "save src/greyFruitsbmp.bmp greyFruitsbmp";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();

    File file = new File("src/greyFruitsbmp.bmp");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image1 = imageUtil.readOtherFormatsFile(imageInput);
    File file2 = new File("src/greyscaleFruitsbmp.bmp");
    BufferedImage imageInput2 = ImageIO.read(file2);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput2);

    assertEquals(
            "File src/fruit.ppm not found!image-name not " + "presentimage-name not present",
            out.toString());
  }

  /**
   * Command Test -rgb-split command on an image.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void rgbSplitCommandTest1() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Koala";
    String input2 = "rgb-split Koala rKoala gKoala bKoala";
    String input3 = "save src/rfruit.ppm rKoala";
    String input4 = "save src/gfruit.ppm gKoala";
    String input5 = "save src/bfruit.ppm bKoala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);
    view.viewOutput(controller.commandExecute(model, input4), out);
    view.viewOutput(controller.commandExecute(model, input5), out);
    assertEquals("", out.toString());
  }

  /**
   * Command Test -rgb-combine command on three images.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void rgbCombineCommandTest1() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Koala";
    String input2 = "rgb-split Koala rKoala gKoala bKoala";
    String input3 = "rgb-combine combinedKoala rKoala gKoala bKoala";
    String input4 = "save src/combinedfruits.ppm combinedKoala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);
    view.viewOutput(controller.commandExecute(model, input4), out);

    assertEquals("", out.toString());
  }

  /**
   * Command Test -rgb-combine - images not present.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void rgbCombineCommanderror() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "rgb-split Koala rKoala gKoala bKoala";
    String input3 = "rgb-combine combinedKoala rKoala kkKoala bKoala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    assertEquals("image-name not present", out.toString());
  }

  /**
   * Command Test - brighten - images .
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void brightenCommand() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm fruits";
    String input2 = "brighten 15 fruits brightfruit";
    String input3 = "save src/brightfruit.ppm brightfruit";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    assertEquals("", out.toString());
  }

  /**
   * Command Test - darken the image.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void darkening() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "brighten -15 Koala darkKoala";
    String input3 = "save src/darkKoala.ppm darkKoala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    assertEquals("", out.toString());
  }

  /**
   * Command Test - brighten - second part of command not integer.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void brightennonint() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "brighten five Koala darkKoala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);

    assertEquals("The second part of the command should be a number", out.toString());
  }

  /**
   * Command Test - brighten - images (value more than 255).
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void brightenover() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "brighten 600 Koala brightKoala";
    String input3 = "save src/brightKoala.ppm brightKoala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    assertEquals("", out.toString());
  }

  /**
   * Command Test - darken the image - < -255.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void brightenunderlim() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/Koala.ppm Koala";
    String input2 = "brighten -260 Koala darkKoala";
    String input3 = "save src/darkKoala.ppm darkKoala";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    assertEquals("", out.toString());
  }

  @Test
  public void runLoadScriptCommandTest() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "run-script src/scr.txt";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    controller.commandExecute(model, input1);

    String output = out.toString().trim();

    assertEquals("Command Executed Successfully", output);
  }

  /**
   * Command Test - run-script where the script is right.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void runScriptCommandTest() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "run-script src/scr.txt";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);

    assertEquals("", out.toString());
  }

  /**
   * Command Test - file not found.
   *
   * @throws IOException - if file not accessible.
   */
  @Test(expected = NoSuchElementException.class)
  public void fileNotFoundCommandTest() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/kk.ppm kk";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    controller.proceed();

    assertEquals("", out.toString());
  }

  /**
   * Command Test - Apply sepia to a ppm image and save it as ppm.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void sepia1() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "sepia Fruits sepiaFruitsppm";
    String input3 = "save src/sepiaFruitsppm.ppm sepiaFruitsppm";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/sepiaFruitsppm.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);

    Scanner sc2 = new Scanner(new FileInputStream("src/PPM/greyFruits.ppm"));
    Pixel image2 = imageUtil.ppmRead(sc2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  @Test
  public void sepiajpeg() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "sepia Fruits sepiaFruitsjpg";
    String input3 = "save src/sepiaFruitsjpg.jpg sepiaFruitsjpg";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();

    File file = new File("src/sepiaFruitsjpeg.jpg");
    BufferedImage imageInput = ImageIO.read(file);
    Pixel image1 = imageUtil.readOtherFormatsFile(imageInput);
    File file2 = new File("src/JPEG/sepiaFruitsjpeg.jpeg");
    BufferedImage imageInput2 = ImageIO.read(file2);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Command Test - Apply blur to a ppm image and save it as ppm.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void blurCommand() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "sepia Fruits blurFruitsppm";
    String input3 = "save src/blurFruitsppm.ppm blurFruitsppm";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/blurFruitsppm.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);

    Scanner sc2 = new Scanner(new FileInputStream("src/PPM/greyFruits.ppm"));
    Pixel image2 = imageUtil.ppmRead(sc2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  @Test
  public void sharpen() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "sepia Fruits sharpenFruitsppm";
    String input3 = "save src/sharpenFruitsppm.ppm sharpenFruitsppm";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();
    Scanner sc = new Scanner(new FileInputStream("src/actualoutput/sharpenFruitsppm.ppm"));
    Pixel image1 = imageUtil.ppmRead(sc);

    Scanner sc2 = new Scanner(new FileInputStream("src/PPM/sharpenFruits.ppm"));
    Pixel image2 = imageUtil.ppmRead(sc2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  /**
   * Command Test - Apply sharpen to a ppm image and save it as bmp.
   *
   * @throws IOException - if file not accessible.
   */
  @Test
  public void sharpenCommandbmp() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();
    String input1 = "load src/fruits.ppm Fruits";
    String input2 = "sharpen Fruits sharpenFruitsbmp";
    String input3 = "save src/sharpenFruitsbmp.bmp sharpenFruitsbmp";

    in = new ByteArrayInputStream(input1.getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);
    view.viewOutput(controller.commandExecute(model, input1), out);
    view.viewOutput(controller.commandExecute(model, input2), out);
    view.viewOutput(controller.commandExecute(model, input3), out);

    ImageUtil imageUtil = new ImageUtil();

    Scanner sc = new Scanner(new FileInputStream("src/actualoutput/sharpenFruitsbmp.bmp"));
    Pixel image1 = imageUtil.ppmRead(sc);

    File file2 = new File("src/BMP/sharpenFruitsbmp.bmp");
    BufferedImage imageInput2 = ImageIO.read(file2);
    Pixel image2 = imageUtil.readOtherFormatsFile(imageInput2);

    assertEquals(
            Arrays.stream(image2.getPixel()).toArray(), Arrays.stream(image1.getPixel()).toArray());
  }

  @Test
  public void compressionTest() throws IOException {
    InputStream in;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/image.png Image";

    String input2 = "compress 10 Image";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);

    controller.commandExecute(model, input2);

    String expectedSuccessMessage = "";
    assertEquals(expectedSuccessMessage, out.toString().trim());
  }

  @Test
  public void compressionInvalidInputTest() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/image.png Image";
    String input2 = "compress 150 Image";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);
    controller.commandExecute(model, input2);
    String expectedErrorMessage = "Invalid input";
    assertEquals(expectedErrorMessage, out.toString().trim());
  }

  @Test
  public void histogramTest() throws IOException {
    InputStream in;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/n3.png Image";
    String input2 = "histogram Image HistogramImage";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);
    controller.commandExecute(model, input2);

    String expectedMessage = "";
    assertEquals(expectedMessage, out.toString().trim());
  }

  @Test
  public void colorCorrectionTest() throws IOException {
    InputStream in;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/n3.png Image";
    String input2 = "color-correction Image ColorCorrImage";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);
    controller.commandExecute(model, input2);

    String expectedSuccessMessage = "";
    assertEquals(expectedSuccessMessage, out.toString().trim());
  }

  @Test
  public void levelAdjustmentTest() throws IOException {
    InputStream in;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/n3.png Image";
    String input2 = "levels-adjust 20 100 250 Image image-level-adjust";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);
    controller.commandExecute(model, input2);

    String expectedSuccessMessage = "";
    assertEquals(expectedSuccessMessage, out.toString().trim());
  }

  @Test
  public void levelAdjustmentOutOfBoundsTest() throws IOException {
    InputStream in = null;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/wee.jpg Image";

    String input2 = "levels-adjust 20 100 260 Image image-level-adjust";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);

    controller.commandExecute(model, input2);

    String expectedErrorMessage = "Out of bounds";
    assertEquals(expectedErrorMessage, out.toString().trim());
  }

  @Test
  public void levelAdjustmentNegativeValueTest() throws IOException {
    InputStream in;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/download.png Image";
    String input2 = "levels-adjust -10 100 200 Image image-level-adjust";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);
    controller.commandExecute(model, input2);

    String expectedErrorMessage = "b,m or w values out of bound.";
    assertEquals(expectedErrorMessage, out.toString().trim());
  }

  @Test
  public void levelAdjustmentMTest() throws IOException {
    InputStream in;
    ImageModel model = new ImageModelImplementation();
    ImageView view = new ImageViewImplementation();

    String input1 = "load src/n3.png Image";
    String input2 = "levels-adjust 50 20 100 Image image-level-adjust";

    in = new ByteArrayInputStream((input1 + "\n" + input2).getBytes());
    OutputStream out = new ByteArrayOutputStream();

    ImageController controller = new ImageControllerImplementation(model, in, out, view);

    controller.commandExecute(model, input1);
    controller.commandExecute(model, input2);

    String expectedErrorMessage = "Invalid input.";
    assertEquals(expectedErrorMessage, out.toString().trim());
  }
}
