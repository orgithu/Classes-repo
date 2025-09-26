
/** extension of binary search tree to include a remove max method */

package dataStructures;

import utilities.*;
import wrappers.*;

public class ExtendedBinarySearchTree extends BinarySearchTree
{
   /** @return element with maximum key and remove it
     * @return null if tree is empty */
   public Object removeMax()
   {
      if (root == null)
         // tree is empty
         return null;

      // p will eventually point to node with maximum key
      BinaryTreeNode p = root,    // search pointer
                     pp = null;   // parent of p

      // follow right child pointers to element with maxkey
      while (p.rightChild != null)
      {// move to right child of p
         pp = p;
         p = p.rightChild;
      }

      // save element to be removed
      Object theElement = ((Data) p.element).element; 
   
      // remove node p from the tree, p has at most one child
      if (p == root)
         root = p.leftChild;
      else
         pp.rightChild = p.leftChild;
   
      return theElement;
   }

   // test extended binary search tree class
   public static void main(String [] args)
   {
      ExtendedBinarySearchTree y = new ExtendedBinarySearchTree();
    
      // insert a few elements
      y.put(new MyInteger(1), new Character('a'));
      y.put(new MyInteger(6), new Character('c'));
      y.put(new MyInteger(4), new Character('b'));
      y.put(new MyInteger(8), new Character('d'));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove a max element
      System.out.println("Removed max element " + y.removeMax());
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove another max element
      System.out.println("Removed max element " + y.removeMax());
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove yet another max element
      System.out.println("Removed max element " + y.removeMax());
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      y.put(new MyInteger(1), new Character('a'));
      y.put(new MyInteger(6), new Character('c'));
      y.put(new MyInteger(4), new Character('b'));
      y.put(new MyInteger(8), new Character('d'));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
   }
}
