
/** reverse a linear list represented using Equation 5.3
  * method is not a member of ArrayCircularList */

package dataStructures;

import utilities.*;

public class ReverseArrayCircularList
{
   public static void reverse(ArrayCircularList x)
   {
      int sizeMinus1 = x.size() - 1;
      for (int i = 0; i < sizeMinus1; i++)
      {
         // retrieve and remove last element
         Object y = x.remove(sizeMinus1);

         // insert at proper place
         x.add(i, y);
      }
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 10, m = 5;
      ArrayCircularList x =
         new ArrayCircularList();
      ArrayCircularList y =
         new ArrayCircularList();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(4 * n - i));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i)));
      
      System.out.println("The reverse of " + x);
      reverse(x);
      System.out.println("is " + x);
      System.out.println("The reverse of " + y);
      reverse(y);
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
      reverse(x);
      System.out.println("is " + x);
   }
}
