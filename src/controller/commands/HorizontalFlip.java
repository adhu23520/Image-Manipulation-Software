package controller.commands;

import java.io.IOException;

import controller.Command;
import model.ImageModel;

/**
 * The HorizontalFlip class represents a command for flipping images horizontally.
 * It implements the Command interface to execute horizontal flipping operations.
 */
public class HorizontalFlip implements Command {

  /**
   * Checks the format and validity of the horizontal flip command.
   *
   * @param command The input command string.
   * @param model   The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 3) {
      return "horizontal-flip command format is not correct \n"
              + "Format: horizontal-flip image-name " +
              "dest-image-name";
    } else if (!model.getRefNames().contains(splitCommands[1])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[2])) {
      return "destination image-name " +
              "already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the horizontal flip command to flip images horizontally.
   *
   * @param command The input command string containing image details.
   * @param model   The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during horizontal flipping.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    model.flipping("Horizontal", splitCommands[1], splitCommands[2]);
    return "Command Executed Successfully";
  }
}
