package controller.commands;

import java.io.IOException;

import controller.Command;
import model.ImageModel;

/**
 * The Brighten class represents a command for adjusting the brightness of an image.
 * It implements the Command interface to execute brightness adjustments on images.
 */
public class Brighten implements Command {

  /**
   * Checks the format and validity of the brighten command.
   *
   * @param command The input command string.
   * @param model   The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {

    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 4) {
      return "brighten command format is not correct \n"
              + "Format: brighten increment image-name "
              + "dest-image-name";
    }
    try {
      Integer.parseInt(splitCommands[1]);
    } catch (NumberFormatException e) {
      return "The second part of the command should be a number";
    }
    if (!model.getRefNames().contains(splitCommands[2])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[3])) {
      return "destination image-name "
              + "already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the brighten command to adjust the brightness of an image.
   *
   * @param command The input command string containing brightness adjustment parameters.
   * @param model   The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during brightness adjustment.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    model.brightenImg(Integer.parseInt(splitCommands[1]), splitCommands[2], splitCommands[3]);
    return "Command Executed Successfully";
  }
}
