
// 2-3 trees

package dataStructures;

public class TwoThreeTree implements Dictionary
{
   // top-level nested classes
   /** data type for nodes in a 2-3 tree */
   static class Node23
   {
      // data members
      Object lElement,        // first element
             rElement;        // possible second element
      Comparable lKey,        // key of first element
                 rKey;        // key of possible second element
      Node23 lChild,          // pointer to left child
             mChild,          // pointer to middle child
             rChild;          // pointer to possible right child

      // constructors
      Node23(Object theLElement, Comparable theLKey,
             Object theRElement, Comparable theRKey,
             Node23 theLChild, Node23 theMChild, Node23 theRChild)
      {
         lElement = theLElement;
         rElement = theRElement;
         lKey = theLKey;
         rKey = theRKey;
         lChild = theLChild;
         mChild = theMChild;
         rChild = theRChild;
      }

      Node23(Object theLElement, Comparable theLKey,
             Node23 theLChild, Node23 theMChild)
         {this(theLElement, theLKey, null, null, theLChild, theMChild, null);}
   }

   /** data type of object returned by recursive insert method */
   static class ReturnTriple
   {
      Comparable key;
      Object element;
      Node23 node;

      // constructor
      ReturnTriple(Object theElement, Comparable theKey, Node23 theNode)
      {
         element = theElement;
         key = theKey;
         node = theNode;
      }
   }

   // data member of TwoThreeTree
   Node23 root;

   // only default constructor is available
   
   /** @return element whose key is theKey
     * @return null if there is no element with key theKey */
   public Object get(Object theKey)
   {
      Comparable searchKey = (Comparable) theKey;

      // pointer p starts at the root and moves through
      // the tree looking for an element with key searchKey
      Node23 p = root;
      while (p != null)
         if (searchKey.compareTo(p.lKey) < 0)
            // move into left subtree
            p = p.lChild;
         else
            if (p.rKey == null)
               // p is a 2-node
               if (searchKey.equals(p.lKey))
                  // found matching element
                  return p.lElement;
              else
                 // searchKey > p.lKey, move to middle subtree
                 p = p.mChild;
           else
              // p is a 3-node
              if ((searchKey.compareTo(p.lKey) > 0)
                  && (searchKey.compareTo(p.rKey) < 0))
                 // move to middle subtree
                 p = p.mChild;
              else
                 if (searchKey.compareTo(p.rKey) > 0)
                    // move to right subtree
                    p = p.rChild;
                 else
                    // searchKey matches one of the elements
                      if (searchKey.equals(p.lKey))
                         return p.lElement;
                      else
                         return p.rElement;
   
      // no match was found
      return null;
   }
   

   // class data members used by recursive put method
   static Object putElement,
                 elementToReturn;
   static Comparable putKey;

   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key
     * @return old element (if any) with key = theKey */
   public Object put(Object theKey, Object theElement)
   {
      // insert recursively
      putElement = theElement;
      putKey = (Comparable) theKey;
      elementToReturn = null;
      ReturnTriple s = thePut(root);
   
      if (s != null)
         // root has split, create new root, this is a 2-node
         root = new Node23(s.element, s.key, root, s.node);
      return elementToReturn;
   }
   
