/** test Graph.directedTC */

package dataStructures;

import utilities.*;

public class TestDirectedTransitiveClosure
{
   public static void main(String [] args)
   {
      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();
      // try three graphs
      for (int q = 1; q <= 3; q++)
      {
         // input a test graph
         System.out.println("Enter number of vertices in the digraph");
         int n = keyboard.readInteger();

         LinkedDigraph g = new LinkedDigraph(n);

         System.out.println("Enter number of edges in the digraph");
         int e = keyboard.readInteger();
         for (int i = 1; i <= e; i++)
         {
            System.out.println("Enter edge " + i);
            int u = keyboard.readInteger();
            int v = keyboard.readInteger();
            g.putEdge(new Edge(u, v));
         }
         System.out.println("The input digraph is");
         g.output();

         // find the transitive closure
         int [][] tc = g.directedTC();

         System.out.println("The transitive closure of the digraph is");
         for (int i = 1; i <= tc.length - 1; i++)
         {
            for (int j = 1; j <= tc.length - 1; j++)
               System.out.print(tc[i][j] + " ");
            System.out.println();
         }
         System.out.println();
      }
   }   
}
