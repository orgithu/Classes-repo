/** standard cell folding */

package applications;

public class StandardCellFolding
{
   /** fold standard cells with widths w[1..w.length - 1] into a
     * rectangle of width rWidth
     * @param h is the height of each component
     * @param l is array of routing space to be left between cell rows
     * @param theH[i][s] is H_{i,s}
     * values of theH[1..w.length][1..w.length] and
     * kay[1..w.length-1] are computed by this method
     * @return height of minimum height folding
     * @return 0 iff the folding is not possible */
   public static int standardCellFolding(int [] w, int [] l, int h,
                             int rWidth, int [][] theH, int [] kay)
   {
      int n = w.length - 1;  // number of cells
      if (w[n] > rWidth)
         // infeasible
         return 0;

      theH[n][n] = h;
      // compute remaining theH values and kay values
      for (int i = n - 1; i > 0; i--)
      {// compute theH[i][]
         if (w[i] > rWidth)
            // infeasible
            return 0;

         // compute theH[i][i] = H_{i,i} using Eqn. 54.7
         int minH = theH[i + 1][i + 1];
         for (int k = i + 2; k <= n; k++)
            if (theH[i + 1][k] < minH)
               minH = theH[i + 1][k];
         
         theH[i][i] = minH + l[i + 1] + h;
         kay[i] = i;

         // compute theH[i][s] = H_{i,s}, s > i, using Eqn. 54.6
        int wsum = w[i];   // wsum(i,s)
        for (int s = i + 1; s <= n; s++)
        {
           wsum += w[s];
           if (wsum <= rWidth)
           {// folding at s + 1 is feasible
              theH[i][s] = theH[i + 1][s];
              if (theH[i][s] < theH[i][kay[i]])
                 // s + 1 is a better fold point for C_i ... C_n
                 kay[i] = s;
           }
           else
              // folding at i + 1 is not feasible
              theH[i][s] = Integer.MAX_VALUE;
        }
      }
   
      return theH[1][kay[1]];
   }
   
   /** output fold points */
   public static void traceback(int [] kay)
   {
      int n = kay.length - 1;   // number of components
   
      if (kay[1] >= n)
         System.out.println("There are no fold points");
      else
      {// there is at least one fold point
         System.out.print("The fold points are ");
         int i = 1;
         while (i < n)
         {
            i = kay[i] + 1;
            System.out.print(i + " ");
         }
         System.out.println();
      }
   }

   /** test standard cell folding */
   public static void main(String [] args)
   {
      // component widths
      int []  w = {0, 4, 3, 5, 2, 1, 6, 2, 4, 3};
      // routing space
      int [] l = {0, 0, 1, 3, 2, 1, 3, 2, 4, 1};
      int rWidth = 10;                   // rectangle width
      int h = 2;                         // component height
      int n = 9;                         // number of components
      int [] kay = new int [n + 1];
      int [][] theH = new int [n + 1][n + 1];

      int minH = standardCellFolding(w, l, h, rWidth, theH, kay);
      
      if (minH == 0)
         System.out.println("Folding is not possible");
      else
      {
         System.out.println("The minimum height is " + minH);
         System.out.print("The H values are ");
         for (int i = 1; i <= n; i++)
         {
            for (int j = 1; j <= n; j++)
               if (theH[i][j] < Integer.MAX_VALUE)
                  System.out.print(theH[i][j] + " ");
               else break;
            System.out.println();
         }
      
         System.out.print("\nkay values are ");
         for (int i = 1; i <= n; i++)
            System.out.print(kay[i] + " ");
         System.out.println();
         
         traceback(kay);
      }
   }
}
