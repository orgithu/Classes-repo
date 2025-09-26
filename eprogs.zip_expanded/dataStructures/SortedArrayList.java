/** implementation of Dictionary using a sorted array */

package dataStructures;

import java.util.*;
import utilities.*;

public class SortedArrayList implements Dictionary
{
   // top-level nested class
   protected static class SortedListNode
   {
      // data members
      protected Comparable key;
      protected Object element;

      // constructors
      protected SortedListNode() {}
     
      protected SortedListNode(Object theKey, Object theElement)
      {
         key = (Comparable) theKey;
         element = theElement;
      }
   }

   // data members of SortedList
   protected SortedListNode [] element;  // array of elements
   protected int size;                   // number of elements in array

   // constructors
   /** create a list with initial capacity initialCapacity
     * @throws IllegalArgumentException
     * when initialCapacity < 1 */
   public SortedArrayList(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
                   ("initialCapacity must be >= 1");
      // size has the default initial value of 0
      element = new SortedListNode [initialCapacity];
   }

   /** create a list with initial capacity 10 */
   public SortedArrayList()
   {// use default capacity of 10
      this(10);
   }

   // methods
   /** @return true iff list is empty */
   public boolean isEmpty()
      {return size == 0;}

   /** @return current number of elements in list */
   public int size()
      {return size;}

   /** @return element with specified key
     * @return null if there is no matching element */
   public Object get(Object theKey)
   {
      // search from left to right for matching element
      // would be faster to use binary search
      int i = 0;
      while (i < size && element[i].key.compareTo(theKey) < 0)
         i++;
   
      // verify match
      if (i >= size || !element[i].key.equals(theKey))
         // no matching element
         return null;
      else
         // there is a match
         return element[i].element;
   }
   
   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key
     * @return old element (if any) with key theKey */
   public Object put(Object theKey, Object theElement)
   {
      // examine elements from left to right
      // would be faster to use binary search
      int i = 0;
      while (i < size && element[i].key.compareTo(theKey) < 0)
         i++;
   
      // verify match
      if (i < size && element[i].key.equals(theKey))
      {// replace old element
         Object elementToReturn = element[i].element;
         element[i].element = theElement;
         return elementToReturn;
      }

      if (size == element.length)
         // no space for new element, double capacity
         element = (SortedListNode []) ChangeArrayLength.changeLength1D
                      (element, 2 * element.length);
   
      // create space for new element by moving
      // [i:size - 1] one spot right
      System.arraycopy(element, i, element, i + 1, size - i);

      // insert at element[i]
      element[i] = new SortedListNode(theKey, theElement);
      size++;
      return null;
   }

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      // search for match with theKey
      // would be faster to use binary search
      int i = 0;
      while (i < size && element[i].key.compareTo(theKey) < 0)
         i++;
   
      // verify match
      if (i >= size || !element[i].key.equals(theKey))
         // no matching element
         return null;
   
      // save matching element in e and remove from list
      Object removedElement = element[i].element;
      System.arraycopy(element, i + 1, element, i, size - i - 1);
      size--;  // new list size

      return removedElement;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // do first element
         s.append(element[0].element.toString());
         // do remaining elements
         for (int i = 1; i < size; i++)
            s.append(", " + element[i].element.toString());
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** create and return an iterator */
   public Iterator iterator()
      {return new SortedArrayArrayListIterator();}

   private class SortedArrayArrayListIterator implements Iterator
   {
      // data member
      private int nextIndex;  // index of next element
   
      // constructor
      public SortedArrayArrayListIterator()
      {
         // nextIndex has the default initial value 0
      }
   
      // methods
      /** @return true iff list has a next element */
      public boolean hasNext()
         {return nextIndex < size;}
   
      /** @return next element in list
        * @throws NoSuchElementException
        * when there is no next element */
      public Object next()
      {
         if (nextIndex < size)
            return element[nextIndex++].element;
         else
            throw new NoSuchElementException("No next element");
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
      SortedArrayList x = new SortedArrayList();

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
