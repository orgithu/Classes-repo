

/** extended stack class derived from ArrayStack */

// methods access the data members of ArrayStack to obtain a fast implementation

package dataStructures;

import java.lang.reflect.*;
import exceptions.*;
import utilities.*;
import wrappers.*;

public class ExtendedArrayStack extends ArrayStack
                                implements ExtendedStack
{
   // constructors
   /** create a stack with the given initial capacity */
   public ExtendedArrayStack(int initialCapacity)
      {super(initialCapacity);}

   /** create a stack with initial capacity 10 */
   public ExtendedArrayStack()
      {this(10);}

   // methods of ExtendedStack
   /** @return number of elements in the stack */
   public int size()
      {return top + 1;}

   /** input a stack from the input stream inStream using
     * the method inputMethod to input each element */
   public void input(Method inputMethod, MyInputStream inStream)
   {
      // input size of new stack
      System.out.println("Enter number of elements in stack");
      int s = inStream.readInteger();
      if (s < 0)
         throw new MyInputException
               ("stack size must be >= 0");

      // increase array size if necessary
      if (s > stack.length)
         stack = new Object [s];

      // input the stack elements bottom to top
      Object [] inputMethodArgs = {inStream};
      System.out.println("Enter stack elements from bottom to top");
      try
      {
         for (int i = 0; i < s; i++)
            stack[i] = inputMethod.invoke(null, inputMethodArgs);
      }
      catch (Exception e)
      {
         System.out.println(e);
         throw new IllegalArgumentException
               ("input method for stack element type "
                + "is incorrect");
      }

      top = s - 1;
   }

   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      if (top >= 0)
      {// nonempty stack
         // do first element
         s.append(stack[0].toString());
         // do remaining elements
         for (int i = 1; i <= top; i++)
            s.append(", " + stack[i].toString());
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** split the stack this into the stacks a and b
     * a gets the bottom-half elements, b gets the rest */
   public void split(ExtendedStack a, ExtendedStack b)
   {
      // determine size of resulting stacks a and b
      int sizeOfA = size() / 2;
      int sizeOfB = size() - sizeOfA;

      // cast a and b into ExtendedArrayStacks
      ExtendedArrayStack aa = (ExtendedArrayStack) a;
      ExtendedArrayStack ab = (ExtendedArrayStack) b;

      // make sure array sizes are adequate
      if (sizeOfA > aa.stack.length)
         aa.stack = new Object [sizeOfA];
      if (sizeOfB > ab.stack.length)
         ab.stack = new Object [sizeOfB];

      // put bottom-half elements into a
      for (int i = 0; i < sizeOfA; i++)
         aa.stack[i] = stack[i];
      aa.top = sizeOfA - 1;

      // put remaining elements into b
      for (int i = sizeOfA; i <= top ; i++)
         ab.stack[i - sizeOfA] = stack[i];
      ab.top = sizeOfB - 1;
   }

   /** set the stack this to contain the elements in a
     * followed by those in b */
   public void combine(ExtendedStack a, ExtendedStack b)
   {
      // cast a and b into ExtendedArrayStacks
      ExtendedArrayStack aa = (ExtendedArrayStack) a;
      ExtendedArrayStack ab = (ExtendedArrayStack) b;

      // make sure array size is adequate
      top = aa.top + ab.top + 1;  // new top of this
      if (top >= stack.length)
         stack = new Object [top + 1];

      // put elements of a into this
      for (int i = 0; i <= aa.top; i++)
         stack[i] = aa.stack[i];

      // put elements of b into this
      for (int i = 0; i <= ab.top; i++)
         stack[i + aa.top + 1] = ab.stack[i];
   }

   /** test program */
   public static void main(String [] args)
   {  
      ExtendedArrayStack a = new ExtendedArrayStack(1);
      ExtendedArrayStack b = new ExtendedArrayStack(1);
      ExtendedArrayStack c = new ExtendedArrayStack(1);

      // input stream for stack input
      MyInputStream keyboard = new MyInputStream();

      // stack elements will be of type MyInteger; these will
      // be input using the method MyInput.input() which has a
      // parameter of type MyInputStream
      Class [] parameterTypes = {MyInputStream.class};

      // inputMethod is a reference to the method MyInput.input()
      Method inputMethod = null;
      try
         {inputMethod = MyInteger.class.getMethod("input", parameterTypes);}
      catch (Exception e)
      {
         System.out.println(e);
         System.exit(1);
      }

      // test input, output, size, split, and combine
      for (int i = 1; i <= 2; i++)
      {
         a.input(inputMethod, keyboard);
         System.out.println("The stack is " + a);
         System.out.println("The stack size is " + a.size());
         a.split(b, c);
         System.out.println("The split stacks are " + b + " and " + c);
         System.out.println("The sizes of the split stacks are " + b.size()
                             + " and " + c.size());
         b.combine(a, c);
         System.out.println("The result of combining the input stack and its "
           + "top half is " + b);
      }
   }  
}
