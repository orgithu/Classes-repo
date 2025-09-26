
/** natural merge sort */

package applications;

import dataStructures.*;

public class NaturalMergeSort
{
   // define a queue used for the start positions of sorted segments
   static ArrayQueue queue = new ArrayQueue(10);
   static int numberOfSegments;

   /** sort the elements a[0 : a.length - 1] using
     * the natural merge sort method */
   public static void naturalMergeSort(Comparable [] a)
   {
      if (a.length <= 1)
         // already sorted
         return;

      // define an auxiliary array to faciltate merging
      Comparable [] b = new Comparable [a.length];

      // find the natural segments and save their start positions in a queue
      queue.put(new Integer(0));  // first segment starts at 0
      numberOfSegments = 1;
      for (int i = 1; i < a.length; i++)
         if (a[i - 1].compareTo(a[i]) > 0)
         {// a new segment starts at i
            numberOfSegments++;
            queue.put(new Integer(i));
         }

      while (numberOfSegments > 1)
      {// numberOfSegments is updated by naturalMergePass
         naturalMergePass(a, b); // merge from a to b
         naturalMergePass(b, a); // merge from b to a
      }
   }

   /** merge pairs of adjacent segments from x to y*/
   public static void naturalMergePass(Comparable [] x, Comparable [] y)
   {
      for (int i = 1; i < numberOfSegments; i += 2)
      {// merge two segments
         // find the start and end of the two segments
         Object a = queue.remove();
         int startA = ((Integer) a).intValue();
         int startB = ((Integer) queue.remove()).intValue();
         Object z = queue.getFrontElement();
         int endB = (z == null || ((Integer) z).intValue() == 0) ?
                       x.length - 1 : ((Integer) z).intValue() - 1;
         // merge them 
         merge(x, y, startA, startB - 1, endB);

         // put segment start on queue
         queue.put(a);
      }
   
      // one segment may remain
      if (numberOfSegments % 2 == 1)
      {// copy last segment to y
         Object a = queue.remove();
         int startA = ((Integer) a).intValue();
         for (int i = startA; i < x.length; i++)
            y[i] = x[i];
         queue.put(a);
      }

      // update number of segments
      if (numberOfSegments % 2 == 1)
         numberOfSegments = numberOfSegments / 2 + 1;
      else
         numberOfSegments /= 2;
   }
   
   /** merge c[l:m] and c[m + 1:r] to d[l:r] */
   public static void merge(Comparable [] c, Comparable [] d,
                            int l, int m, int r)
   {
      int i = l,      // cursor for first segment
          j = m + 1,  // cursor for second
          k = l;      // cursor for result
   
      // merge until i or j exits its segment
      while ((i <= m) && (j <= r))
         if (c[i].compareTo(c[j]) <= 0)
            d[k++] = c[i++];
         else
            d[k++] = c[j++];
   
      // take care of left overs
      if (i > m)
         for (int q = j; q <= r; q++)
             d[k++] = c[q];
      else
         for (int q = i; q <= m; q++)
             d[k++] = c[q];
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
      for (int i = 0; i < a.length; i++)
         System.out.print(a[i] + " ");
      System.out.println();

      // sort the elements
      naturalMergeSort(a);

      // output in sorted order
      System.out.println("The sorted order is");
      for (int i = 0; i < a.length; i++)
         System.out.print(a[i] + " ");
      System.out.println();
   }
}

