

/** a queue class that is derived from ArrayDeque */

package dataStructures;

public class DequeQueue extends ArrayDeque
                        implements Queue
{
   // constructors
   /** create a queue with the given initial capacity */
   public DequeQueue(int initialCapacity)
      {super(initialCapacity);}

   /** create a queue with initial capacity 10 */
   public DequeQueue()
   {// use default capacity of 10
      this(10);
   }

   // methods
   /** @return front element of queue
     * @return null if queue is empty */
   public Object getFrontElement()
      {return getLeftElement();}

   /** @return rear element of queue
     * @return null if the queue is empty */
   public Object getRearElement()
      {return getRightElement();}

   /** insert theElement at the rear of the queue */
   public void put(Object theElement)
      {putAtRight(theElement);}

   /** remove an element from the front of the queue
     * @return removed element
     * @return null if the queue is empty */
   public Object remove()
      {return removeFromLeft();}
   
   /** test program */
   public static void main(String [] args)
   {  
      int x;
      DequeQueue q = new DequeQueue(3);
      // add a few elements
      q.put(new Integer(1));
      q.put(new Integer(2));
      q.put(new Integer(3));
      q.put(new Integer(4));

      // remove and add to test wraparound array doubling
      q.remove();
      q.remove();
      q.put(new Integer(5));
      q.put(new Integer(6));
      q.put(new Integer(7));
      q.put(new Integer(8));
      q.put(new Integer(9));
      q.put(new Integer(10));
      q.put(new Integer(11));
      q.put(new Integer(12));

      // delete all elements
      while (!q.isEmpty())
      {
         System.out.println("Rear element is " + q.getRearElement());
         System.out.println("Front element is " + q.getFrontElement());
         System.out.println("Removed the element " + q.remove());
      }
   }  
}
