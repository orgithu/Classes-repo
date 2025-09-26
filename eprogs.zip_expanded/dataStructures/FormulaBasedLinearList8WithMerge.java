
/** merge two sorted lists represented using Equation 3.2 */

package dataStructures;

import wrappers.*;
import utilities.*;

public class FormulaBasedLinearList8WithMerge
       extends FormulaBasedLinearList8
{
   /** make this the result of merging the sorted lists a and b */
   public void merge(FormulaBasedLinearList8WithMerge a,
                     FormulaBasedLinearList8WithMerge b)
   {
      int ca = a.first;                 // cursor for a
      int cb = b.first;                 // cursor for b
      int ct = 0;                       // cursor for this
      int na = 0;                       // number merged from a
      int nb = 0;                       // number merged from b
      int as = a.size();                // size of a
      int bs = b.size();                // size of b
      // this.element.length must be >= as + bs
      if (element.length < as + bs)
         element = new Object [as + bs];

      // merge from a and b
      while ((na < as) && (nb < bs))
         if (((Comparable) a.element[ca]).lessThanOrEqual(b.element[cb]))
         {// merge from a
            element[ct++] = a.element[ca];
            ca = (ca + 1) % a.element.length;
            na++;
         }
         else
         {// merge from b
            element[ct++] = b.element[cb];
            cb = (cb + 1) % b.element.length;
            nb++;
         }
   
      // take care of left overs
      if (na == a.size())                 // a is finished
         for (int q = nb; q < bs; q++)
         {
            element[ct++] = b.element[cb];
            cb = (cb + 1) % b.element.length;
         }
      else  // b is finished
         for (int q = na; q < as; q++)
         {
            element[ct++] = a.element[ca];
            ca = (ca + 1) % a.element.length;
         }
      first = 0;
      last = ct - 1;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 10, m = 5;
      FormulaBasedLinearList8WithMerge x =
                        new FormulaBasedLinearList8WithMerge();
      FormulaBasedLinearList8WithMerge y = 
                        new FormulaBasedLinearList8WithMerge();
      FormulaBasedLinearList8WithMerge z = 
                        new FormulaBasedLinearList8WithMerge();
      
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
      z.merge(x, y);
      System.out.println("Merged list is " + z);
   }
}
