
/** merge two sorted lists */

package dataStructures;

public class HeadDoubleCircularListWithMerge extends HeadDoubleCircularList
{
   /** make this the result of merging the sorted lists a and b 
     * following the merge, both a and b are empty */
   public void merge(HeadDoubleCircularListWithMerge a,
                     HeadDoubleCircularListWithMerge b)
   {
      DoubleNode
                ca = a.headerNode.next,     // pointer to current node in a
                cb = b.headerNode.next,     // pointer to current node in b
                lastNode = headerNode;      // last node of this

      // merge from a and b until one becomes empty
      while (ca != a.headerNode && cb != b.headerNode)
      {
         if (((Comparable) ca.element).compareTo(cb.element) <= 0)
         {// a element goes first
            lastNode.next = ca;
            ca.previous = lastNode;
            lastNode = ca;
            ca = ca.next;
         }
         else
         {// b element is smaller
            lastNode.next = cb;
            cb.previous = lastNode;
            lastNode = cb;
            cb = cb.next;
         }
      }

      // append the rest, at most one of a and b is nonempty now
      if (ca != a.headerNode)
      {
         lastNode.next = ca;
         ca.previous = lastNode;
         lastNode = a.headerNode.previous;
      }

      if (cb != b.headerNode)
      {
         lastNode.next = cb;
         cb.previous = lastNode;
         lastNode = b.headerNode.previous;
      }

      size = a.size + b.size;
      lastNode.next = headerNode;
      headerNode.previous = lastNode;

      // make a and b empty
      a.clear();
      b.clear();

      return;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 8, m = 5;
      HeadDoubleCircularListWithMerge x = 
          new HeadDoubleCircularListWithMerge();
      HeadDoubleCircularListWithMerge y = 
          new HeadDoubleCircularListWithMerge();
      HeadDoubleCircularListWithMerge z = 
          new HeadDoubleCircularListWithMerge();
      
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
