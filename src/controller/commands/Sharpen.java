package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The Sharpen class represents a command to apply a sharpening filter to an image. It implements
 * the Command interface for sharpening operations.
 */
public class Sharpen implements Command {

  /**
   * Checks the format and validity of the sharpen command.
   *
   * @param command The input command string containing image names for the sharpening operation.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 3) {
      return "sharpen command format is not correct \n"
          + "Format: sharpen image-name dest-image-name";
    } else if (!model.getRefNames().contains(splitCommands[1])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[2])) {
      return "destination image-name " + "already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the sharpen command, applying the sharpening filter to the specified image.
   *
   * @param command The input command string containing image names for the sharpening operation.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the sharpening operation.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length > 3) {
      model.imgSharpen(splitCommands[1], splitCommands[2], Double.valueOf(splitCommands[4]));
    } else {
      model.imgSharpen(splitCommands[1], splitCommands[2], 100);
    }
    return "Command Executed Successfully";
  }
}
