package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The ColorCorrection class represents a command for correcting the color of an image. It
 * implements the Command interface to execute color correction operations on images.
 */
public class ColorCorrection implements Command {

  /**
   * Checks the format and validity of the color correction command.
   *
   * @param command The input command string.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");

    if (!model.getRefNames().contains(splitCommands[1])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[2])) {
      return "destination image-name " + "already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the color correction command to correct the color of an image.
   *
   * @param command The input command string containing color correction parameters.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during color correction.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    model.imageCorrection(splitCommands[1], splitCommands[2], 100);

    return "Command Executed Successfully";
  }
}
