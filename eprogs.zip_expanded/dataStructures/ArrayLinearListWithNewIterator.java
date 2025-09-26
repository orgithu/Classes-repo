
/** array-based implementation of LinearList
  * the iterator class is a member class
  * and includes the additional methods reset, hasPrevious, previous */

package dataStructures;

import java.util.*;
import utilities.*;

public class ArrayLinearListWithNewIterator implements LinearList
{
   // data members
   protected Object [] element;  // array of elements
   protected int size;           // number of elements in array

   // constructors
   /** create a list with initial capacity initialCapacity
     * @throws IllegalArgumentException when
     * initialCapacity < 1 */
   public ArrayLinearListWithNewIterator(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
                   ("initialCapacity must be >= 1");
      // size has the default initial value of 0
      element = new Object [initialCapacity];
   }

   /** create a list with initial capacity 10 */
   public ArrayLinearListWithNewIterator()
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
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1 */
   public Object get(int index)
   {
      checkIndex(index);
      return element[index];
   }
   
   /** @return index of first occurrence of theElement,
     * return -1 if theElement not in list */
   public int indexOf(Object theElement)
   {
      // search element[] for theElement
      for (int i = 0; i < size; i++)
         if (element[i].equals(theElement))
            return i;

      // theElement not found
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

      // valid index, shift elements with higher index
      Object removedElement = element[index];
      for (int i = index + 1; i < size; i++)
         element[i-1] = element[i];
  
      element[--size] = null;   // enable garbage collection
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

      // valid index, make sure we have space
      if (size == element.length)
         // no space, double capacity
         element = ChangeArrayLength.changeLength1D
                      (element, 2 * element.length);

      // shift elements right one position
      for (int i = size - 1; i >= index; i--)
         element[i + 1] = element[i];

      element[index] = theElement;

      size++;
   }
   
   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (size > 0)
      {// nonempty list
         // do first element
         s.append(element[0].toString());
         // do remaining elements
         for (int i = 1; i < size; i++)
            s.append(", " + element[i].toString());
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** create and return an iterator */
   public Iterator iterator()
      {return new ArrayLinearListIterator();}

   private class ArrayLinearListIterator implements Iterator
   {
      // data member
      private int nextIndex;  // index of next element
   
      // constructor
      public ArrayLinearListIterator()
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

      // reset to start of list
      public void reset()
         {nextIndex = 0;}

      // @return false iff at front of list
      public boolean hasPrevious()
         {return nextIndex > 0;}

      /** @return previous element in list
        * @throws NoSuchElementException
        * when there is no previous element */
      public Object previous()
      {
         if (nextIndex > 0)
            return element[--nextIndex];
         else throw new NoSuchElementException("No previous element");
      }
   }

   /** test program */
   public static void main(String [] args)
   {
      ArrayLinearListWithNewIterator x = new ArrayLinearListWithNewIterator();
      int n = 15;           // list size

      for (int i = 0; i < n; i++)
         x.add(0, new Integer(n - i));

      System.out.println("The list is " + x);

      // create an iterator for x
      ArrayLinearListIterator ix = (ArrayLinearListIterator) x.iterator();

      System.out.println("At front, should return false " + ix.hasPrevious());
      System.out.println("0th element is " + ix.next());
      System.out.println("1th element is " + ix.next());
      ix.next();
      System.out.println("3rd element is " + ix.next());
      ix.previous();
      System.out.println("2nd element is " + ix.previous());
      System.out.println("Not at front, should return true " + ix.hasPrevious());
   }
}
