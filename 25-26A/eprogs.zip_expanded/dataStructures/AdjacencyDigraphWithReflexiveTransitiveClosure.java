   
   
/** reflexive transitive closure */

package dataStructures;

import utilities.*;

public class AdjacencyDigraphWithReflexiveTransitiveClosure
             extends AdjacencyDigraph
{
   // constructors
   public AdjacencyDigraphWithReflexiveTransitiveClosure(int theVertices)
      {super(theVertices);}
   
   // default is a 0 vertex graph
   public AdjacencyDigraphWithReflexiveTransitiveClosure()
      {this(0);}

   /** dynamic programming code to return the reflexive
     * transitive closure matrix of a digraph graph, code is
     * a modification of Program 20.8 */
   public boolean [][] reflexiveTransitiveClosure()
   {
      // create array for reflexive transitive closure
      boolean [][] rtc = new boolean [n + 1][n + 1];
   
      // copy adjacency matrix into rtc
      for (int i = 1; i <= n; i++)
         for (int j = 1; j <= n; j++)
            rtc[i][j] = a[i][j];
   
      // set diagonal entries to true
      for (int i = 1; i <= n; i++)
         rtc[i][i] = true;
   
      // 3 nested loops of Program 20.8 using logical operators
      for (int k = 1; k <= n; k++)
         for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
               rtc[i][j] = rtc[i][j] || (rtc[i][k] && rtc[k][j]);
   
      return rtc;
   }

   /** test program */
   public static void main(String [] args)
   {
      // create a graph of each adjacency unweighted type
      AdjacencyDigraphWithReflexiveTransitiveClosure g;

      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();

      // try 3 graphs
      for (int q = 1; q <= 3; q++)
      {
         // input a test graph
         System.out.println("Enter number of vertices and edges");
         int n = keyboard.readInteger();
         int e = keyboard.readInteger();
         g = new AdjacencyDigraphWithReflexiveTransitiveClosure(n);
         for (int i = 1; i <= e; i++)
         {
            System.out.println("enter edge " + i);
            int u = keyboard.readInteger();
            int v = keyboard.readInteger();
            g.putEdge(new Edge(u, v));
         }
   
         System.out.println("\nThe digraph is");
         g.output();
   
         boolean [][] rtc = g.reflexiveTransitiveClosure();
         System.out.println("The reflexive transitive closure matrix is");
         for (int i = 1; i <= n; i++)
         {
            for (int j = 1; j <= n; j++)
               System.out.print(rtc[i][j] + " ");
            System.out.println();
         }
         System.out.println();
         System.out.println();
      }
   }
}
