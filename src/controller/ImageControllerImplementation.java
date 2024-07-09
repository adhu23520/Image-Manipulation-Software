package controller;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import controller.commands.HorizontalFlip;
import controller.commands.LevelsAdjustment;
import controller.commands.Blur;
import controller.commands.ColorCorrection;
import controller.commands.Compress;
import controller.commands.Histogram;
import controller.commands.RedComp;
import controller.commands.Sepia;
import controller.commands.Sharpen;
import controller.commands.Brighten;
import controller.commands.Greyscale;

import controller.commands.Load;
import controller.commands.RGBCombine;
import controller.commands.RGBSplit;
import controller.commands.RunScript;
import controller.commands.Save;
import controller.commands.VerticalFlip;
import model.ImageModel;
import view.ImageView;

/**
 * The ImageControllerImplementation class implements the ImageController interface
 * and orchestrates the execution of various image processing commands.
 */
public class ImageControllerImplementation implements ImageController {
  private final ImageModel model;
  private final InputStream input;
  private final OutputStream output;
  private final ImageView view;

  private Map<String, Function<String, Command>> commands;


  /**
   * Constructs an ImageControllerImplementation.
   *
   * @param model  The ImageModel to be used for image manipulation.
   * @param input  The InputStream for receiving user commands.
   * @param output The OutputStream for displaying command execution results.
   * @param view   The ImageView to display image-related information.
   */
  public ImageControllerImplementation(ImageModel model,
                                       InputStream input, OutputStream output, ImageView view) {

    this.model = model;
    this.input = input;
    this.output = output;
    this.view = view;
    this.commands = new HashMap<>();
    setCommands();
  }

  /**
   * Sets up the available commands supported by the controller.
   */
  private void setCommands() {
    commands.put("load", s -> new Load());
    commands.put("save", s -> new Save());
    commands.put("vertical-flip", s -> new VerticalFlip());
    commands.put("horizontal-flip", s -> new HorizontalFlip());
    commands.put("red-component", s -> new RedComp());
    commands.put("greyscale", s -> new Greyscale());
    commands.put("rgb-split", s -> new RGBSplit());
    commands.put("brighten", s -> new Brighten());
    commands.put("rgb-combine", s -> new RGBCombine());
    commands.put("run-script", s -> new RunScript(input, output, view));
    commands.put("blur", s -> new Blur());
    commands.put("sharpen", s -> new Sharpen());
    commands.put("sepia", s -> new Sepia());
    commands.put("histogram", s -> new Histogram());
    commands.put("levels-adjust", s -> new LevelsAdjustment());
    commands.put("color-correction", s -> new ColorCorrection());
    commands.put("compress", s -> new Compress());
    commands.put("quit", s -> {
      System.exit(0);
      return null;
    });
  }


  /**
   * Executes the given command on the provided ImageModel.
   *
   * @param model   The ImageModel instance on which the command will be executed.
   * @param command A string representation of the command to be executed.
   * @return A message indicating the result of the command execution.
   * @throws IOException If an I/O error occurs during command execution.
   */
  @Override
  public String commandExecute(ImageModel model, String command) throws IOException {
    if (command.isBlank()) {
      return "";
    }
    String[] commandParts = command.split(" ");
    Function<String, Command> getCommand = this.commands.getOrDefault(commandParts[0],
            null);
    if (getCommand != null) {
      Command commandVal = getCommand.apply(command);
      if (!commandVal.cmdCheck(command, model).equals("Valid Command Format")) {
        return commandVal.cmdCheck(command, model);
      }
      return commandVal.cmdExec(command, model);

    } else {
      return "Invalid Command \nAvailable commands:\nload\n"
              + "save\nvertical-flip\nhorizontal-flip\ngreyscale\nrgb-split\nbrighten\n"
              + "rgb-combine\nrun-script\nblur\nsharpen"
              + "\nsepia\nhistogram\ncolor-correction\ncompress\nlevels-adjust";
    }
  }

  /**
   * Starts the image processing controller,
   * displaying available operations and handling user commands.
   *
   * @throws IOException If an I/O error occurs during the controller's operation.
   */
  @Override
  public void proceed() throws IOException {
    view.viewOutput("Available Operations:\n"
            + "1. Load Image ("
            + "Format: load image-path image-name" + ") \n"
            + "2. Save Image (" + "Format: load image-path image-name"
            + ") \n"
            + "3. Flip Image Vertically ("
            + "Format: vertical-flip image-name "
            + "dest-image-name" + ") \n"
            + "4. Flip Image Horizontally "
            + "(" + "Format: horizontal-flip image-name "
            + "dest-image-name"
            + ") \n"
            + "5. Greyscale Image (Components : Red, Green, Blue, Luma, Intensity) ("
            + "Format: greyscale component-name image-name "
            + "dest-image-name, Format(luma-filter): greyscale image-name dest-image-name" + ") \n"
            + "6. Split images into red, green, blue Greyscale versions ("
            + "Format: rgb-split image-name "
            + "dest-image-name-red dest-image-name-green dest-image-name-blue" + ") \n"
            + "7. Brighten or Darken the Images (" + "Format: brighten increment image-name "
            + "dest-image-name"
            + ")\n"
            + "8. Combine the greyscale red, green and blue images ("
            + "Format: rgb-combine image-name red-image "
            + "green-image blue-image"
            + ") \n"
            + "9. Run the script file with Commands (" + "Format: run-script path" + ")\n"
            + "10. Blur Image (" + "Format: blur image-name dest-image-name" + ")\n"
            + "11. Sharpen Image (" + "Format: sharpen image-name dest-image-name" + ")\n"
            + "12. Sepia Image (" + "Format: sepia image-name dest-image-name" + ")\n"
            + "13. Create histogram of an image ("
            + "Format: histogram image-name "
            + "dest-image-name"
            + ") \n"
            + "14. color-correct of an image ("
            + "Format: color-correct image-name "
            + "dest-image-name"
            + ") \n"
            + "15. Compress image ("
            + "Format: compress threshold-value image-name "
            + "dest-image-name"
            + ") \n"
            + "16. levels-adjust image ("
            + "Format: levels-adjust b m w image-name "
            + "dest-image-name \n"
            + "To Quit the application (Format: quit) \n" + "Enter a command: \n"
            + " \n", output);

    String command;
    Scanner scan = new Scanner(input);
    while (true) {
      command = scan.nextLine();
      if (model.quit(command)) {
        view.viewOutput("Program Ended Successfully", output);
        break;
      }
      view.viewOutput(commandExecute(model, command), this.output);
      if (command.split(" ")[0].equals("run-script")) {
        view.viewOutput("Program Ended Successfully", output);
        break;
      }
      System.out.println();
    }
  }
}