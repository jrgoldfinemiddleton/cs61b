/* Nuke2.java */
import java.io.*;

/* A class that provides a main method to read a string from the
 * from the keyboard and prints the same string with its second
 * character removed.  
 * 
 * It will produce errors on input less than two
 * characters in length.
 */
class Nuke2 {

  /** 
   * Waits for the user to enter a string, then removes the string's
   * second character and prints the new string.
   *  @param arg is not used.
   *  @exception Exception thrown if there are any problems parsing the 
   *             user's input, including when the input string contains
   *             less than two characters.
   */
  public static void main(String[] args) throws Exception {

    BufferedReader keyboard;
    String input;

    keyboard = new BufferedReader(new InputStreamReader(System.in));
    input = keyboard.readLine();
    String output = input.charAt(0) + input.substring(2, input.length());
    System.out.println(output);
  }
}
