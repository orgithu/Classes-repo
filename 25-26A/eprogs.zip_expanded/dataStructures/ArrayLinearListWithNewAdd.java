

/** ArrayLinearList with new add method */

package dataStructures;

import java.util.*;
import java.lang.reflect.*;
import utilities.*;

public class ArrayLinearListWithNewAdd extends ArrayLinearList
{
   // constructors
   public ArrayLinearListWithNewAdd(int initialCapacity)
      {super(initialCapacity);}

   public ArrayLinearListWithNewAdd()
   {// use default capacity of 10
      this(10);
   }

   /** Insert an element with specified index.
     * All elements with equal or higher index
     * have their index increased by 1.
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size */
   public void add(int index, Object theElement)
   {
      // valid index, make sure we have space
      if (index < 0 || index > size)
         // invalid list position
         throw new IndexOutOfBoundsException
             ("index = " + index + "  size = " + size);

      if (size == element.length)
      {// no space, double capacity and copy
         // allocate a new array of desired size and type
         Object [] newArray = (Object []) Array.newInstance
                   (element.getClass().getComponentType(), 2 * element.length);

        // copy into newArray
        for (int i = 0; i < index; i++)
           newArray[i] = element[i];
        for (int i = size - 1; i >= index; i--)
         newArray[i + 1] = element[i];

        // make newArray the current element array
        element = newArray;
      }
      else
      {// shift elements right one position
         for (int i = size - 1; i >= index; i--)
            element[i + 1] = element[i];
      }

      // insert new element
      element[index] = theElement;

      size++;
   }

   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      ArrayLinearListWithNewAdd x = new ArrayLinearListWithNewAdd(1);

      // test size
      System.out.println("Initial size is " + x.size());

      // test isEmpty
      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      // test put
      x.add(0, new Integer(2));
      x.add(1, new Integer(6));
      x.add(0, new Integer(1));
      x.add(2, new Integer(4));
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
      System.out.println(x.remove(1) + " removed");
      System.out.println("The list is " + x);
      System.out.println(x.remove(2) + " removed");
      System.out.println("The list is " + x);

      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      System.out.println("List size is " + x.size());
   }
}
