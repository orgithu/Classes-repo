


/** extension of ArrayLinearList to include the method set */

package dataStructures;

public class ArrayLinearListWithSet extends ArrayLinearList
{
   // constructors
   public ArrayLinearListWithSet(int initialCapacity)
      {super(initialCapacity);}

   public ArrayLinearListWithSet()
   {// use default capacity of 10
      this(10);
   }

   /** set the element whose index is theIndex to theElement
     * @throws IndexOutOfBoundsException when theIndex < 0
     * or theIndex >= size 
     * @returns old element with index equal to theIndex */
   public Object set(int theIndex, Object theElement)
   {
      checkIndex(theIndex);
      Object elementToReturn = element[theIndex];
      element[theIndex] = theElement;
      return elementToReturn;
   }
     

   /** test program */
   public static void main(String [] args)
   {
      ArrayLinearListWithSet x = new ArrayLinearListWithSet();

      x.add(0, new Integer(0));
      x.add(1, new Integer(1));
      x.add(2, new Integer(2));
      x.add(3, new Integer(3));
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);

      System.out.println("Element " + x.set(2, new Integer(4))
                         + " changed to 4");
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);
   }
}
