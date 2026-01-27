/** indexed binary search tree */

package dataStructures;

public class IndexedBinarySearchTree extends BinarySearchTree
                                     implements IndexedBSTree
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

   // methods of BSTree and IndexedBSTree
   // ascend is inherited from BinarySearchTree
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
   
   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key
     * @return old element (if any) with key = theKey */
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
              {// overwrite element with same key
                 Object elementToReturn = ((Data) p.element).element;
                 ((Data) p.element).element = theElement;
                 return elementToReturn;
              }
      }
   
      // get a node for theElement and attach to pp
      IndexedElement q = new IndexedElement(0, theElement);
      BinaryTreeNode r = new BinaryTreeNode
                             (new Data(elementKey, q));
      if (root != null)
         // the tree is not empty
         if (elementKey.compareTo(((Data) pp.element).key) < 0)
            pp.leftChild = r;
         else
            pp.rightChild = r;
      else // insertion into empty tree
         root = r;
   
      // a new element has been inserted , update leftSize
      // by following search path to elementKey
      p = root;
      while (true)
         if (elementKey.compareTo(((Data) p.element).key) < 0)
         {// theElement was inserted in left subtree of p
            ((IndexedElement) ((Data) p.element).element).leftSize++;
            p = p.leftChild;
         }
         else
            if (elementKey.compareTo(((Data) p.element).key) > 0)
               // theElement was inserted in right subtree of p
               p = p.rightChild;
            else
               // reached newly inserted element
               return null;
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
         if (searchKey.compareTo(((Data) p.element).key) < 0)
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

   // test indexed binary search tree class
   public static void main(String [] args)
   {
      IndexedBinarySearchTree y = new IndexedBinarySearchTree();
    
      // insert a few elements
      y.put(new Integer(1), new Character('a'));
      y.put(new Integer(6), new Character('c'));
      y.put(new Integer(4), new Character('b'));
      y.put(new Integer(8), new Character('d'));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove an element
      System.out.println("Removed element " + y.remove(new Integer(4)) +
                         " with key 4");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove another element
      System.out.println("Removed element " + y.remove(new Integer(8)) +
                         " with key 8");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // remove yet another element
      System.out.println("Removed element " + y.remove(new Integer(6)) +
                         " with key 6");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // try to remove a nonexistent element
      System.out.println("Removed element " + y.remove(new Integer(6)) +
                         " with key 6");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();

      // make a few inserts
      y.put(new Integer(11), new Character('f'));
      y.put(new Integer(5), new Character('c'));
      y.put(new Integer(3), new Character('b'));
      y.put(new Integer(13), new Character('g'));
      y.put(new Integer(7), new Character('d'));
      y.put(new Integer(15), new Character('h'));
      y.put(new Integer(9), new Character('e'));
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
