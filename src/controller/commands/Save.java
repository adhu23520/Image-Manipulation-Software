package controller.commands;

import controller.Command;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.ImageModel;

/**
 * The {@code Save} class represents a command to save an image to a specified file path. It
 * implements the Command interface for image saving operations.
 */
public class Save implements Command {

  /**
   * Checks the format and validity of the save command.
   *
   * @param command The input command string containing image path and name.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    int dot = splitCommands[1].lastIndexOf('.');
    String format = splitCommands[1].substring(dot + 1);
    if (splitCommands.length != 3) {
      return "save command format is not correct \n" + "Format: save image-path image-name";
    } else if (!(format.equals("jpeg")
        || format.equals("png")
        || format.equals("ppm")
        || format.equals("jpg")
        || format.equals("bmp"))) {
      return "Incorrect File Extension";
    } else if (!model.getRefNames().contains(splitCommands[2])) {
      return "image-name not present";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the save command, saving the image to the specified file path.
   *
   * @param command The input command string containing image path and name.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the saving process.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    int dot = splitCommands[1].lastIndexOf('.');
    String format = splitCommands[1].substring(dot + 1);
    if (format.equals("ppm")) {
      try {
        StringBuilder ppmFormat = model.saveImage(splitCommands[1], splitCommands[2]);
        FileOutputStream fos = new FileOutputStream(splitCommands[1]);
        fos.write(new String(ppmFormat).getBytes());
        fos.close();
      } catch (FileNotFoundException e) {
        return "Incorrect File Path";
      }
    } else {
      BufferedImage image = model.saveImage1(splitCommands[1], splitCommands[2]);
      try {
        File output = new File(splitCommands[1]);
        FileOutputStream fos = new FileOutputStream(splitCommands[1]);
        ImageIO.write(image, format.toUpperCase(), output);
      } catch (FileNotFoundException e) {
        return "Incorrect File Path";
      }
    }
    return "Command Executed Successfully";
  }
}
