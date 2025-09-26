/** doubly linked with special
    iterator DoubleIterator included */

package dataStructures;

import java.util.*;

public class DoublyLinkedListWithDoubleIterator extends DoublyLinkedList
{
   public DoubleIterator doubleIterator()
      {return new DoubleIterator();}

   /** two way iterator */
   private class DoubleIterator implements Iterator
   {
      // data member
      private DoubleNode nextNode;
   
      // constructor
      public DoubleIterator()
         {nextNode = firstNode;}
   
      // methods
      /** @return true iff list has a next element */
      public boolean hasNext()
         {return nextNode != null;}
   
      /** @return true iff list has a previous element */
      public boolean hasPrevious()
         {return (size > 1) && (nextNode != firstNode)
                            && (nextNode != firstNode.next);}
   
      /** @return next element in list
        * @throws NoSuchElementException
        * when there is no next element */
      public Object next()
      {
         if (nextNode != null)
         {
            Object elementToReturn = nextNode.element;
            nextNode = nextNode.next;
            return elementToReturn;
         }
         else throw new NoSuchElementException("No next element");
      }
   
      /** @return previous element in list
        * @throws NoSuchElementException
        * when there is no previous element */
      public Object previous()
      {
         if ((size > 1) && (nextNode != firstNode)
                        && (nextNode != firstNode.next))
         {
            if (nextNode == null)
               // previous node is 2nd last node of list
               nextNode = lastNode;
            else
               // previous node is 2 nodes left of current nextNode
               nextNode = nextNode.previous;
            return nextNode.previous.element;
         }
         else throw new NoSuchElementException("No previous element");
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
      DoublyLinkedListWithDoubleIterator x =
                  new DoublyLinkedListWithDoubleIterator();
      x.add(0, new Integer(2));
      x.add(1, new Integer(6));
      x.add(0, new Integer(1));
      x.add(2, new Integer(4));

      System.out.println("The list is " + x);

      // output using a DoubleIterator
      DoubleIterator ix = x.doubleIterator();
      System.out.print("The list is ");
      while (ix.hasNext())
         System.out.print(ix.next() + " ");
      System.out.println();

      // output backwards using a DoubleIterator
      System.out.print("The list, in reverse order (excluding last element),"
                       + " is ");
      while (ix.hasPrevious())
         System.out.print(ix.previous() + " ");
      System.out.println();

      // go forward 2, then back 1 then forward 2
      System.out.print("Visited elements ");
      System.out.print(ix.next() + " " + ix.next() + " " + ix.previous()
                       + " " + ix.next() + " " + ix.next());
      System.out.println();
   }
}
