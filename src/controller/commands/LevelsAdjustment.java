package controller.commands;

import controller.Command;
import java.io.IOException;
import model.ImageModel;

/**
 * The LevelAdjust class represents a command for adjusting levels in an image. It implements the
 * Command interface to execute level adjustment operations.
 */
public class LevelsAdjustment implements Command {

  @Override
  public String cmdCheck(String command, ImageModel model) {
    String[] splitCommands = command.split(" ");

    try {
      int b = Integer.parseInt(splitCommands[1]);
      int m = Integer.parseInt(splitCommands[2]);
      int w = Integer.parseInt(splitCommands[3]);

      if (b < 0 || b > 255 || m < 0 || m > 255 || w < 0 || w > 255) {
        return "b, m, or w values out of bound. They should be between 0 and 255.";
      }

      if (!(b < m && m < w)) {
        return "Invalid order of values. Ensure b < m < w.";
      }

    } catch (NumberFormatException e) {
      return "b, m, and w must be integers";
    }

    if (!model.getRefNames().contains(splitCommands[4])) {
      return "image-name not present";
    } else if (model.getRefNames().contains(splitCommands[5])) {
      return "destination image-name already used by another image";
    }
    return "Valid Command Format";
  }

  @Override
  public String cmdExec(String command, ImageModel model) throws IOException {
    String[] splitCommands = command.split(" ");
    if (splitCommands.length > 6) {
      model.levelAdj(
          splitCommands[4],
          splitCommands[5],
          Integer.parseInt(splitCommands[1]),
          Integer.parseInt(splitCommands[2]),
          Integer.parseInt(splitCommands[3]),
          Double.parseDouble(splitCommands[7]));
    } else {
      model.levelAdj(
          splitCommands[4],
          splitCommands[5],
          Integer.parseInt(splitCommands[1]),
          Integer.parseInt(splitCommands[2]),
          Integer.parseInt(splitCommands[3]),
          0.0);
    }
    return "Command Executed Successfully";
  }
}
