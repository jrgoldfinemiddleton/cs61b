/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes
 *  a PixImage object.  Descriptions of the methods you must implement appear
 *  below.  They include constructors of the form
 *
 *      public RunLengthEncoding(int width, int height);
 *      public RunLengthEncoding(int width, int height, int[] red, int[] green,
 *                               int[] blue, int[] runLengths) {
 *      public RunLengthEncoding(PixImage image) {
 *
 *  that create a run-length encoding of a PixImage having the specified width
 *  and height.
 *
 *  The first constructor creates a run-length encoding of a PixImage in which
 *  every pixel is black.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts a PixImage object into a run-length encoding of that image.
 *
 *  See the README file accompanying this project for additional details.
 */
import java.util.Iterator;

@SuppressWarnings({ "rawtypes", "unused" })
public class RunLengthEncoding implements Iterable {

  /**
   *  Define any variables associated with a RunLengthEncoding object here.
   *  These variables MUST be private.
   */
  private DList runs;
  private int width, height;



  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with two parameters) constructs a run-length
   *  encoding of a black PixImage of the specified width and height, in which
   *  every pixel has red, green, and blue intensities of zero.
   *
   *  @param width the width of the image.
   *  @param height the height of the image.
   */

  public RunLengthEncoding(int width, int height) {
    this(width, height, new int[]{0}, new int[]{0}, new int[]{0},
        new int[]{width * height});
  }

  /**
   *  RunLengthEncoding() (with six parameters) constructs a run-length
   *  encoding of a PixImage of the specified width and height.  The runs of
   *  the run-length encoding are taken from four input arrays of equal length.
   *  Run i has length runLengths[i] and RGB intensities red[i], green[i], and
   *  blue[i].
   *
   *  @param width the width of the image.
   *  @param height the height of the image.
   *  @param red is an array that specifies the red intensity of each run.
   *  @param green is an array that specifies the green intensity of each run.
   *  @param blue is an array that specifies the blue intensity of each run.
   *  @param runLengths is an array that specifies the length of each run.
   *
   *  NOTE:  All four input arrays should have the same length (not zero).
   *  All pixel intensities in the first three arrays should be in the range
   *  0...255.  The sum of all the elements of the runLengths array should be
   *  width * height.  (Feel free to quit with an error message if any of these
   *  conditions are not met--though we won't be testing that.)
   */

  public RunLengthEncoding(int width, int height, int[] red, int[] green,
                           int[] blue, int[] runLengths) {
    this.width = width;
    this.height = height;
    this.runs = new DList();
    int i = 0;
    while (i < runLengths.length) {
      int[] run = {runLengths[i], red[i], green[i], blue[i]};
      runs.insertBack(run);
      i++;
    }
  }

  /**
   *  getWidth() returns the width of the image that this run-length encoding
   *  represents.
   *
   *  @return the width of the image that this run-length encoding represents.
   */

  public int getWidth() {
    return width;
  }

  /**
   *  getHeight() returns the height of the image that this run-length encoding
   *  represents.
   *
   *  @return the height of the image that this run-length encoding represents.
   */
  public int getHeight() {
    return height;
  }

  /**
   *  iterator() returns a newly created RunIterator that can iterate through
   *  the runs of this RunLengthEncoding.
   *
   *  @return a newly created RunIterator object set to the first run of this
   *  RunLengthEncoding.
   */
  @Override
  public RunIterator iterator() {
    return new RunIterator(runs.head.next);
  }

  /**
   *  toPixImage() converts a run-length encoding of an image into a PixImage
   *  object.
   *
   *  @return the PixImage that this RunLengthEncoding encodes.
   */
  public PixImage toPixImage() {
    PixImage image = new PixImage(width, height);
    RunIterator it = iterator();
    int x = 0, y = 0;
    while (it.hasNext()) {
      int[] curRun = it.next();
      int runLength = curRun[0];
      int r = curRun[1];
      int g = curRun[2];
      int b = curRun[3];
      int posInRun = 1;
      while (posInRun <= runLength) {
        if (x >= width) {
          x = 0;
          y++;
        }
        image.setPixel(x, y, (short)r, (short)g, (short)b);
        x++;
        posInRun++;
      }
    }
    return image;
  }

  /**
   *  toString() returns a String representation of this RunLengthEncoding.
   *
   *  This method isn't required, but it should be very useful to you when
   *  you're debugging your code.  It's up to you how you represent
   *  a RunLengthEncoding as a String.
   *
   *  @return a String representation of this RunLengthEncoding.
   */
  @Override
  public String toString() {
    String s = "";
    if (runs.size == 1) {
      s = "RLE: 1 run\n";
    } else {
      s = "RLE: " + runs.size + " runs\n";
    }
    DListNode cur = runs.head.next;
    long i = 1;
    while (cur.item != null) {
      s += i + ". runLength:" + ((int[])cur.item)[0];
      int r, g, b;
      r = ((int[])cur.item)[1];
      g = ((int[])cur.item)[2];
      b = ((int[])cur.item)[3];
      s += " (" + r + "," + g + "," + b + ")\n";
      cur = cur.next;
    }
    return s;
  }


  /**
   *  The following methods are required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of a specified PixImage.
   * 
   *  Note that you must encode the image in row-major format, i.e., the second
   *  pixel should be (1, 0) and not (0, 1).
   *
   *  @param image is the PixImage to run-length encode.
   */
  public RunLengthEncoding(PixImage image) {
    // I'm sure this can be improved.  Looks really sloppy.
    width = image.getWidth();
    height = image.getHeight();
    runs = new DList();
    int curRunLength = 0;
    // initialize to rgb of first pixel
    int r = image.getRed(0, 0), g = image.getGreen(0, 0),
        b = image.getBlue(0, 0);
    // always a pixel behind because we must check the rgb of next pixel
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (r != image.getRed(x, y) || g != image.getGreen(x, y) ||
            b != image.getBlue(x, y)) {
          runs.insertBack(new int[]{curRunLength, r, g, b});
          curRunLength = 1;
          r = image.getRed(x, y);
          g = image.getGreen(x, y);
          b = image.getBlue(x, y);
        } else {
          curRunLength++;
        }
      }
    }
    // make up for being a pixel behind
    runs.insertBack(new int[]{curRunLength, r, g, b});
    
    check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same RGB intensities, or if the sum of
   *  all run lengths does not equal the number of pixels in the image.
   */
  public void check() {
    int totalRunsLength = 0;
    int r = -1, g = -1, b = -1;
    RunIterator it = iterator();
    
    int runNo = 0;
    while (it.hasNext()) {
      int[] curRun = it.next();
      totalRunsLength += curRun[0];
      int curR = curRun[1];
      int curG = curRun[2];
      int curB = curRun[3];
      runNo++;
      if (r == curR && g == curG && b == curB) {
        System.err.println("Two consecutive runs have the same RGB intensities.");
        System.err.println("Run #" + runNo);
        return;
      }
      r = curR;
      g = curG;
      b = curB;
    }
    if (totalRunsLength != width * height) {
      System.err.println("The sum of all run lengths is not equal to the " +
          "number of pixels in the image.");
    }
  }


  /**
   *  The following method is required for Part IV.
   */

  /**
   *  setPixel() modifies this run-length encoding so that the specified color
   *  is stored at the given (x, y) coordinates.  The old pixel value at that
   *  coordinate should be overwritten and all others should remain the same.
   *  The updated run-length encoding should be compressed as much as possible;
   *  there should not be two consecutive runs with exactly the same RGB color.
   *
   *  @param x the x-coordinate of the pixel to modify.
   *  @param y the y-coordinate of the pixel to modify.
   *  @param red the new red intensity to store at coordinate (x, y).
   *  @param green the new green intensity to store at coordinate (x, y).
   *  @param blue the new blue intensity to store at coordinate (x, y).
   */
  public void setPixel(int x, int y, short red, short green, short blue) {
    RunIterator it = iterator();
    int runLengthsTotal = 0;
    DListNode prev, cur, next;
    prev = null;
    cur = runs.head;
    next = cur.next;
    int[] curRun = (int[])cur.item;
    int pixelPos = y * width + x;
    while (runLengthsTotal <= pixelPos) {
      if (it.hasNext()) {
        curRun = it.next();
        cur = cur.next;
        runLengthsTotal += curRun[0];
      }
    }
    int curRunLength, curR, curG, curB;
    curRunLength = curRun[0];
    curR = curRun[1];
    curG = curRun[2];
    curB = curRun[3];
    
    boolean hasPrevRun, hasNextRun;
    
    prev = cur.prev;
    hasPrevRun = (prev.item != null);
    int prevRunLength = -1, prevR = -1, prevG = -1, prevB = -1;
    if (hasPrevRun) {
      prevRunLength = ((int[])prev.item)[0];
      prevR = ((int[])prev.item)[1];
      prevG = ((int[])prev.item)[2];
      prevB = ((int[])prev.item)[3];
    }

    next = cur.next;
    hasNextRun = (next.item != null);
    int nextRunLength = -1, nextR = -1, nextG = -1, nextB = -1;
    if (hasNextRun) {
      nextRunLength = ((int[])next.item)[0];
      nextR = ((int[])next.item)[1];
      nextG = ((int[])next.item)[2];
      nextB = ((int[])next.item)[3];
    }
    
    // below flags are only useful for pixels on the edge:
    // updated pixel has same rgb as previous run
    boolean sameAsPrev = (prevR == red && prevG == green &&
        prevB == blue);
    // updated pixel has same rgb as next run
    boolean sameAsNext = (nextR == red && nextG == green && nextB == blue);
    
    // pixel is already set correctly
    if (curR == red && curG == green && curB == blue) {
      return;
    }
    // pixel is in a run of length 1
    if (curRunLength == 1) {
      // same as both previous and next runs
      if (sameAsPrev && sameAsNext) {
        runs.remove(prev);
        runs.remove(next);
        cur.item = new int[]{prevRunLength + curRunLength + nextRunLength,
            red, green, blue};
        return;
      }
      // different from both previous and next runs
      if (!sameAsPrev && !sameAsNext) {
        cur.item = new int[]{1, red, green, blue};
        return;
      }
    }
    
    // pixel is at beginning of a run
    if (pixelPos == runLengthsTotal - curRunLength) {
      if (sameAsPrev) {
        prev.item = new int[]{prevRunLength + 1, red, green, blue};
        if (curRunLength == 1) {
          runs.remove(cur);
        } else {
          cur.item = new int[]{curRunLength - 1, curR, curG, curB};
        }
        return;
      }
      // pixel is different from previous and current run
      runs.insertBefore(cur, new int[]{1, red, green, blue});
      cur.item = new int[]{curRunLength - 1, curR, curG, curB};
      return;
    }
    
    // pixel is at end of a run
    if (pixelPos == runLengthsTotal - 1) {
      if (sameAsNext) {
        next.item = new int[] {nextRunLength + 1, red, green, blue};
        if (curRunLength == 1) {
          runs.remove(cur);
        } else {
          cur.item = new int[]{curRunLength - 1, curR, curG, curB};
        }
        return;
      }
      // pixel is different from next and current run
      runs.insertAfter(cur, new int[]{1, red, green, blue});
      cur.item = new int[]{curRunLength - 1, curR, curG, curB};
      return;
    }
    
    // pixel is in the middle of a run
    int posInRun = pixelPos + curRunLength - runLengthsTotal;
    runs.insertBefore(cur, new int[]{posInRun, curR, curG, curB});
    runs.insertAfter(cur, new int[]{curRunLength - (posInRun + 1), curR, curG, curB});
    cur.item = new int[]{1, red, green, blue};
  }


  /**
   * TEST CODE:  YOU DO NOT NEED TO FILL IN ANY METHODS BELOW THIS POINT.
   * You are welcome to add tests, though.  Methods below this point will not
   * be tested.  This is not the autograder, which will be provided separately.
   */


  /**
   * doTest() checks whether the condition is true and prints the given error
   * message if it is not.
   *
   * @param b the condition to check.
   * @param msg the error message to print if the condition is false.
   */
  private static void doTest(boolean b, String msg) {
    if (b) {
      System.out.println("Good.");
    } else {
      System.err.println(msg);
    }
  }

  /**
   * array2PixImage() converts a 2D array of grayscale intensities to
   * a grayscale PixImage.
   *
   * @param pixels a 2D array of grayscale intensities in the range 0...255.
   * @return a new PixImage whose red, green, and blue values are equal to
   * the input grayscale intensities.
   */
  private static PixImage array2PixImage(int[][] pixels) {
    int width = pixels.length;
    int height = pixels[0].length;
    PixImage image = new PixImage(width, height);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setPixel(x, y, (short) pixels[x][y], (short) pixels[x][y],
                       (short) pixels[x][y]);
      }
    }

    return image;
  }

  /**
   * setAndCheckRLE() sets the given coordinate in the given run-length
   * encoding to the given value and then checks whether the resulting
   * run-length encoding is correct.
   *
   * @param rle the run-length encoding to modify.
   * @param x the x-coordinate to set.
   * @param y the y-coordinate to set.
   * @param intensity the grayscale intensity to assign to pixel (x, y).
   */
  private static void setAndCheckRLE(RunLengthEncoding rle,
                                     int x, int y, int intensity) {
    rle.setPixel(x, y,
                 (short) intensity, (short) intensity, (short) intensity);
    rle.check();
  }

  /**
   * main() runs a series of tests of the run-length encoding code.
   */
  public static void main(String[] args) {
    // Be forwarned that when you write arrays directly in Java as below,
    // each "row" of text is a column of your image--the numbers get
    // transposed.
    PixImage image1 = array2PixImage(new int[][] { { 0, 3, 6 },
                                                   { 1, 4, 7 },
                                                   { 2, 5, 8 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 3x3 image.  Input image:");
    System.out.print(image1);
    RunLengthEncoding rle1 = new RunLengthEncoding(image1);
    rle1.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle1.getWidth() == 3 && rle1.getHeight() == 3,
           "RLE1 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(image1.equals(rle1.toPixImage()),
           "image1 -> RLE1 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 42);
    image1.setPixel(0, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           /*
                       array2PixImage(new int[][] { { 42, 3, 6 },
                                                    { 1, 4, 7 },
                                                    { 2, 5, 8 } })),
           */
           "Setting RLE1[0][0] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 0, 42);
    image1.setPixel(1, 0, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][0] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 1, 2);
    image1.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 0, 0, 0);
    image1.setPixel(0, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[0][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 7);
    image1.setPixel(2, 2, (short) 7, (short) 7, (short) 7);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 7 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 2, 2, 42);
    image1.setPixel(2, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[2][2] = 42 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle1, 1, 2, 42);
    image1.setPixel(1, 2, (short) 42, (short) 42, (short) 42);
    doTest(rle1.toPixImage().equals(image1),
           "Setting RLE1[1][2] = 42 fails.");


    PixImage image2 = array2PixImage(new int[][] { { 2, 3, 5 },
                                                   { 2, 4, 5 },
                                                   { 3, 4, 6 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on another 3x3 image.  Input image:");
    System.out.print(image2);
    RunLengthEncoding rle2 = new RunLengthEncoding(image2);
    rle2.check();
    System.out.println("Testing getWidth/getHeight on a 3x3 encoding.");
    doTest(rle2.getWidth() == 3 && rle2.getHeight() == 3,
           "RLE2 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x3 encoding.");
    doTest(rle2.toPixImage().equals(image2),
           "image2 -> RLE2 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 0, 1, 2);
    image2.setPixel(0, 1, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[0][1] = 2 fails.");

    System.out.println("Testing setPixel() on a 3x3 encoding.");
    setAndCheckRLE(rle2, 2, 0, 2);
    image2.setPixel(2, 0, (short) 2, (short) 2, (short) 2);
    doTest(rle2.toPixImage().equals(image2),
           "Setting RLE2[2][0] = 2 fails.");


    PixImage image3 = array2PixImage(new int[][] { { 0, 5 },
                                                   { 1, 6 },
                                                   { 2, 7 },
                                                   { 3, 8 },
                                                   { 4, 9 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 5x2 image.  Input image:");
    System.out.print(image3);
    RunLengthEncoding rle3 = new RunLengthEncoding(image3);
    rle3.check();
    System.out.println("Testing getWidth/getHeight on a 5x2 encoding.");
    doTest(rle3.getWidth() == 5 && rle3.getHeight() == 2,
           "RLE3 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 5x2 encoding.");
    doTest(rle3.toPixImage().equals(image3),
           "image3 -> RLE3 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 4, 0, 6);
    image3.setPixel(4, 0, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[4][0] = 6 fails.");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 1, 6);
    image3.setPixel(0, 1, (short) 6, (short) 6, (short) 6);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][1] = 6 fails.");

    System.out.println("Testing setPixel() on a 5x2 encoding.");
    setAndCheckRLE(rle3, 0, 0, 1);
    image3.setPixel(0, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle3.toPixImage().equals(image3),
           "Setting RLE3[0][0] = 1 fails.");


    PixImage image4 = array2PixImage(new int[][] { { 0, 3 },
                                                   { 1, 4 },
                                                   { 2, 5 } });

    System.out.println("Testing one-parameter RunLengthEncoding constuctor " +
                       "on a 3x2 image.  Input image:");
    System.out.print(image4);
    RunLengthEncoding rle4 = new RunLengthEncoding(image4);
    rle4.check();
    System.out.println("Testing getWidth/getHeight on a 3x2 encoding.");
    doTest(rle4.getWidth() == 3 && rle4.getHeight() == 2,
           "RLE4 has wrong dimensions");

    System.out.println("Testing toPixImage() on a 3x2 encoding.");
    doTest(rle4.toPixImage().equals(image4),
           "image4 -> RLE4 -> image does not reconstruct the original image");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 2, 0, 0);
    image4.setPixel(2, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[2][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 0);
    image4.setPixel(1, 0, (short) 0, (short) 0, (short) 0);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 0 fails.");

    System.out.println("Testing setPixel() on a 3x2 encoding.");
    setAndCheckRLE(rle4, 1, 0, 1);
    image4.setPixel(1, 0, (short) 1, (short) 1, (short) 1);
    doTest(rle4.toPixImage().equals(image4),
           "Setting RLE4[1][0] = 1 fails.");
  }
}
