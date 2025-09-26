/** indexed binary search tree with duplicates */

package dataStructures;

import java.util.*;
import wrappers.*;
import utilities.*;

public class DIndexedBinarySearchTree extends DBinarySearchTree
                                      implements DIndexedBSTree
{
   // top-level nested class
   static class IndexedElement
   {
      // data members
      Object element;        // element in node
      int leftSize;          // number of nodes in left subtree

      // constructors
      IndexedElement(int theLeftSize, Object theElement)
      {
         leftSize = theLeftSize;
         element = theElement;
      }

      IndexedElement(Object theElement)
         {element = theElement;}

      /** @return equivalent string suitable for output */
      public String toString()
         {return element.toString();}
   }

   // class data member
   Random random = new Random();

   // methods of DBSTree and DIndexedBSTree
   // ascend is inherited from DBinarySearchTree
   /** @return element whose key is theKey
     * @return null if there is no element with key theKey */
   public Object get(Object theKey)
   {
      Object theElement = super.get(theKey);
      if (theElement == null)
         // no matching element
         return null;
      else
         return ((IndexedElement) theElement).element;
   }

   /** @return Data object of element whose index is theIndex
     * @return null if there is no element with index theIndex */
   Data getDataElement(int theIndex)
   {
      BinaryTreeNode p = root;
      while (p != null)
      {
         IndexedElement q = (IndexedElement) ((Data) p.element).element;
         if (theIndex < q.leftSize)
            // desired element is in left subtree
            p = p.leftChild;
         else
            if (theIndex > q.leftSize)
            {// desired element is in left subtree
      	        theIndex -= q.leftSize + 1;
                p = p.rightChild;
            }
            else 
               // found desired element
               return (Data) p.element; 
      }

      // no element with index equal to theIndex
      return null;
   }

   /** @return element whose index is theIndex
     * @return null if there is no element with index theIndex */
   public Object get(int theIndex)
   {
      Data theData = getDataElement(theIndex);
      if (theData == null)
         // no element with index theIndex
         return null;
      else
         return ((IndexedElement) theData.element).element;
   }
   
   /** insert an element with the specified key */
   public void put(Object theKey, Object theElement)
   {
      BinaryTreeNode p = root,     // search pointer
                     pp = null;    // parent of p
      Comparable elementKey = (Comparable) theKey;
      // find place to insert theElement
      while (p != null)
      {// examine p.element.key
         pp = p;
         // move p to a child
         if (elementKey.lessThan(((Data) p.element).key))
         {// will insert in left subtree
            ((IndexedElement) ((Data) p.element).element).leftSize++;
            p = p.leftChild;
         }
         else if (elementKey.greaterThan(((Data) p.element).key))
                 // will insert in right subtree
                 p = p.rightChild;
              else
              {// make a random decision
                 if (random.nextFloat() < 0.5)
                 {// will insert in left subtree
                    ((IndexedElement) ((Data) p.element).element).leftSize++;
                    p = p.leftChild;
                  }
                  else
                    // will insert in right subtree
                    p = p.rightChild;
              }
      }
   
      // get a node for theElement and attach to pp
      IndexedElement q = new IndexedElement(0, theElement);
      BinaryTreeNode r = new BinaryTreeNode
                             (new Data(elementKey, q));
      if (root != null)
         // the tree is not empty
         if (elementKey.lessThan(((Data) pp.element).key))
            pp.leftChild = r;
         else
            if (elementKey.greaterThan(((Data) pp.element).key))
               pp.rightChild = r;
           else
           {// equal keys, pp has at most one child
            // figure out where p belongs
              if (((IndexedElement) ((Data) pp.element).element).leftSize == 1
                      && pp.leftChild == null)
                  pp.leftChild = r;
              else
                 pp.rightChild = r;
           }
      else // insertion into empty tree
         root = r;
   }
   
   /** remove the element with key equal to theKey
     * such an element is guaranteed to exist in the tree */
   void theRemove(Object theKey)
   {
      Comparable searchKey = (Comparable) theKey;

      // p will eventually point to node with key searchKey
      BinaryTreeNode p = root,    // search pointer
                     pp = null;   // parent of p

      // follow path to searchKey decrementing leftSize each time
      // we move to a left subtree
      while (!((Data) p.element).key.equals(searchKey))
      {// move to a child of p
         pp = p;
         if (searchKey.lessThan(((Data) p.element).key))
         {
            ((IndexedElement) ((Data) p.element).element).leftSize--;
            p = p.leftChild;
         }
         else
            p = p.rightChild;
      }

      // restructure tree
      // handle case when p has two children
      if (p.leftChild != null && p.rightChild != null)
      {// two children
         // convert to zero or one child case
         // find element with largest key in left subtree of p
         int ls = ((IndexedElement) ((Data) p.element).element)
                                      .leftSize - 1; // save value
         BinaryTreeNode s = p.leftChild,
                        ps = p;  // parent of s
         while (s.rightChild != null)
         {// move to larger element
            ps = s;
            s = s.rightChild;
         }
   
         // move largest element from s to p
         p.element = s.element;
         ((IndexedElement) ((Data) p.element).element).leftSize = ls;
         p = s;
         pp = ps;
      }
   
      // p has at most one child, save this child in c
      BinaryTreeNode c;
      if (p.leftChild == null)
         c = p.rightChild;
      else
         c = p.leftChild;
   
      // remove node p
      if (p == root) root = c;
      else
      {// is p left or right child of pp?
         if (p == pp.leftChild)
            pp.leftChild = c;
         else
            pp.rightChild = c;
      }
   }
   
   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      Object theElement = get(theKey);
      if (theElement == null)
         // no matching element
         return null;

      // there is a matching element, remove it
      theRemove(theKey);

      return theElement;
   }
   
   /** @return element with index theIndex and remove it
     * @return null if there is no element with this index */
     public Object remove(int theIndex)
   {
      Data theData = getDataElement(theIndex);
      if (theData == null)
         // no element with index theIndex
         return null;

      // there is an element with index theIndex, remove it
      theRemove(theData.key);

      return ((IndexedElement) theData.element).element;
   }

   // test indexed binary search tree with duplicates class
   public static void main(String [] args)
   {
      DIndexedBinarySearchTree y = new DIndexedBinarySearchTree();
    
      // insert a few elements
      y.put(new MyInteger(1), new Character('a'));
      y.put(new MyInteger(6), new Character('f'));
      y.put(new MyInteger(3), new Character('c'));
      y.put(new MyInteger(4), new Character('e'));
      y.put(new MyInteger(1), new Character('b'));
      y.put(new MyInteger(6), new Character('g'));
      y.put(new MyInteger(3), new Character('d'));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove an element
      System.out.println("Removed element " + y.remove(new MyInteger(3)) +
                         " with key 3");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove another element
      System.out.println("Removed element " + y.remove(new MyInteger(3)) +
                         " with key 3");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove yet another element
      System.out.println("Removed element " + y.remove(new MyInteger(1)) +
                         " with key 1");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // try to remove a nonexistent element
      System.out.println("Removed element " + y.remove(new MyInteger(8)) +
                         " with key 8");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // make a few inserts
      y.put(new MyInteger(11), new Character('f'));
      y.put(new MyInteger(5), new Character('c'));
      y.put(new MyInteger(3), new Character('b'));
      y.put(new MyInteger(13), new Character('g'));
      y.put(new MyInteger(7), new Character('d'));
      y.put(new MyInteger(15), new Character('h'));
      y.put(new MyInteger(9), new Character('e'));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // access elements by index
      for (int i = 0; i < 9; i++)
         System.out.println("The element with index " + i + " is " + y.get(i));

      // test remove by index
      System.out.println("Removed element " + y.remove(5) +
                         " with index 5");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Removed element " + y.remove(0) +
                         " with index 0");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Removed element " + y.remove(5) +
                         " with index 5");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
   }
}
