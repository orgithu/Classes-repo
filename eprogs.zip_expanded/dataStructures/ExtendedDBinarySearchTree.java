
package dataStructures;

public class ExtendedDBinarySearchTree extends DBinarySearchTreeWithGE
{
   /** @return and delete/remove element with smallest key >= theKey
     * @return null if no element has key >= theKey */
   public Object removeGreaterThanOrEqual(Object theKey)
   {
      // first find the element to delete
      BinaryTreeNode q = root,     // search pointer
                     pq = null,    // parent of q
                     p = null,     // pointer to node with smallest
                                   // key >= theKey found so far
                     pp = null;    // parent of p
      // search the tree
      while (q != null)
      {
         // is q a candidate?
         if (((Data) q.element).key.compareTo(theKey) >= 0)
         {// yes, q is a candidate
            p = q;  // q is a better candidate than old p
            pp = pq;
            // smaller keys (better candidates) in left subtree only
            pq = q;
            q = q.leftChild;
         }
         else
         {// no, q.key too small, try right subtree
            pq = q;
            q = q.rightChild;}
         }
   
      if (p == null)
         // no element has key >= theKey
         return null;
   
      // save smallest element with key >= k
      Object theElement = ((Data) p.element).element;
   
      // proceed to delete this element from the tree
   
      // handle case when p has two children
      if (p.leftChild != null && p.rightChild != null)
      {// two children
         // convert to zero or one child case
         // find element with largest key in left subtree of p
         BinaryTreeNode s = p.leftChild,
                        ps = p;  // parent of s
         while (s.rightChild != null)
         {// move to larger element
            ps = s;
            s = s.rightChild;
         }
   
         // move largest element from s to p
         p.element = s.element;
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
   
      return theElement;
   }
}
