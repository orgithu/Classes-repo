
/** binary search tree */

package dataStructures;

import java.util.*;

public class NewDBinarySearchTree extends BinarySearchTree
                                  implements DBSTree
{
   // class data member
   static Random random = new Random();  // random number generator

   // override BinarySearchTree.put
   /** insert an element with the specified key
     * @return null */
   public Object put(Object theKey, Object theElement)
   {
      BinaryTreeNode p = root,     // search pointer
                     pp = null;    // parent of p
      Comparable elementKey = (Comparable) theKey;
      // find place to insert theElement
      while (p != null)
      {// examine p.element.key
         pp = p;
         // move p to a child
         if (elementKey.compareTo(((Data) p.element).key) < 0)
            p = p.leftChild;
         else if (elementKey.compareTo(((Data) p.element).key) > 0)
                 p = p.rightChild;
              else
                 // make a random decision
                 if (random.nextFloat() < 0.5)
                    p = p.leftChild;
                 else p = p.rightChild;
      }
   
      // get a node for theElement and attach to pp
      BinaryTreeNode r = new BinaryTreeNode
                             (new Data(elementKey, theElement));
      if (root != null)
         // the tree is not empty
         if (elementKey.compareTo(((Data) pp.element).key) < 0 ||
           (elementKey.equals(((Data) pp.element).key)
            && pp.leftChild == null))
            pp.leftChild = r;
         else
            pp.rightChild = r;
         // could randomly select subtree in case of equal keys and pp a leaf
      else // insertion into empty tree
         root = r;

      return null;
   }
   
   // test binary search tree with duplicates class
   public static void main(String [] args)
   {
      NewDBinarySearchTree y = new NewDBinarySearchTree();
    
      // insert a few elements
      y.put(new Integer(1), new Character('a'));
      y.put(new Integer(6), new Character('f'));
      y.put(new Integer(3), new Character('c'));
      y.put(new Integer(4), new Character('e'));
      y.put(new Integer(1), new Character('b'));
      y.put(new Integer(6), new Character('g'));
      y.put(new Integer(3), new Character('d'));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove an element
      System.out.println("Removed element " + y.remove(new Integer(3)) +
                         " with key 3");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove another element
      System.out.println("Removed element " + y.remove(new Integer(3)) +
                         " with key 3");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove yet another element
      System.out.println("Removed element " + y.remove(new Integer(1)) +
                         " with key 1");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // try to remove a nonexistent element
      System.out.println("Removed element " + y.remove(new Integer(8)) +
                         " with key 8");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
   }
}
