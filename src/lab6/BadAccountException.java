/* BadAccountException.java */
package lab6;

/**
 *  Implements an exception that should be thrown for nonexistent accounts.
 **/
@SuppressWarnings("serial")
public class BadAccountException extends Exception {

  public int accountNumber;  // The invalid account number.

  /**
   *  Creates an exception object for nonexistent account "badAcctNumber".
   **/
  public BadAccountException(int badAcctNumber) {
    super("Invalid account number: " + badAcctNumber);

    accountNumber = badAcctNumber;
  }
}
