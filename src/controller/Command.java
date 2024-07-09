package controller;

import java.io.IOException;
import model.ImageModel;

/**
 * The Command interface represents an executable command that operates on an ImageModel. It
 * provides methods to check the command format and execute the command.
 */
public interface Command {

  /**
   * Checks the format and validity of a command given a string representation of the command and an
   * ImageModel.
   *
   * @param command The input command string to be checked.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format or validity.
   */
  String cmdCheck(String command, ImageModel model);

  /**
   * Executes a command given a string representation of the command and an ImageModel.
   *
   * @param command The input command string to be executed.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during command execution.
   */
  String cmdExec(String command, ImageModel model) throws IOException;
}
