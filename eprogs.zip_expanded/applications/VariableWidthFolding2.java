/** variable-width stack folding to minimize rectangle height
  * subject to rectangle width constraint */

package applications;

public class VariableWidthFolding2
{
   // data members
   static int n;                 // number of components;
   static int widthSum;          // sum of widths of all components;
   static int [] h;              // component heights
   static int [] w;              // component widths
   static int [] r;              // space to be left at column ends
   static int [][] hsum;         // hsum(i,k)
   static int [] theW;           // W_i for current height choice
   static int [] kay;            // kay values for current height choice;
   
   /** fold components with heights h[1 .. h.length - 1] and widths
     * w[1 .. w.length - 1] into a rectangle of width theWidth and
     * minimum height
     * @param r is array of space to be left at column ends
     *        length of r must exceed that of h
     * @param theW[i] is W_i
     * @param hsum[i][k] is hsum(i,k)
     * values of bestW[1..h.length] and bestKay[1..h.length-1] are computed
     * by this method
     * @return minimum folding height
     * @return 0 if folding into a theWidth rectangle is not possible */
   public static int minimumHeightFolding(int [] h, int [] r, int [] w,
                         int theWidth, int [] bestW, int [] bestKay)
   {    
      // set class data members
      VariableWidthFolding2.n = h.length - 1;
      VariableWidthFolding2.h = h;
      VariableWidthFolding2.w = w;
      VariableWidthFolding2.r = r;
      VariableWidthFolding2.theW = new int [n + 2];
      theW[n + 1] = 0;
      VariableWidthFolding2.kay = new int [n + 1];
      // determine sum of widths
      widthSum = 0;
      for (int i = 1; i <= n; i++)
         widthSum += w[i];
      // determine hsum(i,k)
      hsum = new int [n + 1][n + 1];
      for (int i = 1; i <= n; i++)
      {
         hsum[i][i] = h[i];
         for (int k = i + 1; k <= n; k++)
            hsum[i][k] = hsum[i][k - 1] + h[k];
      }
      r[0] = r[n + 1] = 0;

      // collect possible rectangle height values into a 1D array
      Integer [] height = new Integer [n * n];
      int k = 0;      // cursor for height[]
      for (int i = 1; i <= n; i++)
         for (int j = 1; j <= n; j++)
            height[k++] = new Integer(hsum[i][j] + r[i] + r[j + 1]);

      // sort the possible heights
      MergeSort.mergeSort(height);

      // eliminate duplicates
      k = 0;
      for (int i = 1; i < n * n; i++)
         if (height[k].compareTo(height[i]) < 0)
            height[++k] = height[i];

      // do a binary search over the k + 1 distinct heights
      int minHeight = 0;
      int left = 0;
      int right = k;
      while (left <= right)
      {
         int middle = (left + right)/2;
         variableWidthFolding(height[middle].intValue());
         if (theW[1] <= theWidth)
         {// height[middle] is feasible
            minHeight = height[middle].intValue();
            // save theW and kay
            for (int i = 1; i <= n; i++)
            {
               bestW[i] = theW[i];
               bestKay[i] = kay[i];
            }
            right = middle - 1;  // do not examine larger heights
         }   
         else
            // height[middle] is infeasible, do not examine smaller heights
            left = middle + 1;
      }
      return minHeight;
   }    

   /** fold components into a minimum width rectangle of height theHeight
     * values of theW[1..h.length] and kay[1..h.length-1] are computed
     * by this method */
   public static void variableWidthFolding(int theHeight)
   {
      for (int i = n; i > 0; i--)
      {// compute theW[i] using Eq. 54.5
         int wmax = 0,                   // wmax(i,k)
             minW = widthSum + 1;        // min value for W_i so far
   
         for (int k = i; k <= n; k++)
         {
            if (hsum[i][k] > theHeight)
               // infeasible
               break;
            if (w[k] > wmax)
               wmax = w[k]; 
            if (hsum[i][k] + r[i] + r[k + 1] <= theHeight &&
                wmax + theW[k + 1] < minW)
            {
               minW = wmax + theW[k + 1];
               kay[i] = k;
            }
         }
         theW[i] = minW;
      }
      return;
   }
   
   /** output fold points */
   public static void traceback(int [] kay)
   {
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

   /** test variable width folding */
   public static void main(String [] args)
   {
      // component heights
      int []  h = {0, 4, 3, 5, 2, 1, 6, 2, 4, 3};
      // routing space
      int [] r = {0, 0, 1, 3, 2, 1, 3, 2, 4, 1, 0};
      int [] w = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
      int n = 9;                         // number of components
      int [] kay = new int [n + 1];
      int [] theW = new int [n + 2];
      for (int rWidth = 1; rWidth <= n; rWidth++)
      {
         int minH = minimumHeightFolding(h, r, w, rWidth, theW, kay);
        
         if (minH == 0)
            System.out.println("Width " + rWidth +
                               " folding is not possible\n");
         else
         {
            System.out.println("Allowable rectangle width is " + rWidth);
            System.out.println("The minimum height is " + minH);
            System.out.print("The W values are ");
            for (int i = 1; i <= n; i++)
                System.out.print(theW[i] + " ");
            System.out.println();
         
            System.out.print("kay values are ");
            for (int i = 1; i <= n; i++)
               System.out.print(kay[i] + " ");
            System.out.println();
            
            traceback(kay);
            System.out.println();
         }
      }
   }
}
