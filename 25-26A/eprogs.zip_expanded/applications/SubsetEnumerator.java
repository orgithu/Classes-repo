
/** enumerator for all subsets, other than null, of n elements */

package applications;

public class SubsetEnumerator
{
   // data member
   int n;      // set size
   int [] x;   // subset array
   
   /** constructor */
   public SubsetEnumerator(int theN)
   {
      n = theN;
      x = new int [n + 1];
   }

   /** @return next subset
     * @return null iff there is no next subset */
   public int [] nextSubset()
   {
      // generate next subset
      int i = n;
      while (i > 0 && x[i] == 1)
      {
         x[i] = 0;
         i--;
      }

      if (i == 0)
         return null;
      else
      {
         x[i] = 1;
         return x;
      }
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 4;
      int [] x;
      SubsetEnumerator sv = new SubsetEnumerator(n);

      // output all subsets other than the null subset
      while (true)
      {
         x = sv.nextSubset();
         if (x == null) break;
         for (int i = 1; i <= n; i++)
            System.out.print(x[i] + " ");
         System.out.println();
      }
   }
}
