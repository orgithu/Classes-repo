
/** clone a binary tree, although the tree is cloned, the elements
  * in the nodes are not cloned */

package dataStructures;

public class BinaryTreeCloning
{
   /** preorder cloning
     * @return root of clone */
   public static BinaryTreeNode preOrderClone(BinaryTreeNode t)
   {
      if (t != null)
      {// nonempty tree
         // make a clone of the root
         BinaryTreeNode ct = new BinaryTreeNode(t.element);

         // now do the subtrees
         ct.leftChild = preOrderClone(t.leftChild);    // clone left subtree
         ct.rightChild = preOrderClone(t.rightChild);  // clone right subtree
         return ct;
      }
      else
         return null;
   }

   /** postorder cloning
     * @return root of clone */
   public static BinaryTreeNode postOrderClone(BinaryTreeNode t)
   {
      if (t != null)
      {// nonempty tree
         // clone left subtree
         BinaryTreeNode cLeft = postOrderClone(t.leftChild);

         // clone right subtree
         BinaryTreeNode cRight = postOrderClone(t.rightChild);

         // clone root
         return new BinaryTreeNode(t.element, cLeft, cRight);
      }
      else
         return null;
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

      x = preOrderClone(z);
      System.out.println("The preorder clone elements in preorder are");
      BinaryTreeTraversal.preOrder(x);
      System.out.println();

      System.out.println("The preorder clone elements in inorder are");
      BinaryTreeTraversal.inOrder(x);
      System.out.println();

      System.out.println("The preorder clone elements in postorder are");
      BinaryTreeTraversal.postOrder(x);
      System.out.println();

      x = postOrderClone(z);
      System.out.println("The postorder clone elements in preorder are");
      BinaryTreeTraversal.preOrder(x);
      System.out.println();

      System.out.println("The postorder clone elements in inorder are");
      BinaryTreeTraversal.inOrder(x);
      System.out.println();

      System.out.println("The postorder clone elements in postorder are");
      BinaryTreeTraversal.postOrder(x);
      System.out.println();

   }
}
