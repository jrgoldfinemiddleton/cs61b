/* List.java */
package list;

/**
 *  A List is a mutable list ADT.  No implementation is provided.
 */
@SuppressWarnings("rawtypes")
public abstract class List implements Iterable {

  /**
   *  size is the number of items in the list.
   */
  protected int size;

  /**
   *  back() returns the node at the back of this List.  If the List is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.
   *
   *  @return a ListNode at the back of this List.
   */
  public abstract ListNode back();

  /**
   *  front() returns the node at the front of this List.  If the List is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.
   *
   *  @return a ListNode at the front of this List.
   */
  public abstract ListNode front();

  /**
   *  insertBack() inserts an item at the back of this List.
   *
   *  @param item is the item to be inserted.
   */
  public abstract void insertBack(Object item);

  /**
   *  insertFront() inserts an item at the front of this List.
   *
   *  @param item is the item to be inserted.
   */
  public abstract void insertFront(Object item);

  /**
   *  isEmpty() returns true if this List is empty, false otherwise.
   *
   *  @return true if this List is empty, false otherwise. 
   *
   *  Performance:  runs in O(1) time.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /** 
   *  length() returns the length of this List. 
   *
   *  @return the length of this List.
   *
   *  Performance:  runs in O(1) time.
   */
  public int length() {
    return size;
  }

  /**
   *  toString() returns a String representation of this List.
   *
   *  @return a String representation of this List.
   */
  @Override
  public abstract String toString();
}
