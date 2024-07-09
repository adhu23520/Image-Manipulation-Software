package view;

import java.awt.image.BufferedImage;
import java.io.IOException;

import controller.Features;

/**
 * View of Graphical User Interface.
 */
public interface UIInterface {

  /**
   * Gets the filepath for loading or saving.
   *
   * @param option Open or Save
   * @return String filepath
   */
  String fetchFilePath(String option);

  /**
   * Sets the image in the GUI.
   *
   * @param image Loaded image.
   * @throws IOException throws if file not found
   */
  void imgSet(BufferedImage image) throws IOException;

  /**
   * Gets the brighten value through the slider.
   *
   * @return Brighten value
   */
  Integer inputBrighten();

  /**
   * Error thrown when the user tries to save without loading any image.
   */
  void errNoImg();

  /**
   * Warning displayed when the user tries to load a new image when already an image is in the GUI.
   */
  void warnNewLoadSave();

  /**
   * Error thrown when file extension is invalid.
   */
  void errInvalidExt();

  /**
   * Pop-up asking whether the user wants to quit the program.
   *
   * @return True or False
   */
  boolean quitGUI();

  /**
   * Adds features to the GUI.
   *
   * @param features New feature
   * @throws IOException throws if file not found
   */
  void addFeats(Features features) throws IOException;
}
