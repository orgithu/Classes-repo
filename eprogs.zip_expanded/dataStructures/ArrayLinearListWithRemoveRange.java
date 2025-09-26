/** extension of ArrayLinearList to include the method removeRange */

package dataStructures;

public class ArrayLinearListWithRemoveRange extends ArrayLinearList
{
   // constructors
   public ArrayLinearListWithRemoveRange(int initialCapacity)
      {super(initialCapacity);}

   public ArrayLinearListWithRemoveRange()
   {// use default capacity of 10
      this(10);
   }

   /** remove the elements whose index is in the range
     * start through finish-1 */
   public void removeRange(int start, int finish)
   {
      // validate start and finish indexes
      if (start < 0)
         start = 0;
      if (finish > size)
         finish = size;
      if (start >= finish)
         // no elements to remove
         return;

      // move elements whose index is finish ... size-1 left
      int numberAtRightOfRange = size - finish;
      System.arraycopy(element, finish, element, start, numberAtRightOfRange);

      // set size and replace vacated slots with null
      int newSize = start + numberAtRightOfRange;
      while (size != newSize)
         element[--size] = null;   // enable garbage collection
   }
     

   /** test program */
   public static void main(String [] args)
   {
      ArrayLinearListWithRemoveRange x = new ArrayLinearListWithRemoveRange();

      x.add(0, new Integer(0));
      x.add(1, new Integer(1));
      x.add(2, new Integer(2));
      x.add(3, new Integer(3));
      x.add(4, new Integer(4));
      x.add(5, new Integer(5));
      x.add(6, new Integer(6));
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);

      x.removeRange(1,4);
      System.out.println("Removed elements 1 through 3");
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);
   }
}
