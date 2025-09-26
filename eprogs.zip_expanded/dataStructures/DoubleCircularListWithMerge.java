
/** merge two sorted lists */

package dataStructures;

public class DoubleCircularListWithMerge extends DoubleCircularList
{
   /** make this the result of merging the sorted lists a and b 
     * following the merge, both a and b are empty */
   public void merge(DoubleCircularListWithMerge a,
                     DoubleCircularListWithMerge b)
   {
      // special cases
      if (a.lastNode == null)
      {// a is empty
         // move all of b to this
         lastNode = b.lastNode;
         size = b.size;

         // make b empty
         b.clear();

         return;
      }

      if (b.lastNode == null)
      {// b is empty
         // move all of a to this
         lastNode = a.lastNode;
         size = a.size;

         // make a empty
         a.clear();

         return;
      }
   
      // neither a nor b is empty
      DoubleNode
                ca = a.lastNode.next,       // pointer to current node in a
                cb = b.lastNode.next,       // pointer to current node in b
                firstNode;                  // first node of this

      // make a and b noncircular
      a.lastNode.next = null;
      b.lastNode.next = null;

      // merge first node for this
      if (((Comparable) ca.element).compareTo(cb.element) <= 0)
      {// first node of this is from a
         firstNode = lastNode = ca;
         ca = ca.next;
      }
      else
      {// first node of this is from b
         firstNode = lastNode = cb;
         cb = cb.next;
      }
   
      // merge from a and b until one becomes empty
      while (ca != null && cb != null)
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

      // append the rest, exactly one of a and b is nonempty now
      if (cb == null)
      {
         lastNode.next = ca;
         ca.previous = lastNode;
         lastNode = a.lastNode;
      }
      else
      {
         lastNode.next = cb;
         cb.previous = lastNode;
         lastNode = b.lastNode;
      }

      size = a.size + b.size;
      lastNode.next = firstNode;   // make this circular

      // make a and b empty
      a.clear();
      b.clear();

      return;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 8, m = 5;
      DoubleCircularListWithMerge x = new DoubleCircularListWithMerge();
      DoubleCircularListWithMerge y = new DoubleCircularListWithMerge();
      DoubleCircularListWithMerge z = new DoubleCircularListWithMerge();
      
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