   /** recursive method to insert putElement into the 2-3 tree with root p
     * overwrite old element (if any) with key equal to putKey
     * @return triple (if any) to be inserted in parent */
   static ReturnTriple thePut(Node23 p)
   {
      if (p == null)
         // insert into an empty tree
         // return a triple to insert in parent of p
         return new ReturnTriple(putElement, putKey, null);
      else
         // insert into a nonempty 2-3 tree
         if (p.rKey == null)
            // p is a 2-node
            if (putKey.compareTo(p.lKey) < 0)
            {// insert recursively in p.lChild
               ReturnTriple s = thePut(p.lChild);
               if (s != null)
               {// p.lChild did split
                  // insert s into p as lElement and mChild
                  p.rElement = p.lElement;
                  p.rKey = p.lKey;
                  p.rChild = p.mChild;
                  p.lElement = s.element;
                  p.lKey = s.key;
                  p.mChild = s.node;
               }
               // p does not split
               return null;
            }
            else
               if (putKey.compareTo(p.lKey) > 0)
               {// insert in p.mChild
                  ReturnTriple s = thePut(p.mChild);
                  if (s != null)
                  {// p.mChild did split
                     // insert s into p as rElement and rChild
                     p.rElement = s.element;
                     p.rKey = s.key;
                     p.rChild = s.node;
                  }
                  // p does not split
                  return null;
               }
               else
               {// putKey = p.lKey, update lElement
                  elementToReturn = p.lElement;
                  p.lElement = putElement;
                  // node p does not split
                  return null;
               }
         else // p is a 3-node
            if (putKey.compareTo(p.lKey) < 0)
            {// insert in p.lChild
               ReturnTriple s = thePut(p.lChild);
               if (s != null)
               {// p.lChild did split, now p will also split
                  // create a new 2-node q for p.rElement
                  Node23 q = new Node23(p.rElement, p.rKey, p.mChild, p.rChild);

                  // the triple (p.lElement, p.lKey, q) is to be
                  // inserted in the parent of p
                  ReturnTriple t = new ReturnTriple(p.lElement, p.lKey, q);
   
                  // p becomes a 2-node with lElement = s.element
                  p.rKey = null;
                  p.lElement = s.element;
                  p.lKey = s.key;
                  p.mChild = s.node;
                  return t;
               }
               return null;
            }
            else if (putKey.compareTo(p.rKey) > 0)
                 {// insert in p.rChild
                    ReturnTriple s = thePut(p.rChild);
                    if (s != null)
                    {// p.rChild did split, now p will also split
                       // create a new 2-node q for s.element
                       Node23 q = new Node23(s.element, s.key,
                                             p.rChild, s.node);
                       // the triple (p.rElement, p.rKey, q) is to be
                       // inserted in the parent of p
                       s = new ReturnTriple(p.rElement, p.rKey, q);

                       // p becomes a 2-node
                       p.rKey = null;
                    }
                    return s;
                 }
                 else if ((putKey.compareTo(p.lKey) > 0) && 
                          (putKey.compareTo(p.rKey) < 0))
                      {// insert in p.mChild
                         ReturnTriple s = thePut(p.mChild);
                         if (s != null)
                         {// p.mChild did split, now m will also split
                            // create a new 2-node q for p.rElement
                            Node23 q = new Node23(p.rElement, p.rKey,
                                                  s.node, p.rChild);
                            // the triple (s.element, s.key, q) is to be
                            // inserted in the parent of p
                            s.node = q;

                            // p becomes a 2-node
                            p.rKey = null;  // p is now a 2-node
                         }
                         return s;
                      }
                      else
                      {// putKey equals one of the keys in p
                         if (putKey == p.lKey)
                         {
                            elementToReturn = p.lElement;
                            p.lElement = putElement;
                         }
                         else
                         {
                            elementToReturn = p.rElement;
                            p.rElement = putElement;
                         }
                         // node p does not split
                         return null;
                      }
   }
            
   
   /** the left child of p has become deficient; fix deficiency
     * @return p iff p becomes deficient as a result of the fix */
   static Node23 fixLeftChild(Node23 p)
   {
      Node23 lp = p.lChild,           // left child of p
             mp = p.mChild;           // middle child of p
   
      // the left child of p must have been a 2-node before the deletion
      if (mp.rKey == null)
      {// middle child of p is a 2-node; combine lp,
       // p.lElement, and mp; lp becomes a 3-node
         lp.lElement = p.lElement;
         lp.lKey = p.lKey;
         lp.rElement = mp.lElement;
         lp.rKey = mp.lKey;
         lp.mChild = mp.lChild;
         lp.rChild = mp.mChild;
         if (p.rKey == null)
            // p was a 2-node and is now deficient
            return p;
         else
         {// p was a 3-node and is now a 2-node
            p.lElement = p.rElement;
            p.lKey = p.rKey;
            p.rKey = null;
            p.mChild = p.rChild;
            return null;
         }
      }
      else
      {// middle child of p is a 3-node, borrow from it
         lp.lElement = p.lElement;
         lp.lKey = p.lKey;
         lp.mChild = mp.lChild;
         p.lElement = mp.lElement;
         p.lKey = mp.lKey;
         mp.lChild = mp.mChild;
         mp.mChild = mp.rChild;
         mp.lElement = mp.rElement;
         mp.lKey = mp.rKey;
         mp.rKey = null;  // mp is now a 2-node
   
         // p is not deficient
         return null;
      }
   }
   
