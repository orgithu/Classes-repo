/** linked binary trees with a preorder enumerator */

package dataStructures;

import java.util.*;

public class LinkedBinaryTreeWithPreorderIterator extends LinkedBinaryTree
{
   /** create and return an enumerator */
   public Iterator iterator()
      {return new PreorderIterator();}

   /** preorder enumerator */
   private class PreorderIterator implements Iterator
   {
      // data members
      private BinaryTreeNode nextNode;  // next node in preorder
      private ArrayStack stack = new ArrayStack(10);
   
      // constructor
      /** set nextNode to first node in preorder */
      public PreorderIterator()
         {nextNode = root;}
   
      // methods
      /** @return true iff tree has more elements */
      public boolean hasNext()
      {
         return nextNode != null;
      }

      /** @return next element in preorder
        * @throws NoSuchElementException
        * when there is no next element */
      public Object next()
      {
         if (nextNode != null)
         {
            // save next preorder element
            Object obj = nextNode.element;
            
            // put right subtree on the stack if not null
            if (nextNode.rightChild != null)
               // right subtree will be examined later
               stack.push(nextNode.rightChild);

            // set nextNode to next node in preorder
            if (nextNode.leftChild != null)
               // root of left subtree is the next node in preorder
               nextNode = nextNode.leftChild;
            else
            {// get next preorder node from the stack
               try
               {
                  nextNode = (BinaryTreeNode) stack.pop();
               }
               catch (Exception e)
               {// no node to back up to
                  nextNode = null;
               }
            }
            return obj;
         }
         else throw new NoSuchElementException("No next element");
      }

      /** unsupported method */
      public void remove()
      {
         throw new UnsupportedOperationException
                   ("remove not supported");
      }   
   }

   /** test program */
   public static void main(String [] args)
   {
      LinkedBinaryTreeWithPreorderIterator
                       a = new LinkedBinaryTreeWithPreorderIterator(),
                       w = new LinkedBinaryTreeWithPreorderIterator(), 
                       x = new LinkedBinaryTreeWithPreorderIterator(), 
                       y = new LinkedBinaryTreeWithPreorderIterator(), 
                       z = new LinkedBinaryTreeWithPreorderIterator();
      y.makeTree(new Integer(1), a, a);
      z.makeTree(new Integer(2), a, a);
      x.makeTree(new Integer(3), y, z);
      y.makeTree(new Integer(4), x, a);
      w.makeTree(new Integer(5), a, a);
      z.makeTree(new Integer(6), a, a);
      x.makeTree(new Integer(7), w, z);
      y.makeTree(new Integer(8), x, y);

      System.out.println("Preorder sequence is ");
      y.preOrderOutput();
      System.out.println();

      System.out.println("Preorder iterator yields");
      Iterator iy = y.iterator();
      while (iy.hasNext())
         System.out.print(iy.next() + " ");
      System.out.println();
   }
}
