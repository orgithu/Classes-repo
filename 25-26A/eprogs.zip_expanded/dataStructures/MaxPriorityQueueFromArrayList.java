   
/** max priority queue derived from ArrayLinearListWithIterator */

package dataStructures;

import java.util.*;

public class MaxPriorityQueueFromArrayList
                        extends ArrayLinearListWithIterator
                        implements MaxPriorityQueue
{
   // constructors
   /** create a priority queue with the given initial capacity */
   public MaxPriorityQueueFromArrayList(int initialCapacity)
      {super(initialCapacity);}
   
   /** create a priority queue with initial capacity 10 */
   public MaxPriorityQueueFromArrayList()
      {super(10);}

   // methods isEmpty and size are inherited

   /** @return maximum element
     * @return null if the priority queue is empty */
   public Comparable getMax()
   {
      if (size() == 0)
         // priority queue is empty
         return null;
   
      // find max element by examining all elements in the queue
      // use the iterator it to avoid accessing the
      // data members element and size
      Iterator it = iterator();
      Comparable currentMax = (Comparable) get(0);
      it.next();
      while (it.hasNext())
      {
         Object temp = it.next();
         if (currentMax.compareTo(temp) < 0)
            currentMax = (Comparable) temp;
      }

      return currentMax;
   }

   /** put theElement into the priority queue */
   public void put(Comparable theElement)
      {add(size(), theElement);}
   
   /** remove max element and return it */
   public Comparable removeMax()
   {
      // if priority queue is empty return null
      if (size() == 0)
         return null;
   
      // find max element by examining all elements in the queue
      Iterator it = iterator();
      Comparable currentMax = (Comparable) get(0);
      int pos = 0;             // index of currentMax
      it.next();
      int currentPos = 0;      // index of current element
      while (it.hasNext())
      {
         Object temp = it.next();
         currentPos++;
         if (currentMax.compareTo(temp) < 0)
         {
            currentMax = (Comparable) temp;
            pos = currentPos;
         }
      }

      // remove max element from priority queue
      remove(pos);
   
      return currentMax;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test constructor and put
      MaxPriorityQueueFromArrayList h =
                 new MaxPriorityQueueFromArrayList(4);
      h.put(new Integer(10));
      h.put(new Integer(20));
      h.put(new Integer(5));

      // test toString
      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      h.put(new Integer(15));
      h.put(new Integer(30));

      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      // test remove max
      System.out.println("The max element is " + h.getMax());
      System.out.println("Deleted max element " + h.removeMax());
      System.out.println("Deleted max element " + h.removeMax());
      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();
   }
}
