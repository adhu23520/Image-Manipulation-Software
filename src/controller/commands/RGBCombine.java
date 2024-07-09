package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The RGBCombine class represents a command to combine multiple images into an RGB image. It
 * implements the Command interface for executing RGB combining operations.
 */
public class RGBCombine implements Command {

  /**
   * Checks the format and validity of the RGB combine command.
   *
   * @param command The input command string.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {

    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 5) {
      return "rgb-combine command format is not correct \n"
          + "Format: rgb-combine image-name red-image "
          + "green-image blue-image";
    } else if (model.getRefNames().contains(splitCommands[1])) {
      return "destination image-name " + "already used by another image";
    } else if ((!model.getRefNames().contains(splitCommands[2]))
        || (!model.getRefNames().contains(splitCommands[3]))
        || (!model.getRefNames().contains(splitCommands[4]))) {
      return "image-name not present";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the RGB combine command to create an RGB image from multiple input images.
   *
   * @param command The input command string containing image details.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the RGB combining process.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    model.rgbImgsCombine(splitCommands[2], splitCommands[3], splitCommands[4], splitCommands[1]);
    return "Command Executed Successfully";
  }
}
