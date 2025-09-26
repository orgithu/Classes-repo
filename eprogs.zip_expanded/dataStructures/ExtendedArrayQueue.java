
/** extended queue class derived from ArrayQueue */

package dataStructures;

import java.lang.reflect.*;
import wrappers.*;
import utilities.*;
import exceptions.*;

public class ExtendedArrayQueue extends ArrayQueue
                                implements ExtendedQueue
{
   // constructors
   /** create a queue with the given initial capacity */
   public ExtendedArrayQueue(int initialCapacity)
      {super(initialCapacity);}

   /** create a queue with initial capacity 10 */
   public ExtendedArrayQueue()
      {this(10);}

   // methods of ExtendedQueue
   /** @return number of elements in the queue */
   public int size()
      {return (queue.length + rear - front) % queue.length;}

   /** input a queue from the input stream inStream using
     * the method inputMethod to input each element */
   public void input(Method inputMethod, MyInputStream inStream)
   {
      // input size of new queue
      System.out.println("Enter number of elements in queue");
      int s = inStream.readInteger();
      if (s < 0)
         throw new MyInputException
                   ("queue size must be >= 0");

      // increase array size if necessary
      if (s > queue.length)
         queue = new Object [s + 1];

      // input the queue elements front to rear
      Object [] inputMethodArgs = {inStream};
      System.out.println("Enter queue elements from front to rear");
      try
      {
         for (int i = 0; i < s; i++)
            queue[i] = inputMethod.invoke(null, inputMethodArgs);
      }
      catch (Exception e)
      {
         System.out.println(e);
         throw new IllegalArgumentException
                   ("input method for queue element type is incorrect");
      }

      front = queue.length - 1;
      if (s == 0)
         // empty queue
         rear = front;
      else
         rear = s - 1;
   }

   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      int size = size();
      if (size != 0)
      {// nonempty queue
         // do first element
         int currentPos = (front + 1) % queue.length;
         s.append(queue[currentPos].toString());
         for (int i = 1; i < size; i++)
         {
            currentPos = (currentPos + 1) % queue.length;
            s.append(", " + queue[currentPos].toString());
         }
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** split the queue this into the queues a and b
     * a gets the bottom-half elements, b gets the rest */
   public void split(ExtendedQueue a, ExtendedQueue b)
   {
      // determine size of resulting queues a and b
      int sizeOfA = (size() + 1) / 2;
      int sizeOfB = size() - sizeOfA;

      // cast a and b into ExtendedArrayQueues
      ExtendedArrayQueue aa = (ExtendedArrayQueue) a;
      ExtendedArrayQueue ab = (ExtendedArrayQueue) b;

      // make sure array sizes are adequate
      if (sizeOfA > aa.queue.length)
         aa.queue = new Object [sizeOfA + 1];
      if (sizeOfB > ab.queue.length)
         ab.queue = new Object [sizeOfB + 1];

      // put elements alternately into a and b
      int currentPos = front;  // position of last element copied over
      for (int i = 0; i < sizeOfB; i++)
      {
         currentPos = (currentPos + 1) % queue.length;
         aa.queue[i] = queue[currentPos];
         currentPos = (currentPos + 1) % queue.length;
         ab.queue[i] = queue[currentPos];
      }
      // copy extra element if any
      if (sizeOfA > sizeOfB)
      {
         currentPos = (currentPos + 1) % queue.length;
         aa.queue[sizeOfA - 1] = queue[currentPos];
      }

      aa.front = aa.queue.length - 1;
      if (sizeOfA == 0)
         // empty queue
         aa.rear = aa.front;
      else
         aa.rear = sizeOfA - 1;

      ab.front = ab.queue.length - 1;
      if (sizeOfB == 0)
         // empty queue
         ab.rear = ab.front;
      else
         ab.rear = sizeOfB - 1;
   }

   /** set the queue this to contain the elements in a and b,
     * elements are taken alternately from a and b */
   public void combine(ExtendedQueue a, ExtendedQueue b)
   {
      // cast a and b into ExtendedArrayQueues
      ExtendedArrayQueue aa = (ExtendedArrayQueue) a;
      ExtendedArrayQueue ab = (ExtendedArrayQueue) b;

      // make sure array size is adequate
      int aSize = aa.size();
      int bSize = ab.size();
      int sizeOfThis = aSize + bSize;
      if (sizeOfThis > queue.length - 1)
         queue = new Object [sizeOfThis + 1];

      // put elements of a and b alternately into this
      int s = Math.min(aSize, bSize);
      int currentA = aa.front;
      int currentB = ab.front;
      for (int i = 0; i < 2 * s; i += 2)
      {
         currentA = (currentA + 1) % aa.queue.length;
         queue[i] = aa.queue[currentA];
         currentB = (currentB + 1) % ab.queue.length;
         queue[i + 1] = ab.queue[currentB];
      }

      // put left over elements of into this
      if (aSize > bSize)
         // a has additional elements
         for (int j = 2 * s; j < sizeOfThis; j++)
         {
            currentA = (currentA + 1) % aa.queue.length;
            queue[j] = aa.queue[currentA];
         }
      else
         if (aSize < bSize)
            // b has additional terms
            for (int j = 2 * s; j < sizeOfThis; j++)
            {
               currentB = (currentB + 1) % ab.queue.length;
               queue[j] = ab.queue[currentB];
            }
       front = queue.length - 1;
       if (sizeOfThis == 0)
         // empty queue
          rear = front;
       else
          rear = sizeOfThis - 1; 
   }

   /** test program */
   public static void main(String [] args)
   {  
      ExtendedArrayQueue a = new ExtendedArrayQueue(1);
      ExtendedArrayQueue b = new ExtendedArrayQueue(1);
      ExtendedArrayQueue c = new ExtendedArrayQueue(1);

      // input stream for queue input
      MyInputStream keyboard = new MyInputStream();

      // queue elements will be of type MyInteger; these will
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
         System.out.println("The queue is " + a);
         System.out.println("The queue size is " + a.size());
         a.split(b, c);
         System.out.println("The split queues are " + b + " and " + c);
         System.out.println("The sizes of the split queues are " + b.size()
                             + " and " + c.size());
         b.combine(a, c);
         System.out.println("The result of combining the input queue and the "
           + "second split queue is " + b);
      }
   }  
}
