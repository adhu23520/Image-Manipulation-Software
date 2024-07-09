package controller.commands;


import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The Compress class represents a command for compressing images.
 * It implements the Command interface to execute image compression operations.
 */
public class Compress implements Command {
  /**
   * Checks the format and validity of the compress command.
   *
   * @param command The input command string.
   * @param model   The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length != 4) {
      return "compress command format is not correct \n"
              + "Format: compress percent image-path";
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
   * Executes the compress command to compress an image.
   *
   * @param command The input command string containing compression parameters.
   * @param model   The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during image compression.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    System.out.println("Compressed cmdExc");
    model.compress(splitCommands[2], splitCommands[3], Integer.parseInt( splitCommands[1]));
    return "Command Executed Successfully";
  }
}

