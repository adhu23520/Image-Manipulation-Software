package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Compression utility functions.
 */
public class CompressionHelper {
  private final int rows;
  private final int columns;
  private final ArrayList<double[][]> changedImg = new ArrayList<>();
  private double threeshold;

  /**
   * This is a constructor of ImageCompressionFunction class.
   */
  public CompressionHelper(int rows, int columns) {
    this.rows = rows;
    this.columns = columns;
  }

  /**
   * This method is used to transform a 1D array by parsing it by length.
   */
  public static double[] har1DTransformation(double[] sArray) {
    double[] arr = Arrays.copyOf(sArray, sArray.length);
    for (int k = 0; k < sArray.length; k += 2) {
      double x = (sArray[k] + sArray[k + 1]) / Math.sqrt(2);
      double y = (sArray[k] - sArray[k + 1]) / Math.sqrt(2);
      arr[k / 2] = x;
      arr[k / 2 + sArray.length / 2] = y;
    }
    return arr;
  }

  /**
   * This method is used to perform a haar transform on the rows of the array.
   */
  public static double[] transformed(double[] sArray) {
    int n = sArray.length;
    double[] har1DTransformedArray = Arrays.copyOf(sArray, sArray.length);
    while (n > 1) {
      double[] sLeft = har1DTransformation(Arrays.copyOfRange(har1DTransformedArray, 0, n));
      double[] sRight = Arrays.copyOfRange(har1DTransformedArray,
              n, har1DTransformedArray.length);
      System.arraycopy(sLeft, 0, har1DTransformedArray, 0, sLeft.length);
      System.arraycopy(sRight, 0, har1DTransformedArray,
              sLeft.length, sRight.length);
      n = n / 2;
    }
    return har1DTransformedArray;
  }

  /**
   * This method is used to perform an inverse of the haar transform on the rows of the array.
   */
  public static double[] inverseHar1dRowTransformation(double[] sArray) {
    int n = 1;
    double[] har1DInverseTransformedArray = Arrays.copyOf(sArray, sArray.length);
    while (n < sArray.length) {
      double[] leftArray = Arrays.copyOfRange(har1DInverseTransformedArray, 0, n);
      double[] secondHalf = Arrays.copyOfRange(har1DInverseTransformedArray, n, 2 * n);

      for (int k = 0; k < n; k++) {
        double aArray = leftArray[k];
        double dArray = secondHalf[k];
        har1DInverseTransformedArray[k * 2] = (aArray + dArray) / Math.sqrt(2);
        har1DInverseTransformedArray[k * 2 + 1] = (aArray - dArray) / Math.sqrt(2);
      }
      n *= 2;
    }
    return har1DInverseTransformedArray;
  }


  void retrieveThresholdValue(double thresholdPercentage) {
    ArrayList<Double> threshVal = new ArrayList<>();
    for (double[][] transformedImage : changedImg) {
      for (int k = 0; k < transformedImage.length; k++) {
        for (int l = 0; l < transformedImage[k].length; l++) {
          if (!precisionCheck(threshVal, Math.abs(transformedImage[k][l]))
                  && Math.abs(transformedImage[k][l]) >= 0.001) {
            threshVal.add(Math.abs(transformedImage[k][l]));
          }
        }
      }
    }
    Collections.sort(threshVal);
    int thresholdIndex = thresholdPercentage == 100.0 ? threshVal.size() - 1 :
            (int) ((thresholdPercentage / 100) * (threshVal.size()));
    threeshold = threshVal.get(thresholdIndex);
  }

  /**
   * This method checks the precision of the values.
   */
  private boolean precisionCheck(List<Double> list, double value) {
    for (double item : list) {
      if (Math.abs(item - value) < 0.0000001) {
        return true;
      }
    }
    return false;
  }


  /**
   * Send har 2D transformed image.
   *
   * @param originalImage original name.
   * @return 2d double array of har 2D transformed image.
   */
  public static double[][] har2DTransformation(double[][] originalImage) {
    int width = originalImage.length;
    int height = originalImage[0].length;
    double[][] transformed1 = new double[width][height];
    for (int k = 0; k < width; k++) {
      transformed1[k] = transformed(originalImage[k]);
    }

    for (int k = 0; k < height; k++) {
      double[] col = new double[width];
      for (int l = 0; l < width; l++) {
        col[l] = transformed1[l][k];
      }
      col = transformed(col);
      for (int m = 0; m < width; m++) {
        transformed1[m][k] = col[m];
      }
    }

    return transformed1;
  }

  /**
   * This method is used to perform an inverse of haar transform on a 2D image.
   */
  public static double[][] inversehar2dTransformation(double[][] originalImage) {
    int width = originalImage.length;
    int height = originalImage[0].length;

    double[][] har2DInverseTransformedArray = new double[width][height];
    for (int k = 0; k < height; k++) {
      double[] col = new double[width];
      for (int l = 0; l < width; l++) {
        col[l] = originalImage[l][k];
      }
      col = inverseHar1dRowTransformation(col);
      for (int m = 0; m < width; m++) {
        har2DInverseTransformedArray[m][k] = col[m];
      }
    }
    for (int o = 0; o < width; o++) {
      har2DInverseTransformedArray[o] =
              inverseHar1dRowTransformation(har2DInverseTransformedArray[o]);
    }
    return har2DInverseTransformedArray;
  }


