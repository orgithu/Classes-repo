/** test Graph.bipartite */

package dataStructures;

import utilities.*;

public class TestBipartite
{
   public static void main(String [] args)
   {
      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();
      // try two graphs
      for (int q = 1; q <= 2; q++)
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

         // do a bipartite labeling
         int [] label = g.bipartite();

         if (label != null)
         {
            System.out.println("The graph is bipartite, the labeling is");
            for (int i = 1; i <= label.length - 1; i++)
               System.out.print(label[i] + " ");
            System.out.println();
         }
         else
            System.out.println("The graph is not bipartite");
      }   
   }   
}
