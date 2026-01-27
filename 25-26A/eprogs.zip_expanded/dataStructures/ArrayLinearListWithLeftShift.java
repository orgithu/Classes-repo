
/** extension of ArrayLinearList to include the method leftShift */

package dataStructures;

public class ArrayLinearListWithLeftShift extends ArrayLinearList
{
   // constructors
   public ArrayLinearListWithLeftShift(int initialCapacity)
      {super(initialCapacity);}

   public ArrayLinearListWithLeftShift()
   {// use default capacity of 10
      this(10);
   }

   /** shift elements left by amountOfShift
     * @throws IllegalArgumentException when amountOfShift < 0 */
   public void leftShift(int amountOfShift)
   {
      // empty list is easily shifted by any amount
      if (size == 0)
         return;

      // validate shift amount
      if (amountOfShift < 0)
         throw new IllegalArgumentException("shift amount must be >= 0,"
                          + " you have specified " + amountOfShift);

      int newSize = 0;
      if (amountOfShift < size)
      {// at least one element remains
         newSize = size - amountOfShift;
         System.arraycopy(element, amountOfShift, element, 0, newSize);
      }

      // set size and replace vacated slots with null
      while (size != newSize)
         element[--size] = null;   // enable garbage collection
   }
     

   /** test program */
   public static void main(String [] args)
   {
      ArrayLinearListWithLeftShift x = new ArrayLinearListWithLeftShift();

      x.add(0, new Integer(0));
      x.add(1, new Integer(1));
      x.add(2, new Integer(2));
      x.add(3, new Integer(3));
      x.add(4, new Integer(4));
      x.add(5, new Integer(5));
      x.add(6, new Integer(6));
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);

      x.leftShift(1);
      System.out.println("Shifted by 1");
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);

      x.leftShift(3);
      System.out.println("Shifted by 3");
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);

      x.leftShift(10);
      System.out.println("Shifted by 10");
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);
   }
}
