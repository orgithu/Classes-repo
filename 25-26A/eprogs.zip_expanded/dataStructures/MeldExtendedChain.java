/** meld two extended chains */

package dataStructures;

import java.util.*;

public class MeldExtendedChain
{
   /** meld alternately from a and b */
   public static ExtendedChain meld(ExtendedChain a, ExtendedChain b)
   {
      // initialize iterators for a and b
      Iterator ia = a.iterator();  // iterator for a
      Iterator ib = b.iterator();  // iterator for b
   
      // create result chain
      ExtendedChain c = new ExtendedChain();
   
      // do the meld
      while (ia.hasNext() && ib.hasNext())
      {
         c.add(ia.next());   // append at right end
         c.add(ib.next());   // append at right end
      }
   
      // append the rest
      // at most one of a and b can be nonempty now
      while (ia.hasNext())
         c.add(ia.next());
   
      while (ib.hasNext())
         c.add(ib.next());
   
      return c;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 10, m = 5;
      ExtendedChain x = new ExtendedChain();
      ExtendedChain y = new ExtendedChain();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i) + 1));
      
      System.out.println("First list is " + x);
      System.out.println("Second list is " + y);
      System.out.println("Melded list is " + meld(x, y));
   }
}
