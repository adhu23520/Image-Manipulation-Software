package controller.commands;

import controller.Command;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import model.ImageModel;
import view.ImageView;

/**
 * The {@code RunScript} class represents a command to execute a script file. It implements the
 * Command interface for script execution operations.
 */
public class RunScript implements Command {
  private InputStream in;
  private OutputStream out;
  private ImageView view;

  /**
   * Initializes a RunScript object with input and output streams along with an image view.
   *
   * @param in The InputStream for reading input data.
   * @param out The OutputStream for writing output data.
   * @param view The ImageView for displaying images.
   */
  public RunScript(InputStream in, OutputStream out, ImageView view) {
    this.in = in;
    this.out = out;
    this.view = view;
  }

  /**
   * Checks the format and validity of the run-script command.
   *
   * @param command The input command string.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the status of the command format.
   */
  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");
    int dot = splitCommands[1].lastIndexOf('.');
    String format = splitCommands[1].substring(dot + 1);
    if (splitCommands.length != 2) {
      return "run-script command format is not correct \n" + "Format: run-script path";
    } else if (!format.equals("txt")) {
      return "Incorrect File Extension";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the run-script command, reading commands from a script file and running them.
   *
   * @param command The input command string containing the script file path.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the script execution process.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    Scanner scanner;
    String[] splitCommands = command.split(" ");
    try {
      scanner = new Scanner(new FileInputStream(splitCommands[1]));
    } catch (FileNotFoundException e) {
      return "File " + splitCommands[1] + " not found!";
    }
    model.runScript(scanner, model, this.in, this.out, this.view);
    return "Command Executed Successfully";
  }
}
