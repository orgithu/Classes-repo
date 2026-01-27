

/** extended linked stack class derived from LinkedStack */

package dataStructures;

import java.lang.reflect.*;
import wrappers.*;
import utilities.*;
import exceptions.*;

public class ExtendedLinkedStack extends LinkedStack
                                 implements ExtendedStack
{
   // data member
   int size;  // default initial value is 0

   // constructors
   /** create a stack with the given initial capacity */
   public ExtendedLinkedStack(int initialCapacity)
      {super(initialCapacity);}

   /** create a stack with initial capacity 10 */
   public ExtendedLinkedStack()
      {this(10);}

   // need to change push and pop
   public void push(Object theElement)
   {
      super.push(theElement);
      size++;
   }

   public Object pop()
   {
      size--;
      return super.pop();
   }

   // methods of ExtendedStack
   /** @return number of elements in the stack */
   public int size()
      {return size;}

   /** input a stack from the input stream inStream using
     * the method inputMethod to input each element */
   public void input(Method inputMethod, MyInputStream inStream)
   {
      // input size of new stack
      System.out.println("Enter number of elements in stack");
      size = inStream.readInteger();
      if (size < 0)
         throw new MyInputException
                   ("stack size must be >= 0");

      topNode = null;

      // input the stack elements bottom to top
      Object [] inputMethodArgs = {inStream};
      System.out.println("Enter stack elements from bottom to top");
      try
      {
         for (int i = 0; i < size; i++)
         {
            Object theElement = inputMethod.invoke(null, inputMethodArgs);
            topNode = new ChainNode(theElement, topNode);
         }
      }
      catch (Exception e)
      {
         System.out.println(e);
         throw new IllegalArgumentException
                   ("input method for stack element type is incorrect");
      }
   }

   /** convert to a string */
   public String toString()
   {
      // collect elements in an array for bottom to top conversion
      Object [] temp = new Object [size];
      ChainNode currentNode = topNode;
      for (int i = size - 1; i >= 0; i--)
      {
         temp[i] = currentNode.element;
         currentNode = currentNode.next;
      }

      StringBuffer s = new StringBuffer("["); 
      if (size >= 0)
      {// nonempty stack
         // do first element
         s.append(temp[0].toString());
         // do remaining elements
         for (int i = 1; i < size; i++)
            s.append(", " + temp[i].toString());
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** split the stack this into the stacks a and b
     * a gets the bottom-half elements, b gets the rest */
   public void split(ExtendedStack a, ExtendedStack b)
   {
      // cast a and b into ExtendedLinkedStacks
      ExtendedLinkedStack aa = (ExtendedLinkedStack) a;
      ExtendedLinkedStack ab = (ExtendedLinkedStack) b;

      // determine size of resulting stacks a and b
      aa.size = size / 2;
      ab.size = size - aa.size;

      // put top-half elements into b
      ChainNode currentNode = topNode;
      ChainNode lastNodeOfB = null;
      for (int i = 0; i < ab.size; i++)
      {
          ChainNode temp = new ChainNode(currentNode.element);
          if (lastNodeOfB == null)
             // temp becomes the top node of b
             ab.topNode = temp;
          else
             // link last node of B to temp
             lastNodeOfB.next = temp;
          lastNodeOfB = temp;
          currentNode = currentNode.next;
      }
      if (lastNodeOfB == null)
         // b is empty
         ab.topNode = null;
      else
         // b is not empty
         lastNodeOfB.next = null;
            
      // put remaining elements into a
      ChainNode lastNodeOfA = null;
      while (currentNode != null)
      {
          ChainNode temp = new ChainNode(currentNode.element);
          if (lastNodeOfA == null)
             // temp becomes the top node of a
             aa.topNode = temp;
          else
             // link last node of A to temp
             lastNodeOfA.next = temp;
          lastNodeOfA = temp;
          currentNode = currentNode.next;
      }
      if (lastNodeOfA == null)
         // a is empty
         aa.topNode = null;
      else
         // a is not empty
         lastNodeOfA.next = null;
   }

   /** set the stack this to contain the elements in a
     * followed by those in b */
   public void combine(ExtendedStack a, ExtendedStack b)
   {
      // cast a and b into ExtendedLinkedStacks
      ExtendedLinkedStack aa = (ExtendedLinkedStack) a;
      ExtendedLinkedStack ab = (ExtendedLinkedStack) b;

      // put elements of b into this
      ChainNode currentNodeOfB = ab.topNode;
      ChainNode lastNode = null;
      while (currentNodeOfB != null)
      {
          ChainNode temp = new ChainNode(currentNodeOfB.element);
          if (lastNode == null)
             // temp becomes the top node of this
             topNode = temp;
          else
             // link last node of this to temp
             lastNode.next = temp;
          lastNode = temp;
          currentNodeOfB = currentNodeOfB.next;
      }

      // put elements of a into this
      ChainNode currentNodeOfA = aa.topNode;
      while (currentNodeOfA != null)
      {
          ChainNode temp = new ChainNode(currentNodeOfA.element);
          if (lastNode == null)
             // temp becomes the top node of this
             topNode = temp;
          else
             // link last node of this to temp
             lastNode.next = temp;
          lastNode = temp;
          currentNodeOfA = currentNodeOfA.next;
      }

      if (lastNode == null)
         // this is empty
         topNode = null;
      else
         // this is not empty
         lastNode.next = null;

      size = aa.size + ab.size;
   }

   /** test program */
   public static void main(String [] args)
   {  
      ExtendedLinkedStack a = new ExtendedLinkedStack(1);
      ExtendedLinkedStack b = new ExtendedLinkedStack(1);
      ExtendedLinkedStack c = new ExtendedLinkedStack(1);

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