   /** the middle child of p has become deficient, fix deficiency
     * @return p iff p becomes deficient as a result of the fix */
   static Node23 fixMiddleChild(Node23 p)
   {
      Node23 lp = p.lChild,        // left child of p
             mp = p.mChild;        // middle child of p
   
      // p.mChild must have been a 2-node before the deletion
      if (lp.rKey == null)
      {// left child of p is a 2-node; combine lp,
       // p.lElement and mp; lp becomes a 3-node
         lp.rElement = p.lElement;
         lp.rKey = p.lKey;
         lp.rChild = mp.lChild;
   
         if (p.rKey == null)  // p was a 2-node
            // p becomes deficient
            return p;
         else
         {// p was a 3-node and is now a 2-node
            p.lElement = p.rElement;
            p.lKey = p.rKey;
            p.rKey = null;
            p.mChild = p.rChild;
            return null;
         }
      }
      else
      {// left child of p is a 3-node, borrow from it
         mp.lElement = p.lElement;
         mp.lKey = p.lKey;
         mp.mChild = mp.lChild;
         mp.lChild = lp.rChild;
         p.lElement = lp.rElement;
         p.lKey = lp.rKey;
         lp.rKey = null;  // left child is now a 2-node
   
         // p is not deficient
         return null;
      }
   }
   
   /** the right child of p has become deficient, fix deficiency
     * @return null because p cannot become deficient as a result of the fix */
   static Node23 fixRightChild(Node23 p)
   {
      Node23 rp = p.rChild,        // right child of p
             mp = p.mChild;        // middle child of p
   
      // p is a 3-node, p.rChild must have been a 2-node before the deletion
      if (mp.rKey == null)
      {// middle child of p is a 2-node; combine mp,
       // p.rElement and rp; mp becomes a 3-node
         mp.rElement = p.rElement;
         mp.rKey = p.rKey;
         mp.rChild = rp.lChild;
         p.rKey = null;
      }
      else
      {// middle child of p is a 3-node, borrow from it
         rp.lElement = p.rElement;
         rp.lKey = p.rKey;
         rp.mChild = rp.lChild;
         rp.lChild = mp.rChild;
         p.rElement = mp.rElement;
         p.rKey = mp.rKey;
         mp.rKey = null;  // middle child is now a 2-node
      }
   
      return null;
   }
   
   /** remove the largest element in the subtree with root p,
     * @return the removed element, its key,
     * and node p (if p becomes deficient) */
   static ReturnTriple removeLargest(Node23 p)
   {
      if (p.lChild != null)
         // p is not a leaf
         if (p.rKey == null)
         {// p is a 2-node
            ReturnTriple s = removeLargest(p.mChild);
            if (s.node != null)
               s.node = fixMiddleChild(p);
            return s;
         }
         else
         {// p is a 3-node
            ReturnTriple s = removeLargest(p.rChild);
            if (s.node != null)
               s.node = fixRightChild(p);
            return s;
         }
      else
         // p is a leaf
         if (p.rKey == null)
            // p is a 2-node which becomes deficient
            return new ReturnTriple(p.lElement, p.lKey, p);
         else
         {// p is a 3-node which does not become deficient
            ReturnTriple s = new ReturnTriple(p.rElement, p.rKey, null);
            p.rKey = null;
            return s;
         }
   }
   

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      Comparable searchKey = (Comparable) theKey;
      ReturnTriple s = theRemove(searchKey, root);
      if (s.node != null)
         // root has become deficient
         root = root.lChild;
      
