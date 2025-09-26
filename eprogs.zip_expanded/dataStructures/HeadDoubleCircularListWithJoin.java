/** join two doubly linked circular lists with header nodes */

package dataStructures;

public class HeadDoubleCircularListWithJoin
             extends HeadDoubleCircularList
{
   /** append list a to this
     * following the join, a is empty */
   public void join(HeadDoubleCircularListWithJoin a)
   {
      // special cases
      if (a.size == 0)
         // a is empty, nothing to join to this
         return;

      // link nodes of list a (other than its header) into end of this
      DoubleNode lastNode = headerNode.previous;
      DoubleNode aFirstNode = a.headerNode.next;
      DoubleNode aLastNode = a.headerNode.previous;
      lastNode.next = aFirstNode;
      aFirstNode.previous = lastNode;
      aLastNode.next = headerNode;
      headerNode.previous = aLastNode;
      size += a.size;

      // make a empty
      a.clear();

      return;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 8, m = 5;
      HeadDoubleCircularListWithJoin x = new HeadDoubleCircularListWithJoin();
      HeadDoubleCircularListWithJoin y = new HeadDoubleCircularListWithJoin();
      HeadDoubleCircularListWithJoin z = new HeadDoubleCircularListWithJoin();
      
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