  /**
   * Sets image pixel values to 0 if they fall below the threshold after transformation.
   */
  void afterThresh() {
    for (double[][] trnsfrmImg : changedImg) {
      for (int k = 0; k < trnsfrmImg.length; k++) {
        for (int l = 0; l < trnsfrmImg[k].length; l++) {
          if (Math.abs(trnsfrmImg[k][l]) <= threeshold) {
            trnsfrmImg[k][l] = 0.0;
          }
        }
      }
    }
  }

  /**
   * Double array to RGB.
   *
   * @param doubleArr double array.
   * @param chan      channel R/G/B.
   * @return RGB pixels.
   */
  public Pixel convertDoubleArrayToRGB(double[][] doubleArr, String chan) {
    List<Integer> rgb = null;
    int width = doubleArr.length;
    int height = doubleArr[0].length;
    List<Integer>[][] img = new List[width][height];

    for (int k = 0; k < width; k++) {
      for (int l = 0; l < height; l++) {
        List<Integer> addd = new ArrayList<>();

        if (Objects.equals(chan, "red")) {
          addd = Arrays.asList((int) Math.round(doubleArr[k][l]), 0, 0);
        } else if (Objects.equals(chan, "green")) {
          addd = Arrays.asList(0, (int) Math.round(doubleArr[k][l]), 0);
        } else if (Objects.equals(chan, "blue")) {
          addd = Arrays.asList(0, 0, (int) Math.round(doubleArr[k][l]));
        }
        img[k][l] = addd;
      }
    }
    return new PixelImplementation(img);
  }

  /**
   * Unpad the inverse.
   *
   * @return returns array of RGB.
   */
  ArrayList<Pixel> unpadInverse() {
    ArrayList<Pixel> imgInverseUnpad = new ArrayList<>();
    ArrayList<double[][]> doubArrInvUnpassed = new ArrayList<>();
    for (double[][] transformedImage : changedImg) {
      double[][] invImg = inversehar2dTransformation(transformedImage);
      double[][] unpadImg = getUnpaddedImage(invImg, rows, columns);
      doubArrInvUnpassed.add(unpadImg);
    }
    imgInverseUnpad.add(convertDoubleArrayToRGB(doubArrInvUnpassed.get(0),
            "red"));
    imgInverseUnpad.add(convertDoubleArrayToRGB(doubArrInvUnpassed.get(1),
            "green"));
    imgInverseUnpad.add(convertDoubleArrayToRGB(doubArrInvUnpassed.get(2),
            "blue"));
    return imgInverseUnpad;
  }


  /**
   * Extracts unpadded image from the given padded image.
   *
   * @param ogImg  the padded image as a 2D array
   * @param ogRows the number of rows in original unpadded image
   * @param ogCols the number of columns in original unpadded image
   * @return a 2D array containing the unpadded image of original dimensions
   *          extracted from the center of the padded image
   */
  public double[][] getUnpaddedImage(double[][] ogImg,
                                     int ogRows, int ogCols) {
    double[][] unpaddedImg = new double[ogRows][ogCols];
    int rows = ogImg.length;
    int columns = ogImg[0].length;

    for (int k = 0; k < ogRows; k++) {
      for (int l = 0; l < ogCols; l++) {
        if (k < rows && l < columns) {
          unpaddedImg[k][l] = ogImg[k][l];
        }
      }
    }
    return unpaddedImg;
  }


  /**
   * Compress the image using threshold.
   *
   * @param originalImage RGB Pixels.
   */

  public void compThreshold(Pixel originalImage) {
    double[][] doubleArrayedImg = rgbToDoubleArr(originalImage);
    double[][] paddedImg = getPaddedImage(doubleArrayedImg);
    double[][] tranformedImg = har2DTransformation(paddedImg);
    changedImg.add(tranformedImg);
  }

  /**
   * Padding the image using zeros.
   *
   * @param originalImage original Image to be paded.
   * @return return double array.
   */

  public double[][] getPaddedImage(double[][] originalImage) {
    int width = originalImage.length;
    int height = originalImage[0].length;
    int dim = Math.max(width, height);
    int dimToPad = 1;
    while (dimToPad < dim) {
      dimToPad *= 2;
    }
    double[][] paddedImg = new double[dimToPad][dimToPad];

    for (int k = 0; k < dimToPad; k++) {
      for (int l = 0; l < dimToPad; l++) {
        if (k < width && l < height) {
          paddedImg[k][l] = originalImage[k][l];
        } else {
          paddedImg[k][l] = 0;
        }
      }
    }
    return paddedImg;
  }

  /**
   * convert RGB To double array.
   *
   * @param ogImg source image.
   * @return double array.
   */

  public double[][] rgbToDoubleArr(Pixel ogImg) {
    int rows = ogImg.getPixel()[0].length;
    int columns = ogImg.getPixel().length;
    double[][] doubleArred = new double[rows][columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        doubleArred[i][j] = (ogImg.getPixel()[i][j].get(0) +
                ogImg.getPixel()[i][j].get(1) + ogImg.getPixel()[i][j].get(2));
      }
    }
    return doubleArred;
  }
}