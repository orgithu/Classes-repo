
/** find an element by rank, do not use recursion */

package applications;

import utilities.*;

public class ModifiedSelect
{
   /** @return k'th smallest elment in a[0 : a.length - 1] */
   public static Comparable select(Comparable [] a, int k)
   {
      if (k < 1 || k > a.length)
         throw new IllegalArgumentException
            ("k must be between 1 and a.length");

      // move largest element to right end
      MyMath.swap(a, a.length - 1, MyMath.max(a, a.length - 1));

      // set left and right ends of working segment
      int leftEnd = 0;
      int rightEnd = a.length - 1;

      while (leftEnd < rightEnd)
      {
         int leftCursor = leftEnd,                 // left-to-right cursor
             rightCursor = rightEnd + 1;           // right-to-left cursor
         Comparable pivot = a[leftEnd];
      
         // swap elements >= pivot on left side
         // with elements <= pivot on right side
         while (true)
         {
            do
            {// find >= element on left side
               leftCursor++;
            } while (a[leftCursor].compareTo(pivot) < 0);
   
            do
            {// find <= element on right side
               rightCursor--;
            } while (a[rightCursor].compareTo(pivot) > 0);
   
            if (leftCursor >= rightCursor) break;  // swap pair not found
            MyMath.swap(a, leftCursor, rightCursor);
         }
      
         if (rightCursor - leftEnd + 1 == k) return pivot;
   
         // place pivot
         a[leftEnd] = a[rightCursor];
         a[rightCursor] = pivot;
      
         // set working segment and new k
         if (rightCursor - leftEnd + 1 < k)
         {// target element is k - (rightCursor - leftEnd + 1)'th
          // element in right part
            k -= rightCursor - leftEnd + 1;
            leftEnd = rightCursor + 1;
         }
         else
            // target element is k'th element in left part
            rightEnd = rightCursor - 1;
      }
      return a[leftEnd];
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

      // output elements
      System.out.println("The elements are");
      for (int i = 0; i < a.length; i++)
         System.out.print(a[i] + " ");
      System.out.println();

      // output by rank
      for (int j = 1; j <= a.length; j++)
         System.out.println("The " + j + " element is " + select(a, j));
   }
}

