package model;

import java.util.List;

/**
 * Implementation of the Pixel interface representing an image's pixel data.
 */
public class PixelImplementation implements Pixel {

  private final List<Integer>[][] pixel;

  /**
   * Constructs a PixelImplementation object with a two-dimensional array
   * of lists containing image pixel values.
   *
   * @param imageArray A two-dimensional array of lists containing pixel values.
   */
  public PixelImplementation(List<Integer>[][] imageArray) {
    this.pixel = imageArray;
  }

  /**
   * Retrieves the pixel values stored in a two-dimensional array of lists.
   *
   * @return A two-dimensional array of lists containing pixel values.
   */
  @Override
  public List<Integer>[][] getPixel() {
    return this.pixel;
  }
}
