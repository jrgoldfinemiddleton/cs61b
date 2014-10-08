/* Pixel.java */
public class Pixel {
  protected short r, g, b;
  
  public Pixel() {
    this((short)0, (short)0, (short)0);
  }
  
  public Pixel(short red, short green, short blue) {
    if ( (red < 0 || red > 255) || (green < 0 || green > 255) ||
         (blue < 0 || blue > 255) ) {
      System.err.println("Invalid rgb values: " + red + "," + green +
          "," + blue);
      System.exit(0);
    }
    r = red;
    g = green;
    b = blue;
  }
}
