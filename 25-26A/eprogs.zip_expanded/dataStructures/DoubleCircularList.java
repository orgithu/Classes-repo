
/** doubly linked circular linked list implementation of ExtendedLinearList */

package dataStructures;

import java.util.*;

public class DoubleCircularList implements ExtendedLinearList
{
   // data members
   protected DoubleNode lastNode;
   protected int size;

   // constructors
   /** create a list that is empty */
   public DoubleCircularList(int initialCapacity)
   {
       // the default initial values of lastNode and size
       // are null and 0, respectively
   }

   public DoubleCircularList()
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
      DoubleNode currentNode = lastNode.next;
      for (int i = 0; i < index; i++)
         currentNode = currentNode.next;

      return currentNode.element;
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if elem not in list */
   public int indexOf(Object elem)
   {
      if (lastNode == null)
         return -1;   // list is empty

      // search the circular list for elem
      DoubleNode currentNode = lastNode.next;
      int index = 0;  // index of currentNode
      while (currentNode != lastNode &&
             !currentNode.element.equals(elem))
      {
         // move to next node
         currentNode = currentNode.next;
         index++;
      }

      // make sure we found matching element
      if (currentNode.element.equals(elem))
         return index;
      else
         return -1;
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
         currentNode = lastNode.next;
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
         lastNode = null;
      else
      {// nonempty list remains
         // remove currentNode
         currentNode.previous.next = currentNode.next;
         currentNode.next.previous = currentNode.previous;

         // update lastNode if necessary
         if (currentNode == lastNode)
            lastNode = currentNode.previous;
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

      if (size == 0)
      {// insert into empty list
         lastNode = new DoubleNode(theElement);
         lastNode.next = lastNode.previous = lastNode;
         size = 1;
         return;
      }

      // insert into nonempty list

      // define a pointer that will eventually point to node
      // just before insertion point
      DoubleNode currentNode;
      if (index <= size / 2)
      {// move from left to right
         currentNode = lastNode;
         for (int i = 0; i < index; i++)
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
      if (index == size)
         // new last node
         lastNode = currentNode.next;
      size++;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (lastNode != null)
      {// nonempty list
         // output first element
         DoubleNode firstNode = lastNode.next;
         s.append(firstNode.element.toString());

         // output remaining elements
         DoubleNode currentNode = firstNode.next;
         while(currentNode != firstNode)
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
      {return new DoubleCircularIterator();}

   /** iterator */
   private class DoubleCircularIterator implements Iterator
   {
      // data member
      private DoubleNode nextNode;
   
      // constructor
      public DoubleCircularIterator()
      {
         if (lastNode == null)
            nextNode = null;
         else 
            nextNode = lastNode.next;
      }
   
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
            if (nextNode == lastNode)
               nextNode = null;
            else
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
      lastNode = null;
      size = 0;
   }

   /** Add theElement to the right end of the list. */
   public void add(Object theElement)
   {
      if (lastNode == null)
      {// list is empty
         DoubleNode newNode = new DoubleNode(theElement);
         lastNode = newNode;
         lastNode.next = lastNode.previous = lastNode;
      }
      else
      {// attach newNode next to lastNode
          lastNode.next = new DoubleNode(theElement, lastNode, lastNode.next);
          lastNode = lastNode.next;
          lastNode.next.previous = lastNode;
      }
      size++;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      DoubleCircularList x = new DoubleCircularList();

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
