

/** a deque class that uses a one-dimensional array */

package dataStructures;

public class ArrayDeque implements Deque
{
   // data members
   int leftEnd;       // one counterclockwise from leftmost element
   int rightEnd;      // position of rightmost element of deque
   Object [] deque;   // element array

   // constructors
   /** create a deque with the given initial capacity */
   public ArrayDeque(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
                   ("initialCapacity must be >= 1");
      deque = new Object [initialCapacity + 1];
      leftEnd = rightEnd = 1;
   }

   /** create a deque with initial capacity 10 */
   public ArrayDeque()
   {// use default capacity of 10
      this(10);
   }

   // methods
   /** @return true iff deque is empty */
   public boolean isEmpty()
      {return leftEnd == rightEnd;}


   /** @return leftmost element of deque
     * @return null if deque is empty */
   public Object getLeftElement()
   {
      if (isEmpty())
         return null;
      else
         return deque[(leftEnd + 1) % deque.length];
   }

   /** @return rightmost element of deque
     * @return null if the deque is empty */
   public Object getRightElement()
   {
      if (isEmpty())
         return null;
      else
         return deque[rightEnd];
   }

   /** insert theElement at the rightEnd of the deque */
   public void putAtRight(Object theElement)
   {
      // increase array size if necessary
      if ((rightEnd + 1) % deque.length == leftEnd)
      {// double array size
         // allocate a new array
         Object [] newDeque = new Object [2 * deque.length];

         // copy elements into new array
         int j = 0;   // position in newDeque to copy to
         for (int i = (leftEnd + 1) % deque.length;
                  i != rightEnd; i = (i + 1) % deque.length)
            newDeque[j++] = deque[i];
         newDeque[j] = deque[rightEnd];  // copy last element
                   // use arraycopy to speed above copying

         // switch to newDeque and set leftEnd and rightEnd
         deque = newDeque;
         leftEnd = newDeque.length - 1;
         rightEnd = j;
      }

      // put theElement at the rightEnd of the deque
      rightEnd = (rightEnd + 1) % deque.length;
      deque[rightEnd] = theElement;
   }

   /** insert theElement at the leftEnd of the deque */
   public void putAtLeft(Object theElement)
   {
      // increase array size if necessary
      if ((rightEnd + 1) % deque.length == leftEnd)
      {// double array size
         // allocate a new array
         Object [] newDeque = new Object [2 * deque.length];

         // copy elements into new array
         int j = 0;   // position in newDeque to copy to
         for (int i = (leftEnd + 1) % deque.length;
                  i != rightEnd; i = (i + 1) % deque.length)
            newDeque[j++] = deque[i];
         newDeque[j] = deque[rightEnd];  // copy last element
                   // use arraycopy to speed above copying

         // switch to newDeque and set leftEnd and rightEnd
         deque = newDeque;
         leftEnd = newDeque.length - 1;
         rightEnd = j;
      }

      // put theElement at the leftEnd of the deque
      deque[leftEnd] = theElement;
      leftEnd = (deque.length + leftEnd - 1) % deque.length;
   }

   /** remove an element from the left end of the deque
     * @return removed element
     * @return null if the deque is empty */
   public Object removeFromLeft()
   {
      if (isEmpty())
         return null;
      leftEnd = (leftEnd + 1) % deque.length;
      return deque[leftEnd];
   }

   /** remove an element from the right end of the deque
     * @return removed element
     * @return null if the deque is empty */
   public Object removeFromRight()
   {
      if (isEmpty())
         return null;
      // save object to be returned
      Object temp = deque[rightEnd];

      // update rightEnd to reflect removal
      rightEnd = (deque.length + rightEnd - 1) % deque.length;

      return temp;
   }
   
   /** test program */
   public static void main(String [] args)
   {  
      int x;
      ArrayDeque q = new ArrayDeque(3);
      // add a few elements
      q.putAtRight(new Integer(1));
      q.putAtLeft(new Integer(2));
      q.putAtRight(new Integer(3));
      q.putAtLeft(new Integer(4));
      q.putAtRight(new Integer(5));
      q.putAtLeft(new Integer(6));
      q.putAtRight(new Integer(7));
      q.putAtLeft(new Integer(8));


      // delete all elements
      while (!q.isEmpty())
      {
         System.out.println("Right element is " + q.getRightElement());
         System.out.println("Left element is " + q.getLeftElement());
         System.out.println("Removed the element " + q.removeFromRight());
         if (!q.isEmpty())
            System.out.println("Removed the element " + q.removeFromLeft());
      }
   }  
}
