/** level with maximum number of nodes */

package dataStructures;

public class BinaryTreeMaxLevel
{
   /** @return level with max umber of nodes */
   public static int maxLevel(BinaryTreeNode t)
   {
      if (t == null)
         // tree is empty
         return 0;

      // maxLevel is current level with max nodes
      // maxNodes is number of nodes on level maxLevel
      int maxLevel = 0;
      int maxNodes = 0;
      
      // create a unique pointer to use an end of level marker in queue
      BinaryTreeNode endOfLevel = new BinaryTreeNode();
   
      // put t and endOfLevel marker on queue q
      ArrayQueue q = new ArrayQueue();
      q.put(t);
      q.put(endOfLevel);

      // do a level order traversal
      int currentLevel = 1;    // level of nodes being examined
      int numOfNodes = 0;      // number of nodes seen of currentLevel
      while (true) 
      {
         BinaryTreeNode p = (BinaryTreeNode) q.remove();
         if (p == endOfLevel)
         {
            // see if currentLevel has more nodes than MaxLevel
            if (numOfNodes > maxNodes)
            {// found a level with more nodes
               maxNodes = numOfNodes;
                maxLevel = currentLevel;
            }
            else
               if (numOfNodes == 0)
                  // empty level, no more nodes
                  return maxLevel;

            // set currentLevel and numOfNodes to start new level
            currentLevel++;
            numOfNodes = 0;

            q.put(endOfLevel);
         }
         else
         {// continuation of current level
            numOfNodes++;

            // put p's children on queue
            if (p.leftChild != null) 
               q.put(p.leftChild);
            if (p.rightChild != null)
               q.put(p.rightChild);
         }
      }
    }

   /** test program */
   public static void main(String [] args)
   {
      BinaryTreeNode y = new BinaryTreeNode(new Integer(4));
      BinaryTreeNode z = new BinaryTreeNode(new Integer(5));
      BinaryTreeNode x = new BinaryTreeNode(new Integer(2), y, z);
      BinaryTreeNode w = new BinaryTreeNode(new Integer(7));
      BinaryTreeNode v = new BinaryTreeNode(new Integer(6), null, w);
      y = new BinaryTreeNode(new Integer(3), v, null);
      z = new BinaryTreeNode(new Integer(1), x, y);

      System.out.println("The elements in preorder are");
      BinaryTreeTraversal.preOrder(z);
      System.out.println();

      System.out.println("The elements in inorder are");
      BinaryTreeTraversal.inOrder(z);
      System.out.println();

      System.out.println("The elements in postorder are");
      BinaryTreeTraversal.postOrder(z);
      System.out.println();

      System.out.println("Level " + maxLevel(z) + 
                         " has the maximum number of nodes");
   }
}
