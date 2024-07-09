package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The Greyscale class represents a command for applying greyscale filters to images. It implements
 * the Command interface to execute greyscale image operations.
 */
public class Greyscale implements Command {

  /**
   * Checks the format and validity of the greyscale command.
   *
   * @param command The input command string.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    if (!(splitCommands.length == 3 || splitCommands.length == 4)) {
      return "greyscale command format is not correct \n"
          + "Format: greyscale component-name image-name "
          + "dest-image-name, Format(luma-filter): greyscale image-name dest-image-name";
    }
    if (splitCommands.length == 4) {
      if (!model.getRefNames().contains(splitCommands[2])) {
        return "image-name not present";
      } else if (model.getRefNames().contains(splitCommands[3])) {
        return "destination image-name " + "already used by another image";
      } else if (model.check(splitCommands[1]).equals("Unrecognized Component")) {
        return model.check(splitCommands[1]);
      }
    } else {
      if (!model.getRefNames().contains(splitCommands[1])) {
        return "image-name not present";
      } else if (model.getRefNames().contains(splitCommands[2])) {
        return "destination image-name " + "already used by another image";
      }
    }

    return "Valid Command Format";
  }

  /**
   * Executes the greyscale command to apply greyscale filters to an image.
   *
   * @param command The input command string containing greyscale filter parameters.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during greyscale image processing.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length == 4) {
      model.greyscaleImg(model.check(splitCommands[1]), splitCommands[2], splitCommands[3]);
    } else {
      model.newGreyscaleImg(splitCommands[1], splitCommands[2]);
    }
    return "Command Executed Successfully";
  }
}
