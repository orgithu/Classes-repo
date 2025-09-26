
package dataStructures;

import java.util.*;

public class SortedChainWithHeadAndTail implements Dictionary
{
   // top-level nested class
   protected static class SortedChainNode
   {
      // data members
      protected Comparable key;
      protected Object element;
      protected SortedChainNode next;

      // constructors
      protected SortedChainNode() {}
     
      protected SortedChainNode(Object theKey, Object theElement)
      {
         key = (Comparable) theKey;
         element = theElement;
      }

      protected SortedChainNode(Object theKey, Object theElement,
                                SortedChainNode theNext)
      {
         key = (Comparable) theKey;
         element = theElement;
         next = theNext;
      }
   }

   // data members of SortedChainWithHeadAndTail
   protected SortedChainNode headerNode;
   protected SortedChainNode tailNode;
   protected int size;

   // constructors
   /** create an empty sorted chain */
   public SortedChainWithHeadAndTail(int initialCapacity)
   {
      headerNode = new SortedChainNode();
      tailNode = new SortedChainNode();
      headerNode.next = tailNode;
      // the default initial value of size is 0
   }

   public SortedChainWithHeadAndTail()
      {this(0);}

   // methods
   /** @return true iff the chain is empty */
   public boolean isEmpty()
      {return size == 0;}

   /** @return current number of elements in list */
   public int size()
      {return size;}

   /** @return element with specified key
     * @return null if there is no matching element */
   public Object get(Object theKey)
   {
      // put theKey into tailNode
      tailNode.key = (Comparable) theKey;

      SortedChainNode currentNode = headerNode.next;

      // search for match with theKey
      while (currentNode.key.compareTo(theKey) < 0)
         currentNode = currentNode.next;

      // verify match
      if (currentNode != tailNode && currentNode.key.equals(theKey))
         // yes, found match
         return currentNode.element;

      // no match
      return null;
   }
   
   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key 
     * @return old element (if any) with key theKey */
   public Object put(Object theKey, Object theElement)
   {
      // first put theKey into the tail node
      tailNode.key = (Comparable) theKey;

      SortedChainNode currentNode = headerNode;
   
      // move currentNode so that theElement can be inserted after currentNode
      while (currentNode.next.key.compareTo(theKey) < 0)
         {currentNode = currentNode.next;}
   
      // check if there is a matching element
      if (currentNode.next != tailNode && currentNode.next.key.equals(theKey))
      {// replace old element
         Object elementToReturn = currentNode.next.element;
         currentNode.next.element = theElement;
         return elementToReturn;
      }
   
      // insert just after currentNode
      currentNode.next = new SortedChainNode(theKey, theElement,
                                              currentNode.next);
      size++;
      return null;
   }

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      // first put theKey into the tail node
      tailNode.key = (Comparable) theKey;

      SortedChainNode currentNode = headerNode;
   
      // search for matching element
      while (currentNode.next.key.compareTo(theKey) < 0)
         {currentNode = currentNode.next;}
   
      // check if there is a matching element
      if (currentNode.next != tailNode && currentNode.next.key.equals(theKey))
      {// there is a matching element, save in elementToReturn
       // and remove from chain
         Object elementToReturn = currentNode.next.element;
         currentNode.next = currentNode.next.next;
         size--;
         return elementToReturn;
      }
   
      // no matching element to remove
      return null;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      SortedChainNode currentNode = headerNode.next;
      if (currentNode != tailNode)
      {// nonempty list
         // do first element
         s.append(currentNode.element.toString());
         currentNode = currentNode.next;
         while (currentNode != tailNode)
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
      {return new SortedChainIterator();}

   /** sorted chain iterator */
   private class SortedChainIterator implements Iterator
   {
      // data member
      private SortedChainNode nextNode;
   
      // constructor
      public SortedChainIterator()
         {nextNode = headerNode.next;}
   
      // methods
      /** @return true iff list has more elements */
      public boolean hasNext()
         {return nextNode != tailNode;}
   
      /** @return next element in list
        * @throws NoSuchElementException
        * if there is no next element */
      public Object next()
      {
         if (nextNode != tailNode)
         {
            Object obj = nextNode.element;
            nextNode = nextNode.next;
            return obj;
         }
         else throw new NoSuchElementException
                        ("No next element");
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
      // test default constructor
      SortedChainWithHeadAndTail x = new SortedChainWithHeadAndTail();

      // test put
      x.put(new Integer(2), new Integer(12));
      System.out.println("The list is " + x);
      x.put(new Integer(6), new Integer(16));
      System.out.println("The list is " + x);
      x.put(new Integer(1), new Integer(11));
      System.out.println("The list is " + x);
      x.put(new Integer(4), new Integer(14));
      System.out.println("The list is " + x);
      x.put(new Integer(6), new Integer(26));
      System.out.println("The list is " + x);

      // test get
      System.out.println("element " + x.get(new Integer(2))
                         + " has key 2");
      System.out.println("element " + x.get(new Integer(1))
                         + " has key 1");
      System.out.println("element " + x.get(new Integer(6))
                         + " has key 6");

      // test iterator
      Iterator ix = x.iterator();
      System.out.print("The list is [");
      while (ix.hasNext())
         System.out.print(ix.next() + " ");
      System.out.println("]");

      // test remove
      System.out.println("removed element " + x.remove(new Integer(2))
                         + " with key 2");
      System.out.println("The list is " + x);
      System.out.println("removed element " + x.remove(new Integer(1))
                         + " with key 1");
      System.out.println("The list is " + x);
      System.out.println("removed element " + x.remove(new Integer(6))
                         + " with key 6");
      System.out.println("The list is " + x);
   }
}
