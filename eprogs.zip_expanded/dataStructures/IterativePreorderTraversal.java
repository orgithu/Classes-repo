
/** iterative preorder traversal of a linked binary tree */

package dataStructures;

public class IterativePreorderTraversal
{
   /** visit method that prints the element in the node */ 
   public static void visit(BinaryTreeNode t)
      {System.out.print(t.element + " ");}

   /** preorder traversal */
   public static void preOrder(BinaryTreeNode t)
   {
      ArrayStack stack = new ArrayStack(10);
      BinaryTreeNode currentNode = t;
      while (true)
      {// traverse subtree rooted at currentNode in preorder

         // is subtree empty
         if (currentNode == null)
            // yes it is, get a subtree to traverse from the stack
            try
            {
               currentNode = (BinaryTreeNode) stack.pop();
            }
            catch (Exception e)
            {// no untraversed subtrees left
               return;
            }
   
         // first visit the root of the subtree
         visit(currentNode);
   
         // save pointer to right subtree for future traversal
         if (currentNode.rightChild != null)
            stack.push(currentNode.rightChild);
   
         // move into left subtree
         currentNode = currentNode.leftChild;
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
      preOrder(z);
      System.out.println();
   }
}
