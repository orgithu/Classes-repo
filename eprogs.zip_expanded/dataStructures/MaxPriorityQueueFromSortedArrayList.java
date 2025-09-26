   
/** max priority queue derived from SortedArrayList2 */

package dataStructures;

import java.util.*;

public class MaxPriorityQueueFromSortedArrayList
                        extends SortedArrayList2
                        implements MaxPriorityQueue
{
   // constructors
   /** create a priority queue with the given initial capacity */
   public MaxPriorityQueueFromSortedArrayList(int initialCapacity)
      {super(initialCapacity);}
   
   /** create a priority queue with initial capacity 10 */
   public MaxPriorityQueueFromSortedArrayList()
      {super(10);}

   // methods isEmpty, size, and put are inherited from SortedArrayList2

   /** @return maximum element
     * @return null if the priority queue is empty */
   public Comparable getMax()
   {
      if (size() == 0)
         // priority queue is empty
         return null;
      else
         return get(size() - 1);
   }

   /** remove max element and return it */
   public Comparable removeMax()
   {
      // if priority queue is empty return null
      if (size() == 0)
         return null;

      // save and remove max element
      Comparable maxElement = get(size() - 1);
      remove(size() - 1);
   
      return maxElement;
   }
   
   /** put theElement into the priority queue */
   public void put(Comparable theElement)
      {add(theElement);}

   /** test program */
   public static void main(String [] args)
   {
      // test constructor and put
      MaxPriorityQueueFromSortedArrayList h =
                 new MaxPriorityQueueFromSortedArrayList(4);
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
