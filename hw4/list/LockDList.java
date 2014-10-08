/* LockDList.java */
package list;

/**
 *  A LockDList is a mutable doubly-linked list ADT. Its implementation
 *  is circularly-linked and employs a sentinel (dummy) node at the head
 *  of the list. It is possible to lock a node to prevent it from being
 *  removed from the list.
 */

public class LockDList extends DList {

  @Override
  protected DListNode newNode(Object item, DListNode prev, DListNode next) {
    return new LockDListNode
        (item, (LockDListNode) prev, (LockDListNode) next);
  }

  /**
  *  LockDList() constructor for an empty DList.
  */
  public LockDList() {
    head = new LockDListNode(null, null, null);
    head.next = head;
    head.prev = head;
    ((LockDListNode) head).locked = true;
    size = 0;
  }

  public void lockNode(DListNode node) {
    ((LockDListNode) node).locked = true;
  }

  @Override
  public void remove(DListNode node) {
    if (((LockDListNode) node).locked == true) {
      return;
    }
    super.remove(node);
  }

  public static void main(String[] args) {
    System.out.println("Constructing a new DList.");
    LockDList l1 = new LockDList();
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

    System.out.println("\nAttempting to lock the node after head.");
    l1.lockNode(l1.head.next);
    System.out.println("Locked? " + ((LockDListNode) l1.head.next).locked);
    
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
    LockDList l2 = new LockDList();
    System.out.println(l2);

    System.out.print("\nAttempting to set the head node of the new DList ");
    System.out.println("to the first node of the first DList.");
    System.out.print("Good luck with that! (I bet it will screw up the ");
    System.out.println("invariants.)\n");
    l2.head = l1.head.next;
    System.out.println("First DList:");
    l1.checkInvariants();
    System.out.println("Second DList:");
    l2.checkInvariants();
  }
}

