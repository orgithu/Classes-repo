/** iterative backtracking method to find a max independent set */

package dataStructures;

import wrappers.*;
import utilities.*;

public class AdjacencyGraphWithMaxIndependentSet extends AdjacencyGraph
{
   // constructors
   public AdjacencyGraphWithMaxIndependentSet(int theVertices)
      {super(theVertices);}

   public AdjacencyGraphWithMaxIndependentSet()
      {this(0);}

   // class data members used by backtracking max independent set
   static int [] currentIndependentSet;
   static int sizeOfCurrentIndependentSet;
   static int sizeOfMaxIndependentSetSoFar;
   static int [] maxIndependentSetSoFar;
   
   /** solve max-independent-set problem using backtracking
     * @return size of max independent set
     * @param maxIndependentSet set maxIndependentSet[i] = 1
     * iff i is in max independent set */
   public int btMaxIndependentSet(int [] maxIndependentSet)
   {
      // initialize for rIndependentSet
      currentIndependentSet = new int [n + 1];
      sizeOfCurrentIndependentSet = 0;
      sizeOfMaxIndependentSetSoFar = 0;
      maxIndependentSetSoFar = maxIndependentSet;
   
      // find max independent set
      rIndependentSet(1);
      return sizeOfMaxIndependentSetSoFar;
   }
   
   /** recursive backtracking code to compute largest independent set */
   private void rIndependentSet(int currentLevel)
   {
      if (currentLevel > n)
      {// at leaf, found a larger independent set
         // update maxIndependentSetSoFar and sizeOfMaxIndependentSetSoFar
         for (int j = 1; j <= n; j++)
            maxIndependentSetSoFar[j] = currentIndependentSet[j];
         sizeOfMaxIndependentSetSoFar = sizeOfCurrentIndependentSet;
         return;
      }
   
      // not at leaf, see if vertex currentLevel is connected to any vertex
      // in current independent set
      boolean connected = false;
      for (int j = 1; j < currentLevel; j++)
         if (currentIndependentSet[j] == 1 && a[currentLevel][j])
         {// vertex currentLevel is connected to vertex j
            connected = true;
            break;
         }
   
      if (!connected)
      {// try currentIndependentSet[currentLevel] = 1
         // add vertex currentLevel to independent set
         currentIndependentSet[currentLevel] = 1;
         sizeOfCurrentIndependentSet++;
         rIndependentSet(currentLevel + 1);
         sizeOfCurrentIndependentSet--;
      }
   
      if (sizeOfCurrentIndependentSet + n - currentLevel
                > sizeOfMaxIndependentSetSoFar)
      {// try currentIndependentSet[currentLevel] = 0
         currentIndependentSet[currentLevel] = 0;
         rIndependentSet(currentLevel + 1);
      }
   }

   public static void main(String [] args)
   {
      int n = 7;
      AdjacencyGraphWithMaxIndependentSet g =
                    new AdjacencyGraphWithMaxIndependentSet(n);

      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();

      // input a test graph
      System.out.println("enter number of edges in 7 vertex graph");
      int e = keyboard.readInteger();
      for (int i = 1; i <= e; i++)
      {
         System.out.println("enter unweighted edge " + i);
         int u = keyboard.readInteger();
         int v = keyboard.readInteger();
         g.putEdge(new Edge(u, v));
      }

      System.out.println("The undirected graph is");
      g.output();

      // test btMaxIndependentSet
      int [] v = new int [n + 1];
      System.out.println("\nMax Independent Set size is "
                         + g.btMaxIndependentSet(v));
      System.out.print("\nSolution vector is ");
      for (int i = 1; i <= n; i++)
         System.out.print(v[i] + " ");
      System.out.println();
   }
}
