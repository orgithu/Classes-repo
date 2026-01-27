/** splay tree */

package dataStructures;

public class SplayTree extends BinarySearchTree
{
   // top-level nested class
   static class StackElement
   {
      // data members
      BinaryTreeNode node;        // a node in the tree
      boolean left;               // true iff we move to the left child of node

      // constructors
      StackElement(BinaryTreeNode theNode, boolean theLeft)
      {
         left = theLeft;
         node = theNode;
      }
   }

   // stack used by splay method
   static ArrayStack stack = new ArrayStack();

   // ascend is inherited from BinarySearchTree

   /** @return element whose key is theKey
     * @return null if there is no element with key theKey */
   public Object get(Object theKey)
   {
      Object theElement = null;    // element to return

      // pointer p starts at the root and moves through
      // the tree looking for an element with key theKey
      // nodes on the search path are saved on a stack
      // for later use by the splay method
      BinaryTreeNode p = root;

      Comparable searchKey = (Comparable) theKey;
      while (p != null)
         // examine p.element.key
         if (searchKey.compareTo(((Data) p.element).key) < 0)
         {
            stack.push(new StackElement(p, true));
            p = p.leftChild;
         }
         else
            if (searchKey.compareTo(((Data) p.element).key) > 0)
            {
               stack.push(new StackElement(p, false));
               p = p.rightChild;
            }
            else // found matching element
            {
               stack.push(new StackElement(p, false));
               theElement = ((Data) p.element).element;
               break;
            }

      splay();

      return theElement;
   }

   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key
     * @return old element (if any) with key = theKey */
   public Object put(Object theKey, Object theElement)
   {
      BinaryTreeNode p = root;     // search pointer
      Comparable elementKey = (Comparable) theKey;
      // find place to insert theElement
      while (p != null)
      {// examine p.element.key
         // move p to a child
         if (elementKey.compareTo(((Data) p.element).key) < 0)
         {
            stack.push(new StackElement(p, true));
            p = p.leftChild;
         }
         else
         {
            stack.push(new StackElement(p, false));
            if (elementKey.compareTo(((Data) p.element).key) > 0)
                 p = p.rightChild;
            else
            {// overwrite element with same key
               Object elementToReturn = ((Data) p.element).element;
               ((Data) p.element).element = theElement;
               splay();
               return elementToReturn;
            }
         }
      }
   
      // get a node for theElement and attach to pp
      BinaryTreeNode r = new BinaryTreeNode
                             (new Data(elementKey, theElement));
      if (root != null)
      {// the tree is not empty
         StackElement pp = (StackElement) stack.peek();
         if (pp.left)
            pp.node.leftChild = r;
         else
            pp.node.rightChild = r;
         stack.push(new StackElement(r, false));
         splay();
      }
      else // insertion into empty tree
         root = r;

      return null;
   }

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      Comparable searchKey = (Comparable) theKey;

      // set p to point to node with key searchKey
      BinaryTreeNode p = root;    // search pointer
      while (p != null && !((Data) p.element).key.equals(searchKey))
         // move to a child of p
         if (searchKey.compareTo(((Data) p.element).key) < 0)
         {
            stack.push(new StackElement(p, true));
            p = p.leftChild;
         }
         else
         {
            stack.push(new StackElement(p, false));
            p = p.rightChild;
         }

      if (p == null) // no element with key searchKey
      {
         splay();
         return null;
      }
   
      // save element to be removed
      Object theElement = ((Data) p.element).element; 

      // pp is parent of p
      BinaryTreeNode pp;
      if (stack.empty())
         pp = null;
      else
         pp = ((StackElement) stack.peek()).node;
   
      // restructure tree
      // handle case when p has two children
      if (p.leftChild != null && p.rightChild != null)
      {// two children
         // convert to zero or one child case
         // find element with largest key in left subtree of p
         BinaryTreeNode s = p.leftChild;
         stack.push(new StackElement(p, true));
         while (s.rightChild != null)
         {// move to larger element
            stack.push(new StackElement(s, false));
            s = s.rightChild;
         }
   
         // move largest element from s to p
         p.element = s.element;
         p = s;
         pp = ((StackElement) stack.peek()).node;
      }
   
      // p has at most one child, save this child in c
      BinaryTreeNode c;
      if (p.leftChild == null)
         c = p.rightChild;
      else
         c = p.leftChild;
   
      // remove node p
      if (p == root)
         root = c;
      else
      {// is p left or right child of pp?
         if (p == pp.leftChild)
            pp.leftChild = c;
         else
            pp.rightChild = c;
      }
   
