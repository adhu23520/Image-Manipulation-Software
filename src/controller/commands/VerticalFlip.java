package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The VerticalFlip class represents a command to perform a vertical flip on an image. It implements
 * the Command interface for flipping operations.
 */
public class VerticalFlip implements Command {

  /**
   * Checks the format and validity of the vertical flip command.
   *
   * @param command The input command string containing image names for the vertical flip operation.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 3) {
      return "vertical-flip command format is not correct \n"
          + "Format: vertical-flip image-name "
          + "dest-image-name";
    } else if (!model.getRefNames().contains(splitCommands[1])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[2])) {
      return "destination image-name " + "already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the vertical flip command, flipping the specified image vertically.
   *
   * @param command The input command string containing image names for the vertical flip operation.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the vertical flip operation.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    model.flipping("Vertical", splitCommands[1], splitCommands[2]);
    return "Command Executed Successfully";
  }
}
