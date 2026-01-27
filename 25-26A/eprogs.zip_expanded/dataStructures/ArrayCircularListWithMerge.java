
/** merge two sorted lists represented using Equation 5.3 */

package dataStructures;

import utilities.*;

public class ArrayCircularListWithMerge
       extends ArrayCircularList
{
   // constructors
   public ArrayCircularListWithMerge(int initialCapacity)
      {super(initialCapacity);}

   public ArrayCircularListWithMerge()
   {// use default capacity of 10
      this(10);
   }

   /** make this the result of merging the sorted lists a and b */
   public void merge(ArrayCircularListWithMerge a,
                     ArrayCircularListWithMerge b)
   {
      int ca = a.first;                 // cursor for a
      int cb = b.first;                 // cursor for b
      int ct = 0;                       // cursor for this
      int na = 0;                       // number merged from a
      int nb = 0;                       // number merged from b
      int as = a.size();                // size of a
      int bs = b.size();                // size of b
      // get big enough array for result
      // if you opt to do this only when as + bs > element.length,
      // be sure to set relevant positions of element to null
      // to enable garbage collection
      element = new Object [as + bs];

      // merge from a and b
      while ((na < as) && (nb < bs))
         if (((Comparable) a.element[ca]).compareTo(b.element[cb]) <= 0)
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
      ArrayCircularListWithMerge x =
                        new ArrayCircularListWithMerge();
      ArrayCircularListWithMerge y = 
                        new ArrayCircularListWithMerge();
      ArrayCircularListWithMerge z = 
                        new ArrayCircularListWithMerge();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i)));
      
      System.out.println("First list is " + x);
      System.out.println("Second list is " + y);
      z.merge(x, y);
      System.out.println("Merged list is " + z);
      z.merge(x, y);
      System.out.println("Merged list is " + z);
   }
}
