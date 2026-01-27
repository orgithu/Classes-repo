/** equal-width stack folding */

package applications;

public class EqualWidthFolding
{
   /** fold components with heights h[1 .. h.length - 1] into a
     * rectangle of height theHeight
     * @param w is width of each component
     * @param r is array of space to be left at column ends
     * @param theW[i] is W_i
     * values of theW[1..h.length] and kay[1..h.length-1] are computed
     * by this method
     * @return true iff the folding is possible */
   public static boolean equalWidthFolding(int [] h, int [] r, int w,
                                       int theHeight, int [] theW, int [] kay)
   {
      int n = h.length - 1;  // number of components
      theW[n + 1] = 0;
      for (int i = n; i > 0; i--)
      {// compute theW[i] using Eq. 54.2
         int hsum = 0,             // hsum(i,k)
             minW = w * n + 1;     // min W_i so far
   
         for (int k = i; k <= n; k++)
         {
            hsum += h[k];
            if (hsum > theHeight)
               // infeasible
               break;
            if (hsum + r[i] + r[k + 1] <= theHeight && theW[k + 1] < minW)
            {
               minW = theW[k + 1];
               kay[i] = k;
            }
         }
   
         theW[i] = w + minW;
      }
   
      if (theW[1] > w * n)
        // infeasible
        return false;
      else
         return true;
   }
   
   /** output fold points */
   public static void traceback(int [] kay)
   {
      int n = kay.length - 1;   // number of components
   
      if (kay[1] >= n)
         System.out.println("There are no fold points");
      else
      {// there is at least one fold point
         int i = 1;
         System.out.print("The fold points are ");
         while (kay[i] < n)
         {
            i = kay[i] + 1;
            System.out.print(i + " ");
         }
         System.out.println();
      }
   }

   /** test equal width folding */
   public static void main(String [] args)
   {
      // component heights
      int []  h = {0, 4, 3, 5, 2, 1, 6, 2, 4, 3};
      // routing space
      int [] r = {0, 1, 1, 3, 2, 1, 3, 2, 4, 1, 2};
      int height = 10;                   // stack height
      int w = 3;                         // component width
      int n = 9;                         // number of components
      int [] kay = new int [n + 1];
      int [] theW = new int [n + 2];

      equalWidthFolding(h, r, w, height, theW, kay);
      
      System.out.print("The W values are ");
      for (int i = 1; i <= n; i++)
          System.out.print(theW[i] + " ");
      System.out.println();
   
      System.out.print("kay values are ");
      for (int i = 1; i <= n; i++)
         System.out.print(kay[i] + " ");
      System.out.println();
      
      traceback(kay);
   }
}
