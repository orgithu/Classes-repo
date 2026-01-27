
/** FormulaBasedLinearList with new insertElementAt method */

package dataStructures;

import java.util.*;
import java.lang.reflect.*;
import wrappers.*;
import utilities.*;

public class FormulaBasedLinearList2 extends FormulaBasedLinearList
{
   /** Insert an element with specified index.
     * All elements with equal or higher index
     * have their index increased by 1.
     * @exception IllegalArgumentException thrown
     * if index is not between 0 and size */
   public void insertElementAt(Object obj, int index)
   {
      if (index < 0 || index > size)
         // invalid list position
         throw new IllegalArgumentException
            ("FormulaBasedLinearList.insertElementAt: " +
             "index must be between 0 and size");

      // valid index, make sure we have space
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
      element[index] = obj;

      size++;
   }

   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      LinearList x = new FormulaBasedLinearList2();

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
