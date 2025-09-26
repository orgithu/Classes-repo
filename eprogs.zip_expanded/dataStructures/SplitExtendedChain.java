
/** split an extended chain into two */

package dataStructures;

import java.util.*;

public class SplitExtendedChain
{
   /** split a into b and c */
   public static void split(ExtendedChain a, ExtendedChain b,
                            ExtendedChain c)
   {
      // first empty out b and c
      b.clear();
      c.clear();
   
      // assign elements of a alternately to b and c using an
      // iterator ia for a
      Iterator ia = a.iterator();
      while (ia.hasNext())
      {
         // first give b an element
         b.add(ia.next());
         if (!ia.hasNext()) break;

         // now give c an element
         c.add(ia.next());
      }
   }
   
   /** test program */
   public static void main(String [] args)
   {
      int n = 10, m = 5;
      ExtendedChain x = new ExtendedChain();
      ExtendedChain y = new ExtendedChain();
      ExtendedChain a = new ExtendedChain();
      ExtendedChain b = new ExtendedChain();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i) + 1));
      
      System.out.println("The list is " + x);
      split(x, a, b);
      System.out.println("The split lists are " + a + " and " + b);
      System.out.println();
      
      System.out.println("The list is " + y);
      split(y, a, b);
      System.out.println("The split lists are " + a + " and " + b);
      System.out.println();
   }
}
