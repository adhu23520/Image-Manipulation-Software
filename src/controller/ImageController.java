package controller;

import java.io.IOException;

import model.ImageModel;

/**
 * The ImageController interface defines methods to execute commands on an ImageModel
 * and proceed with controller operations.
 */
public interface ImageController {

  /**
   * Executes a command on the given ImageModel.
   *
   * @param model   The ImageModel instance on which the command will be executed.
   * @param command A string representation of the command to be executed.
   * @return A message indicating the result of the command execution.
   * @throws IOException If an I/O error occurs during command execution.
   */
  String commandExecute(ImageModel model, String command) throws IOException;

  /**
   * Proceeds with the controller's operations, handling exceptions if needed.
   *
   * @throws IOException If an I/O error occurs during the controller's operation.
   */
  void proceed() throws IOException;
}
