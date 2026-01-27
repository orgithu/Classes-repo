/* sorted array-based linear list */

package dataStructures;

import java.util.*;
import utilities.*;

public class SortedArrayList2
{
   // data members
   Comparable [] element;      // array of elements
   int size;                   // number of elements in array

   // constructors
   /** create a list with initial capacity initialCapacity
     * @throws IllegalArgumentException
     * when initialCapacity < 1 */
   public SortedArrayList2(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
               ("initialCapacity must be >= 1");
      // size has the default initial value of 0
      element = new Comparable [initialCapacity];
   }

   /** create a list with initial capacity 10 */
   public SortedArrayList2()
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

   /** @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1 */
   void checkIndex(int index)
   {
      if (index < 0 || index >= size)
         throw new IndexOutOfBoundsException
             ("index = " + index + "  size = " + size);
   }

   /** @return element with specified index
     * @throws IllegalArgumentException when
     * index is not between 0 and size - 1 */
   public Comparable get(int index)
   {
      checkIndex(index);
      return element[index];
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if elem not in list */
   public int indexOf(Comparable theElement)
   {
      // search element[] for theElement
      for (int i = 0; i < size; i++)
         if (element[i].equals(theElement))
            return i;

      // theElement not found
      return -1;
   }      

   /** @return element that matches theElement
     * @return null if there is no matching element */
   public Comparable get(Comparable theElement)
   {
      // search from left to right for matching element
      int i = 0;
      while (i < size && element[i].compareTo(theElement) < 0)
         i++;
   
      // verify match
      if (i >= size || !element[i].equals(theElement))
         // no matching element
         return null;
      else
         // there is a match
         return element[i];
   }
   
   /** insert an element into the sorted list
     * overwrite old element if there is already a
     * matching element 
     * @return matching element if any */
   public Comparable add(Comparable theElement)
   {
      // examine elements from left to right
      int i = 0;
      while (i < size && element[i].compareTo(theElement) < 0)
         i++;
   
      // verify match
      if (i < size && element[i].equals(theElement))
      {// replace old element
         Comparable elementToReturn = element[i];
         element[i] = theElement;
         return elementToReturn;
      }

      if (size == element.length)
         // no space for new element, double capacity
         element = (Comparable []) ChangeArrayLength.changeLength1D
                      (element, 2 * element.length);
   
      // create space for new element by moving
      // [i:size - 1] one spot right
      System.arraycopy(element, i, element, i + 1, size - i);

      // insert at element[i]
      element[i] =  theElement;
      size++;
      return null;
   }

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theElement)
   {
      // search for match with theElement
      int i = 0;
      while (i < size && element[i].compareTo(theElement) < 0)
         i++;
   
      // verify match
      if (i >= size || !element[i].equals(theElement))
         // no matching element
         return null;
   
      // save matching element in elementToReturn and remove from list
      Comparable elementToReturn = element[i];
      System.arraycopy(element, i + 1, element, i, size - i - 1);
      size--;  // new list size

      return elementToReturn;
   }
   
   /** Remove the element with specified index.
     * All elements with higher index have their
     * index reduced by 1.
     * @throw IllegalArgumentException when
     * index is not between 0 and size - 1
     * @return removed element */
   public Comparable remove(int index)
   {
      checkIndex(index);

      // valid index, shift elements with higher index
      Comparable elementToReturn = element[index];
      System.arraycopy(element, index + 1, element, index, size - index - 1);
  
      size--;
      return elementToReturn;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // do first element
         s.append(element[0].toString());
         for (int i = 1; i < size; i++)
            s.append(", " + element[i].toString());
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** create and return an iterator */
   public Iterator iterator()
      {return new SortedArrayList2Iterator();}

   private class SortedArrayList2Iterator implements Iterator
   {
      // data member
      private int nextIndex;  // index of next element
   
      // constructor
      public SortedArrayList2Iterator()
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
            return element[nextIndex++];
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
      SortedArrayList2 x = new SortedArrayList2();

      // test put
      x.add(new Integer(12));
      System.out.println("The list is " + x);
      x.add(new Integer(16));
      System.out.println("The list is " + x);
      x.add(new Integer(11));
      System.out.println("The list is " + x);
      x.add(new Integer(14));
      System.out.println("The list is " + x);
      x.add(new Integer(26));
      System.out.println("The list is " + x);

      // test get
      System.out.println("element " + x.get(new Integer(12))
                         + " has key 12");
      System.out.println("element " + x.get(new Integer(14))
                         + " has key 14");
      System.out.println("element " + x.get(new Integer(26))
                         + " has key 26");

      // test remove
      System.out.println("removed element " + x.remove(new Integer(26))
                         + " with key 26");
      System.out.println("The list is " + x);
      System.out.println("removed element " + x.remove(new Integer(14))
                         + " with key 14");
      System.out.println("The list is " + x);
      System.out.println("removed element " + x.remove(new Integer(11))
                         + " with key 11");
      System.out.println("The list is " + x);

      // test isEmpty
      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      // test put
      x.add(new Integer(2));
      x.add(new Integer(6));
      x.add(new Integer(1));
      x.add(new Integer(4));
      System.out.println("List size is " + x.size());

      // test toString
      System.out.println("The list is " + x);

      // test indexOf
      int index = x.indexOf(new Integer(4));
      if (index < 0)
         System.out.println("4 not found");
      else System.out.println("The index of 4 is " + index);

      index = x.indexOf(new Integer(3));
      if (index < 0)
         System.out.println("3 not found");
      else System.out.println("The index of 3 is " + index);

      // test get
      System.out.println("Element at 0 is " + x.get(0));
      System.out.println("Element at 3 is " + x.get(3));

      // test remove
      x.remove(1);
      System.out.println("The list is " + x);
      x.remove(2);
      System.out.println("The list is " + x);

      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      System.out.println("List size is " + x.size());
      
   }
}
