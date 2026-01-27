/** merge two sorted lists */

package dataStructures;

import wrappers.*;
import utilities.*;

public class FormulaBasedLinearListWithMerge
       extends FormulaBasedLinearList
{
   /** make this the result of merging the sorted lists a and b */
   public void merge(FormulaBasedLinearListWithMerge a,
                     FormulaBasedLinearListWithMerge b)
   {
      int ca = 0;                       // cursor for a
      int cb = 0;                       // cursor for b
      int ct = 0;                       // cursor for this
      // make sure this.element.length is large enough
      if (element.length < a.size + b.size)
         element = new Object [a.size + b.size];

      // merge from a  and b
      while ((ca < a.size) && (cb < b.size))
         if (((Comparable) a.element[ca]).lessThanOrEqual(b.element[cb]))
            element[ct++] = a.element[ca++];
         else element[ct++] = b.element[cb++];
   
      // take care of left overs
      if (ca == a.size)                 // a is finished
         for (int q = cb; q < b.size; q++)
            element[ct++] = b.element[q];
      else
         for (int q = ca; q < a.size; q++)
            element[ct++] = a.element[q];
      size = ct;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 10, m = 5;
      FormulaBasedLinearListWithMerge x = new FormulaBasedLinearListWithMerge();
      FormulaBasedLinearListWithMerge y = new FormulaBasedLinearListWithMerge();
      FormulaBasedLinearListWithMerge z = new FormulaBasedLinearListWithMerge();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.insertElementAt(new MyInteger(2*(n - i)), 0);
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.insertElementAt(new MyInteger(2*(n - i)), 0);
      
      System.out.println("First list is " + x);
      System.out.println("Second list is " + y);
      z.merge(x, y);
      System.out.println("Merged list is " + z);
      z.merge(y, x);
      System.out.println("Merged list is " + z);
   }
}
