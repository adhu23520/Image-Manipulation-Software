package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The Sepia class represents a command to apply sepia filter to an image. It implements the Command
 * interface for sepia filter operations.
 */
public class Sepia implements Command {

  /**
   * Checks the format and validity of the sepia command.
   *
   * @param command The input command string containing image names for the sepia operation.
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
   * Executes the sepia command, applying the sepia filter to the specified image.
   *
   * @param command The input command string containing image names for the sepia operation.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the sepia filter application.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length > 3) {
      model.imgSepia(splitCommands[1], splitCommands[2], Double.valueOf(splitCommands[4]));
    } else {
      model.imgSepia(splitCommands[1], splitCommands[2], 100);
    }

    return "Command Executed Successfully";
  }
}
