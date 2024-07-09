package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The Blur class represents a command for applying blurring effects to images. It implements the
 * Command interface to execute blur operations on images.
 */
public class Blur implements Command {

  /**
   * Checks the format and validity of the blur command.
   *
   * @param command The input command string.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 3) {
      return "blur command format is not correct \n" + "Format: blur image-name dest-image-name";
    } else if (!model.getRefNames().contains(splitCommands[1])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[2])) {
      return "destination image-name " + "already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the blur command on the specified image.
   *
   * @param command The input command string containing blur parameters.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during image blurring.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {

    String[] splitCommands = command.split(" ");
    if (splitCommands.length > 4) {
      model.imgblur(splitCommands[1], splitCommands[2], Double.valueOf(splitCommands[4]));
    } else {
      model.imgblur(splitCommands[1], splitCommands[2], 100);
    }
    return "Command Executed Successfully";
  }
}
