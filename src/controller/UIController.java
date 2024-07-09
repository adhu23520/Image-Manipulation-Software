package controller;

import controller.commands.BlueComp;
import controller.commands.Blur;
import controller.commands.Brighten;
import controller.commands.ColorCorrection;
import controller.commands.Compress;
import controller.commands.GreenComp;
import controller.commands.Greyscale;
import controller.commands.HorizontalFlip;
import controller.commands.LevelsAdjustment;
import controller.commands.Load;
import controller.commands.RedComp;
import controller.commands.Save;
import controller.commands.Sepia;
import controller.commands.Sharpen;
import controller.commands.VerticalFlip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import model.ImageModel;
import view.UIInterface;

/** Controller for the GUI that extends features and ImageController interface. */
public class UIController implements Features, ImageController {

  private ImageModel model;
  private UIInterface view;
  private String loadedImage;

  /**
   * Constructor that sets the model and view.
   *
   * @param model Model created
   * @param view View created
   * @throws IOException throws if file not found
   */
  public UIController(ImageModel model, UIInterface view) throws IOException {
    this.model = model;
    this.view = view;
    this.loadedImage = "image";
  }

  /**
   * Helper function to add features in the View class.
   *
   * @throws IOException throws if file not found
   */
  private void setViewFeatures() throws IOException {
    this.view.addFeats(this);
  }

  /**
   * Helper function to extracting required file path.
   *
   * @param filePath Input filepath
   * @return Extracted filepath
   */
  private String filterImagePath(String filePath) {
    if (filePath == null || filePath.isBlank()) {
      return "";
    }
    String[] currentDirectoryPath = System.getProperty("user.dir").split("/");
    String currentDirectory = currentDirectoryPath[currentDirectoryPath.length - 1];
    return filePath.substring(
        filePath.lastIndexOf(currentDirectory) + currentDirectory.length() + 1);
  }

  /**
   * Sets the image in the GIU.
   *
   * @throws IOException throws if file not found
   */
  private void setLoadedImage() throws IOException {
    BufferedImage image = model.viewImage(loadedImage);
    view.imgSet(image);
  }

  //  @Override
  //  public void goAhead() throws IOException {
  //    setViewFeatures();
  //  }

  @Override
  public void loadImage() throws IOException {
    if (model.checkImageLoaded()) {
      view.warnNewLoadSave();
    }
    String filePath = view.fetchFilePath("Open File");
    if (Objects.equals(filePath, "")) {
      return;
    } else {
      filePath = filterImagePath(filePath);
    }
    if (filePath.isBlank()) {
      return;
    }
    int dotIndex = filePath.lastIndexOf('.');
    String extension = filePath.substring(dotIndex + 1);
    if (!(extension.equals("ppm")
        || extension.equals("png")
        || extension.equals("bmp")
        || extension.equals("jpeg")
        || extension.equals("jpg"))) {
      view.errInvalidExt();
      return;
    }
    String command = "load " + filePath + " " + loadedImage;
    Command cmd = new Load();
    String status = cmd.cmdExec(command, model);
    if (status.equals("Command Executed Successfully")) {
      setLoadedImage();
    }
  }

  @Override
  public void saveImage() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    String filePath = filterImagePath(view.fetchFilePath("Save File"));
    String command = "save " + filePath + " " + loadedImage;
    Command cmd = new Save();
    cmd.cmdExec(command, model);
  }

  @Override
  public void blurImage(double threshold) throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new Blur();
    String command = "blur " + loadedImage + " " + loadedImage + " split " + threshold;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void verticalFlip() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new VerticalFlip();
    String command = "vertical-flip " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void greyscaleImage() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new Greyscale();
    String command = "greyscale " + "luma-component " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void compressImage(int splitPercentage) throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new Compress();
    String command = "compress " + splitPercentage + " " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void sepia(double threshold) throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new Sepia();
    String command = "sepia " + loadedImage + " " + loadedImage + " split " + threshold;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void colorCorrect() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new ColorCorrection();
    String command = "color-correction " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void levelsAdjustment(int b, int m, int w, double splitPercentage) throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new LevelsAdjustment();
    String command =
        "levels-adjust "
            + b
            + " "
            + m
            + " "
            + w
            + " "
            + loadedImage
            + " "
            + loadedImage
            + " split "
            + splitPercentage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void sharpen() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new Sharpen();
    String command = "sharpen " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void horizontalFlip() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new HorizontalFlip();
    String command = "horizontal-flip " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void redComp() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new RedComp();
    String command = "red-component " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void greenComp() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new GreenComp();
    String command = "green-component " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void blueComp() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Command cmd = new BlueComp();
    String command = "blue-component " + loadedImage + " " + loadedImage;
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void brightenImage() throws IOException {
    if (!model.checkImageLoaded()) {
      view.errNoImg();
      return;
    }
    Integer brightenValue = view.inputBrighten();
    if (brightenValue == 0) {
      return;
    }
    String command = "brighten " + brightenValue + " " + loadedImage + " " + loadedImage;
    Command cmd = new Brighten();
    cmd.cmdExec(command, model);
    setLoadedImage();
  }

  @Override
  public void quit() throws IOException {
    if (view.quitGUI()) {
      System.exit(0);
    }
  }

  @Override
  public String commandExecute(ImageModel model, String command) throws IOException {
    return null;
  }

  @Override
  public void proceed() throws IOException {
    setViewFeatures();
  }
}
