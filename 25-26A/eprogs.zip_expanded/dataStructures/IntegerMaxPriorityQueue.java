/** max priority queue for integer priorities */

package dataStructures;

public class IntegerMaxPriorityQueue
{
   ArrayQueue [] priority;  // queues of elements with same priority
   int size;                // number of elements in queues
   int highestNonEmpty;     // max nonempty queue

   // constructors
   /** create a priority queue with the given max priority
     * @throws IllegalArgumentException when maxPriority < 1 */
   public IntegerMaxPriorityQueue(int maxPriority)
   {
      if (maxPriority < 1)
         throw new IllegalArgumentException
               ("Max priority must be >= 1, it is " + maxPriority);

      // create the queues
      priority = new ArrayQueue [maxPriority + 1];
      for (int i = 1; i <= maxPriority; i++)
         priority[i] = new ArrayQueue();

      // size and highestNonEmpty are 0
   }
   
   /** @return true iff no elements */
   public boolean isEmpty()
      {return size == 0;}

   /** @return number of elements in all queues together */
   public int size()
      {return size;}

   /** @return maximum element
     * @return null if the priority queue is empty */
   public Object getMax()
   {
      if (size == 0)
         // priority queue is empty
         return null;
   
      return priority[highestNonEmpty].getFrontElement();
   }

   /** put theElement into the priority queue
     * @throws IllegalArgumentException when thePriority > maxPriority
     * or thePriority < 1 */
   public void put(int thePriority, Object theElement)
   {
      if (thePriority < 1 || thePriority >= priority.length)
         throw new IllegalArgumentException
               ("Priority must be between 1 and " + (priority.length - 1)
                + " it is " + thePriority);

      // put theElement into the proper queue
      priority[thePriority].put(theElement);
      size++;

      if (thePriority > highestNonEmpty)
         highestNonEmpty = thePriority;
   }

   
   /** remove max element and return it */
   public Object removeMax()
   {
      // if priority queue is empty return null
      if (size() == 0)
         return null;
   
      // remove max element from priority queue
      Object removedElement = priority[highestNonEmpty].remove();
      size--;

      if (priority[highestNonEmpty].isEmpty())
         // update highestNonEmpty
         if (size == 0)
            highestNonEmpty = 0;
         else
            while (priority[--highestNonEmpty].isEmpty());
   
      return removedElement;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test constructor and put
      IntegerMaxPriorityQueue h =
                 new IntegerMaxPriorityQueue(4);
      h.put(1, new Integer(10));
      h.put(4, new Integer(20));
      h.put(4, new Integer(5));
      h.put(2, new Integer(15));
      h.put(2, new Integer(30));

      System.out.println("Elements in priority order are");
      while (!h.isEmpty())
         System.out.println(h.getMax() + " " + h.removeMax());
   }
}
