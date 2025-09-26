
/** merge two sorted lists represented as circular lists with header nodes */

package dataStructures;

public class HeadCircularListWithMerge extends HeadCircularList
{
   /** make this the result of merging the sorted lists a and b 
     * following the merge, both a and b are empty */
   public void merge(HeadCircularListWithMerge a, HeadCircularListWithMerge b)
   {
      ChainNode ca = a.headerNode.next,     // pointer to current node in a
                cb = b.headerNode.next;     // pointer to current node in b

      ChainNode ct = headerNode;            // pointer to last node in this

      // merge from a and b until one becomes empty
      while (ca != a.headerNode && cb != b.headerNode)
         if (((Comparable) ca.element).compareTo(cb.element) <= 0)
         {// a element goes first
            ct.next = ca;
            ct = ca;
            ca = ca.next;
         }
         else
         {// b element is smaller
            ct.next = cb;
            ct = cb;
            cb = cb.next;
         }

      // append the rest
      if (ca != a.headerNode)
      {
         ct.next = ca;
         ct = a.lastNode;
      }  

      if (cb != b.headerNode)
      {
         ct.next = cb;
         ct = b.lastNode;
      }

      // make into a circular list
      ct.next = headerNode;
      lastNode = ct;
      size = a.size + b.size;

      // make a and b empty
      a.clear();
      b.clear();

      return;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 8, m = 5;
      HeadCircularListWithMerge x = new HeadCircularListWithMerge();
      HeadCircularListWithMerge y = new HeadCircularListWithMerge();
      HeadCircularListWithMerge z = new HeadCircularListWithMerge();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i) - 1));
      
      System.out.println("First list is " + x);
      System.out.println("Second list is " + y);
      z.merge(x, y);
      System.out.println("Merged list is " + z);
      System.out.println();

      System.out.println("First list is " + x);
      System.out.println("Second list is " + y);
      z.merge(y, x);
      System.out.println("Merged list is " + z);
      System.out.println();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i) - 1));
      
      System.out.println("First list is " + y);
      System.out.println("Second list is " + x);
      z.merge(y, x);
      System.out.println("Merged list is " + z);
      System.out.println();
   }
}
