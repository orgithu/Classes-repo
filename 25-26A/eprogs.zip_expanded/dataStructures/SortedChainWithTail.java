
package dataStructures;

import java.util.*;

public class SortedChainWithTail implements Dictionary
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

   // data members of SortedChainWithTail
   protected SortedChainNode firstNode;
   protected SortedChainNode tailNode;
   protected int size;

   // constructors
   /** create an empty sorted chain */
   public SortedChainWithTail(int initialCapacity)
   {
       // create the tail node
       tailNode = new SortedChainNode();
       firstNode = tailNode;   
       // the default initial value of size is 0
   }

   public SortedChainWithTail()
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

      SortedChainNode currentNode = firstNode;

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
     * @return old element (if any) with key = theKey */
   public Object put(Object theKey, Object theElement)
   {
      // first put theKey into the tail node
      tailNode.key = (Comparable) theKey;

      SortedChainNode currentNode = firstNode;
      SortedChainNode trailNode = null;  // node left of currentNode
   
      // move currentNode so that theElement can be inserted just before
      // currentNode
      while (currentNode.key.compareTo(theKey) < 0)
      {
         trailNode = currentNode;
         currentNode = currentNode.next;
      }
   
      // check if there is a matching element
      if (currentNode != tailNode && currentNode.key.equals(theKey))
      {// replace old element
         Object elementToReturn = currentNode.element;
         currentNode.element = theElement;
         return elementToReturn;
      }
   
      // insert just before currentNode
      SortedChainNode p = new SortedChainNode(theKey, theElement,
                                              currentNode);
      if (currentNode == firstNode)
         // p is new first node
         firstNode = p;
      else
         // p is not new first node, set link from node to its left
         trailNode.next = p;
      size++;
      return null;
    }

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      // first put theKey into the tail node
      tailNode.key = (Comparable) theKey;

      SortedChainNode currentNode = firstNode;
      SortedChainNode trailNode = null;  // node left of currentNode
   
      // move currentNode to node with natching element (if any)
      while (currentNode.key.compareTo(theKey) < 0)
      {
         trailNode = currentNode;
         currentNode = currentNode.next;
      }
   
      // check if there is a matching element
      if (currentNode != tailNode && currentNode.key.equals(theKey))
      {// there is a matching element, save in e and remove from chain
         Object e = currentNode.element;
         if (currentNode == firstNode)
            // remove first node
            firstNode = firstNode.next;
         else
            // remove interior node
            trailNode.next = currentNode.next;
         size--;
         return e;
      }
   
      // no matching element to remove
      return null;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      SortedChainNode currentNode = firstNode;
      if (currentNode != tailNode)
      {// nonempty chain
         // do first element
         s.append(currentNode.element.toString());
         // do remaining elements
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
      {return new SortedChainWithTailIterator();}

   /** sorted chain with tail iterator */
   private class SortedChainWithTailIterator implements Iterator
   {
      // data member
      private SortedChainNode nextNode;
   
      // constructor
      public SortedChainWithTailIterator()
         {nextNode = firstNode;}
   
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
      SortedChainWithTail x = new SortedChainWithTail();

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

      // test iterator
      Iterator ix = x.iterator();
      System.out.print("The list is [");
      while (ix.hasNext())
         System.out.print(ix.next() + " ");
      System.out.println("]");

      // test get
      System.out.println("element " + x.get(new Integer(2))
                         + " has key 2");
      System.out.println("element " + x.get(new Integer(1))
                         + " has key 1");
      System.out.println("element " + x.get(new Integer(6))
                         + " has key 6");

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
