/* BinaryTree.java */

package dict;

/**
 *  BinaryTree implements a Dictionary as a binary tree (unbalanced).  Multiple
 *  entries with the same key are permitted.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 *
 *  @author Jonathan Shewchuk
 **/
public class BinaryTree implements Dictionary {

  /** 
   *  size is the number of items stored in the dictionary.
   *  root is the BinaryTreeNode that serves as root of the tree.
   *  If there are no items, size is zero and root is null.
   **/
  protected int size;
  protected BinaryTreeNode root;

  /**
   *  Construct an empty binary tree.
   **/
  public BinaryTree() {
    makeEmpty();
  }

  /**
   *  makeEmpty() removes all the entries from the dictionary.
   */
  @Override
  public void makeEmpty() {
    size = 0;
    root = null;
  }

  /** 
   *  size() returns the number of entries stored in the dictionary.
   *
   *  @return the number of entries stored in the dictionary.
   **/
  @Override
  public int size() {
    return size;
  }

  /** 
   *  isEmpty() tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /** 
   *  insert() constructs and inserts a new Entry object, consisting of
   *  a (key, value) pair, into the dictionary, and returns a reference to the
   *  new Entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  @param key the key by which the entry can be retrieved.  Must be of
   *  a class that implements java.lang.Comparable.
   *  @param value an arbitrary object associated with the key.
   *  @return an Entry object referencing the key and value.
   **/
  @SuppressWarnings("rawtypes")
  @Override
  public Entry insert(Object key, Object value) {
    Entry entry = new Entry(key, value);
    if (root == null) {
      root = new BinaryTreeNode(entry);
    } else {
      insertHelper(entry, (Comparable) key, root);
    }

    size++;
    return entry;
  }

  /**
   *  insertHelper() recursively does the work of inserting a new Entry object
   *  into the dictionary.
   *
   *  @param entry the Entry object to insert into the tree.
   *  @param key the key by which the entry can be retrieved.
   *  @param node the root of a subtree in which the new entry will be
   *         inserted.
   **/
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void insertHelper(Entry entry, Comparable key, BinaryTreeNode node) {
    if (key.compareTo(node.entry.key()) <= 0) {
      if (node.leftChild == null) {
        node.leftChild = new BinaryTreeNode(entry, node);
      } else {
        insertHelper(entry, key, node.leftChild);
      }
    } else {
      if (node.rightChild == null) {
        node.rightChild = new BinaryTreeNode(entry, node);
      } else {
        insertHelper(entry, key, node.rightChild);
      }
    }
  }

  /** 
   *  find() searches for an entry with the specified key.  If such an entry is
   *  found, it returns the Entry object; otherwise, it returns null.  If more
   *  than one entry has the key, one of them is chosen arbitrarily and
   *  returned.
   *
   *  @param key the search key.  Must be of a class that implements
   *         java.lang.Comparable.
   *  @return an Entry referencing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/
  @SuppressWarnings("rawtypes")
  @Override
  public Entry find(Object key) {
    BinaryTreeNode node = findHelper((Comparable) key, root);
    if (node == null) {
      return null;
    } else {
      return node.entry;
    }
  }

  /**
   *  Search for a node with the specified key, starting from "node".  If
   *  a matching key is found (meaning that key1.compareTo(key2) == 0), return
   *  a node containing that key.  Otherwise, return null.
   *
   *  Be sure this method returns null if node == null.
   **/

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private BinaryTreeNode findHelper(Comparable key, BinaryTreeNode node) {
    if (node == null) {
      return null;
    }
    if (key.compareTo(node.entry.key()) == 0) {
      return node;
    }
    if (key.compareTo(node.entry.key()) < 0) {
      if (node.leftChild == null) {
        return null;
      } else {
        return findHelper(key, node.leftChild);
      }
    } else {
      if (node.rightChild == null) {
        return null;
      } else {
        return findHelper(key, node.rightChild);
      }
    }
  }

  /** 
   *  remove() searches for an entry with the specified key.  If such an entry
   *  is found, it removes the Entry object from the Dictionary and returns it;
   *  otherwise, it returns null.  If more than one entry has the key, one of
   *  them is chosen arbitrarily, removed, and returned.
   *
   *  @param key the search key.  Must be of a class that implements
   *         java.lang.Comparable.
   *  @return an Entry referencing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/
  @SuppressWarnings("rawtypes")
  @Override
  public Entry remove(Object key) {
    if (root == null) {
      return null;
    }
    BinaryTreeNode n = findHelper((Comparable) key, root);
    if (n == null) {
      return null;
    }
    Entry entry = n.entry;
    if (n.leftChild == null && n.rightChild == null) {
      if (n == root) {
        root = null;
      } else {
        if (n == n.parent.leftChild) {
          n.parent.leftChild = null;
        } else {
          n.parent.rightChild = null;
        }
      }
    } else if (n.leftChild == null) {
      if (n == root) {
        root = n.rightChild;
        root.parent = null;
      } else {
        n.rightChild.parent = n.parent;
        if (n == n.parent.leftChild) {
          n.parent.leftChild = n.rightChild;
        } else {
          n.parent.rightChild = n.rightChild;
        }
      }
    } else if (n.rightChild == null) {
      if (n == root) {
        root = n.leftChild;
        root.parent = null;
      } else {
        n.leftChild.parent = n.parent;
        if (n == n.parent.leftChild) {
          n.parent.leftChild = n.leftChild;
        } else {
          n.parent.rightChild = n.leftChild;
        }
      }
    } else { // has two children
      BinaryTreeNode old = n;
      // let n be the least greater key than the one in old
      n = n.rightChild;
      while (n.leftChild != null) {
        n = n.leftChild;
      }
      n.leftChild = old.leftChild;
      if (n != old.rightChild) { // if n is a left child
        // give n's old child to n's parent (which would now have no left child)
        n.parent.leftChild = n.rightChild;
        if (n.rightChild != null) {
          n.rightChild.parent = n.parent;
        }
        // give n the removed node's right child
        n.rightChild = old.rightChild;
      }
      // the left child of the removed node has a new parent
      old.leftChild.parent = n;
      // the right child of the removed node has a new parent
      old.rightChild.parent = n;
      // n takes over for old node
      n.parent = old.parent;

      if (old == root) {
        root = n;
      } else { // let n be the child of old's parent
        if (old == old.parent.leftChild) {
          old.parent.leftChild = n;
        } else {
          old.parent.rightChild = n;
        }
      }
    }
    size--;
    return entry;
  }

  /**
   *  Convert the tree into a string.
   **/

