import controller.Command;
import controller.ImageController;
import controller.ImageControllerImplementation;
import controller.UIController;
import controller.commands.RunScript;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.ImageModel;
import model.ImageModelImplementation;
import view.ImageView;
import view.ImageViewImplementation;
import view.UIInterfaceImplementation;

/** The Main class serves as the entry point for the image processing application. */
public class Main {

  /**
   * The main method that initializes the image processing application.
   *
   * @param args Command-line arguments (not used in this application).
   * @throws IOException If an I/O exception occurs while interacting with input/output streams.
   */
  public static void main(String[] args) throws IOException {
    ImageModel model = new ImageModelImplementation();
    if (args.length == 0) {
      UIInterfaceImplementation.setDefaultLookAndFeelDecorated(false);
      UIInterfaceImplementation view = new UIInterfaceImplementation();
      try {
        // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

      } catch (UnsupportedLookAndFeelException e) {
        // handle exception
      } catch (ClassNotFoundException e) {
        // handle exception
      } catch (InstantiationException e) {
        // handle exception
      } catch (IllegalAccessException e) {
        // handle exception
      }
      ImageController controller = new UIController(model, view);
      controller.proceed();
    } else if (args[0].equals("-text")) {
      ImageView view = new ImageViewImplementation();
      ImageController controller =
          new ImageControllerImplementation(model, System.in, System.out, view);
      controller.proceed();
    } else if (args[0].equals("-file")) {
      ImageView view = new ImageViewImplementation();
      Command executeScript = new RunScript(System.in, System.out, view);
      String command = "run-script " + args[1];
      executeScript.cmdExec(command, model);
    } else {
      System.out.println("Invalid Command");
    }
  }
}
