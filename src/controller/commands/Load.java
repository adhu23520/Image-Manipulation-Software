package controller.commands;

import controller.Command;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.imageio.ImageIO;
import model.ImageModel;

/**
 * The Load class represents a command to load images. It implements the Command interface for
 * executing load operations.
 */
public class Load implements Command {
  /**
   * Checks the format and validity of the load command.
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
    if (splitCommands.length != 3) {
      return "load command format is not correct \n " + "Format: load image-path image-name";
    } else if (!(format.equals("jpeg")
        || format.equals("png")
        || format.equals("ppm")
        || format.equals("jpg")
        || format.equals("bmp"))) {
      return "Incorrect File Extension";
    } else if (model.getRefNames().contains(splitCommands[2])) {
      return "image-name already used by another image";
    }
    return "Valid Command Format";
  }

  /**
   * Executes the load command to load an image.
   *
   * @param command The input command string containing image details.
   * @param model The ImageModel used for image operations.
   * @return A message indicating the successful execution of the command.
   * @throws IOException If an I/O error occurs during the image loading process.
   */
  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    Scanner scanner = null;
    BufferedImage image = null;
    Path path = Paths.get(splitCommands[1]);
    String fileName = path.getFileName().toString();
    int dot = fileName.lastIndexOf('.');
    String format = fileName.substring(dot + 1);

    try {
      if (format.equals("ppm")) {
        scanner = new Scanner(new FileInputStream(splitCommands[1]));
      } else if (format.equals("jpeg")
          || format.equals("png")
          || format.equals("jpg")
          || format.equals("bmp")) {
        File file = new File(splitCommands[1]);
        image = ImageIO.read(file);
      }

    } catch (FileNotFoundException e) {
      return "File " + splitCommands[1] + " not found!";
    }
    if (format.equals("ppm")) {
      boolean success = model.loadImage(scanner, splitCommands[2]);
      if (!success) {
        return "Invalid PPM file: plain RAW file should " + "begin with P3";
      }
    } else {
      model.loadImage(image, splitCommands[2]);
    }

    return "Command Executed Successfully";
  }
}