  @Override
  public String toString() {
    if (root == null) {
      return "";
    } else {
      return root.toString();
    }
  }

  /* Tests the binary search tree. */
  public static void main(String[] args) {
    BinaryTree tree = new BinaryTree();

    System.out.println("Inserting 1A, 6V, 3K, 2Z, 5L, 9L:");
    tree.insert(new Integer(1), "A");
    tree.insert(new Integer(6), "V");
    tree.insert(new Integer(3), "K");
    tree.insert(new Integer(2), "Z");
    tree.insert(new Integer(5), "L");
    tree.insert(new Integer(9), "L");
    System.out.println("The tree is:  " + tree);
    System.out.println("Size:  " + tree.size());

    System.out.println("\nTesting find() ...");
    tree.testFind(1, "A");
    tree.testFind(9, "L");
    tree.testFind(5, "L");
    tree.testFind(4, null);
    tree.testFind(6, "V");
    tree.testFind(3, "K");

    System.out.println("\nTesting remove() (for nodes with < 2 children) ...");
    tree.testRemove(5, "1A(((2Z)3K)6V(9L))");
    tree.testRemove(3, "1A((2Z)6V(9L))");
    tree.testRemove(1, "(2Z)6V(9L)");
    tree.insert(new Integer(7), "S");
    tree.insert(new Integer(8), "X");
    tree.insert(new Integer(10), "B");
    System.out.println("After inserting 7S, 8X, 10B:  " + tree);
    System.out.println("Size:  " + tree.size());
    if (tree.size() != 6) {
      System.out.println("  SHOULD BE 6.");
    }

    System.out.println("\nTesting remove() (for nodes with 2 children) ...");
    tree.testRemove(6, "(2Z)7S((8X)9L(10B))");
    tree.testRemove(9, "(2Z)7S((8X)10B)");
    System.out.println("Size:  " + tree.size());
    if (tree.size() != 4) {
      System.out.println("  SHOULD BE 4.");
    }
    
    System.out.println("\nAdditional test code:");
    
    tree = new BinaryTree();
    tree.insert(new Integer(5), "A");
    tree.insert(new Integer(6), "V");
    tree.insert(new Integer(7), "K");
    tree.insert(new Integer(4), "Z");
    tree.insert(new Integer(3), "L");
    tree.insert(new Integer(4), "X");
    tree.insert(new Integer(5), "E");
    tree.insert(new Integer(10), "Y");
    tree.insert(new Integer(11), "T");
    tree.insert(new Integer(8), "R");
    tree.insert(new Integer(9), "S");

    
    System.out.println("\nTree 2");
    System.out.println(tree);
    System.out.println("Tree after removing 1");
    tree.remove(1);
    System.out.println(tree);
    System.out.println("Tree after removing 5");
    tree.remove(5);
    System.out.println(tree);
    System.out.println("Tree after removing 8");
    tree.remove(8);
    System.out.println(tree);
    System.out.println("Tree after removing 9");
    tree.remove(9);
    System.out.println(tree);
    System.out.println("Tree after removing 4");
    tree.remove(4);
    System.out.println(tree);
    System.out.println("Tree after removing 3");
    tree.remove(3);
    System.out.println(tree);

    tree = new BinaryTree();
    tree.insert(new Integer(10), "A");
    tree.insert(new Integer(7), "V");
    tree.insert(new Integer(13), "C");
    tree.insert(new Integer(15), "K");
    tree.insert(new Integer(12), "Z");
    tree.insert(new Integer(14), "L");
    tree.insert(new Integer(17), "X");
    
    System.out.println("\nTree 3");
    System.out.println(tree);
    System.out.println("Tree after removing 13");
    tree.remove(13);
    System.out.println(tree); 
    
  }

  private void testRemove(int n, String shouldBe) {
    Integer key = new Integer(n);
    System.out.print("After remove(" + n + "):  ");
    remove(key);
    System.out.println(this);
    if (!toString().equals(shouldBe)) {
      System.out.println("  SHOULD BE " + shouldBe);
    }
  }

  private void testFind(int n, Object truth) {
    Integer key = new Integer(n);
    Entry entry = find(key);
    System.out.println("Calling find() on " + n);
    if (entry == null) {
      System.out.println("  returned null.");
      if (truth != null) {
        System.out.println("  SHOULD BE " + truth + ".");
      }
    } else {
      System.out.println("  returned " + entry.value() + ".");
      if (!entry.value().equals(truth)) {
        if (truth == null) {
          System.out.println("  SHOULD BE null.");
        } else {
          System.out.println("  SHOULD BE " + truth + ".");
        }
      }
    }
  }

}
