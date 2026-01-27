/** test Graph.maxIndependentSet */

package dataStructures;

import utilities.*;

public class TestMaxIndependentSet
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

         // find max independent set
         int [] indepSet = g.maxIndependentSet();

         System.out.println("The size of the independent set is "
                             + indepSet.length);
         System.out.println("The independent set is");
            for (int i = 0; i <= indepSet.length - 1; i++)
               System.out.print(indepSet[i] + " ");
            System.out.println();
      }   
   }   
}
