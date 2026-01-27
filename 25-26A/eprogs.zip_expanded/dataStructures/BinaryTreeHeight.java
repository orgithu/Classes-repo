

/** find the height of a binary tree using a postorder traversal */

package dataStructures;

public class BinaryTreeHeight
{
   /** @return tree height */
   public static int height(BinaryTreeNode t)
   {
      if (t != null)
      {// nonempty tree
         // find height of left subtree
         int hLeft = height(t.leftChild);

         // find height of right subtree
         int hRight = height(t.rightChild);

         // return overall height
         return Math.max(hLeft, hRight) + 1;
      }
      else
         return 0;
   }

   /** test program */
   public static void main(String [] args)
   {
      BinaryTreeNode y = new BinaryTreeNode(new Integer(4));
      BinaryTreeNode z = new BinaryTreeNode(new Integer(5));
      BinaryTreeNode x = new BinaryTreeNode(new Integer(2), y, z);
      y = new BinaryTreeNode(new Integer(3));
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

      System.out.println("The tree height is " + height(z));
   }
}
