/** dynamic programming recursive knapsack with traceback to find x's */

package applications;

import utilities.*;

public class RecursiveDPKnapsack2
{
   static int [] profit;
   static int [] weight;    
   static int numberOfObjects;
   static int [][] d;        // decision array

   /** set class data members and invoke method f 
     * @param theProfit[1:theProfit.length - 1] gives object profits
     * @param theWeight[1:theWeight.length-1] gives object weights
     * @param theD[1:p.length - 1][c+ 1] is array to return decisions in
     * @return value of optimal knapsack filling */
   public static int knapsack(int [] theProfit, int [] theWeight,
                              int knapsackCapacity, int [][] theD)
   {
      profit = theProfit;
      weight = theWeight;
      numberOfObjects = theProfit.length - 1;
      d = theD;
      return f(1, knapsackCapacity);
   }
      
   /** recursive method to solve dynamic programming recurrence
     * @return f(i, theCapacity) */
   private static int f(int i, int theCapacity)
   {
      if (i == numberOfObjects)
         // use Equation 20.1
         if (theCapacity < weight[numberOfObjects])
         {// x[numberOfObjects] is 0
           d[numberOfObjects][theCapacity] = 0;
           return 0;
         }
         else
         {// x[numberOfObjects] is 1
            d[numberOfObjects][theCapacity] = 1;
            return profit[numberOfObjects];
         }
   
      // use Equation 20.2
      if (theCapacity < weight[i])
      {// x[i] is 0
         d[i][theCapacity] = 0;
         return f(i + 1, theCapacity); 
      }
      
      int p0 = f(i + 1, theCapacity);
      int p1 = f(i + 1, theCapacity - weight[i]) + profit[i];
      if (p0 < p1)
      {// x[i] is 1
         d[i][theCapacity] = 1;
         return p1;
      }

      // x[i] is 0
      d[i][theCapacity] = 0;
      return p0;
   }
   
   /** compute x for optimal filling
     * @param theD is decision array
     * @param theWeight[1:theWeight.length-1] gives object weights */
   public static void traceback(int [][] theD, int [] theWeight,
                                int knapsackCapacity, int [] x)
   {
      numberOfObjects = theWeight.length - 1;
      for (int i = 1; i < numberOfObjects; i++)
         if (d[i][knapsackCapacity] == 1)
         {
            x[i] = 1;
            knapsackCapacity -= theWeight[i];
         }
         else
            x[i] = 0;
      x[numberOfObjects] = d[numberOfObjects][knapsackCapacity];
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
      int [][] d = new int [n + 1] [c + 1];

      for (int i = 1; i <= n; i++)
      {
         System.out.println("Enter profit and weight of object " + i);
         p[i] = keyboard.readInteger();
         w[i] = keyboard.readInteger();
      }

      System.out.print("Optimal value is ");
      System.out.println(knapsack(p, w, c, d));

      // compute x values
      int [] x = new int [n + 1];
      traceback(d, w, c, x);
      System.out.print("The solution vector is ");
      for (int i = 1; i <= n; i++)
         System.out.print(x[i] + " ");
      System.out.println();
   }
}
