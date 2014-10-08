/* LockDListNode.java */
package list;

/**
 *  A LockDListNode is a node in a LockDList (doubly-lined list)
 *  that can be locked to prevent removal from the list.
 */

public class LockDListNode extends DListNode {

  /**
   *  locked true if the node cannot be removed from the list,
   *  false otherwise.
   */

  protected boolean locked = false;

  /**
   *  LockDListNode() constructor.
   *  @param i the item to store in the node.
   *  @param p the node previous to this node.
   *  @param n the node following this node.
   */
  LockDListNode(Object i, LockDListNode p, LockDListNode n) {
      super(i, p, n);
  }

  /**
   *  LockDListNode() constructor.
   *  @param i the item to store in the node.
   *  @param p the node previous to this node.
   *  @param n the node following this node.
   *  @param l true if the node is locked, false otherwise.
   */
  LockDListNode(Object i, LockDListNode p, LockDListNode n, boolean l) {
      super(i, p, n);
      locked = l;
  }
}
