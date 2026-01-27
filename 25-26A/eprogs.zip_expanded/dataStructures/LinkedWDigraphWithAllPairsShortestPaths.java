
/** all pairs shortest paths for linked weighted digraphs */

package dataStructures;

import java.util.*;
import utilities.*;
import wrappers.*;

public class LinkedWDigraphWithAllPairsShortestPaths extends LinkedWDigraph
{
   // constructors
   public LinkedWDigraphWithAllPairsShortestPaths(int theVertices)
      {super(theVertices);}
   
   // default is a 0 vertex graph
   public LinkedWDigraphWithAllPairsShortestPaths()
      {this(0);}
   

   /** dynamic programming all pairs shortest paths algorithm
     * compute c[i][j] and kay[i][j] for all i and j */
   public void allPairs(Operable [][] c, int [][] kay)
   {
      // first find a nonzero weight from which we can determine
      // the zero element
      Operable notNull = null;   // eventually a nonnull element
      for (int i = 1; i <= n; i++)
         if (!aList[i].isEmpty())
         {// vertex i has nonzero out degree
            notNull = (Operable) ((WeightedEdgeNode) aList[i].get(0)).weight;
            break;
         }
      if (notNull == null) // graph has no edges
         return;
      
      // initialize c[i][j] = c(i,j,0)
      // first set all to null
      for (int i = 1; i <= n; i++)
         for (int j = 1; j <= n; j++)
            c[i][j] = null;
      // now change the ones for which there is an edge in the graph
      for (int i = 1; i <= n; i++)
      {
         Iterator ii = aList[i].iterator();
         while (ii.hasNext())
         {
            WeightedEdgeNode wEdge = (WeightedEdgeNode) ii.next();
            c[i][wEdge.vertex] = (Operable) wEdge.weight;
         }
      }
      // finally, set diagonal entries to zero
      for (int i = 1; i <= n; i++)
         c[i][i] = (Operable) notNull.zero();
      
      // compute c[i][j] = c(i,j,k)
      for (int k = 1; k <= n; k++)
         for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
            {
               Operable t1 = c[i][k];
               Operable t2 = c[k][j];
               Operable t3 = c[i][j];
               if (t1 != null && t2 != null &&
                  (t3 == null || t3.compareTo(t1.add(t2)) > 0))
               {// smaller value for c[i][j] found
                    c[i][j] = (Operable) t1.add(t2);
                    kay[i][j] = k;
               }
            }
   }

   /** output shortest path from i to j */
   public void outputPath(Operable [][] c, int [][] kay, int i, int j)
   {
      // validate i and j
      if (i < 0 || i > n || j < 0 || j > n)
         throw new IllegalArgumentException
                   ("illegal vertex index(es)");

      if (c[i][j] == null)
         System.out.println("There is no path from " + i + " to " + j);
      else
      {
         System.out.print("The path is " + i + " ");
         outputPath(kay, i, j);
         System.out.println();
      }
   }
   
   /** actual code to output i to j path */
   public static void outputPath(int [][] kay, int i, int j)
   {
      if (i == j)
         return;
      if (kay[i][j] == 0)  // no intermediate vertices on path
         System.out.print(j + " ");
      else
      {// kay[i][j] is an intermediate vertex on the path
         outputPath(kay, i, kay[i][j]);
         outputPath(kay, kay[i][j], j);
      }
   }

   public static void main(String [] args)
   {
      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();
      // input a test graph
      System.out.println("enter number of vertices and edges in the "
                          + "weighted digraph");
      int n = keyboard.readInteger();
      int e = keyboard.readInteger();
      LinkedWDigraphWithAllPairsShortestPaths g =
               new LinkedWDigraphWithAllPairsShortestPaths(n); 

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

      // test allPairs
      MyInteger [][] c = new MyInteger [n + 1][n + 1];
      int [][] kay = new int [n + 1][n + 1];
      System.out.println("\nWorking on weighted directed graph");
      g.allPairs(c, kay);
      System.out.println("cost matrix is");
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= n; j++)
            System.out.print(c[i][j] + " ");
         System.out.println();
      }
      
      System.out.println("\nkay matrix is");
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= n; j++)
            System.out.print(kay[i][j] + " ");
         System.out.println();
      }
      System.out.println();
   
      g.outputPath(c, kay, 1, 5);
   }
}
