/** adjacency matrix representation of an n vertex graph
  * using an (n-1) x n two-dimensional array */

package dataStructures;

public class GraphAdjacencyMatrix
{
   // data members
   boolean [][] a;   // adjacency array

   // constructor
   public GraphAdjacencyMatrix(int theVertices)
   {
      // validate theVertices
      if (theVertices < 0)
         throw new IllegalArgumentException
                   ("number of vertices must be >= 0");
   
      a = new boolean [theVertices][theVertices + 1];
   }
   
   // methods
   /** @throws IndexOutOfBoundsException when
     * either i or j is not between 1 and a.length */
   void checkIndex(int i, int j)
   {
      if (i < 1 || j < 1 || i >  a.length || j >  a.length)
         throw new IndexOutOfBoundsException
             ("i = " + i + "  j = " + j + " vertices = "
              + a.length);
   }

   /** @return A(i,j) */
   public int get(int i, int j)
   {
      checkIndex(i, j);

      // diagonal entry is always zero
      if (i == j)
         return 0;
   
      // not a diagonal entry
      if (i < j)
         // upper triangle
         return (a[i-1][j-1]) ? 1 : 0;
      else
         // lower triangle
         return (a[i-2][j-1]) ? 1 : 0;
   }
   
   /** set A(i,j) = newValue */
   public void set(int i, int j, int newValue)
   {
      checkIndex(i, j);

      // validate newValue
      if (newValue < 0 || newValue > 1)
         throw new IllegalArgumentException
                   ("new value must be 0 or 1");
   
      // store newValue
      if (i < j)
         // upper triangle
         a[i-1][j-1] = (newValue == 1) ? true : false;
      else
         if (i > j)
            // lower triangle
            a[i-2][j-1] = (newValue == 1) ? true : false;
           else
              // diagonal entry
              if (newValue == 1)
                 throw new IllegalArgumentException
                           ("diagonal value must be 0");
   }
   
   
   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      int n = 5;     // number of vertices
      GraphAdjacencyMatrix x = new GraphAdjacencyMatrix(n);
      GraphAdjacencyMatrix y = new GraphAdjacencyMatrix(0);

      // set a few edges
      x.set(1, 3, 1);
      x.set(3, 4, 1);
      x.set(5, 2, 1);
      x.set(2, 1, 1);
      x.set(1, 1, 0);
      x.set(1, 2, 0);

      // output all
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= n; j++)
            System.out.print(x.get(i, j) + "  ");
         System.out.println();
      }
   }
}
