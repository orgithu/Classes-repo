/** union/find with weighting and path splitting */

package applications;

public class FastUnionFindWithPathSplitting
{
   // top-level nested class
   private static class Node
   {
      // data members
      int parent;     // pointer to parent in tree
      boolean root;   // true iff node is a root
   
      // constructor
      private Node()
      {
         parent = 1;    // one node in tree
         root = true;   // this node is a root
      }
   }

   // data member of FastUnionFindWithPathSplitting
   Node [] node;

   /** initialize n trees, one element per tree/class/set */
   public FastUnionFindWithPathSplitting(int n)
   {
      // allocate an array for the nodes
      node = new Node [n + 1];

      // now allocate the nodes
      for (int e = 1; e <= n; e++)
         node[e] = new Node();
   }
   
   /** @return root of the tree containing element e
     * perform path splitting */
   public int find(int e)
   {
      int current = e,  // current node
          p,            // parent of current
          gp;           // grandparent of current
   
      // see if there are pointers to change
      if (node[current].root)
         // e is the tree root, no path to split
         return current;
      p = node[current].parent;
      if (node[p].root)
         // e is a level 2 node, no path to split
         return p;
      gp = node[p].parent;
   
      // at least one pointer to change
      // move to the root, splitting the path from e to the root
      while(true)
      {
         node[current].parent = gp;  // path splitting
         if (node[gp].root)
            // no more parent pointers to change
            return gp;
   
         // move current, p, and gp one level up the tree
         current = p;
         p = gp;
         gp = node[p].parent;
      }
   }
   
   /** combine trees with roots i and j */
   public void union(int i, int j)
   {// Use the weighting rule.
      if (node[i].parent < node[j].parent)
      {
         // i becomes subtree of j
         node[j].parent += node[i].parent;
         node[i].root = false;
         node[i].parent = j;
      }
      else
      {// j becomes subtree of i
         node[i].parent += node[j].parent;
         node[j].root = false;
         node[j].parent = i;
      }
   }

   /** test program */
   public static void main(String [] args)
   {
      FastUnionFindWithPathSplitting x =
               new FastUnionFindWithPathSplitting(10);
      x.union(1, 2);
      x.union(3, 4);
      x.union(1, 3);
      System.out.println("find(1) = " + x.find(1) +
                         " find(2) = " + x.find(2));
      System.out.println("find(3) = " + x.find(3) +
                         " find(4) = " + x.find(4));
      System.out.println("find(5) = " + x.find(5) +
                         " find(6) = " + x.find(6));
   }
}
