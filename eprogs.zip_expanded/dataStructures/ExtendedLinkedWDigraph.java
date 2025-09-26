
/** extension of LinkedWDigraph to include an input method */

package dataStructures;

import java.lang.reflect.*;
import java.util.*;
import wrappers.*;
import utilities.*;
import exceptions.*;

public class ExtendedLinkedWDigraph extends LinkedWDigraph
{
   // constructors
   public ExtendedLinkedWDigraph(int theVertices)
      {super(theVertices);}
   
   // default is a 0 vertex graph
   public ExtendedLinkedWDigraph()
      {super(0);}

   /** put edge e into the digraph, do not check if it is a duplicate */
   public void putEdgeNoCheck(Object theEdge)
   {
      WeightedEdge edge =  (WeightedEdge) theEdge;
      int v1 = edge.vertex1;
      int v2 = edge.vertex2;
      if (v1 < 1 || v2 < 1 || v1 > n || v2 > n || v1 == v2)
         throw new IllegalArgumentException
                   ("(" + v1 + "," + v2 + ") is not a permissible edge");

      // put v2 at front of chain aList[v1]
      aList[v1].add(0, new WeightedEdgeNode(v2, edge.weight));
      e++;
   }
   
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

      // create a new array aList for the adjacency chains
      aList = new GraphChain [n + 1];
      for (int i = 1; i <= n; i++)
         aList[i] = new GraphChain();
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
                      ("static method input() not defined");
         }
         putEdgeNoCheck(newEdge);
      }

      // check for duplicate edges
      boolean [] seen = new boolean [n + 1];
      for (int i = 1; i <= n; i++)
      {// check vertex i
         Iterator ig = iterator(i);
         while (ig.hasNext())
         {// not at list end 
            int v = ((WeightedEdgeNode) ig.next()).vertex;
            if (seen[v])
               // duplicate edge
               throw new MyInputException
                         ("Duplicate edges encountered in input");
            else seen[v] = true;
         }
   
         // reset seen
         ig = iterator(i);  
         while (ig.hasNext())
            // not at list end 
            seen[((WeightedEdgeNode) ig.next()).vertex] = false;
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
      ExtendedLinkedWDigraph g = new ExtendedLinkedWDigraph(4);

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
