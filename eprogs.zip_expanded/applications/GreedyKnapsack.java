/** greedy 0/1 knapsack solution */

package applications;

import dataStructures.*;

public class GreedyKnapsack
{
   static class Element implements Comparable
   {
      // data members
      int id;   // object identifier
      double d; // profit density

      // constructor
      private Element(int theID, double theDensity)
      {
         id = theID;
         d = theDensity;
      }

     // method of Comparable
     /** @return true iff this < x */
     public int compareTo(Object x)
     {
        double xD = ((Element) x).d;
        if (d < xD)
           return -1;
        if (d == xD)
           return 0;
        return 1;
      }
  
      /** @return true iff this == x */
      public boolean equals(Object x)
         {return d == ((Element) x).d;}
   }

   /** set x[i] = 1 iff object i included in knapsack, 1 <= i < p.length
     * @param p[1:p.length-1] array of profits
     * @param w[1:w.length-1] array of weights
     * @param c knapsack capacity
     * @return value of greedy knapsack filling */
   public static double greedyKnapsack(double [] p, double [] w, double c,
                                       int [] x)
   {
      // define an element array to be sorted by profit density
      Element [] q = new Element [p.length];
      for (int i = 1; i < p.length; i++)
         // array of profit densities
         q[i] = new Element(i, p[i] / w[i]);
   
      // sort by density
      HeapSort.heapSort(q);
   
      // load in nonincreasing order of density and compute profit sum
      double profitSum = 0;      // will be sum of profits
      for (int i = p.length - 1; i > 0; i--)
      {
         int id = q[i].id;
         if (w[id] <= c )
         {// object id fits
            x[id] = 1;
            c -= w[id];
            profitSum += p[id];
         }
         else // object id does not fit
            x[id] = 0;
      }
      return profitSum;
   }
   
   /** test greedyKnapsack */
   public static void main(String [] args)
   {
      double [] p = {0, 6, 3, 5, 4, 6};
      double [] w = {0, 2, 2, 6, 5, 4};
      int [] x = new int [6];
      int n = 5;
      double c = 10;
      System.out.print("Greedy value is ");
      System.out.println(greedyKnapsack(p, w, c, x));
      System.out.print("x vector is ");
      for (int i = 1; i <= n; i++)
         System.out.print(x[i] + "  ");
      System.out.println();
   }
}
