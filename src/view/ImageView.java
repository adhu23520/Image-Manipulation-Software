package view;

import java.io.OutputStream;

/** Interface representing an image viewer functionality. */
public interface ImageView {

  /**
   * Displays the image output to the specified OutputStream.
   *
   * @param output The image output data to be displayed.
   * @param out The OutputStream to which the image output will be sent.
   */
  void viewOutput(String output, OutputStream out);
}
