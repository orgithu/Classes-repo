/** merge two sorted lists */

package dataStructures;

public class ArrayLinearListWithMerge
       extends ArrayLinearList
{
   /** make this the result of merging the sorted lists a and b */
   public void merge(ArrayLinearListWithMerge a,
                     ArrayLinearListWithMerge b)
   {
      int ca = 0;                       // cursor for a
      int cb = 0;                       // cursor for b
      int ct = 0;                       // cursor for this
      // get big enough array for result
      // if you opt to do this only when a.size + b.size > element.length,
      // be sure to set element[a.size + b.size : size] to null
      // to enable garbage collection
      element = new Object [a.size + b.size];

      // merge from a  and b
      while ((ca < a.size) && (cb < b.size))
         if (((Comparable) a.element[ca]).compareTo(b.element[cb]) <= 0)
            element[ct++] = a.element[ca++];
         else
            element[ct++] = b.element[cb++];
   
      // take care of left overs
      if (ca == a.size)                 // a is finished
         for (int q = cb; q < b.size; q++)
            element[ct++] = b.element[q];
      else
         for (int q = ca; q < a.size; q++)
            element[ct++] = a.element[q];
      size = ct;

      // code to set excess elements of this.element to null
      // to enable garbage collection has been omitted
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 10, m = 5;
      ArrayLinearListWithMerge x = new ArrayLinearListWithMerge();
      ArrayLinearListWithMerge y = new ArrayLinearListWithMerge();
      ArrayLinearListWithMerge z = new ArrayLinearListWithMerge();
      
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
      z.merge(y, x);
      System.out.println("Merged list is " + z);
   }
}
