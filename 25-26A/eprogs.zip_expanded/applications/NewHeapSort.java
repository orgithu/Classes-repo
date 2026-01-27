
/** heap sort using MaxHeapWithMinAndMaxElements */

package applications;

import dataStructures.*;

public class NewHeapSort
{
   /** sort the elements a[1 : a.length - 1] using
     * the heap sort method */
   public static void heapSort(Comparable [] a)
   {
      if (a.length <= 2)
         // array has fewer than two elements
         return;

      // more than 1 element, first find min and max elements
      Comparable min = a[1];
      Comparable max = a[1];
      for (int i = 2; i < a.length; i++)
         if (min.compareTo(a[i]) > 0)
            min = a[i];
         else
            if (max.compareTo(a[i]) < 0)
               max = a[i];
   
      // create a max heap of the elements
      MaxHeapWithMinAndMaxElements h =
             new MaxHeapWithMinAndMaxElements(1, min, max);
      h.initialize(a, a.length - 1);
   
      // extract one by one from the max heap
      for (int i = a.length - 1; i >= 1; i--)
         a[i] = h.removeMax();
        
   }

   /** test program */
   public static void main(String [] args)
   {
      Integer [] a = {new Integer(3),
                        new Integer(2),
                        new Integer(4),
                        new Integer(1),
                        new Integer(6),
                        new Integer(9),
                        new Integer(8),
                        new Integer(7),
                        new Integer(5),
                        new Integer(0)};

      // output elements to be sorted
      System.out.println("The elements are");
      for (int i = 1; i < a.length; i++)
         System.out.print(a[i] + " ");
      System.out.println();

      // sort the elements
      heapSort(a);

      // output in sorted order
      System.out.println("The sorted order is");
      for (int i = 1; i < a.length; i++)
         System.out.print(a[i] + " ");
      System.out.println();
   }
}
