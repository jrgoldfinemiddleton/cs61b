/* OpenCommercial.java */
import java.net.*;
import java.io.*;

/**  A class that provides a main function to read five lines of a commercial
 *   Web page and print them in reverse order, given the name of a company.
 */

class OpenCommercial {

  /** Prompts the user for the name X of a company (a single string), opens
   *  the Web site corresponding to www.X.com, and prints the first five lines
   *  of the Web page in reverse order.
   *  @param arg is not used.
   *  @exception Exception thrown if there are any problems parsing the 
   *             user's input or opening the connection.
   */
  public static void main(String[] arg) throws Exception {

    BufferedReader keyboard;
    String inputLine;

    keyboard = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Please enter the name of a company (without spaces): ");
    System.out.flush();        /* Make sure the line is printed immediately. */
    inputLine = keyboard.readLine();
    keyboard.close();	/* Close the reader. */

    URL u = new URL("http://www." + inputLine + ".com");
    InputStream ins = u.openStream();
    InputStreamReader isr = new InputStreamReader(ins);
    BufferedReader comp = new BufferedReader(isr);
    String[] lines = new String[5];

    /* Read the first five lines and store them in an array. */
    int i;
    for (i = 0; i < lines.length; i++) {
      lines[i] = comp.readLine();
    }

    /* Print the lines in reverse order. */
    for (i = lines.length - 1; i >= 0; i--) {
      System.out.println(lines[i]);
    }
    comp.close();
  }
}
