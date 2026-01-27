/** quick sort with last recursive call replaced by a while loop */

package applications;

import utilities.*;

public class ModifiedQuickSort
{
   // data member
   static Comparable [] a;   // array of elements to be sorted

   /** sort a[0 : a.length - 1] using the quick sort method */
   public static void quickSort(Comparable [] a)
   {
      ModifiedQuickSort.a = a;
      if (a.length <= 1) return;
      // move largest element to right end
      MyMath.swap(a, a.length - 1, MyMath.max(a, a.length - 1));
      quickSort(0, a.length - 2);
   }
   
   /** sort a[leftEnd:rightEnd], a[rightEnd+1] >= a[leftEnd:rightEnd] */
   private static void quickSort(int leftEnd, int rightEnd)
   {

      while (leftEnd < rightEnd)
      {// at least 2 elements to sort
         int leftCursor = leftEnd,        // left-to-right cursor
             rightCursor = rightEnd + 1;  // right-to-left cursor
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
      
         // place pivot
         a[leftEnd] = a[rightCursor];
         a[rightCursor] = pivot;
      
         quickSort(leftEnd, rightCursor - 1);   // sort left segment
         leftEnd = rightCursor + 1;             // set to sort right segment
      }
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
      quickSort(a);

      // output in sorted order
      System.out.println("The sorted order is");
      for (int i = 0; i < a.length; i++)
         System.out.print(a[i] + " ");
      System.out.println();
   }
}

