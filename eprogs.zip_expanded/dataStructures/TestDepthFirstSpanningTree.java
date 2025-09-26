

/** test Graph.dfSpanningTree */

package dataStructures;

import utilities.*;

public class TestDepthFirstSpanningTree
{
   public static void main(String [] args)
   {
      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();
      // try three graphs
      for (int q = 1; q <= 3; q++)
      {
         // input a test graph
         System.out.println("Enter number of vertices in graph");
         int n = keyboard.readInteger();

         LinkedGraph g = new LinkedGraph(n);

         System.out.println("Enter number of edges in graph");
         int e = keyboard.readInteger();
         for (int i = 1; i <= e; i++)
         {
            System.out.println("Enter edge " + i);
            int u = keyboard.readInteger();
            int v = keyboard.readInteger();
            g.putEdge(new Edge(u, v));
         }
         System.out.println("The input graph is");
         g.output();

         // find the spanning tree
         Edge [] theTree = g.dfSpanningTree(q);

         if (theTree != null)
         {
            System.out.println("The depth-first spanning tree edges are");
            for (int i = 0; i <= theTree.length - 1; i++)
               System.out.print(theTree[i] + " ");
            System.out.println();
         }
         else
            System.out.println("The graph has no spanning tree");
      }   
   }   
}
