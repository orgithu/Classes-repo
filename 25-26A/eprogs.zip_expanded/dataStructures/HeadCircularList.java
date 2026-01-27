
/** circular linked list with header node
  * implementation of ExtendedLinearList */

package dataStructures;

import java.util.*;

public class HeadCircularList implements ExtendedLinearList
{
   // data members
   protected ChainNode headerNode;
   protected ChainNode lastNode;
   protected int size;

   // constructors
   /** create a list that is empty */
   public HeadCircularList(int initialCapacity)
   {
       // create head node
       headerNode = new ChainNode();
       headerNode.next = headerNode;
       lastNode = headerNode;
       // the default initial value of size is 0
   }

   public HeadCircularList()
      {this(0);}

   // methods
   /** @return true iff list is empty */
   public boolean isEmpty()
      {return size == 0;}

   /** @return current number of iterator in list */
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
      ChainNode currentNode = headerNode.next;
      for (int i = 0; i < index; i++)
         currentNode = currentNode.next;

      return currentNode.element;
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if elem not in list */
   public int indexOf(Object elem)
   {
      // put elem in head node
      headerNode.element = elem;

      // search the circular list for elem
      ChainNode currentNode = headerNode.next;
      int index = 0;  // index of currentNode
      while (!currentNode.element.equals(elem))
      {
         // move to next node
         currentNode = currentNode.next;
         index++;
      }

      // make sure we found matching element
      return (index == size) ? -1 : index;
   }
   
   /** Remove the element with specified index.
     * All iterator with higher index have their
     * index reduced by 1.
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1
     * @return removed element */
   public Object remove(int index)
   {
      checkIndex(index);

      // use q to get to node that precedes node to be deleted
      ChainNode q = headerNode;
      for (int i = 0; i < index; i++)
         q = q.next;
     
      // check if lastNode is to be removed
      if (q.next == lastNode)  // yes
         lastNode = q;         // q will be the new last node

      // remove the index'th node
      Object removedElement = q.next.element;
      q.next = q.next.next;
      size--;
      return removedElement;
   }
   
   /** Insert an element with specified index.
     * All iterator with equal or higher index
     * have their index increased by 1.
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size */
   public void add(int index, Object theElement)
   {
      if (index < 0 || index > size)
         // invalid list position
         throw new IndexOutOfBoundsException
                   ("index = " + index + "  size = " + size);

      // find node that precedes new node
      ChainNode p = headerNode;
      for (int i = 0; i < index; i++)
         p = p.next;
  
      // insert after p
      p.next = new ChainNode(theElement, p.next);
      size++;

      // see if new node is now the last node
      if (p == lastNode)   // yes, lastNode has changed
         lastNode = p.next;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // do first element
         ChainNode currentNode = headerNode.next;
         s.append(currentNode.element.toString());
         // do remaining iterator
         currentNode = currentNode.next;
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
      {return new HeadCircularListIterator();}

   /** iterator */
   private class HeadCircularListIterator implements Iterator
   {
      // data member
      private ChainNode nextNode;
   
      // constructor
      public HeadCircularListIterator()
         {nextNode = headerNode.next;}
   
      // methods
      /** @return true iff list has more iterator */
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
   
   /** make the circular list empty */
   public void clear()
   {
      headerNode.next = headerNode;
      lastNode = headerNode;
      size = 0;
   }

   /** add theElement to the right end of the circular list */
   public void add(Object theElement)
   {
      ChainNode y = new ChainNode(theElement);
      y.next = headerNode;
      lastNode.next = y;
      lastNode = y;
      size++;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      HeadCircularList x = new HeadCircularList();

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
