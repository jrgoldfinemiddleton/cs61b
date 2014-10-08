/* DList.java */
package list;

import java.util.Iterator;

/**
 * A mutable doubly-linked list ADT.  Its implementation is circularly-linked 
 * and employs a sentinel node at the head of the list.
 */
public class DList extends List {

  /* Instance Fields
   * (inherited)  size is the number of items in the list.
   * head references the sentinel node.
   * Note that the sentinel node does not store an item, and is not included
   * in the count stored by the "size" field.
   */
  protected DListNode head;

  /* DList invariants:
   *  1)  head != null.
   *  2)  For every DListNode x in a DList, x.next != null.
   *  3)  For every DListNode x in a DList, x.prev != null.
   *  4)  For every DListNode x in a DList, if x.next == y, then y.prev == x.
   *  5)  For every DListNode x in a DList, if x.prev == y, then y.next == x.
   *  6)  For every DList l, l.head.myList = null.  (Note that l.head is the
   *      sentinel.)
   *  7)  For every DListNode x in a DList l EXCEPT l.head (the sentinel),
   *      x.myList = l.
   *  8)  size is the number of DListNodes, NOT COUNTING the sentinel,
   *      that can be accessed from the sentinel (head) by a sequence of
   *      "next" references.
   */

  /**
   *  DList() constructs for an empty DList.
   */
  public DList() {
    head = newNode(null, null, null, null);
    head.next = head;
    head.prev = head;
    size = 0;
  }

  /**
   *  back() returns the node at the back of this DList.  If the DList is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
   *
   *  @return a ListNode at the back of this DList.
   *
   *  Performance:  runs in O(1) time.
   */
  @Override
  public ListNode back() {
    return head.prev;
  }

  /**
   *  front() returns the node at the front of this DList.  If the DList is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
   *
   *  @return a ListNode at the front of this DList.
   *
   *  Performance:  runs in O(1) time.
   */
  @Override
  public ListNode front() {
    return head.next;
  }

  /**
   *  insertBack() inserts an item at the back of this DList.
   *
   *  @param item is the item to be inserted.
   *
   *  Performance:  runs in O(1) time.
   */
  @Override
  public void insertBack(Object item) {
    DListNode newNode = newNode(item, this, head.prev, head);
    head.prev = newNode;
    newNode.prev.next = newNode;
    size++;
  }

  /**
   *  insertFront() inserts an item at the front of this DList.
   *
   *  @param item is the item to be inserted.
   *
   *  Performance:  runs in O(1) time.
   */
  @Override
  public void insertFront(Object item) {
    DListNode newNode = newNode(item, this, head, head.next);
    head.next = newNode;
    newNode.next.prev = newNode;
    size++;
  }

  /**
   *  iterator() returns a new Iterator for this list.
   */
  @SuppressWarnings("rawtypes")
  @Override
  public Iterator iterator() {
    List toIterate = this;
    return new Iterator(){
      List list = toIterate;
      ListNode cur = list.front();

      @Override
      public boolean hasNext() {
        return cur.isValidNode();
      }

      @Override
      public Object next() {
        Object item = cur.item;
        try {
          cur = cur.next();
        } catch (InvalidNodeException ine) {
          ine.printStackTrace();
          System.exit(0);
        }
        return item;
      }
    };
  }

  
  /**
   *  newNode() calls the DListNode constructor.  Use this method to allocate
   *  new DListNodes rather than calling the DListNode constructor directly.
   *  That way, only this method need be overridden if a subclass of DList
   *  wants to use a different kind of node.
   *
   *  @param item the item to store in the node.
   *  @param list the list that owns this node.  (null for sentinels.)
   *  @param prev the node previous to this node.
   *  @param next the node following this node.
   */
  protected DListNode newNode(Object item, DList list,
                              DListNode prev, DListNode next) {
    return new DListNode(item, list, prev, next);
  }

  /**
   *  toString() returns a String representation of this DList.
   *
   *  @return a String representation of this DList.
   *
   *  Performance:  runs in O(n) time, where n is the length of the list.
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder("[  ");
    DListNode current = head.next;
    while (current != head) {
      s.append(current.item + "  ");
      current = current.next;
    }
    s.append("]");
    return s.toString();
  }
}
