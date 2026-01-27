

/** extended linked queue class derived from LinkedQueue */

package dataStructures;

import java.lang.reflect.*;
import wrappers.*;
import utilities.*;
import exceptions.*;

public class ExtendedLinkedQueue extends LinkedQueue
                                implements ExtendedQueue
{
   // data member
   int size;   // default initial value is 0

   // constructors
   /** create a queue with the given initial capacity */
   public ExtendedLinkedQueue(int initialCapacity)
      {super(initialCapacity);}

   /** create a queue with initial capacity 10 */
   public ExtendedLinkedQueue()
      {this(10);}


   // need to change put and remove
   public void put(Object theElement)
   {
      super.put(theElement);
      size++;
   }

   public Object remove()
   {
      size--;
      return super.remove();
   }

   // methods of ExtendedQueue
   /** @return number of elements in the queue */
   public int size()
      {return size;}

   /** input a queue from the input stream inStream using
     * the method inputMethod to input each element */
   public void input(Method inputMethod, MyInputStream inStream)
   {
      // input size of new queue
      System.out.println("Enter number of elements in queue");
      size = inStream.readInteger();
      if (size < 0)
         throw new MyInputException
                   ("queue size must be >= 0");

      // input the queue elements front to rear
      Object [] inputMethodArgs = {inStream};
      front = null;  // start with an empty queue
      System.out.println("Enter queue elements from front to rear");
      try
      {
         for (int i = 0; i < size; i++)
         {
            // input the element
            Object theElement = inputMethod.invoke(null, inputMethodArgs);

            // create a node for theElement
            ChainNode p = new ChainNode(theElement);
   
            // append p to the chain
            if (front == null)
              // queue is currently empty
              front = p;     
            else rear.next = p;                // nonempty queue
            rear = p;
         }
      }
      catch (Exception e)
      {
         System.out.println(e);
         throw new IllegalArgumentException
                    ("input method for queue element type is incorrect");
      }

      if (size > 0) 
         rear.next = null;
   }

   /** convert to a string */
   public String toString()
   {
      StringBuffer s = new StringBuffer("["); 
      int size = size();
      if (size != 0)
      {// nonempty queue
         // do first element
         s.append(front.element.toString());
         // do remaining elements
         ChainNode currentNode = front.next;
         for (int i = 1; i < size; i++)
         {
            s.append(", " + currentNode.element.toString());
            currentNode = currentNode.next;
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
      // cast a and b into ExtendedLinkedQueues
      ExtendedLinkedQueue aa = (ExtendedLinkedQueue) a;
      ExtendedLinkedQueue ab = (ExtendedLinkedQueue) b;

      // determine size of resulting queues a and b
      aa.size = (size + 1) / 2;
      ab.size = size - aa.size;

      // put elements alternately into a and b
      ChainNode currentNode = front;
      aa.front = null;
      ab.front = null;
      for (int i = 0; i < ab.size; i++)
      {
         // add to a
         ChainNode p = new ChainNode(currentNode.element);
         if (aa.front == null)
           // queue is currently empty
           aa.front = p;     
         else aa.rear.next = p;                // nonempty queue
         aa.rear = p;
       
         // advance to next node of this
         currentNode = currentNode.next;

         // add to b
         p = new ChainNode(currentNode.element);
         if (ab.front == null)
           // queue is currently empty
           ab.front = p;     
         else ab.rear.next = p;                // nonempty queue
         ab.rear = p;

         // advance to next node of this
         currentNode = currentNode.next;
      }

      // copy extra element if any
      if (aa.size > ab.size)
      {
         ChainNode p = new ChainNode(currentNode.element);
         if (aa.front == null)
           // queue is currently empty
           aa.front = p;     
         else aa.rear.next = p;                // nonempty queue
         aa.rear = p;
      }

      // set next field of rear nodes
      if (aa.front != null)
         // not empty
         aa.rear.next = null;
      if (ab.front != null)
         // not empty
         ab.rear.next = null;
   }

   /** set the queue this to contain the elements in a and b,
     * elements are taken alternately from a and b */
   public void combine(ExtendedQueue a, ExtendedQueue b)
   {
      // cast a and b into ExtendedLinkedQueues
      ExtendedLinkedQueue aa = (ExtendedLinkedQueue) a;
      ExtendedLinkedQueue ab = (ExtendedLinkedQueue) b;

      size = aa.size + ab.size;

      // put elements of a and b alternately into this
      int s = Math.min(aa.size, ab.size);
      ChainNode currentA = aa.front;
      ChainNode currentB = ab.front;
      front = null;  // make this empty
      for (int i = 0; i < 2 * s; i += 2)
      {
         // copy from a
         ChainNode p = new ChainNode(currentA.element);
         if (front == null)
           // queue is currently empty
           front = p;     
         else rear.next = p;                // nonempty queue
         rear = p;
         currentA = currentA.next;

         // copy from b
         p = new ChainNode(currentB.element);
         rear.next = p;                   // cannot be empty
         rear = p;
         currentB = currentB.next;
      }

      // put left over elements of into this
      while (currentA != null)
      {// a has additional elements
         ChainNode p = new ChainNode(currentA.element);
         if (front == null)
           // queue is currently empty
           front = p;     
         else rear.next = p;                // nonempty queue
         rear = p;
         currentA = currentA.next;
      }

      while (currentB != null)
      {// b has additional elements
         ChainNode p = new ChainNode(currentB.element);
         if (front == null)
           // queue is currently empty
           front = p;     
         else rear.next = p;                // nonempty queue
         rear = p;
         currentB = currentB.next;
      }

      // set next field of rear node
      if (front != null)
         rear.next = null;
   }

   /** test program */
   public static void main(String [] args)
   {  
      ExtendedLinkedQueue a = new ExtendedLinkedQueue(1);
      ExtendedLinkedQueue b = new ExtendedLinkedQueue(1);
      ExtendedLinkedQueue c = new ExtendedLinkedQueue(1);

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
