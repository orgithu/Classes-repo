/** max heap with key */

package dataStructures;

import wrappers.*;
import utilities.*;

public class MaxHeapWithKey implements MaxPriorityQueueWithKey
{
   // top-level nested class
   public static class HeapElement
   {
      // data members
      Comparable key;
      Object element;

      // constructors
      public HeapElement() {}
     
      public HeapElement(Comparable theKey, Object theElement)
      {
         key = theKey;
         element = theElement;
      }

      public String toString()
         {return element.toString();}
   }

   // data members of MaxHeapWithKey
   HeapElement [] heap;   // array for complete binary tree
   int size;              // number of elements in heap

   // constructors
   /** create a heap with the given initial capacity */
   public MaxHeapWithKey(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
                   ("initialCapacity must be >= 1");
      heap = new HeapElement [initialCapacity + 1];
      size = 0;
   }
   
   /** create a heap with initial capacity 10 */
   public MaxHeapWithKey()
      {this(10);}

   // methods
   /** @return true iff the heap is empty */
   public boolean isEmpty()
      {return size == 0;}

   /** @return number of elements in the heap */
   public int size()
      {return size;}

   /** @return maximum element
     * @return null if the heap is empty */
   public Object getMax()
   {
      if (size == 0)
         return null;
      else
         return heap[1].element;
   }

   /** put theElement into the heap */
   public void put(Comparable theKey, Object theElement)
   {
      // increase array size if necessary
      if (size == heap.length - 1)
         heap = (HeapElement []) ChangeArrayLength.changeLength1D
                                    (heap, 2 * heap.length);
   
      // find place for theElement
      // i starts at new leaf and moves up tree
      int i = ++size;
      while (i != 1 && heap[i / 2].key.compareTo(theKey) < 0)
      {
         // cannot put theElement in heap[i]
         heap[i] = heap[i / 2]; // move element down
         i /= 2;                // move to parent
      }
   
      heap[i] = new HeapElement(theKey, theElement);
   }
   
   /** remove max element and return it */
   public Object removeMax()
   {
      // if heap is empty return null
      if (size == 0) return null;       // heap empty
   
      Object x = heap[1].element;       // max element
   
      // restucture heap
      HeapElement y = heap[size--];     // last element
   
      // find place for y starting at root
      int i = 1,  // current node of heap
          ci = 2; // child of i
      while (ci <= size)
      {
         // heap[ci] should be larger child of i
         if (ci < size &&
             heap[ci].key.compareTo(heap[ci + 1].key) < 0)
                ci++;
   
         // can we put y in heap[i]?
         if (y.key.compareTo(heap[ci].key) >= 0)
            break;   // yes
   
         // no
         heap[i] = heap[ci]; // move child up
         i = ci;             // move down a level
         ci *= 2;
      }
      heap[i] = y;
   
      return x;
   }
   
   /** initialize max heap to element array theHeap */
   public void initialize(HeapElement [] theHeap, int theSize)
   {
      heap = theHeap;
      size = theSize;
   
      // make into a max heap
      for (int i = size / 2; i >= 1; i--)
      {
         HeapElement y = heap[i]; // root of subtree
   
         // find place to put y
         int c = 2 * i; // parent of c is target
                        // location for y
         while (c <= size)
         {
            // heap[c] should be larger sibling
            if (c < size &&
                heap[c].key.compareTo(heap[c + 1].key) < 0)
               c++;
   
            // can we put y in heap[c/2]?
            if (y.key.compareTo(heap[c].key) >= 0)
               break;  // yes
   
            // no
            heap[c / 2] = heap[c]; // move child up
            c *= 2;                // move down a level
         }
         heap[c / 2] = y;
      }
   }
   
   public String toString()
   {
      StringBuffer s = new StringBuffer(); 
      s.append("The " + size + " elements are [");
      for (int i = 1; i <= size; i++)
      {
         if (i != 1)  // not first element
            s.append(", ");
         s.append(heap[i]);
      }
      s.append("]");

      return new String(s);
   }

   /** test program */
   public static void main(String [] args)
   {
      // test constructor and put
      MaxHeapWithKey h = new MaxHeapWithKey(1);
      h.put(new Integer(10), new Integer(10));
      h.put(new Integer(35), new Integer(35));
      h.put(new Integer(20), new Integer(20));

      // test toString
      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      h.put(new Integer(15), new Integer(15));
      h.put(new Integer(30), new Integer(30));

      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      // test remove max
      System.out.println("The max element is " + (Integer) h.getMax());
      System.out.println("Deleted max element " + (Integer) h.removeMax());
      System.out.println("Deleted max element " + (Integer) h.removeMax());
      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      // test initialize
      HeapElement [] z = new HeapElement [10];
      for (int i = 1; i < 10; i++)
         z[i] = new HeapElement(new Integer(i), new Integer(i));
      h.initialize(z, 9);
      System.out.println("Elements in array order are");
      System.out.println(h);
   }
}
