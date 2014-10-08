/* HashTableChained.java */
package dict;

import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  protected List[] table;
  // number of entries
  protected int size = 0;

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    table = new List[nextPrime(sizeEstimate)];
    for (int i = 0; i < table.length; i++) {
      table[i] = new DList();
    }
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    this(100);
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  int compFunction(int code) {
    return (Math.abs((7 * code + 13) * nextPrime(numBuckets() * 75))) %
        numBuckets();
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  @Override
  public int size() {
    return size;
  }
  
  /**
   * Returns the number of buckets in the dictionary.
   */
  public int numBuckets() {
    return table.length;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  @Override
  public boolean isEmpty() {
    for (int i = 0; i < size; i++) {
      if (!table[i].isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  @Override
  public Entry insert(Object key, Object value) {
    if (key != null) {
      int n = compFunction(key.hashCode());
      Entry e = new Entry();
      e.key = key;
      e.value = value;

      table[n].insertBack(e);
      size++;
      return e;
    }
    return null;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  @Override
  public Entry find(Object key) {
    if (key != null) {
      int n = compFunction(key.hashCode());
      ListNode node = table[n].front();
      try {
        for (int i = 0; i < table[n].length(); i++) {
          if (key.equals(((Entry)node.item()).key())) {
            // found same key, return entry
            return (Entry)node.item();
          }
          node = node.next();
        }
      } catch (InvalidNodeException ine) {
      }
      // key not found
    }
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  @Override
  public Entry remove(Object key) {
    if (key != null) {
      int n = compFunction(key.hashCode());
      ListNode node = table[n].front();
      try {
        for (int i = 0; i < table[n].length(); i++) {
          if (key.equals(((Entry)node.item()).key())) {
            // found key, removing it
            Entry e = (Entry)node.item();
            node.remove();
            size--;
            // return removed entry
            return e;
          }
          node = node.next();
        }
      } catch (InvalidNodeException ine) {
      }
      // key not found, not removing anything
    }
    return null;
  }

  /**
   *  Remove all entries from the dictionary.
   */
  @Override
  public void makeEmpty() {
    for (int i = 0; i < table.length; i++) {
      table[i] = new DList();
    }
    size = 0;
  }

  /**
   * Return the number of expected collisions given keys selected
   * randomly, where a collision is defined as any addition of a key
   * to a bucket with at least one key already inside it.
   */
  public int expectedCollisions() {
    int n = size;           // number of keys
    int N = numBuckets();   // number of buckets
    return (int) (n - N + N * Math.pow(1 - 1.0 / N, n));
  }

  /**
   * Return the number of collisions in the table, where a collision is
   * defined as any addition of a key to a bucket with at least one key
   * already inside it.
   */
  public int numCollisions() {
    int count = 0;
    for (int i = 0; i < numBuckets(); i++) {
      if (table[i].length() > 1) {
        count+= table[i].length() - 1;
      }
    }
    return count;
  }

  /**
   * Print a histogram showing how many items are in each bucket
   * as well as the number of buckets with 1-10 entries.
   */
  public void printHistogram() {
    int[] count = new int[numBuckets()];

    for (int i = 0; i < size; i++) {
      count[table[i].length()]++;
      System.out.println("Bucket " + i + ": " + table[i].length());
    }

    System.out.println();
    for (int j = 1; j <= 10; j++) {
      System.out.println("Number of buckets with " + j + " entries: " +
          count[j]);
    }
  }

  /**
   * Private method that finds the next prime number greater than or
   * equal to n.
   * @param n the number to begin the search at
   * @return the first prime found greater than or equal to n. Returns
   * -1 if a prime is not found less than n + 200.
   */
  private static int nextPrime(int n) {
    if (n <= 2) {
      return 2;
    }

    boolean[] prime = new boolean[n + 200];
    int i;
    for (i = 2; i < n + 200; i++) {
      prime[i] = true;
    }

    for (int divisor = 2; divisor * divisor < n + 200; divisor++) {
      if (prime[divisor]) {
        for (i = 2 * divisor; i < n + 200; i += divisor) {
          prime[i] = false;
        }
      }
    }

    for (i = n; i < n + 200; i++) {
      if (prime[i]) {
        return i;
      }
    }

    return -1; // this should not occur if n < 5 * 10^6
  }
}
