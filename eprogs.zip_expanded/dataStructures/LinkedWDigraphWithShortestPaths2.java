
/** single-source all-destinations algorithm using a modified min heap and
  * the linked adjacency list representation of a weighted digraph */

package dataStructures;

import java.util.*;
import utilities.*;
import wrappers.*;

public class LinkedWDigraphWithShortestPaths2 extends LinkedWDigraph
{
   // constructors
   public LinkedWDigraphWithShortestPaths2(int theVertices)
      {super(theVertices);}
   
   // default is a 0 vertex graph
   public LinkedWDigraphWithShortestPaths2()
      {this(0);}


   /** find shortest paths from vertex s
     * @return shortest distances in d
     * @return predecessor information in p */
   public void shortestPathsUsingAHeap(int s, Operable [] d, int [] p)
   {
      if (s < 1 || s > n)
         throw new IllegalArgumentException
                   ("source vertex cannot be " + s);

      ModifiedMinHeap heap = new ModifiedMinHeap(); // min heap of d values
            // for reachable vertices for which a path is yet to be generated
     
      // initialize d, p, and l
      // first set all d values to null and all p values to -1
      for (int i = 1; i <= n; i++)
      {
          d[i] = null;
          p[i] = -1;
      }
      // now change d and p values for vertices adjacent from s
      Iterator is = aList[s].iterator();
      while (is.hasNext())
      {
         WeightedEdgeNode wNode = (WeightedEdgeNode) is.next();
         d[wNode.vertex] = (Operable) wNode.weight;
         d[s] = (Operable) ((Operable) wNode.weight).zero();
         p[wNode.vertex] = s; 
         heap.put(new WeightedEdgeNode(wNode.vertex, d[wNode.vertex]));
      }
      p[s] = 0;  // source vertex has no predecessor
   
      // update d and p
      while (!heap.isEmpty())
      {// more paths exist
         // extract vertex v with least d from the min heap
         int v = ((WeightedEdgeNode) heap.removeMin()).vertex;
   
         // next shortest path is to vertex v
         // update d values
         Iterator iv = aList[v].iterator();
         while (iv.hasNext())
         {
            WeightedEdgeNode wNode = (WeightedEdgeNode) iv.next();
            int j = wNode.vertex;
      	    if (p[j] == -1 || d[j].compareTo(d[v].add(wNode.weight)) > 0)
            {
               // d[j] decreases
               d[j] = (Operable) d[v].add(wNode.weight);
               // add j to heap if not already there, else update heap entry
               if (p[j] == -1)
                  // j is not in the min heap
                  heap.put(new WeightedEdgeNode(j, d[j]));
               else
                  // j is in the min heap
                  heap.decreaseWeight(new WeightedEdgeNode(j, d[j]));
               p[j] = v;
            }
         }
      }
   }

   /** test shortestPathsUsingAHeap */
   public static void main(String [] args)
   {
      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();

      // input a test graph
      System.out.println("Enter number of vertices and edges");
      int n = keyboard.readInteger();
      int e = keyboard.readInteger();
      LinkedWDigraphWithShortestPaths2 g =
                    new LinkedWDigraphWithShortestPaths2(n);
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

      // test shortestPaths
      MyInteger [] dist = new MyInteger [n + 1];
      int [] p = new int [n + 1];
      g.shortestPathsUsingAHeap(1, dist, p);
      System.out.println("\nThe dist and p values for the directed graph are");
      for (int i = 1; i <= n; i++)
         System.out.println(dist[i] + " " + p[i]);
   }
}
