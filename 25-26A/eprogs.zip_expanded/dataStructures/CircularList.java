/** circular linked list implementation of ExtendedLinearList */

package dataStructures;

import java.util.*;

public class CircularList implements ExtendedLinearList
{
   // data members
   protected ChainNode lastNode;
   protected int size;

   // constructors
   /** create a list that is empty */
   public CircularList(int initialCapacity)
   {
       // the default initial values of lastNode and size
       // are null and 0, respectively
   }

   public CircularList()
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
     * @throws IndexOutOfBoundsException
     * when index is not between 0 and size - 1 */
   public Object get(int index)
   {
      checkIndex(index);

      // move to desired node
      ChainNode currentNode = lastNode.next;
      for (int i = 0; i < index; i++)
         currentNode = currentNode.next;

      return currentNode.element;
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if elem not in list */
   public int indexOf(Object elem)
   {
      if (size == 0)
         return -1;   // list is empty

      // search the circular list for elem
      ChainNode currentNode = lastNode.next;
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
     * index is not between 0 and size - 1 */
   public Object remove(int index)
   {
      checkIndex(index);

      Object removedElement;
      if (size == 1)
      {// empty list remains
         removedElement = lastNode.element;
         lastNode = null;
      }
      else
      {
         // use q to get to node that precedes node to be deleted
         ChainNode q = lastNode;
         for (int i = 0; i < index; i++)
            q = q.next;

         // update lastNode if it is to be removed
         if (q.next == lastNode)
            lastNode = q;

         // remove the index'th node
         removedElement = q.next.element;
         q.next = q.next.next;
      }
      size--;
      return removedElement;
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
         lastNode = new ChainNode(theElement);
         lastNode.next = lastNode;
      }
      else
         // insert into nonempty list
         if (index == size)
         {// insert at end
            ChainNode newNode = new ChainNode(theElement, lastNode.next);
            lastNode.next = newNode;
            lastNode = newNode;
         }
         else
         {// find node that precedes new node
             ChainNode p = lastNode;
             for (int i = 0; i < index; i++)
                p = p.next;
   
             // insert after p
             p.next = new ChainNode(theElement, p.next);
         }
      size++;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // output first element
         ChainNode firstNode = lastNode.next;
         s.append(firstNode.element.toString());

         // output remaining elements
         ChainNode currentNode = firstNode.next;
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
      {return new CircularListIterator();}

   /** iterator */
   private class CircularListIterator implements Iterator
   {
      // data member
      private ChainNode nextNode;
   
      // constructor
      public CircularListIterator()
      {
         if (size == 0)
            nextNode = null;
         else 
            nextNode = lastNode.next;
      }
   
      // methods
      /** @return true iff list has more elements */
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
   
   /** make the circular list empty */
   public void clear()
   {
      lastNode = null;
      size = 0;
   }

   /** add theElement to the right end of the circular list */
   public void add(Object theElement)
   {
      ChainNode y = new ChainNode(theElement);
      if (size == 0)
         // list is empty
         lastNode = y.next = y;
      else
      {// attach y next to lastNode
         y.next = lastNode.next;
         lastNode.next = y;
         lastNode = y;
      }
      size++;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      CircularList x = new CircularList();

      // test size
      System.out.println("Initial size is " +
                         x.size());

      // test isEmpty
      if (x.isEmpty())
         System.out.println("The list is empty");
      else
         System.out.println("The list is not empty");

      // test add
      x.add(0, new Integer(2));
      x.add(1, new Integer(6));
      x.add(0, new Integer(1));
      x.add(2, new Integer(4));
      System.out.println("List size is " +
                         x.size());

      // test toString
      System.out.println("The list is " + x);

      // test indexOf
      int index = x.indexOf(new Integer(4));
      if (index < 0)
         System.out.println("4 not found");
      else
         System.out.println("The index of 4 is "
                              + index);

      index = x.indexOf(new Integer(3));
      if (index < 0)
         System.out.println("3 not found");
      else
         System.out.println("The index of 3 is "
                              + index);

      // test get
      System.out.println("Element at 0 is " +
                         x.get(0));
      System.out.println("Element at 3 is " +
                         x.get(3));

      // test remove
      x.remove(1);
      System.out.println("The list is " + x);
      x.remove(2);
      System.out.println("The list is " + x);

      if (x.isEmpty())
         System.out.println("The list is empty");
      else
         System.out.println("The list is not empty");

      System.out.println("List size is " +
                         x.size());
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
      System.out.println("List size is " +
                         x.size());
   }
}
