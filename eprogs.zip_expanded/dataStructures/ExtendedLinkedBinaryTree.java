
/** extension of linked binary trees to include methods to compare,
  * clone, and swap subtrees  */

package dataStructures;

import java.lang.reflect.*;

public class ExtendedLinkedBinaryTree extends LinkedBinaryTree
                                      implements ExtendedBinaryTree
{
   /** @return true iff the trees this and theTree are structurally
     * the same and have the same elements in corresponding nodes */
   public boolean compare(ExtendedBinaryTree theTree)
   {
      // cast theTree into an extended linked binary tree
      ExtendedLinkedBinaryTree tree = (ExtendedLinkedBinaryTree) theTree;

      return theCompare(root, tree.root);
   }

   /** recursive method to compare subtrees with root aRoot and bRoot
     * @return true iff the subtrees are equivalent */
   static boolean theCompare(BinaryTreeNode aRoot, BinaryTreeNode bRoot)
   {
      if (aRoot == null && bRoot == null)
         // both subtrees are empty
         return true;

      if (aRoot == null || bRoot == null)
         // only one subtree is empty
         return false;

      // neither subtree is empty, compare root elements and their subtrees
      if (aRoot.element.equals(bRoot.element) &&
          theCompare(aRoot.leftChild, bRoot.leftChild) &&
          theCompare(aRoot.rightChild, bRoot.rightChild))
         // subtrees are the same
         return true;
      else
         return false;
   }

   /** @return a clone of this, do not clone elements in nodes */
   public Object clone()
   {
      ExtendedLinkedBinaryTree theTree = new ExtendedLinkedBinaryTree();
      theTree.root = theClone(root);
      return theTree;
   }

   /** recursive method to clone a subtree
     * @return root of clone of subtree with root theRoot */
   static BinaryTreeNode theClone(BinaryTreeNode theRoot)
   {
      if (theRoot == null)
         return null;
      else
      {// nonempty subtree
         // copy theRoot.element
         BinaryTreeNode temp = new BinaryTreeNode(theRoot.element);
         temp.leftChild = theClone(theRoot.leftChild);   // clone left subtree
         temp.rightChild = theClone(theRoot.rightChild); // clone right subtree
         return temp;
      }
   }

   static Method theSwap;    // method to swap subtrees of a node

   // method to initialize class data members
   static
   { 
      try
      {
         Class lbt = ExtendedLinkedBinaryTree.class;
         theSwap = lbt.getMethod("swap", paramType);
      }
      catch (Exception e) {}
         // exception not possible
   }

   /** visit method to swap subtrees */
   public static void swap(BinaryTreeNode t)
   {
      BinaryTreeNode temp = t.leftChild;
      t.leftChild = t.rightChild;
      t.rightChild = temp;
   }

   /** swap subtrees of all nodes */
   public void swapSubtrees()
      {postOrder(theSwap);}

   /** test program */
   public static void main(String [] args)
   {
      ExtendedLinkedBinaryTree a = new ExtendedLinkedBinaryTree(),
                               x = new ExtendedLinkedBinaryTree(), 
                               y = new ExtendedLinkedBinaryTree(), 
                               z = new ExtendedLinkedBinaryTree();
      y.makeTree(new Integer(1), a, a);
      z.makeTree(new Integer(2), a, a);
      x.makeTree(new Integer(3), y, z);
      y.makeTree(new Integer(4), x, a);

      System.out.println("Preorder sequence is ");
      y.preOrderOutput();
      System.out.println();

      System.out.println("Inorder sequence is ");
      y.inOrderOutput();
      System.out.println();

      System.out.println("Postorder sequence is ");
      y.postOrderOutput();
      System.out.println();

      // test clone
      ExtendedLinkedBinaryTree u = (ExtendedLinkedBinaryTree) y.clone();
      System.out.println("Preorder sequence of clone is ");
      u.preOrderOutput();
      System.out.println();

      System.out.println("Inorder sequence of clone is ");
      u.inOrderOutput();
      System.out.println();

      System.out.println("Postorder sequence of clone is ");
      u.postOrderOutput();
      System.out.println();

      // test compare subtrees
      if (u.compare(y))
         System.out.println("Tree and its clone are the same");

      // test swap subtrees
      y.swapSubtrees();
      System.out.println("Preorder sequence of swapped tree is ");
      y.preOrderOutput();
      System.out.println();

      System.out.println("Inorder sequence of swapped tree is ");
      y.inOrderOutput();
      System.out.println();

      System.out.println("Postorder sequence of swapped tree is ");
      y.postOrderOutput();
      System.out.println();

      // test compare subtrees
      if (!u.compare(y))
         System.out.println("Tree and result of swapping subtrees are" +
                            " different");
   }
}