      return s.element;
   }

   /** remove the element with key theKey
     * @return a triple s such that s.element is the removed
     * element and s.node == null iff the root p does not become deficient */
   static ReturnTriple theRemove(Comparable theKey, Node23 p)
   {
      if (p == null)
         // empty subtree
         return new ReturnTriple(null, null, null);
   
      if (theKey.compareTo(p.lKey) < 0)
      {// delete from left subtree
         ReturnTriple s = theRemove(theKey, p.lChild);
         if (s.node != null)
            // p.lChild has become deficient
            s.node = fixLeftChild(p);
         return s;
      }
      else
         if (theKey.equals(p.lKey))
         {// p.lElement is element to delete
            ReturnTriple s = new ReturnTriple(p.lElement, null, null);
            if (p.lChild != null)
            {// p is not a leaf, delete largest element
             // in p.lChild and place it in p.lElement
               ReturnTriple t = removeLargest(p.lChild);
               p.lElement = t.element;
               p.lKey = t.key;
               if (t.node != null)
                  // p.lChild has become deficient
                  s.node = fixLeftChild(p);
               return s;
            }
            else
               if (p.rKey == null)
               {// deletion from a 2-node leaf
                  s.node = p;
                  return s;
                }
                else
                {// deletion from a 3-node leaf
                   p.lElement = p.rElement;
                   p.lKey = p.rKey;
                   p.rKey = null;
                   return s;
                }
         }
         else
            if (p.rKey == null)  
            {// p is a 2-node, delete from mChild
               ReturnTriple s = theRemove(theKey, p.mChild);
               if (s.node != null)
                  // p.mChild has become deficient
                  s.node = fixMiddleChild(p);
               return s;
            }
            else
               // p is a 3-node
               if (theKey.compareTo(p.rKey) > 0)
               {// delete from rChild
                  ReturnTriple s = theRemove(theKey, p.rChild);
                   if (s.node != null)
                      // p.rChild has become deficient
                      s.node = fixRightChild(p);
                   return s;  // p is not deficient
               }
               else
                  if (theKey.compareTo(p.rKey) < 0)
                  {// delete from mChild
                     ReturnTriple s = theRemove(theKey, p.mChild);
                     if (s.node != null)
                         s.node = fixMiddleChild(p);
                     return s;
                   }
                   else
                   {// p.rElement is element to delete
                      ReturnTriple s = new ReturnTriple(p.rElement, null, null);
                      if (p.lChild != null)
                      {// p is not a leaf, deletest largest element in
                       // p.mChild and place in p.rElement
                          ReturnTriple t = removeLargest(p.mChild);
                          p.rElement = t.element;
                          p.rKey = t.key;
                          if (t.node != null)
                             // p.mChild has become deficient
                             s.node = fixMiddleChild(p);
                          return s;
                      }   
                      else
                      {// deletion from a 3-node leaf
                         p.rKey = null;
                         return s;
                      }
                   }
   }
   
   /** output elements in ascending order */
   public void ascend()
      {theAscend(root);}

   /** output elements in 2-3 tree with root t in ascending order */
   static void theAscend(Node23 t)
   {// use an inorder traversal.
      if (t != null)
      {
         theAscend(t.lChild);
         System.out.print(t.lElement + " ");
         theAscend(t.mChild);
         if (t.rKey != null)
         {// t is a 3-node
            System.out.print(t.rElement + " ");
            theAscend(t.rChild);
         }
      }
   }
   
   /** output elements in postorder */
   public void postOutput()
      {thePostOutput(root);}

   /** output elements in 2-3 tree with root t in ascending order */
   public void thePostOutput(Node23 t)
   {// do a postorder traversal
      if (t != null)
      {
         thePostOutput(t.lChild);
         thePostOutput(t.mChild);
         System.out.print(t.lElement + " ");
         if (t.rKey != null)
         {
            thePostOutput(t.rChild);
            System.out.print(t.rElement + " ");
         }
      }
   }

   /** test program for TwoThreeTree */
   public static void main(String [] args)
   {
      // define an empty 2-3 tree
      TwoThreeTree y = new TwoThreeTree();
   
      // first test insert and ascending output
      y.put(new Integer(21), new Integer(21));
      y.put(new Integer(29), new Integer(29));
      y.put(new Integer(30), new Integer(30));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();

      // cause leftmost node to split
      y.put(new Integer(9), new Integer(9));
      y.put(new Integer(4), new Integer(4));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();

      // cause middle leaf and root to split
      y.put(new Integer(14), new Integer(14));
      y.put(new Integer(15), new Integer(15));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();

      // cause righmost leaf to split
      y.put(new Integer(32), new Integer(32));
      y.put(new Integer(31), new Integer(31));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();

      // cause a double split
      y.put(new Integer(22), new Integer(22));
      y.put(new Integer(23), new Integer(23));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();

      y.put(new Integer(24), new Integer(24));
      y.put(new Integer(25), new Integer(25));
   
      // now test the search operation
      if (y.get(new Integer(1)) == null)
         System.out.println("1 was not found");
      if (y.get(new Integer(28)) == null)
         System.out.println("28 was not found");
      if (y.get(new Integer(40)) == null)
         System.out.println("40 was not found");
      if (y.get(new Integer(23)) != null)
         System.out.println("23 was found");
      if (y.get(new Integer(30)) != null)
         System.out.println("30 was found");
   
      // cause triple split
      y.put(new Integer(27), new Integer(27));
      y.put(new Integer(28), new Integer(28));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
   
      // now test the delete operation
      y.remove(new Integer(4));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(14));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(21));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(30));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(32));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(28));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
    
      // do several inserts
      y.put(new Integer(26), new Integer(26));
      y.put(new Integer(18), new Integer(18));
      y.put(new Integer(36), new Integer(36));
      y.put(new Integer(40), new Integer(40));
      y.put(new Integer(32), new Integer(32));
      y.put(new Integer(34), new Integer(34));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
   
      // delete from the interior
      y.remove(new Integer(29));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(22));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(27));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(25));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(36));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(26));
      y.remove(new Integer(15));
      y.remove(new Integer(32));
      y.remove(new Integer(23));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(34));
      y.remove(new Integer(24));
      y.remove(new Integer(18));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
      y.remove(new Integer(31));
      y.remove(new Integer(9));
      y.remove(new Integer(40));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOutput();
      System.out.println();
   }
}
