

/** reverse a circular list with a header node,
  * method is not a member of CircularList */

package dataStructures;

public class ReverseHeadCircularList
{
   public static void reverse(HeadCircularList x)
   {
      int sizeMinus1 = x.size() - 1;
      for (int i = 0; i < sizeMinus1; i++)
      {
         // retrieve and remove first element
         Object y = x.remove(0);

         // insert at proper place
         x.add(sizeMinus1 - i, y);
      }
   }

    /** test program */
    public static void main(String [] args)
    {
       int n = 10, m = 5;
       HeadCircularList x = new HeadCircularList();
       HeadCircularList y = new HeadCircularList();
       
       //  put elements in x
       for (int i = 0; i < n; i++)
          x.add(0, new Integer(4 * n - i));
       
       //  put elements in y
       for (int i = 0; i < m; i++)
          y.add(0, new Integer(2 * (n - i)));
       
       System.out.println("The reverse of " + x);
       reverse(x);
       System.out.println("is " + x);
       System.out.println("The reverse of " + y);
       reverse(y);
       System.out.println("is " + y);
 
 
       // remove a few elements from the end of x
       x.remove(x.size() - 1);
       x.remove(x.size() - 1);
       x.remove(x.size() - 1);
       x.remove(x.size() - 1);
 
       // add a few more at the front
       for (int i = 0; i < n; i++)
          x.add(0, new Integer(2 * n - i));
 
       System.out.println("The reverse of " + x);
       reverse(x);
       System.out.println("is " + x);
   }   
}
