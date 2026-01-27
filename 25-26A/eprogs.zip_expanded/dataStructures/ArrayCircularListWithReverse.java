
/** reverse a linear list represented using Equation 5.3 */

package dataStructures;

import utilities.*;

public class ArrayCircularListWithReverse
       extends ArrayCircularList
{
   // constructors
   public ArrayCircularListWithReverse(int initialCapacity)
      {super(initialCapacity);}

   public ArrayCircularListWithReverse()
   {// use default capacity of 10
      this(10);
   }

   /** reverse the linear list */
   public void reverse()
   {
      int size = size();
      for (int i = 0; i < size / 2; i++)
          MyMath.swap(element, (first + i) % element.length,
                     (element.length + last - i) % element.length);
   }


   /** test program */
   public static void main(String [] args)
   {
      int n = 10, m = 5;
      ArrayCircularListWithReverse x =
         new ArrayCircularListWithReverse();
      ArrayCircularListWithReverse y =
         new ArrayCircularListWithReverse();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(4 * n - i));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i)));
      
      System.out.println("The reverse of " + x);
      x.reverse();
      System.out.println("is " + x);
      System.out.println("The reverse of " + y);
      y.reverse();
      System.out.println("is " + y);


      // remove a few elements from the end of x
      x.remove(x.size() - 1);
      x.remove(x.size() - 1);
      x.remove(x.size() - 1);
      x.remove(x.size() - 1);

      // add a few more at the front
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * n - i));

      System.out.println("The reverse of " + x);
      x.reverse();
      System.out.println("is " + x);
   }
}
