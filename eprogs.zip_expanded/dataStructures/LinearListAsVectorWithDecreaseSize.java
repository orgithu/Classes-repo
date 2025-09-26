
/** Vector implementation of LinearList that decreases the size
  * of the element array when occupancy drops below 25% */

package dataStructures;

import java.util.*;
import wrappers.*;
import utilities.*;

public class LinearListAsVectorWithDecreaseSize
                         extends LinearListAsVector
{
   // additional data member
   protected int initialCapacity;       // initial size of element array

   // constructors
   /** create a list with initial capacity theIinitialCapacity */
   public LinearListAsVectorWithDecreaseSize(int theInitialCapacity)
   {
      super(theInitialCapacity);
      initialCapacity = theInitialCapacity;
   }

   /** create a list with initial capacity 10 */
   public LinearListAsVectorWithDecreaseSize()
   {// use default capacity of 10
      this(10);
   }

   /** overrides LinearListAsVector.remove */
   public Object remove(int index)
   {
      Object elementToReturn = super.remove(index);
  
      // decrease size of Vector if occupancy is below 25%
      if (element.size() < element.capacity() / 4 &&
          element.size() >= initialCapacity)
         element.trimToSize();

      return elementToReturn;
   }

   /** test program */
   public static void main(String [] args)
   {
      LinearList x = new LinearListAsVectorWithDecreaseSize(2);
      int n = 10;

      // insert n elements
      for (int i = 0; i < n; i++)
      {
         x.add(0, new Integer(i));
         System.out.println("The capacity is " +
                ((LinearListAsVector) x).element.capacity());
      }
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);


      // remove n-1 elements
      for (int i = 0; i < n - 1; i++)
      {
         System.out.println("Removing element " + x.remove(0));
         System.out.println("The capacity is " +
                ((LinearListAsVector) x).element.capacity());
      }
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);
   }
}
