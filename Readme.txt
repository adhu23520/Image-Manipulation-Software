Assignment 6

Description

This Image manipulation project takes in multiple image formats as input and perform a wide range of operations on the image based on user input.
The program follows a Model View Controller approach where control is initially given to the controller, which then calls the model based on the requirement and displays contents via view. If the user provides an invalid command, the program will throw an exception and continue to wait for the correct input command. 


Interfaces and Classes


ImageController: This interface includes the commandExecutor and proceed methods. The commandExecutor processes commands, while the procced method initiates the controller's operation.

Command (Interface): This interface defines the methods cmdCheck and cmdExec. cmdCheck validates command formats, while cmdExec handles file operations and command execution.

ImageControllerImplementation: This class implements the ImageController interface. It activates various model functions based on user input. The proceed() method in this class processes inputs until a 'quit' command is received.

UIController - Controller that implements Features and Image Controller interface.

ImageView: This interface declares the viewOutput method's signature.

ImageViewImplementation: This class implements the ImageView interface. It presents output using a PrintStream object named outstream.

UIInterface-Interface that represents View in the form of GUI.

UIInterface Implementation-This class implements the UIInterface.


ImageUtil - This is the helper function where all the implementations of the functions is present. It contains function to -
1)Create a PPM image array format
2) Flip an image horizontally or vertically
3) Convert an image to provided values' greyscale
4) Brighten or darken the image
5) Combining greyscale images to a RGB image
6) Reads a PPM file and saves it to an array
7) Create jpeg, jpg, bmp, png image array format 
8) Applies the color transformation on images - greyscale and sepia
9) Applies filter to the images - blur, sharpen
10) reads the jpeg, jpg, bmp and png file extensions.

ImageModel
Manages image data and processing in the application.
Methods:
check(String component): Determines if a specified image component (e.g., color channel) is recognized.

getRefNames(): Retrieves names of images stored in the model.


loadImage(BufferedImage inputImg, String references): Loads an image from a BufferedImage.

saveImage(String filePath, String referenceName): Saves a PPM image to a specified file path.

saveImage1(String filePath, String referenceName): Saves an image in JPEG/PNG/BMP format.

flipping(String flipOption, String imageName, String newImageName): Flips an image horizontally or vertically.

greyscaleImg(String componentOption, String imageName, String newImageName): Converts an image to grayscale based on a specified component.

brightenImg(int value, String imageName, String newImageName): Adjusts the brightness of an image.

rgbImgsCombine(String rComponent, String gComponent, String bComponent, String newImageName): Combines separate RGB components into one image.

runScript(Scanner sc, ImageModel model, InputStream in, OutputStream out, ImageView view): Executes commands from a script file.

imgblur(String imageName, String newImageName): Applies a blur effect to an image.

imgSharpen(String imageName, String newImageName): Sharpens an image.

newGreyscaleImg(String imageName, String newImageName): Creates a new grayscale image.

imgSepia(String imageName, String newImageName): Applies a sepia tone to an image.

quit(String checkCommand): Determines if the program should quit based on a command.

histogram(String imageName, String destImageName): Generates a histogram of an image.

levelAdj(String imgName, String newImgName, int b, int m, int w, double splitPercent): Adjusts the levels of an image.

imageCorrection(String imgName, String newImgName, double splitPercent): Corrects the image based on specified parameters.



ImageModelImplementation: 
The ImageModelImplementation serves as the main data model for an image processing application. It manages and manipulates image data, providing functionalities like loading, saving, and processing images with various operations such as flipping, greyscaling, adjusting brightness, and applying color filters like sepia or greyscale. This class uses a Map<String, Pixel> to store images with unique names.

Pixel:
The Pixel interface focuses on the representation of an image array. It provides a method, getPixel(), which returns a 2D array of pixel values, encapsulated as a List<Integer>[][]. This interface allows for a more abstract and flexible handling of image data, making it easier to perform operations.


How to Run:
To run the application.
1. Run the Main program from the src folder. 
2. The program will wait for user commands.
3. The user can either type input commands in the IntelliJ terminal or use the load command in the terminal to load the text file that contains the commands.

Operations:
Our application can perform the following operations:
1. Load : The application can read an image file
2. Save : The application can write an image into multiple file format.
3. Flip vertical: The application can flip an image vertically.
4. Flip Horizontal: The application can flip an image horizontally.
5. Gray scale images: The application can generate a gray scale version of the image based on value, intensity or luma component
7. Brighten an image: The application can brighten an image or darken an image.
8. Sepia tone an image: The application can generate sepia tone of an image.
9.  blur: The filter can blur an image.
10. sharpen: The filter can sharpen an image.
11. compress: the application can compress an image.
12. Histogram:This operation can generate an visual representation of the images histogram.
13.Color correction:It can align the peaks of histogram values.
14.Level adjustment:Can change the contrast and brightness of an image.



Changes made to design from assignment 4 to assignment 5: 
Commands design pattern has been changed to reduce lines of code and also improve the readability.
We have used the pixel class as we felt it would make the model more effecient and also make it easier to run operations.

Changes made from assignment 5 to 6:
Added a new UI Controller which extends Features and ImageController interface.
New interface "Features" defines the supported GUI features.
Added a new UIinterface that displays the GUI and lets the user work with it.



Image of the BMW motorsport shoe is photographed and owned by Adhavan Alexander. I authorize the use of this Image for assignment 4,5,6 of Program Design Paradigm.