/* ListSorts.java */

import list.*;

public class ListSorts {

  private final static int SORTSIZE = 1000;

  /**
   *  makeQueueOfQueues() makes a queue of queues, each containing one item
   *  of q.  Upon completion of this method, q is empty.
   *  @param q is a LinkedQueue of objects.
   *  @return a LinkedQueue containing LinkedQueue objects, each of which
   *    contains one object from q.
   **/
  public static LinkedQueue makeQueueOfQueues(LinkedQueue q) {
    LinkedQueue queues = new LinkedQueue();
    try {
      int size = q.size();
      for (int i = 0; i < size; i++) {
        Object o = q.dequeue();
        LinkedQueue temp = new LinkedQueue();
        temp.enqueue(o);
        queues.enqueue(temp);
      }
    } catch (QueueEmptyException uf) {
      System.err.println("Error:  attempt to dequeue from empty queue.");
    }
    return queues;
  }

  /**
   *  mergeSortedQueues() merges two sorted queues into a third.  On completion
   *  of this method, q1 and q2 are empty, and their items have been merged
   *  into the returned queue.
   *  @param q1 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @param q2 is LinkedQueue of Comparable objects, sorted from smallest 
   *    to largest.
   *  @return a LinkedQueue containing all the Comparable objects from q1 
   *   and q2 (and nothing else), sorted from smallest to largest.
   **/
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static LinkedQueue mergeSortedQueues(LinkedQueue q1, LinkedQueue q2) {
    LinkedQueue q = new LinkedQueue();
    try {
      while (!q1.isEmpty() && !q2.isEmpty()) {
        Comparable item1 = (Comparable) q1.front();
        Comparable item2 = (Comparable) q2.front();
        if (item1.compareTo(item2) <= 0) { // <= makes a stable sort
          q.enqueue(q1.dequeue());
        } else {
          q.enqueue(q2.dequeue());
        }
      }
      // only one of the following two loops will execute
      while (!q1.isEmpty()) {
        q.enqueue(q1.dequeue());
      }
      while (!q2.isEmpty()) {
        q.enqueue(q2.dequeue());
      }
    } catch (QueueEmptyException uf) {
      System.err.println("Error:  attempt to dequeue from empty queue.");
    }
    return q;
  }

  /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a LinkedQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a LinkedQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a LinkedQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a LinkedQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/   
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void partition(LinkedQueue qIn, Comparable pivot, 
      LinkedQueue qSmall, LinkedQueue qEquals, 
      LinkedQueue qLarge) {
    try {
      while (!qIn.isEmpty()) {
        Comparable c = (Comparable) qIn.dequeue();
        if (c.compareTo(pivot) < 0) {
          qSmall.enqueue(c);
        } else if (c.compareTo(pivot) > 0) {
          qLarge.enqueue(c);
        } else {
          qEquals.enqueue(c);
        }
      }
    } catch (QueueEmptyException uf) {
      System.err.println("Error:  attempt to dequeue from empty queue.");
    }
  }

  /**
   *  mergeSort() sorts q from smallest to largest using mergesort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  public static void mergeSort(LinkedQueue q) {
    if (q.size() <= 1) {
      return;
    }
    LinkedQueue queues = makeQueueOfQueues(q);
    LinkedQueue queuesTemp = new LinkedQueue();
    try {
      while (queues.size() > 1) {
        while (queues.size() > 1) {
          LinkedQueue q1 = (LinkedQueue) queues.dequeue();
          LinkedQueue q2 = (LinkedQueue) queues.dequeue();
          LinkedQueue comb = mergeSortedQueues(q1, q2);
          queuesTemp.enqueue(comb);
        }
        // if there was an odd number of queues in q, put the extra one in the temp
        if (!queues.isEmpty()) {
          queuesTemp.enqueue(queues.dequeue());
        }
        queues = queuesTemp;
        queuesTemp = new LinkedQueue();
      }
      queues = (LinkedQueue) queues.dequeue();
      q.append(queues);
    } catch (QueueEmptyException uf) {
      System.err.println("Error:  attempt to dequeue from empty queue.");
    }
  }

  /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
  @SuppressWarnings("rawtypes")
  public static void quickSort(LinkedQueue q) {
    if (q.size() < 2) {
      return;
    }
    LinkedQueue qSmall = new LinkedQueue();
    LinkedQueue qEquals = new LinkedQueue();
    LinkedQueue qLarge = new LinkedQueue();
    
    int pivotPos = (int) (1 + q.size() * Math.random());
    Comparable pivot = (Comparable) q.nth(pivotPos);
    partition(q, pivot, qSmall, qEquals, qLarge);
    quickSort(qSmall);
    quickSort(qLarge);
    q.append(qSmall);
    q.append(qEquals);
    q.append(qLarge);
  }

  /**
   *  makeRandom() builds a LinkedQueue of the indicated size containing
   *  Integer items.  The items are randomly chosen between 0 and size - 1.
   *  @param size is the size of the resulting LinkedQueue.
   **/
  public static LinkedQueue makeRandom(int size) {
    LinkedQueue q = new LinkedQueue();
    for (int i = 0; i < size; i++) {
      q.enqueue(new Integer((int) (size * Math.random())));
    }
    return q;
  }

  /**
   *  main() performs some tests on mergesort and quicksort.  Feel free to add
   *  more tests of your own to make sure your algorithms works on boundary
   *  cases.  Your test code will not be graded.
   **/
  public static void main(String [] args) {

    LinkedQueue test1 = new LinkedQueue();
    test1.enqueue(new Integer(5));
    test1.enqueue(new Integer(8));
    test1.enqueue(new Integer(3));
    test1.enqueue(new Integer(4));
    test1.enqueue(new Integer(1));
    System.out.println(test1);
    LinkedQueue queues1 = makeQueueOfQueues(test1);
    System.out.println(queues1);
    System.out.println(test1);

    LinkedQueue q2 = new LinkedQueue();
    mergeSort(q2);
    System.out.println(q2.toString());

    LinkedQueue q3 = makeRandom(1);
    System.out.println(q3.toString());
    mergeSort(q3);
    System.out.println(q3.toString());

    LinkedQueue q4 = makeRandom(2);
    System.out.println(q4.toString());
    mergeSort(q4);
    System.out.println(q4.toString());
    System.out.println();

    // given test code

    LinkedQueue q = makeRandom(10);
    System.out.println(q.toString());
    mergeSort(q);
    System.out.println(q.toString());

    q = makeRandom(10);
    System.out.println(q.toString());
    quickSort(q);
    System.out.println(q.toString());

    Timer stopWatch = new Timer();
    q = makeRandom(SORTSIZE);
    stopWatch.start();
    mergeSort(q);
    stopWatch.stop();
    System.out.println("Mergesort time, " + SORTSIZE + " Integers:  " +
                       stopWatch.elapsed() + " msec.");

    stopWatch.reset();
    q = makeRandom(SORTSIZE);
    stopWatch.start();
    quickSort(q);
    stopWatch.stop();
    System.out.println("Quicksort time, " + SORTSIZE + " Integers:  " +
                       stopWatch.elapsed() + " msec.");
  }

}
