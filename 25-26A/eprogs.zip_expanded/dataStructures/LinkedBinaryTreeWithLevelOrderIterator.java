/** linked binary trees with a level order enumerator */

package dataStructures;

import java.util.*;

public class LinkedBinaryTreeWithLevelorderIterator extends LinkedBinaryTree
{
   /** create and return an iterator */
   public Iterator iterator()
      {return new LevelorderIterator();}

   /** level order iterator */
   private class LevelorderIterator implements Iterator
   {
      // data members
      private BinaryTreeNode nextNode;  // next node in level order
      private ArrayQueue queue = new ArrayQueue();
   
      // constructor
      /** set nextNode to first node in level order */
      public LevelorderIterator()
         {nextNode = root;}
   
      // methods
      /** @return true iff tree has more iterator */
      public boolean hasNext()
         {return nextNode != null;}

      /** @return next element in level order
        * @throws NoSuchElementException
        * when there is no next element */
      public Object next()
      {
         if (nextNode != null)
         {
            // save next level order element
            Object obj = nextNode.element;

            // put children on queue
            if (nextNode.leftChild != null) 
               queue.put(nextNode.leftChild);
            if (nextNode.rightChild != null)
               queue.put(nextNode.rightChild);
      
            // get next node to visit
            nextNode = (BinaryTreeNode) queue.remove();
            
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
      LinkedBinaryTreeWithLevelorderIterator
                       a = new LinkedBinaryTreeWithLevelorderIterator(),
                       w = new LinkedBinaryTreeWithLevelorderIterator(), 
                       x = new LinkedBinaryTreeWithLevelorderIterator(), 
                       y = new LinkedBinaryTreeWithLevelorderIterator(), 
                       z = new LinkedBinaryTreeWithLevelorderIterator();
      y.makeTree(new Integer(1), a, a);
      z.makeTree(new Integer(2), a, a);
      x.makeTree(new Integer(3), y, z);
      y.makeTree(new Integer(4), x, a);
      w.makeTree(new Integer(5), a, a);
      z.makeTree(new Integer(6), a, a);
      x.makeTree(new Integer(7), w, z);
      y.makeTree(new Integer(8), x, y);

      System.out.println("Level order sequence is ");
      y.preOrderOutput();
      System.out.println();

      System.out.println("Level order iterator yields");
      Iterator ey = y.iterator();
      while (ey.hasNext())
         System.out.print(ey.next() + " ");
      System.out.println();
   }
}
