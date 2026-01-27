
/** a stack class that uses a one-dimensional array */

package dataStructures;

import java.util.EmptyStackException;
import utilities.*;

public class ArrayStack implements Stack
{
   // data members
   protected int top;          // current top of stack
   protected Object [] stack;  // element array

   // constructors
   /** create a stack with the given initial capacity
     * @throws IllegalArgumentException when initialCapacity < 1 */
   public ArrayStack(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
               ("initialCapacity must be >= 1");
      stack = new Object [initialCapacity];
      top = -1;
   }

   /** create a stack with initial capacity 10 */
   public ArrayStack()
      {this(10);}

   // methods
   /** @return true iff stack is empty */
   public boolean empty()
      {return top == -1;}


   /** @return top element of stack
     * @throws EmptyStackException when the stack is empty */
   public Object peek()
   {
      if (empty())
         throw new EmptyStackException();
      return stack[top];
   }

   /** add theElement to the top of the stack */
   public void push(Object theElement)
   {
      // increase array size if necessary
      if (top == stack.length - 1)
         stack = ChangeArrayLength.changeLength1D(stack, 2 * stack.length);
         
      // put theElement at the top of the stack
      stack[++top] = theElement;
   }

   /** remove top element of stack and return it
     * @throws EmptyStackException when the stack is empty */
   public Object pop()
   {
      if (empty())
         throw new EmptyStackException();
      Object topElement = stack[top];
      stack[top--] = null;   // enable garbage collection
      return topElement;
   }
   
   public int size() {
	   return top + 1;
   }
   
   public String toString() {
	   ArrayStack temp = new ArrayStack();
	   StringBuffer s = new StringBuffer("[");
	   // put elements into the buffer
	   while(!this.empty()) {
		   Object t = this.pop();
		   s.append(t.toString() + ", ");
		   temp.push(t);
	   }
	   s.delete(s.length() - 2, s.length()); // remove last ", "
	   s.append("]");
	   while(!temp.empty()) {
		   this.push(temp.pop());
	   }
	   // create equivalent String
	   return new String(s);
   }

   /** test program */
   public static void main(String [] args)
   {  
      int x;
      ArrayStack s = new ArrayStack(3);
      // add a few elements
      s.push(new Integer(1));
      s.push(new Integer(2));
      s.push(new Integer(3));
      s.push(new Integer(4));


      // delete all elements
      while (!s.empty())
      {
         System.out.println("Top element is " + s.peek());
      }
      while (!s.empty())
      {
         System.out.println("Removed the element " + s.pop());
      }
   }  
}
