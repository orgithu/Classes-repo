

/** merge two circular lists */

package dataStructures;

import java.util.*;

public class MergeCircularList
{
   /** merge the sorted lists a and b */
   public static CircularList merge(CircularList a, CircularList b)
   {
      // initialize iterators for a and b
      Iterator ia = a.iterator();     // iterator for a
      Iterator ib = b.iterator();     // iterator for b
      Comparable elementA = null;     // current element of a
      if (ia.hasNext())
         elementA = (Comparable) ia.next();
      Object elementB = null;         // current element of b
      if (ib.hasNext())
         elementB = ib.next();
   
      // create result list
      CircularList c = new CircularList();
   
      // do the merge
      while (elementA != null && elementB != null)
      {
         if (elementA.compareTo(elementB) <= 0)
         {// elementA goes next
            c.add(elementA);
            if (ia.hasNext())
               elementA = (Comparable) ia.next();
            else
               elementA = null;
         }
         else
         {// elementB goes next
            c.add(elementB);
            if (ib.hasNext())
               elementB = ib.next();
            else
               elementB = null;
         }
      }
   
      // add remaining elements
      if (elementA != null)
         c.add(elementA);
      if (elementB != null)
         c.add(elementB);

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
      CircularList x = new CircularList();
      CircularList y = new CircularList();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i) + 1));
      
      System.out.println("First list is " + x);
      System.out.println("Second list is " + y);
      System.out.println("Merged list is " + merge(x, y));
   }
}