      splay();
      return theElement;
   }

   /** type L splay step
     * @param q is splay node
     * @param p is parent of splay node */
   void lSplay(BinaryTreeNode q, BinaryTreeNode p)
   {
      p.leftChild = q.rightChild;
      q.rightChild = p;
   }

   /** type R splay step
     * @param q is splay node
     * @param p is parent of splay node */
   void rSplay(BinaryTreeNode q, BinaryTreeNode p)
   {
      p.rightChild = q.leftChild;
      q.leftChild = p;
   }

   /** type LL splay step
     * @param q is splay node
     * @param p is parent of splay node
     * @param gp is grandparent of splay node */
   void llSplay(BinaryTreeNode q, BinaryTreeNode p, BinaryTreeNode gp)
   {
      gp.leftChild = p.rightChild;
      p.leftChild = q.rightChild;
      p.rightChild = gp;
      q.rightChild = p;
   }

   /** type RR splay step
     * @param q is splay node
     * @param p is parent of splay node
     * @param gp is grandparent of splay node */
   void rrSplay(BinaryTreeNode q, BinaryTreeNode p, BinaryTreeNode gp)
   {
      gp.rightChild = p.leftChild;
      p.rightChild = q.leftChild;
      p.leftChild = gp;
      q.leftChild = p;
   }

   /** type LR splay step
     * @param q is splay node
     * @param p is parent of splay node
     * @param gp is grandparent of splay node */
   void lrSplay(BinaryTreeNode q, BinaryTreeNode p, BinaryTreeNode gp)
   {
      gp.leftChild = q.rightChild;
      p.rightChild = q.leftChild;
      q.leftChild = p;
      q.rightChild = gp;
   }

   /** type RL splay step
     * @param q is splay node
     * @param p is parent of splay node
     * @param gp is grandparent of splay node */
   void rlSplay(BinaryTreeNode q, BinaryTreeNode p, BinaryTreeNode gp)
   {
      gp.rightChild = q.leftChild;
      p.leftChild = q.rightChild;
      q.rightChild = p;
      q.leftChild = gp;
   }
   
   /** perform the splay operation, splay node is at the top of the stack */
   void splay()
   {
      if (stack.empty())
         // no splay node
         return;

      // get splay node from stack
      BinaryTreeNode q = ((StackElement) stack.pop()).node;

      while (!stack.empty())
      {// splay node is not at level 1 yet
         // get parent of splay node
         StackElement p = (StackElement) stack.pop();

         if (stack.empty())
         {// splay node is at level 2
            if (p.left)
               // type L splay
               lSplay(q, p.node);
            else
               // type R splay
               rSplay(q, p.node);
            break;
          }
          else
          {// splay node is at level > 2
             StackElement gp = (StackElement) stack.pop();
             if (gp.left)
                if (p.left)
                   llSplay(q, p.node, gp.node);
                else
                   lrSplay(q, p.node, gp.node);
             else
                if (p.left)
                   rlSplay(q, p.node, gp.node);
                else
                   rrSplay(q, p.node, gp.node);
          }
      }   

      root = q;
   }

   // test splay tree
   public static void main(String [] args)
   {
      SplayTree y = new SplayTree();

      // first test insert and ascending output
      y.put(new Integer(21), new Integer(21));
      y.put(new Integer(26), new Integer(26));
      y.put(new Integer(30), new Integer(30));
      System.out.println("Two type R splay steps have been done");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // test type LL and L splay steps
      y.put(new Integer(9), new Integer(9));
      y.put(new Integer(4), new Integer(4));
      System.out.println("A type LL and two type L splay steps have been done");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // test RR splay step
      y.put(new Integer(14), new Integer(14));
      System.out.println("An LL and an RR splay step have been done");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // test type LR splay step
      y.put(new Integer(28), new Integer(28));
      System.out.println("An LR and an RR splay steps have been done");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // more type LR and L splay steps
      y.put(new Integer(18), new Integer(18));
      y.put(new Integer(15), new Integer(15));
      System.out.println("Two type LR and one type L rotations have been done");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // type RL and RR splay steps
      y.put(new Integer(29), new Integer(29));
      System.out.println("A type RL and a type RR splay step have been done");
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // now test the delete operation
      y.remove(new Integer(2));
      y.remove(new Integer(3));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      y.remove(new Integer(10));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      y.remove(new Integer(18));
      y.remove(new Integer(4));
      y.remove(new Integer(9));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      y.remove(new Integer(30));
      y.remove(new Integer(14));
      y.remove(new Integer(7));
      y.remove(new Integer(15));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // several inserts
      y.put(new Integer(10), new Integer(10));
      y.put(new Integer(5), new Integer(5));
      y.put(new Integer(30), new Integer(30));
      y.put(new Integer(29), new Integer(29));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      y.remove(new Integer(29));
      y.remove(new Integer(28));
      y.remove(new Integer(30));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      y.put(new Integer(3), new Integer(3));
      y.remove(new Integer(26));
      y.remove(new Integer(21));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
    
      // empty out
      y.remove(new Integer(5));
      y.remove(new Integer(3));
      y.remove(new Integer(10));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // create a new splay tree
      y.put(new Integer(40), new Integer(40));
      y.put(new Integer(5), new Integer(5));
      y.put(new Integer(50), new Integer(50));
      y.put(new Integer(3), new Integer(3));
      y.put(new Integer(10), new Integer(10));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();

      y.put(new Integer(45), new Integer(45));
      y.put(new Integer(60), new Integer(60));
      y.put(new Integer(1), new Integer(1));
      y.put(new Integer(7), new Integer(7));
      y.put(new Integer(30), new Integer(30));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();

      y.put(new Integer(41), new Integer(41));
      y.put(new Integer(47), new Integer(47));
      y.put(new Integer(55), new Integer(55));
      y.put(new Integer(70), new Integer(70));
      y.put(new Integer(20), new Integer(20));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      y.put(new Integer(46), new Integer(46));
      y.put(new Integer(52), new Integer(52));
      y.put(new Integer(65), new Integer(65));
      y.put(new Integer(72), new Integer(72));
      y.put(new Integer(68), new Integer(68));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   
      // should cause multiple rotations
      y.get(new Integer(5));
      y.get(new Integer(1));
      y.get(new Integer(7));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();

      y.remove(new Integer(20));
      y.remove(new Integer(10));
      y.remove(new Integer(30));
      y.remove(new Integer(65));
      System.out.println("Elements in ascending order are");
      y.ascend();
      System.out.println();
      System.out.println("Elements in postorder order are");
      y.postOrderOutput();
      System.out.println();
   }
}
