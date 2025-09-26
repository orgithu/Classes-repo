
/** reverse a linear list represented using Equation 3.2 */

package dataStructures;

import wrappers.*;
import utilities.*;

public class FormulaBasedLinearList8WithReverse
       extends FormulaBasedLinearList8
{
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
      FormulaBasedLinearList8WithReverse x =
         new FormulaBasedLinearList8WithReverse();
      FormulaBasedLinearList8WithReverse y =
         new FormulaBasedLinearList8WithReverse();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.insertElementAt(new MyInteger(4 * n - i), 0);
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.insertElementAt(new MyInteger(2*(n - i)), 0);
      
      System.out.print("The reverse of " + x + " is ");
      x.reverse();
      System.out.println(x);
      System.out.print("The reverse of " + y + " is ");
      y.reverse();
      System.out.println(y);


      // remove a few elements from the end of x
      x.removeElementAt(x.size() - 1);
      x.removeElementAt(x.size() - 1);
      x.removeElementAt(x.size() - 1);
      x.removeElementAt(x.size() - 1);

      // add a few more at the front
      for (int i = 0; i < n; i++)
         x.insertElementAt(new MyInteger(2 * n - i), 0);

      System.out.print("The reverse of " + x + " is ");
      x.reverse();
      System.out.println(x);
   }
}
