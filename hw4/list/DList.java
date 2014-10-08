/* DList.java */
package list;

/**
 *  A DList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel (dummy) node at the head
 *  of the list.
 *
 *  DO NOT CHANGE ANY METHOD PROTOTYPES IN THIS FILE.
 */

public class DList {

  /**
   *  head references the sentinel node.
   *  size is the number of items in the list.  (The sentinel node does not
   *       store an item.)
   *
   *  DO NOT CHANGE THE FOLLOWING FIELD DECLARATIONS.
   */

  protected DListNode head;
  protected int size;

  /* DList invariants:
   *  1)  head != null.
   *  2)  For any DListNode x in a DList, x.next != null.
   *  3)  For any DListNode x in a DList, x.prev != null.
   *  4)  For any DListNode x in a DList, if x.next == y, then y.prev == x.
   *  5)  For any DListNode x in a DList, if x.prev == y, then y.next == x.
   *  6)  size is the number of DListNodes, NOT COUNTING the sentinel,
   *      that can be accessed from the sentinel (head) by a sequence of
   *      "next" references.
   */

  /**
   *  newNode() calls the DListNode constructor.  Use this class to allocate
   *  new DListNodes rather than calling the DListNode constructor directly.
   *  That way, only this method needs to be overridden if a subclass of DList
   *  wants to use a different kind of node.
   *  @param item the item to store in the node.
   *  @param prev the node previous to this node.
   *  @param next the node following this node.
   */
  protected DListNode newNode(Object item, DListNode prev, DListNode next) {
    return new DListNode(item, prev, next);
  }

  /**
   *  DList() constructor for an empty DList.
   */
  public DList() {
    head = newNode(null, null, null);
    head.prev = head;
    head.next = head;
    size = 0;
  }

  /**
   *  isEmpty() returns true if this DList is empty, false otherwise.
   *  @return true if this DList is empty, false otherwise. 
   *  Performance:  runs in O(1) time.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /** 
   *  length() returns the length of this DList. 
   *  @return the length of this DList.
   *  Performance:  runs in O(1) time.
   */
  public int length() {
    return size;
  }

  /**
   *  insertFront() inserts an item at the front of this DList.
   *  @param item is the item to be inserted.
   *  Performance:  runs in O(1) time.
   */
  public void insertFront(Object item) {
    DListNode front = newNode(item, head, head.next);
    front.next.prev = front;
    head.next = front;
    size++;
  }

  /**
   *  insertBack() inserts an item at the back of this DList.
   *  @param item is the item to be inserted.
   *  Performance:  runs in O(1) time.
   */
  public void insertBack(Object item) {
    DListNode back = newNode(item, head.prev, head);
    back.prev.next = back;
    head.prev = back;
    size++;
  }

  /**
   *  front() returns the node at the front of this DList.  If the DList is
   *  empty, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @return the node at the front of this DList.
   *  Performance:  runs in O(1) time.
   */
  public DListNode front() {
    if (head.next != head) {
      return head.next;
    }
    return null;
  }

  /**
   *  back() returns the node at the back of this DList.  If the DList is
   *  empty, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @return the node at the back of this DList.
   *  Performance:  runs in O(1) time.
   */
  public DListNode back() {
    if (head.prev != head) {
      return head.prev;
    }
    return null;
  }

  /**
   *  next() returns the node following "node" in this DList.  If "node" is
   *  null, or "node" is the last node in this DList, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @param node the node whose successor is sought.
   *  @return the node following "node".
   *  Performance:  runs in O(1) time.
   */
  public DListNode next(DListNode node) {
    if (node != null && node.next != head) {
      return node.next;
    }
    return null;
  }

  /**
   *  prev() returns the node prior to "node" in this DList.  If "node" is
   *  null, or "node" is the first node in this DList, return null.
   *
   *  Do NOT return the sentinel under any circumstances!
   *
   *  @param node the node whose predecessor is sought.
   *  @return the node prior to "node".
   *  Performance:  runs in O(1) time.
   */
  public DListNode prev(DListNode node) {
    if (node != null && node.prev != head) {
      return node.prev;
    }
    return null;
  }

  /**
   *  insertAfter() inserts an item in this DList immediately following "node".
   *  If "node" is null, do nothing.
   *  @param item the item to be inserted.
   *  @param node the node to insert the item after.
   *  Performance:  runs in O(1) time.
   */
  public void insertAfter(Object item, DListNode node) {
    if (node != null) {
      DListNode newNode = newNode(item, node, node.next);
      node.next = newNode;
      newNode.next.prev = newNode;
      size++;
    }
  }

  /**
   *  insertBefore() inserts an item in this DList immediately before "node".
   *  If "node" is null, do nothing.
   *  @param item the item to be inserted.
   *  @param node the node to insert the item before.
   *  Performance:  runs in O(1) time.
   */
  public void insertBefore(Object item, DListNode node) {
    if (node != null) {
      DListNode newNode = newNode(item, node.prev, node);
      node.prev = newNode;
      newNode.prev.next = newNode;
      size++;
    }
  }

