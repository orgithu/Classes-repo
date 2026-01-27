
/** FormulaBasedLinearList with method half() */

package dataStructures;

import java.util.*;
import wrappers.*;
import utilities.*;

public class FormulaBasedLinearList7 extends FormulaBasedLinearList
{
   /** Save element[i], for i = 0, 2, 4, ...
     * Compact saved elements. */
   public void half()
   {
      for (int i = 2; i < size; i += 2)
         element[i/2] = element[i];
      size = (size + 1) / 2;
   }

   /** test program */
   public static void main(String [] args)
   {
      // initialize two lists
      FormulaBasedLinearList7 x = new FormulaBasedLinearList7();
      FormulaBasedLinearList7 y = new FormulaBasedLinearList7();
      int n = 10;  // size of x
      int m = 5;   // size of y
      for (int i = 0; i < n; i++)
         x.insertElementAt(new MyInteger(2 * (n - i)), 0);
      for (int i = 0; i < m; i++)
         y.insertElementAt(new MyInteger(2 * (n - i) + 1), 0);
   
      System.out.println("One list is " + x);
      x.half();
      System.out.println("After half(), the list is " + x);

      System.out.println("The second list is " + y);
      y.half();
      System.out.println("After half(), the list is " + y);

   }
}
