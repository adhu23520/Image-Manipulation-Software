package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The RGBSplit class represents a command to split an image into its RGB components. It implements
 * the Command interface for executing RGB splitting operations.
 */
public class RGBSplit implements Command {

  /**
   * Checks the format and validity of the RGB split command.
   *
   * @param command The input command string.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 5) {
      return "rgb-split command format is not correct \n"
          + "Format: rgb-split image-name "
          + "dest-image-name-red dest-image-name-green dest-image-name-blue";
    } else if (!model.getRefNames().contains(splitCommands[1])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[2])
        || model.getRefNames().contains(splitCommands[3])
        || model.getRefNames().contains(splitCommands[4])) {
      return "destination image-name " + "already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the RGB split command to split the input image into its Red, Green, and Blue
   * components.
   *
   * @param command The input command string containing image details.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the RGB splitting process.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    model.greyscaleImg("Red", splitCommands[1], splitCommands[2]);
    model.greyscaleImg("Green", splitCommands[1], splitCommands[3]);
    model.greyscaleImg("Blue", splitCommands[1], splitCommands[4]);
    return "Command Executed Successfully";
  }
}
