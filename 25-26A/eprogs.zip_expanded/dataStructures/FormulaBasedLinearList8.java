

/** formula-based implementation of LinearList using Equation 3.2 */

package dataStructures;

import java.util.*;
import wrappers.*;
import utilities.*;

public class FormulaBasedLinearList8 implements LinearList
{
   // data members
   protected Object [] element;  // array of elements
   protected int first;          // location of first element
   protected int last;           // location of last element

   // constructors
   /** create a list with initial capacity initialCapacity
     * @exception IllegalArgumentException thrown
     * if initialCapacity < 1 */
   public FormulaBasedLinearList8(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
            ("FormulaBasedLinearList.constructor: " +
             "initialCapacity must be >= 1");
      // size has the default initial value of 0
      element = new Object [initialCapacity];

      first = -1;   // list is empty
   }

   /** create a list with initial capacity 10 */
   public FormulaBasedLinearList8()
   {// use default capacity of 10
      this(10);
   }

   // methods
   /** @return true iff list is empty */
   public boolean isEmpty()
       {return first == -1;}

   /** @return current number of elements in list */
   public int size()
   {
      if (first == -1)
         return 0;
      else
         return (element.length + last - first) % element.length + 1;
   }

   /** @return element with specified index
     * @exception IllegalArgumentException thrown
     * if index is not between 0 and size - 1 */
   public Object elementAt(int index)
   {
      if (index < 0 || index >= size())
         // invalid list position
         throw new IllegalArgumentException
            ("FormulaBasedLinearList.elementAt: " +
             "index must be between 0 and size - 1");

      return element[(first + index) % element.length];
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if elem not in list */
   public int indexOf(Object elem)
   {
      // search element[] for elem
      int size = size();
      for (int i = 0; i < size; i++)
         if (element[(first + i) % element.length].equals(elem))
            return i;

      // elem not found
      return -1;
   }      
   
   /** Remove the element with specified index.
     * All elements with higher index have their
     * index reduced by 1.
     * @exception IllegalArgumentException thrown
     * if index is not between 0 and size - 1 */
   public void removeElementAt(int index)
   {
      if (index < 0 || index >= size())
         // invalid list position
         throw new IllegalArgumentException
            ("FormulaBasedLinearList.removeElementAt: " +
             "index must be between 0 and size - 1");

      // valid index, remove element
      if (size() == 1)
      {// list becomes empty
         first = -1;
         return;
      }

      // determine which side has fewer elements
      // and shift the smaller number of elements
      int size = size();
      if (index <= (size - 1) / 2)
      {// shift elements index - 1 ... 0
         for (int i = index - 1; i >= 0; i--)
             element[(first + i + 1) % element.length]
                = element[(first + i) % element.length];
         first = (first + 1) % element.length;
      }
      else
      {// shift elements index + 1 ... size() - 1
         for (int i = index + 1; i < size(); i++)
             element[(first + i - 1) % element.length]
                = element[(first + i) % element.length];
         last = (element.length + last - 1) % element.length;
      }
   }
   
   /** Insert an element with specified index.
     * All elements with equal or higher index
     * have their index increased by 1.
     * @exception IllegalArgumentException thrown
     * if index is not between 0 and size */
   public void insertElementAt(Object obj, int index)
   {
      if (index < 0 || index > size())
         // invalid list position
         throw new IllegalArgumentException
            ("FormulaBasedLinearList.insertElementAt: " +
             "index must be between 0 and size");

      // valid index, handle empty list as special case
      if (first == -1)
      {// insert into empty list
         first = last = 0;
         element[0] = obj;
         return;
      }

      // insert into a nonempty list, make sure we have space
      if (size() == element.length)
      {// no space, double capacity
         // allocate a new array
         Object [] newArray = new Object [2 * element.length];

         // copy elements into new array
         int j = 0;   // position in newArray to copy to
         for (int i = first;
                  i != last; i = (i + 1) % element.length)
            newArray[j++] = element[i];
         newArray[j] = element[last];  // copy last element

         // switch to newArray and set first and last
         element = newArray;
         first = 0;
         last = j;
      }

      // create space for new element
      if (index <= size() / 2)
      {// shift elements 0 through index - 1
            for (int i = 0; i < index; i++)
                element[(element.length + first + i - 1) % element.length]
                   = element[(first + i) % element.length];
            first = (element.length + first - 1) % element.length;
      }
      else
      {// shift elements size() - 1  ... index
          for (int i = size() - 1; i >= index; i--)
              element[(first + i + 1) % element.length]
                 = element[(first + i) % element.length];
          last = (last + 1) % element.length;
      }
          
      // insert new element
      element[(first + index) % element.length] = obj;
   }
   
   /** convert to a string */
   public String toString()
   {
      int size = size();
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // do first element
         s.append(element[first % element.length].toString());
         for (int i = 1; i < size; i++)
            s.append(", " + element[(first + i) % element.length].toString());
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      LinearList x = new FormulaBasedLinearList8();

      // test size
      System.out.println("Initial size is " + x.size());

      // test isEmpty
      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      // test insertElementAt
      x.insertElementAt(new MyInteger(2), 0);
      x.insertElementAt(new MyInteger(6), 1);
      x.insertElementAt(new MyInteger(1), 0);
      x.insertElementAt(new MyInteger(4), 2);
      System.out.println("List size is " + x.size());

      // test toString
      System.out.println("The list is " + x);

      // test indexOf
      int index = x.indexOf(new MyInteger(4));
      if (index < 0)
         System.out.println("4 not found");
      else System.out.println("The index of 4 is " + index);

      index = x.indexOf(new MyInteger(3));
      if (index < 0)
         System.out.println("3 not found");
      else System.out.println("The index of 3 is " + index);

      // test elementAt
      System.out.println("Element at 0 is " + x.elementAt(0));
      System.out.println("Element at 3 is " + x.elementAt(3));

      // test removeElementAt
      x.removeElementAt(1);
      System.out.println("The list is " + x);
      x.removeElementAt(2);
      System.out.println("The list is " + x);

      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      System.out.println("List size is " + x.size());
   }
}
