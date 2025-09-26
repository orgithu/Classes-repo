/** extension of AdjacencyWDigraph to include an input method */

package dataStructures;

import java.lang.reflect.*;
import wrappers.*;
import utilities.*;
import exceptions.*;

public class ExtendedAdjacencyWDigraph extends AdjacencyWDigraph
{
   // constructors
   public ExtendedAdjacencyWDigraph(int theVertices)
      {super(theVertices);}
   
   // default is a 0 vertex graph
   public ExtendedAdjacencyWDigraph()
      {super(0);}
   
   /** input a weighted digraph and store as this
     * @param inputMethod input method to use
     * @param inStream input stream for input */
   public void input(Method inputMethod, MyInputStream inStream)
   {
      // input number of vertices and edges
      System.out.println("Enter the number of vertices in the digraph");
      n = inStream.readInteger();
      if (n < 0)
         throw new MyInputException
                   ("number of vertices must be >= 0");

      System.out.println("Enter the number of edges in the digraph");
      int numOfEdges = inStream.readInteger();
      if (numOfEdges < 0)
         throw new MyInputException
                   ("number of edges must be >= 0");

      if (numOfEdges > n * (n - 1))
         throw new MyInputException("too many edges");

      // create a new 2D array a[][], initial values are null
      a = new Object [n + 1][n + 1];
      e = 0;    // reset current number of edges to zero
   
      // now input the edges and add them to a[][]
      WeightedEdge newEdge = new WeightedEdge(0, 0, null);
      Object [] inputMethodArgs = {inStream};
      for (int i = 1; i <= numOfEdges; i++)
      {
         System.out.println("Enter edge " + i);
         newEdge.vertex1 = inStream.readInteger();
         newEdge.vertex2 = inStream.readInteger();
         try
         {
            newEdge.weight = 
               (Operable) inputMethod.invoke(null, inputMethodArgs);
         }
         catch (Exception e)
         {
            throw new IllegalArgumentException
                      ("input: static method input() not defined");
         }
         putEdge(newEdge);
      }
   }

   /** test program */
   public static void main(String [] args)
   {
      // define an input stream to read from
      MyInputStream keyboard = new MyInputStream();

      // weights are of type MyInteger, set the input method to use
      Class [] parameterTypes = {MyInputStream.class};
      Method inputMethod = null;
      try
      {
         inputMethod = 
                MyInteger.class.getMethod("input", parameterTypes);
      }
      catch (Exception e)
      {
         // not possible
      }

      // create weighted digraph that is to be input
      ExtendedAdjacencyWDigraph g = new ExtendedAdjacencyWDigraph(4);

      // 2 digraphs
      for (int i = 1; i <= 2; i++)
      {
         // input the weighted digraph
         g.input(inputMethod, keyboard);
   
         // output the weighted digraph
         System.out.println("The number of vertices is " + g.vertices());
         System.out.println("The number of edges is " + g.edges());
         System.out.println("The graph is");
         g.output();
         System.out.println();
         System.out.println();
      }
   }
}
