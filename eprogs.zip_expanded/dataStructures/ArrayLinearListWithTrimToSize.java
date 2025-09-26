

/** extension of ArrayLinearList to include trimToSize */

package dataStructures;

import utilities.*;

public class ArrayLinearListWithTrimToSize extends ArrayLinearList
{
   // constructors
   public ArrayLinearListWithTrimToSize(int initialCapacity)
      {super(initialCapacity);}

   public ArrayLinearListWithTrimToSize()
   {// use default capacity of 10
      this(10);
   }

   /** make element.length = size */
   public void trimToSize()
      {element = ChangeArrayLength.changeLength1D(element, size, size);}

   /** test program */
   public static void main(String [] args)
   {
      ArrayLinearListWithTrimToSize x = new ArrayLinearListWithTrimToSize();

      x.add(0, new Integer(0));
      x.add(1, new Integer(1));
      x.add(2, new Integer(2));
      x.add(3, new Integer(3));
      System.out.println("List size is " + x.size());
      System.out.println("Array length is " + x.element.length);
      System.out.println("The list is " + x);

      x.trimToSize();
      System.out.println("List has been trimmed to size");
      System.out.println("List size is " + x.size());
      System.out.println("Array length is " + x.element.length);
      System.out.println("The list is " + x);
   }
}
