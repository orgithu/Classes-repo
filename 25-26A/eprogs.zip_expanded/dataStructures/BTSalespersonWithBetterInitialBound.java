
/** backtracking salesperson problem with better bound */

package dataStructures;

import utilities.*;
import wrappers.*;

public class BTSalespersonWithBetterInitialBound
             extends AdjacencyWDigraph
{

   // class data members
   static int [] partialTour;
   static int [] bestTourSoFar;
   static Object costOfBestTourSoFar;
   static Operable costOfPartialTour;

   // constructors
   public BTSalespersonWithBetterInitialBound(int theVertices)
      {super(theVertices);}

   public BTSalespersonWithBetterInitialBound()
      {this(0);}
   
   /** traveling salesperson by backtracking, modified to use a better
     * starting bound
     * @param theZero zero weight
     * @param theOne any positive weight
     * @param bestTour bestTour[1:n] is set to best tour
     * @return cost of best tour */
   public Object btSalesperson2(int [] bestTour, Operable theZero,
                                Operable theOne)
   {
      // compute initial value of costOfBestTourSoFar
      costOfBestTourSoFar = theOne;
      for (int i = 1; i <= n; i++)
      {// find max cost edge out of vertex i
         // maxCost will eventually be max weight of an out edge from i
         Operable maxCost = theZero;
         // outEdge will become true iff i has an out edge
         boolean outEdge = false;
         for (int j = 1; j <= n; j++)
            if (a[i][j] != null)
            {// found an out edge from vertex i
               outEdge = true;
               if (((Operable) a[i][j]).compareTo(maxCost) > 0)
                  maxCost = (Operable) a[i][j];
            }
   
         // make sure vertex i has an out edge
         if (!outEdge)
            // no edge out of vertex i, no tour is possible
            return null;;
   
         // vertex i has an out edge
         ((Operable) costOfBestTourSoFar).increment(maxCost);
        }
   
      // set partialTour to identity permutation
      partialTour = new int [n + 1];
      for (int i = 1; i <= n; i++)
         partialTour[i] = i;
   
      bestTourSoFar = bestTour;
      costOfPartialTour = theZero;
   
      // search permutations of partialTour[2:n]
      rTSP2(2);
   
      return costOfBestTourSoFar;
   }
   
   /** recursive backtracking code for traveling salesperson
     * using a better starting bound
     * search the permutation tree for best tour */
   private void rTSP2(int currentLevel)
   {
      if (currentLevel == n)
      {// at parent of a leaf
         // complete tour by adding last two edges
         if (a[partialTour[n - 1]][partialTour[n]] != null &&
             a[partialTour[n]][1] != null &&
             ((Operable) ((Operable) costOfPartialTour
               .add(a[partialTour[n - 1]][partialTour[n]]))
               .add(a[partialTour[n]][1]))
               .compareTo(costOfBestTourSoFar) < 0)
         {// better tour found
            for (int j = 1; j <= n; j++)
               bestTourSoFar[j] = partialTour[j];
            costOfBestTourSoFar = ((Operable) costOfPartialTour
                         .add(a[partialTour[n - 1]][partialTour[n]]))
                         .add(a[partialTour[n]][1]);
         }
      }
      else
      {// try out subtrees
         for (int j = currentLevel; j <= n; j++)
            // is move to subtree labeled partialTour[j] possible?
            if (a[partialTour[currentLevel - 1]][partialTour[j]] != null &&
                ((Operable) costOfPartialTour
                  .add(a[partialTour[currentLevel - 1]][partialTour[j]]))
                  .compareTo(costOfBestTourSoFar) < 0)
            {// search this subtree
               MyMath.swap(partialTour, currentLevel, j);
               costOfPartialTour.increment(a[partialTour[currentLevel - 1]]
                             [partialTour[currentLevel]]);
               rTSP2(currentLevel + 1);
               costOfPartialTour.decrement(a[partialTour[currentLevel - 1]]
                             [partialTour[currentLevel]]);
               MyMath.swap(partialTour, currentLevel, j);
            }
      }
   }

   public static void main(String [] args)
   {

      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();
      // input a test graph
      System.out.println("Enter number of vertices and edges");
      int n = keyboard.readInteger();
      int e = keyboard.readInteger();
      BTSalespersonWithBetterInitialBound g
               = new BTSalespersonWithBetterInitialBound(n);
      for (int i = 1; i <= e; i++)
      {
         System.out.println("enter weighted edge " + i);
         int u = keyboard.readInteger();
         int v = keyboard.readInteger();
         int w = keyboard.readInteger();
         g.putEdge(new WeightedEdge(u, v, new MyInteger(w)));
      }

      System.out.println("\nThe weighted digraph is");
      g.output();

      // test btSalesperson2
      int [] p = new int [n + 1];
      System.out.print("\nThe length of the shortest tour is ");
      System.out.println(g.btSalesperson2(p, new MyInteger(0),
                                             new MyInteger(1)));
      System.out.print("The shortest tour is ");
      for (int i = 1; i <= n; i++)
         System.out.print(p[i] + " ");
      System.out.println();
   }
}
