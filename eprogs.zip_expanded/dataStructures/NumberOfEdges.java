

/** number of edges in the adjacency matrix of a digraph */

package dataStructures;

public class NumberOfEdges
{
   // data members
   boolean [][] a;   // adjacency array

   // constructor
   public NumberOfEdges(int theVertices)
   {
      // validate theVertices
      if (theVertices < 0)
         throw new IllegalArgumentException
                   ("number of vertices must be >= 0");
   
      a = new boolean [theVertices + 1][theVertices + 1];
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
      return (a[i][j]) ? 1 : 0;
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
      if (i != j)
         // not a diagonal entry
         a[i][j] = (newValue == 1) ? true : false;
      else
         // diagonal entry
         if (newValue == 1)
            throw new IllegalArgumentException
                      ("diagonal value must be 0");
   }

   /** @return number of edges in the digraoh */
   public int numberOfEdges()
   {
      int count = 0;      // default initial value is 0
      for (int i = 1; i < a.length; i++)
         for (int j = 1; j < a.length; j++)
            if (a[i][j])
               // directed edge (i,j) exists
               count++;

      return count;
   }

   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      int n = 5;     // number of vertices
      NumberOfEdges x = new NumberOfEdges(n);
      NumberOfEdges y = new NumberOfEdges(0);

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

      // number of edges
      System.out.println("The number of edges is " + x.numberOfEdges());
      System.out.println("The number of edges in a 0 vertex digraph is "
                         + y.numberOfEdges());
   }
}
