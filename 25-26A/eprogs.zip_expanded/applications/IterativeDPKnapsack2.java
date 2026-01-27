

/** iterative dynamic programming 0/1/2 knapsack */

package applications;

import utilities.*;

public class IterativeDPKnapsack2
{
   /** iterative method to solve dynamic programming recurrence
     * x values may be 0, 1, or 2
     * computes f[1][c] and f[i][y], 2 <= i <= n, 0 <= y <= c
     * @param p[1:p.length - 1] gives object profits
     * @param w[1:w.length-1] gives object weights
     * @param c is the knapsack capacity */
   public static void knapsack(int [] p, int [] w, int c, int [][] f)
   {
      int n = p.length - 1;   // number of objects

      // initialize f[n][]
      int yMax = Math.min(w[n] - 1, c);
      for (int y = 0; y <= yMax; y++)
         // x[n] = 0
         f[n][y] = 0;
      yMax = Math.min(2 * w[n] - 1, c);
      for (int y = w[n]; y <= yMax; y++)
         // x[n] = 1
         f[n][y] = p[n];
      for (int y = 2 * w[n]; y <= c; y++)
         // x[n] = 2
         f[n][y] = 2 * p[n];
      
      // compute f[i][y], 1 < i < n
      for (int i = n - 1; i > 1; i--)
      {
         yMax = Math.min(w[i] - 1, c);
         for (int y = 0; y <= yMax; y++)
            f[i][y] = f[i + 1][y];
         for (int y = w[i]; y <= c; y++)
            f[i][y] = Math.max(f[i + 1][y],
                          f[i + 1][y - w[i]] + p[i]);
         for (int y = 2 * w[i]; y <= c; y++)
            f[i][y] = Math.max(f[i][y],
                          f[i + 1][y - 2 * w[i]] + 2 * p[i]);
      }
      // compute f[1][c]
      f[1][c] = f[2][c];
      if (c >= w[1])
         f[1][c] = Math.max(f[1][c], f[2][c-w[1]] + p[1]);
      if (c >= 2 * w[1])
         f[1][c] = Math.max(f[1][c], f[2][c - 2 * w[1]] + 2 * p[1]);
   }
   
   /** compute solution vector x */
   public static void traceback(int [][] f, int [] p, int [] w, int c, int [] x)
   {
      int n = w.length - 1;   // number of objects

      for (int i = 1; i < n; i++)
         if (f[i][c] == f[i+1][c])
            x[i] = 0;
         else
            if (f[i][c] == f[i + 1][c - w[i]] + p[i])
            {
               x[i] = 1;
               c -= w[i];
            }
            else
            {
               x[i] = 2;
               c -= 2 * w[i];
            }
   
      // compute x[n]
      if (c < w[n])
         x[n] = 0;
      else
         if (c < 2 * w[n])
            x[n] = 1;
          else
             x[n] = 2;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // define the input stream to be the standard input stream
      MyInputStream keyboard = new MyInputStream();

      System.out.println("Enter number of objects and knapsack capacity");
      int n = keyboard.readInteger();
      int c = keyboard.readInteger();
      int [] p = new int [n + 1];
      int [] w = new int [n + 1];
      int [] x = new int [n + 1];
      int [][] f = new int [n + 1][c + 1];

      for (int i = 1; i <= n; i++)
      {
         System.out.println("Enter profit and weight of object " + i);
         p[i] = keyboard.readInteger();
         w[i] = keyboard.readInteger();
      }

      knapsack(p, w, c, f);

      System.out.print("Optimal value is ");
      System.out.println(f[1][c] + "\n");
      System.out.println("Rest of table is");
      for (int i = 2; i <= n; i++)
      {
         for (int j = 0; j <= c; j++)
            System.out.print(f[i][j] + " ");
         System.out.println();
      }

      traceback(f, p, w, c, x);

      System.out.println();
      for (int i = 1; i <= n; i++)
         System.out.print(x[i] + " ");
      System.out.println();
   }
}
