/** join two doubly linked circular lists */

package dataStructures;

public class DoubleCircularListWithJoin extends DoubleCircularList
{
   /** append list a to this
     * following the join, a is empty */
   public void join(DoubleCircularListWithJoin a)
   {
      // special cases
      if (a.size == 0)
         // a is empty, nothing to join to this
         return;

      if (size == 0)
      {// this is empty,
         // move all nodes of a to this
         lastNode = a.lastNode;
         size = a.size;

         // make a empty
         a.clear();

         return;
      }
   
      // neither this nor a is empty
      // link a into end of this
      DoubleNode firstNode = lastNode.next;
      DoubleNode aFirstNode = a.lastNode.next;
      lastNode.next = aFirstNode;
      aFirstNode.previous = lastNode;
      lastNode = a.lastNode;
      firstNode.previous = lastNode;
      lastNode.next = firstNode;
      size += a.size;

      // make a empty
      a.clear();

      return;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 8, m = 5;
      DoubleCircularListWithJoin x = new DoubleCircularListWithJoin();
      DoubleCircularListWithJoin y = new DoubleCircularListWithJoin();
      DoubleCircularListWithJoin z = new DoubleCircularListWithJoin();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      //  put elements in y
      for (int i = 0; i < m; i++)
         y.add(0, new Integer(2 * (n - i) - 1));
      
      System.out.println("First list is " + z);
      System.out.println("Second list is " + y);
      z.join(y);
      System.out.println("Joined list is " + z);
      System.out.println("Second list is " + y);
      System.out.println();

      System.out.println("First list is " + x);
      System.out.println("Second list is " + y);
      x.join(y);
      System.out.println("Joined list is " + x);
      System.out.println();
      
      System.out.println("First list is " + z);
      System.out.println("Second list is " + x);
      z.join(x);
      System.out.println("Joined list is " + z);
      System.out.println("Second list is " + x);
      System.out.println();
   }
}
