/** merge two sorted lists */

package dataStructures;

public class DoublyLinkedListWithMerge extends DoublyLinkedList
{
   /** make this the result of merging the sorted lists a and b 
     * following the merge, both a and b are empty */
   public void merge(DoublyLinkedListWithMerge a, DoublyLinkedListWithMerge b)
   {
      // special cases
      if (a.size == 0)
      {// a is empty
         firstNode = b.firstNode;
         lastNode = b.lastNode;
         size = b.size;
         b.clear();
         return;
      }

      if (b.size == 0)
      {// b is empty
         firstNode = a.firstNode;
         lastNode = a.lastNode;
         size = a.size;
         a.clear();
         return;
      }
   
      // neither a nor b is empty
      DoubleNode
                ca = a.firstNode,       // pointer to current node in a
                cb = b.firstNode;       // pointer to current node in b
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

      // make a and b empty
      a.clear();
      b.clear();

      return;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 8, m = 5;
      DoublyLinkedListWithMerge x = new DoublyLinkedListWithMerge();
      DoublyLinkedListWithMerge y = new DoublyLinkedListWithMerge();
      DoublyLinkedListWithMerge z = new DoublyLinkedListWithMerge();
      
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
