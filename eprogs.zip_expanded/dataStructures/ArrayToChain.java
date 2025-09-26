
/** convert from ArrayLinearList to Chain */

package dataStructures;

public class ArrayToChain
{
   /** convert the array list f to a chain c
     * @return Chain version of f */
   public static Chain arrayToChain(ArrayLinearList f)
   {
      Chain c = new Chain();
      int s = f.size();
      for (int i = s - 1; i >= 0; i--)
         // copy i'th element of f
         c.add(0, f.get(i));
      
      return c;
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 10;
      ArrayLinearList x = new ArrayLinearList();
      
      //  put elements in x
      for (int i = 0; i < n; i++)
         x.add(0, new Integer(2 * (n - i)));
      
      System.out.println("The array-based linear list is " + x);
      
      System.out.println("The chain is " + arrayToChain(x));
   }
}
