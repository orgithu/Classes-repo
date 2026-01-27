/** doubly linked implementation of ExtendedLinearList */

package dataStructures;

import java.util.*;

public class DoublyLinkedList implements ExtendedLinearList
{
   // data members
   protected DoubleNode firstNode;
   protected DoubleNode lastNode;
   protected int size;

   // constructors
   /** create a list that is empty */
   public DoublyLinkedList(int initialCapacity)
   {
       // the default initial values of firstNode and lastNode
       // are null; the default for size is 0
   }

   public DoublyLinkedList()
      {this(0);}

   // methods
   /** @return true iff list is empty */
   public boolean isEmpty()
       {return size == 0;}

   /** @return current number of elements in list */
   public int size()
      {return size;}

   /** @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1 */
   void checkIndex(int index)
   {
      if (index < 0 || index >= size)
         throw new IndexOutOfBoundsException
             ("index = " + index + "  size = " + size);
   }

   /** @return element with specified index
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1 */
   public Object get(int index)
   {
      checkIndex(index);

      DoubleNode currentNode;

      // move to desired node
      if (index < size / 2)
      {// move from left to right
         currentNode = firstNode;
         for (int i = 0; i < index; i++)
            currentNode = currentNode.next;
      }
      else
      {// move from right to left
         currentNode = lastNode;
         int numberToMove = size - index - 1;
         for (int i = 0; i < numberToMove; i++)
            currentNode = currentNode.previous;
      }

      return currentNode.element;
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if elem not in list */
   public int indexOf(Object elem)
   {
      // search the list for elem
      DoubleNode currentNode = firstNode;
      int index = 0;  // index of currentNode
      while (currentNode != null &&
             !currentNode.element.equals(elem))
      {
         // move to next node
         currentNode = currentNode.next;
         index++;
      }

      // make sure we found matching element
      if (currentNode == null)
         return -1;
      else
         return index;
   }
   
   /** Remove the element with specified index.
     * All elements with higher index have their
     * index reduced by 1.
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1
     * @return removed element */
   public Object remove(int index)
   {
      checkIndex(index);

      DoubleNode currentNode;  // will point to node with element that is
                               // to be removed

      // move to element that is to be removed
      if (index < size / 2)
      {// move from left to right
         currentNode = firstNode;
         for (int i = 0; i < index; i++)
            currentNode = currentNode.next;
      }
      else
      {// move from right to left
         currentNode = lastNode;
         int numberToMove = size - index - 1;
         for (int i = 0; i < numberToMove; i++)
            currentNode = currentNode.previous;
      }

      // currentNode has the element that is to be removed
      if (size == 1)    // list becomes empty
         firstNode = null;
      else              // nonempty list remains
         if (index == 0)
         {// remove first node
            firstNode = firstNode.next;
            firstNode.previous = null;
         }
         else
            if (index == size - 1)
            {// remove last node
               lastNode = lastNode.previous;
               lastNode.next = null;
            }
            else
            {// remove from interior
               currentNode.previous.next = currentNode.next;
               currentNode.next.previous = currentNode.previous;
            }
      size--;
      return currentNode.element;
   }
   
   /** Insert an element with specified index.
     * All elements with equal or higher index
     * have their index increased by 1.
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size */
   public void add(int index, Object theElement)
   {
      if (index < 0 || index > size)
         // invalid list position
         throw new IndexOutOfBoundsException
                   ("index = " + index + "  size = " + size);

      if (index == 0)
      {// insert at front
         firstNode = new DoubleNode(theElement, null, firstNode);
         if (firstNode.next == null)
            // list was empty before this insert
            lastNode = firstNode;
         else
            firstNode.next.previous = firstNode;
      }
      else
         if (index == size)
         {// insert at end of nonempty list
            lastNode.next = new DoubleNode(theElement, lastNode, null);
            lastNode = lastNode.next;
         }
         else
         {// insert in interior
            // find index - 1'th node
            DoubleNode currentNode;
            if (index <= size / 2)
            {// move from left to right
               currentNode = firstNode;
               for (int i = 0; i < index - 1; i++)
                  currentNode = currentNode.next;
            }
            else
            {// move from right to left
               currentNode = lastNode;
               int numberToMove = size - index;
               for (int i = 0; i < numberToMove; i++)
                  currentNode = currentNode.previous;
            }

            // insert after currentNode
            currentNode.next =
                   new DoubleNode(theElement, currentNode, currentNode.next);
            currentNode.next.next.previous = currentNode.next;
      }
      size++;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // do first element
         s.append(firstNode.element.toString());
         DoubleNode currentNode = firstNode.next;
         while (currentNode != null)
         {
            s.append(", " + currentNode.element.toString());
            currentNode = currentNode.next;
         }
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** create and return an iterator */
   public Iterator iterator()
      {return new DoubleIterator();}

   /** iterator */
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

      /** unsupported method */
      public void remove()
      {
         throw new UnsupportedOperationException
                   ("remove not supported");
      }   
   }
   
   /** Make the list empty. */
   public void clear()
   {
      firstNode = null;
      size = 0;
   }

   /** Add theElement to the right end of the list. */
   public void add(Object theElement)
   {
      DoubleNode newNode = new DoubleNode(theElement, lastNode, null);
      if (size == 0)
      {// list is empty
         firstNode = lastNode = newNode;
         firstNode.previous = null;
      }
      else
      {// attach newNode next to lastNode
          lastNode.next = newNode;
          newNode.previous = lastNode;
          lastNode = newNode;
      }
      size++;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      DoublyLinkedList x = new DoublyLinkedList();

      // test size
      System.out.println("Initial size is " +
                         x.size());

      // test isEmpty
      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      // test add
      x.add(0, new Integer(3));
      x.add(1, new Integer(6));
      x.add(0, new Integer(2));
      x.add(2, new Integer(4));
      x.add(4, new Integer(8));
      x.add(4, new Integer(7));
      x.add(3, new Integer(5));
      x.add(0, new Integer(0));
      x.add(1, new Integer(1));
      System.out.println("List size is " + x.size());

      // test toString
      System.out.println("The list is " + x);

      // test indexOf
      int index = x.indexOf(new Integer(4));
      if (index < 0)
         System.out.println("4 not found");
      else
         System.out.println("The index of 4 is " + index);

      index = x.indexOf(new Integer(3));
      if (index < 0)
         System.out.println("3 not found");
      else
         System.out.println("The index of 3 is " + index);

      // test get
      System.out.println("Element at 0 is " + x.get(0));
      System.out.println("Element at 3 is " + x.get(3));
      System.out.println("Element at 5 is " + x.get(5));
      System.out.println("Element at 6 is " + x.get(6));
      System.out.println("Element at 8 is " + x.get(8));

      // test remove
      System.out.println("Removed " + x.remove(0) + " the list is " + x);
      System.out.println("Removed " + x.remove(7) + " the list is " + x);
      System.out.println("Removed " + x.remove(2) + " the list is " + x);
      System.out.println("Removed " + x.remove(3) + " the list is " + x);

      if (x.isEmpty())
         System.out.println("The list is empty");
      else
         System.out.println("The list is not empty");

      System.out.println("List size is " + x.size());

      // output using an iterator
      Iterator ix = x.iterator();
      System.out.print("The list is ");
      while (ix.hasNext())
         System.out.print(ix.next() + " ");
      System.out.println();

      // append an element
      x.add(new Integer(99));
      System.out.println("After appending 99, the list is " + x);

      // make empty, then append
      x.clear();
      x.add(new Integer(6));
      System.out.println("After appending 6 to an empty list, the list is " + x);
      System.out.println("List size is " + x.size());
   }
}
