
/** extended stack class derived from DerivedArrayStack which, in turn,
  * is derived from ArrayLinearList */

// methods do not access the data members of ArrayLinearList
// faster implementations are possible if we directly access
// ArrayLinearList.size and ArrayLinearList.element

package dataStructures;

import java.lang.reflect.*;
import exceptions.*;
import utilities.*;
import wrappers.*;

public class ExtendedDerivedArrayStack extends DerivedArrayStack
                                       implements ExtendedStack
{
   // constructors
   /** create a stack with the given initial capacity */
   public ExtendedDerivedArrayStack(int initialCapacity)
      {super(initialCapacity);}

   /** create a stack with initial capacity 10 */
   public ExtendedDerivedArrayStack()
      {this(10);}

   // methods of ExtendedStack
   // methods size() and toString() are inherited from ArrayLinearList

   /** input a stack from the input stream inStream using
     * the method inputMethod to input each element */
   public void input(Method inputMethod, MyInputStream inStream)
   {
      // first empty the stack
      for (int i = size() - 1; i >= 0; i--)
         remove(i);

      // input size of new stack
      System.out.println("Enter number of elements in stack");
      int s = inStream.readInteger();
      if (s < 0)
         throw new MyInputException
                   ("stack size must be >= 0");

      // input the stack elements bottom to top
      Object [] inputMethodArgs = {inStream};
      System.out.println("Enter stack elements from bottom to top");
      try
      {
         for (int i = 0; i < s; i++)
            add(i, inputMethod.invoke(null, inputMethodArgs));
      }
      catch (Exception e)
      {
         System.out.println(e);
         throw new IllegalArgumentException
                   ("input method for stack element type is incorrect");
      }
   }

   /** split the stack this into the stacks a and b
     * a gets the bottom-half elements, b gets the rest */
   public void split(ExtendedStack a, ExtendedStack b)
   {
      // first emty out a and b
      for (int i = a.size() - 1; i >= 0; i--)
         ((ExtendedDerivedArrayStack) a).remove(i);
      for (int i = b.size() - 1; i >= 0; i--)
         ((ExtendedDerivedArrayStack) b).remove(i);

      // put bottom-half elements into a
      int halfSize = size() / 2;
      for (int i = 0; i < halfSize; i++)
         ((ExtendedDerivedArrayStack) a).add(i, get(i));

      // put remaining elements into b
      for (int i = halfSize; i < size(); i++)
         ((ExtendedDerivedArrayStack) b).add(i - halfSize, get(i));
   }

   /** set the stack this to contain the elements in a
     * followed by those in b */
   public void combine(ExtendedStack a, ExtendedStack b)
   {
      // first emty out this
      for (int i = size() - 1; i >= 0; i--)
         remove(i);

      // put elements of a into this
      for (int i = 0; i < a.size(); i++)
         add(i, ((ExtendedDerivedArrayStack) a).get(i));

      // put elements of b into this
      for (int i = 0; i < b.size(); i++)
         add(a.size() + i, ((ExtendedDerivedArrayStack) b).get(i));
   }

   /** test program */
   public static void main(String [] args)
   {  
      ExtendedDerivedArrayStack a = new ExtendedDerivedArrayStack(1);
      ExtendedDerivedArrayStack b = new ExtendedDerivedArrayStack(1);
      ExtendedDerivedArrayStack c = new ExtendedDerivedArrayStack(1);

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
