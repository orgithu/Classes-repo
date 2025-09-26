
/** shaker sort */

package applications;

import utilities.*;

public class ShakerSort
{
   /** sort the elements a[0 : a.length - 1] using
     * the shaker sort method */
   public static void shakerSort(Comparable [] a)
   {
      for (int p = 1; p <= a.length / 2; p++)
      {// phase p of shaker sort
         // first do left to right bubbling pass
         for (int i = p - 1; i < a.length - p; i++)
            if (a[i].compareTo(a[i+1]) > 0)
               MyMath.swap(a, i, i + 1);
        
         // now do right to left bubbling pass
         for (int i = a.length - p - 1; i >= p; i--)
            if (a[i].compareTo(a[i-1]) < 0)
               MyMath.swap(a, i, i - 1);
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
      shakerSort(a);

      // output in sorted order
      System.out.println("The sorted order is");
      for (int i = 0; i < a.length; i++)
         System.out.print(a[i] + " ");
      System.out.println();
   }
}

