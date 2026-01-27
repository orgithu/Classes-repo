/** equal-width stack folding, width of rectangle is known */

package applications;

public class EqualWidthFolding2
{
   /** fold components with heights h[1 .. h.length - 1] into a
     * rectangle of width theWidth
     * @param w is width of each component
     * @param r is array of space to be left at column ends
     * @param theH[i][j] is H_(i,j)
     * values of theH[1..h.length - 1][1..theWidth/w] and
     * kay[1..h.length-1][1..theWidth/w] are computed by this method
     * @return true iff the folding is possible */
   public static boolean equalWidthFolding(int [] h, int [] r, int w,
                                   int theWidth, int [][] theH, int [][] kay)
   {
      int n = h.length - 1;  // number of components
      int s = theWidth / w;  // max number of stacks
      if (n < 1)
         return true;
      if (s < 1)
         return false;
      if (n < 2)
         return true;

      // set boundary values of theH
      int hsum = 0;
      for (int i = n; i > 0; i--)
      {
         hsum += h[i];
         theH[i][1] = hsum + r[i];
      }
      for (int j = 1; j <= s; j++)
         theH[n][j] = h[n] + r[n];

      // compute remaining values using Eq. 54.4
      for (int i = n - 1; i > 0; i--)
         for (int j = 2; j <= s; j++)
         {
            hsum = 0;
            int minH = theH[1][1];   // upper bound
            int theK = 0;
            for (int k = i; k < n; k++)
            {
               hsum += h[k];
               int maxTerm = Math.max(hsum + r[i] + r[k + 1],
                                      theH[k + 1][j - 1]);
               if (maxTerm < minH)
               {
                  minH = maxTerm;
                  theK = k;
               }
            }   
            theH[i][j] = minH;
            kay[i][j] = theK;
         }
      return true;
   }
   
   /** output fold points */
   public static void traceback(int [][] kay, int theWidth, int w)
   {
      int n = kay.length - 1;   // number of components
      int s = theWidth / w;     // max number of stacks
   
      // find first nonzero kay[1][], this is also the number of stacks
      int j;
      for (j = s; j > 1; j--)
         if (kay[1][j] > 0)
            break;

      if (j < 2 || n < 2)
         System.out.println("There are no fold points");
      else
      {// there is at least one fold point
         System.out.print("The fold points are ");
         int i = 1;
         while (j > 1)
         {
            i = kay[i][j] + 1;
            System.out.print(i + " ");
            j--;
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
      int [] r = {0, 0, 1, 3, 2, 1, 3, 2, 4, 1};
      int theWidth = 9;                  // rectangle width
      int w = 3;                         // component width
      int n = 9;                         // number of components
      int [][] kay = new int [n + 1][6];
      int [][] theH = new int [n + 2][6];

      equalWidthFolding(h, r, w, theWidth, theH, kay);
      
      System.out.println("The H values are");
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= theWidth / w; j++)
          System.out.print(theH[i][j] + " ");
         System.out.println();
      }
      System.out.println();
   
      System.out.println("kay values are");
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= theWidth / w; j++)
            System.out.print(kay[i][j] + " ");
         System.out.println();
      }
      System.out.println();
      
      traceback(kay, theWidth, w);
   }
}
