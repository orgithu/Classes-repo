
/** iterative backtracking method to find a max clique */

package dataStructures;

import wrappers.*;
import utilities.*;

public class AdjacencyGraphWithIterativeMaxClique extends AdjacencyGraph
{
   // constructors
   public AdjacencyGraphWithIterativeMaxClique(int theVertices)
      {super(theVertices);}

   public AdjacencyGraphWithIterativeMaxClique()
      {this(0);}

   
   /** solve max-clique problem using backtracking
     * @return size of max clique
     * @param maxClique set maxClique[i] = 1 iff i is in max clique */
   public int btMaxClique(int [] maxClique)
   {
      int [] currentClique = new int [n + 1];
      int sizeOfCurrentClique = 0;
      int [] maxCliqueSoFar = maxClique;
      int sizeOfMaxCliqueSoFar = 0;
   
      // search the tree
      int currentLevel = 1;      // also gives next vertex to decide on
      while (true)
      {
         // move down and left as far as possible
         while (currentLevel <= n)
         {
            // see if vertex currentLevel is connected to others
            // in current clique
            boolean connected = true;
            for (int j = 1; j < currentLevel; j++)
               if (currentClique[j] == 1 && !a[currentLevel][j])
               {// vertex currentLevel not connected to vertex j
                  connected = false;
                  break;
               }
      
            if (connected)
            {// move to a left child
               // add vertex currentLevel to clique
               currentClique[currentLevel] = 1;
               sizeOfCurrentClique++;
               currentLevel++;
            }
            else
               break;
         }

         if (currentLevel > n)
         {// at leaf, found a larger clique,
            // update maxCliqueSoFar and sizeOfMaxCliqueSoFar
            for (int j = 1; j <= n; j++)
               maxCliqueSoFar[j] = currentClique[j];
            sizeOfMaxCliqueSoFar = sizeOfCurrentClique;
            currentLevel--; 
              // currentLevel is now last vertex for which a decision was made
         }
         else
            // move to a right child
            currentClique[currentLevel] = 0;
      
         // backup if necessary
         while (sizeOfCurrentClique + n - currentLevel <= sizeOfMaxCliqueSoFar)
         {// this subtree does not have a better leaf, backup
            while (currentLevel > 0 && currentClique[currentLevel] == 0)
               // backup from a right child
               currentLevel--;

            if (currentLevel == 0) return sizeOfMaxCliqueSoFar;

            // move to a right child, currentClique[currentLevel] was 1
            currentClique[currentLevel] = 0;
            sizeOfCurrentClique--;
         }
         currentLevel++;  // next vertex
      }
   }

   public static void main(String [] args)
   {
      int n = 7;
      AdjacencyGraphWithIterativeMaxClique g =
                    new AdjacencyGraphWithIterativeMaxClique(n);

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

      // test btMaxClique
      int [] v = new int [n + 1];
      System.out.println("\nMax Clique size is " + g.btMaxClique(v));
      System.out.print("\nSolution vector is ");
      for (int i = 1; i <= n; i++)
         System.out.print(v[i] + " ");
      System.out.println();
   }
}
