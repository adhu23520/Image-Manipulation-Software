package model;

import java.util.List;

/**
 * An interface representing a Pixel object, defining the method to retrieve a two-dimensional array
 * of lists containing pixel values.
 */
public interface Pixel {

  /**
   * Retrieves the image stored as a two-dimensional array of lists.
   *
   * @return A two-dimensional array of lists containing image values.
   */
  List<Integer>[][] getPixel();
}
