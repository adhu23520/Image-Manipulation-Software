package view;

import java.io.OutputStream;
import java.io.PrintStream;

/** Implementation of the ImageView interface for displaying image output. */
public class ImageViewImplementation implements ImageView {

  /**
   * Displays the image output to the specified OutputStream.
   *
   * @param output The image output data to be displayed.
   * @param out The OutputStream to which the image output will be sent.
   */
  @Override
  public void viewOutput(String output, OutputStream out) {
    PrintStream outStream = new PrintStream(out);
    outStream.printf(output);
    System.out.println();
  }
}
