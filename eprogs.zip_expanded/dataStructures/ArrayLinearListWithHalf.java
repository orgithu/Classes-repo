
/** ArrayLinearList with method half() */

package dataStructures;

public class ArrayLinearListWithHalf extends ArrayLinearList
{
   /** Save element[i], for i = 0, 2, 4, ...
     * Compact saved elements. */
   public void half()
   {
      for (int i = 2; i < size; i += 2)
         element[i/2] = element[i];

      int newSize = (size + 1) / 2;
      for (int i = newSize; i < size; i++)
         element[i] = null;   // enable garbage collection

      size = newSize;
   }

   /** test program */
   public static void main(String [] args)
   {
      // initialize two lists
      ArrayLinearListWithHalf x = new ArrayLinearListWithHalf();
      ArrayLinearListWithHalf y = new ArrayLinearListWithHalf();
      int n = 10;  // size of x
      int m = 5;   // size of y
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i) + 1));
   
      System.out.println("One list is " + x);
      x.half();
      System.out.println("After half(), the list is " + x);

      System.out.println("The second list is " + y);
      y.half();
      System.out.println("After half(), the list is " + y);

   }
}
