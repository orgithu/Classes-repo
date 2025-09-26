

/** doubly linked circular list with header node
  * implementation of ExtendedLinearList */

package dataStructures;

import java.util.*;

public class HeadDoubleCircularList implements ExtendedLinearList
{
   // data members
   protected DoubleNode headerNode;
   protected int size;

   // constructors
   /** create a list that is empty */
   public HeadDoubleCircularList(int initialCapacity)
   {
       // create head node
       headerNode = new DoubleNode();
       headerNode.next = headerNode;
       headerNode.previous = headerNode;
       // the default initial value of size is 0
   }

   public HeadDoubleCircularList()
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

      // move to desired node
      DoubleNode currentNode = headerNode.next;
      for (int i = 0; i < index; i++)
         currentNode = currentNode.next;

      return currentNode.element;
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if elem not in list */
   public int indexOf(Object theElement)
   {

      // search the circular list for theElement
      DoubleNode currentNode = headerNode.next;
      int index = 0;  // index of currentNode
      headerNode.element = theElement;
      while(!currentNode.element.equals(theElement))
      {
         // move to next node
         currentNode = currentNode.next;
         index++;
      }

      // make sure we found matching element
      return (index == size) ? -1 : index;
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
         currentNode = headerNode.next;
         for (int i = 0; i < index; i++)
            currentNode = currentNode.next;
      }
      else
      {// move from right to left
         currentNode = headerNode.previous;
         int numberToMove = size - index - 1;
         for (int i = 0; i < numberToMove; i++)
            currentNode = currentNode.previous;
      }

      // remove currentNode
      currentNode.previous.next = currentNode.next;
      currentNode.next.previous = currentNode.previous;

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

      // define a pointer that will eventually point to node
      // just before insertion point
      DoubleNode currentNode;
      if (index <= size / 2)
      {// move from left to right
         currentNode = headerNode;
         for (int i = 0; i < index; i++)
            currentNode = currentNode.next;
      }
      else
      {// move from right to left
         currentNode = headerNode.previous;
         int numberToMove = size - index;
         for (int i = 0; i < numberToMove; i++)
            currentNode = currentNode.previous;
      }

      // insert after currentNode
      currentNode.next =
             new DoubleNode(theElement, currentNode, currentNode.next);
      currentNode.next.next.previous = currentNode.next;
      size++;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // output first element
         DoubleNode firstNode = headerNode.next;
         s.append(firstNode.element.toString());

         // output remaining elements
         DoubleNode currentNode = firstNode.next;
         while(currentNode != headerNode)
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
      {return new HeadDoubleCircularIterator();}

   /** iterator */
   private class HeadDoubleCircularIterator implements Iterator
   {
      // data member
      private DoubleNode nextNode;
   
      // constructor
      public HeadDoubleCircularIterator()
         {nextNode = headerNode.next;}
   
      // methods
      /** @return true iff list has a next element */
      public boolean hasNext()
         {return nextNode != headerNode;}
   
      /** @return next element in list
        * @throws NoSuchElementException
        * when there is no next element */
      public Object next()
      {
         if (nextNode != headerNode)
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
      headerNode.next = headerNode.previous = headerNode;
      size = 0;
   }

   /** Add theElement to the right end of the list. */
   public void add(Object theElement)
   {
      DoubleNode newNode = new DoubleNode(theElement,
                           headerNode.previous, headerNode);
      headerNode.previous = newNode;
      newNode.previous.next = newNode;
      size++;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      HeadDoubleCircularList x = new HeadDoubleCircularList();

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
