/* DList.java */
/**
 *  A DList is a mutable doubly-linked list.  Its implementation is
 *  circularly-linked and employs a sentinel (dummy) node at the head
 *  of the list.
 */

public class DList {

  /**
   *  head references the sentinel node.
   */

  protected DListNode head;
  protected long size;

  /* DList invariants:
   *  1)  head != null.
   *  2)  For any DListNode x in a DList, x.next != null.
   *  3)  For any DListNode x in a DList, x.prev != null.
   *  4)  For any DListNode x in a DList, if x.next == y, then y.prev == x.
   *  5)  For any DListNode x in a DList, if x.prev == y, then y.next == x.
   *  6)  size is the number of DListNodes, NOT COUNTING the sentinel
   *      (denoted by "head"), that can be accessed from the sentinel by
   *      a sequence of "next" references.
   */

  /**
   *  DList() constructor for an empty DList.
   */
  public DList() {
    head = new DListNode();
    head.item = null;
    head.next = head;
    head.prev = head;
    size = 0;
  }

  /**
   *  DList() constructor for a one-node DList.
   */
  public DList(Object a) {
    head = new DListNode();
    head.item = null;
    head.next = new DListNode();
    head.next.item = a;
    head.prev = head.next;
    head.next.prev = head;
    head.prev.next = head;
    size = 1;
  }

  /**
   *  DList() constructor for a two-node DList.
   */
  public DList(Object a, Object b) {
    head = new DListNode();
    head.item = null;
    head.next = new DListNode();
    head.next.item = a;
    head.prev = new DListNode();
    head.prev.item = b;
    head.next.prev = head;
    head.next.next = head.prev;
    head.prev.next = head;
    head.prev.prev = head.next;
    size = 2;
  }

  /**
   *  insertFront() inserts an item at the front of a DList.
   */
  public void insertFront(Object i) {
    DListNode front = new DListNode(i);
    front.next = head.next;
    front.next.prev = front;
    head.next = front;
    front.prev = head;
    size++;
  }
  
  /**
   * insertBack() inserts an item at the back of a DList.
   */
  public void insertBack(Object i) {
    DListNode back = new DListNode(i);
    back.prev = head.prev;
    back.prev.next = back;
    head.prev = back;
    back.next = head;
    size++;
  }
  
  /**
   * insertAfter() inserts an item after a given item in the DList.
   */
  public void insertAfter(DListNode n, Object i) {
    DListNode newNode = new DListNode(i);
    newNode.next = n.next;
    newNode.next.prev = newNode;
    n.next = newNode;
    newNode.prev = n;
    size++;
  }
  
  /**
   * insertBefore() inserts an item before a given item in the DList.
   */
  public void insertBefore(DListNode n, Object i) {
    DListNode newNode = new DListNode(i);
    newNode.prev = n.prev;
    newNode.prev.next = newNode;
    n.prev = newNode;
    newNode.next = n;
    size++;
  }

  /**
   *  removeFront() removes the first item (and first non-sentinel node) from
   *  a DList.  If the list is empty, do nothing.
   */
  public void removeFront() {
    if (size > 0) {
      head.next = head.next.next;
      head.next.prev = head;
      size--;
    }
  }
  
  /**
   * removeBack() removes the last item from a DList.  If the list is empty,
   * do nothing.
   */
  public void removeBack() {
    if (size > 0) {
      head.prev = head.prev.prev;
      head.prev.next = head;
      size--;
    }
  }
  
  /**
   * remove() removes an item from a DList.  If the list is empty, do
   * nothing.
   */
  public void remove(DListNode n) {
    if (size > 0) {
      n.prev.next = n.next;
      n.next.prev = n.prev;
      n.prev = null;
      n.next = null;
      size--;
    }
  }

  /**
   *  toString() returns a String representation of this DList.
   *
   *  @return a String representation of this DList.
   */
  @Override
  public String toString() {
    String result = "[  ";
    DListNode current = head.next;
    while (current != head) {
      result = result + current.item + "  ";
      current = current.next;
    }
    return result + "]";
  }
}
