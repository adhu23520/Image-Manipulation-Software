package controller;

import java.io.IOException;

/** This interface is for the Features that the GUI can support. */
public interface Features {

  /**
   * Loads an image.
   *
   * @throws IOException throws if file not found
   */
  void loadImage() throws IOException;

  /**
   * Saves the image.
   *
   * @throws IOException throws if file not found
   */
  void saveImage() throws IOException;

  /**
   * Blurs an image.
   *
   * @throws IOException throws if file not found
   */
  void blurImage(double threshold) throws IOException;

  /**
   * Vertical flips and image.
   *
   * @throws IOException throws if file not found
   */
  void verticalFlip() throws IOException;

  /**
   * Converts an image to greyscale.
   *
   * @throws IOException throws if file not found
   */
  void greyscaleImage() throws IOException;

  void compressImage(int split) throws IOException;

  /**
   * Puts a sepia filter on the image.
   *
   * @throws IOException throws if file not found
   */
  void sepia(double threshold) throws IOException;

  void colorCorrect() throws IOException;

  /**
   * Level adjusts an image.
   *
   * @throws IOException throws if file not found
   */
  void levelsAdjustment(int b, int m, int w, double split) throws IOException;

  /**
   * Sharpens an image.
   *
   * @throws IOException throws if file not found
   */
  void sharpen() throws IOException;

  /**
   * Flips an image horizontally.
   *
   * @throws IOException throws if file not found
   */
  void horizontalFlip() throws IOException;

  /**
   * Brightens an image.
   *
   * @throws IOException throws if file not found
   */
  void brightenImage() throws IOException;

  /**
   * Quits the program.
   *
   * @throws IOException throws if file not found
   */
  void quit() throws IOException;

  /**
   * To highlight the red component in an image.
   *
   * @throws IOException For error handling.
   */
  void redComp() throws IOException;

  /**
   * To highlight the green component in an image.
   *
   * @throws IOException For error handling.
   */
  void greenComp() throws IOException;

  /**
   * * To highlight the blur component in an image.
   *
   * @throws IOException For error handling.
   */
  void blueComp() throws IOException;
}
