/** singly-linked implementation of Deque */

package dataStructures;

import java.util.*;

public class LinkedDeque implements Deque
{
   // data members
   protected ChainNode leftEnd;
   protected ChainNode rightEnd;

   // constructors
   /** create a deque that is empty */
   public LinkedDeque(int initialCapacity)
   {
      // the default initial values of leftEnd and rightEnd are null
   }
  

   public LinkedDeque()
      {this(0);}

   // methods
   /** @return true iff deque is empty */
   public boolean isEmpty()
      {return leftEnd == null;}

   /** @return leftmost element of deque
     * @return null if deque is empty */
   public Object getLeftElement()
   {
      if (isEmpty())
         return null;
      else
         return leftEnd.element;
   }

   /** @return rightmost element of deque
     * @return null if the deque is empty */
   public Object getRightElement()
   {
      if (isEmpty())
         return null;
      else
         return rightEnd.element;
   }

   /** insert theElement at the rightEnd of the deque */
   public void putAtRight(Object theElement)
   {
      // create node for theElement
      ChainNode x = new ChainNode(theElement);
      if (isEmpty())
         // create a one element deque
         leftEnd = rightEnd = x;
      else
      {// add at right end
         rightEnd.next = x;
         rightEnd = x;
      }
   }

   /** insert theElement at the leftEnd of the deque */
   public void putAtLeft(Object theElement)
   {
      // create node for theElement
      ChainNode x = new ChainNode(theElement);
      if (isEmpty())
         // create a one element deque
         leftEnd = rightEnd = x;
      else
      {// add at left end
         x.next = leftEnd;
         leftEnd = x;
      }
   }

   /** remove an element from the left end of the deque
     * @return removed element
     * @return null if the deque is empty */
   public Object removeFromLeft()
   {
      if (isEmpty())
         return null;

      // deque is not empty, save element to be removed
      Object e = leftEnd.element;

      // remove from deque
      leftEnd = leftEnd.next;

      return e;
   }

   /** remove an element from the right end of the deque
     * @return removed element
     * @return null if the deque is empty */
   public Object removeFromRight()
   {
      if (isEmpty())
         return null;

      // deque is not empty, save element to be removed
      Object e = rightEnd.element;

      // remove from deque
      if (leftEnd == rightEnd)
         // deque becomes empty
         leftEnd = null;
      else
      {// deque is not empty after removal of e
         // find element that precedes right most element
         ChainNode newRightEnd = leftEnd;
         while (newRightEnd.next != rightEnd)
            newRightEnd = newRightEnd.next;
      
         // remove last node
         rightEnd = newRightEnd;
         rightEnd.next = null;
      }

      return e;
   }
   
   /** test program */
   public static void main(String [] args)
   {  
      int x;
      LinkedDeque q = new LinkedDeque(3);

      // add a few elements
      q.putAtRight(new Integer(1));
      q.putAtLeft(new Integer(2));
      q.putAtRight(new Integer(3));
      q.putAtLeft(new Integer(4));
      q.putAtRight(new Integer(5));
      q.putAtLeft(new Integer(6));
      q.putAtRight(new Integer(7));
      q.putAtLeft(new Integer(8));


      // delete all elements
      while (!q.isEmpty())
      {
         System.out.println("Right element is " + q.getRightElement());
         System.out.println("Left element is " + q.getLeftElement());
         System.out.println("Removed the element " + q.removeFromRight());
         if (!q.isEmpty())
            System.out.println("Removed the element " + q.removeFromLeft());
      }
      System.out.println();

      // add a few elements
      q.putAtLeft(new Integer(2));
      q.putAtRight(new Integer(3));
      q.putAtLeft(new Integer(4));
      q.putAtRight(new Integer(5));
      q.putAtLeft(new Integer(6));
      q.putAtRight(new Integer(7));


      // delete all elements
      while (!q.isEmpty())
      {
         System.out.println("Right element is " + q.getRightElement());
         System.out.println("Left element is " + q.getLeftElement());
         System.out.println("Removed the element " + q.removeFromRight());
         if (!q.isEmpty())
            System.out.println("Removed the element " + q.removeFromLeft());
      }
   }  
}