  /**
   *  remove() removes "node" from this DList.  If "node" is null, do nothing.
   *  Performance:  runs in O(1) time.
   */
  public void remove(DListNode node) {
    if (node != null && node != head) {
      node.prev.next = node.next;
      node.next.prev = node.prev;
      node.prev = null;
      node.next = null;
      size--;
    }
  }

  /**
   *  toString() returns a String representation of this DList.
   *
   *  DO NOT CHANGE THIS METHOD.
   *
   *  @return a String representation of this DList.
   *  Performance:  runs in O(n) time, where n is the length of the list.
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

  public void checkInvariants() {
    System.out.println("\nVerifying invariants.");
    boolean tightShip = true;    
    // 1) head != null.
    if (head == null) {
      System.out.println("ERROR: Invariant 1 failed.");
      System.out.println("head == null!");
      tightShip = false;
    }
    
    DListNode x = head;
    int cur = 0;
    do
    {
      // 2) For any DListNode x in a DList, x.next != null.
      if (x.next == null) {
        System.out.println("ERROR: Invariant 2 failed.");
        System.out.println("x.next == null for element " + cur);
        tightShip = false;
        cur--;
        break;
      }

      // 3) For any DListNode x in a DList, x.prev != null.
      if (x.prev == null) {
        System.out.println("ERROR: Invariant 3 failed.");
        System.out.println("x.prev == null for element " + cur);
        tightShip = false;
      }

      // 4) For any DListNode x in a DList, if x.next == y, then y.prev == x.
      DListNode y = x.next;
      if (y.prev != x) {
        System.out.println("ERROR: Invariant 4 failed.");
        System.out.println("x.next == y but y.prev != x for element " + cur);
        tightShip = false;
      }

      // 5) For any DListNode x in a DList, if x.prev == y, then y.next == x.
      y = x.prev;
      if (y.next != x) {
        System.out.println("ERROR: Invariant 5 failed.");
        System.out.println("x.prev == y but y.next != x for element " + cur);
        tightShip = false;
      }

      y = null;
      x = x.next;
      cur++;
    } while (x != head);

    // 6) size is the number of DListNodes, NOT COUNTING the sentinel,
    // that can be accessed from the sentinel (head) by a sequence of
    // "next" references.
    if (size != --cur) {
      System.out.println("ERROR: Invariant 6 failed.");
      System.out.println("size == " + size);
      System.out.println("Should be " + cur + ".");
      tightShip = false;
    }

    System.out.print("You ");
    if (!tightShip) {
      System.out.print("do not ");
    }
    System.out.println("run a tight ship!\n");
  }

  /**
   *  main() contains test code for making new DList objects, inserting and
   *  removing nodes, and detemining the size of DList objects.
   */
  public static void main(String[] args) {
    System.out.println("Constructing a new DList.");
    DList l1 = new DList();
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.println("\nAttempting to remove the head node.");
    l1.remove(l1.head);
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.println("\nAttempting to remove the node after head.");
    l1.remove(l1.head.next);
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.println("\nAttempting to insert a new front node containing 3.");
    l1.insertFront(new Integer(3));
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.println("\nAttempting to insert a new front node containing 2.");
    l1.insertFront(new Integer(2));
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.println("\nAttempting to insert a new front node containing 99.");
    l1.insertFront(new Integer(99));
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.println("\nAttempting to remove the node after head.");
    l1.remove(l1.head.next);
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());
    l1.checkInvariants();

    System.out.println("Attempting to insert a new back node containing 9");
    l1.insertBack(new Integer(9));
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.print("\nAttempting to insert a new back node containing ");
    System.out.println("\"deleteMe\".");
    l1.insertBack(new String("deleteMe"));
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.print("\nAttempting to insert a new node containing 4 after the ");
    System.out.println("second node.");
    l1.insertAfter(4, l1.next(l1.head).next);
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.println("\nAttempting to remove the back node.");
    l1.remove(l1.prev(l1.head));
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());

    System.out.print("\nAttempting to insert a new node containing 8 after ");
    System.out.println("the second to last node.");
    l1.insertAfter(8, l1.prev(l1.back()));
    System.out.println(l1);
    System.out.println("Current size: " + l1.length());
    l1.checkInvariants();

    System.out.println("Constructing a new DList.");
    DList l2 = new DList();
    System.out.println(l2);

    System.out.print("\nAttempting to set the head node of the new DList ");
    System.out.println("to the first node of the old DList.");
    System.out.print("Good luck with that! (I bet it will screw up the ");
    System.out.println("invariants.)\n");
    l2.head = l1.head.next;
    System.out.println("First DList:");
    l1.checkInvariants();
    System.out.println("Second DList:");
    l2.checkInvariants();
  }
}
