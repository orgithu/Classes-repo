
/** array stack implementation that decreases the length
  * of the element array when occupancy drops below 25% */

package dataStructures;

import utilities.*;

public class ArrayStackWithDecreaseCapacity
                           extends ArrayStack
{
   // additional data member
   protected int initialCapacity;       // initial size of stack array

   // constructors
   /** create a stack with initial capacity theInitialCapacity */
   public ArrayStackWithDecreaseCapacity(int theInitialCapacity)
   {
      super(theInitialCapacity);
      initialCapacity = theInitialCapacity;
   }

   /** create a stack with initial capacity 10 */
   public ArrayStackWithDecreaseCapacity()
   {// use default capacity of 10
      this(10);
   }

   /** overrides ArrayStack.pop */
   public Object pop()
   {
      Object x = super.pop();
  
      // decrease size of stack array if occupancy is below 25%
      if (top + 1 < stack.length / 4 && stack.length / 2 >= initialCapacity)
         // halve the size of the array
         stack = ChangeArrayLength.changeLength1D
                       (stack, top + 1, stack.length / 2);

      return x;
   }

   /** test program */
   public static void main(String [] args)
   {
      Stack s = new ArrayStackWithDecreaseCapacity(2);
      int n = 10;

      // insert n elements
      for (int i = 0; i < n; i++)
      {
         s.push(new Integer(i));
         System.out.println("The capacity is " +
                ((ArrayStack) s).stack.length);
      }

      // remove n-1 elements
      for (int i = 0; i < n - 1; i++)
      {
         System.out.println("Removing element " + s.peek());
         s.pop();
         System.out.println("The capacity is " +
                ((ArrayStack) s).stack.length);
      }
   }
}
