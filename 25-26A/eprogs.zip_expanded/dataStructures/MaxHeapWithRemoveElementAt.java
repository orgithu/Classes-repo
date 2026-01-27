   
/** max heap with method removeElementAt(i) */

package dataStructures;

public class MaxHeapWithRemoveElementAt extends MaxHeap
{
   // constructors
   /** create a heap with the given initial capacity */
   public MaxHeapWithRemoveElementAt(int initialCapacity)
      {super(initialCapacity);}
   
   /** create a heap with initial capacity 10 */
   public MaxHeapWithRemoveElementAt()
      {super(10);}

   
   /** remove element in heap[i] and return it
     * @return null iff heap[i] is not part of the heap */
   public Comparable removeElementAt(int i)
   {
      if (i < 1 || i > size)
         // heap elements are heap[1:size]
         return null;
   
      // handle removal of last element as special case
      if (i == size)
         return heap[size--];

      // removal of element other than last one
      Comparable x = heap[i];           // element to remove
   
      // take last element out of heap, will try to put it at heap[i]
      Comparable y = heap[size--];      // last element
   
      // see if y is <= element at parent of i
      if (i == 1 || y.compareTo(heap[i / 2]) <= 0)
      {// no problem with parent element, move down the tree
       // looking for place for y 
         int ci = 2 * i; // child of i
         while (ci <= size)
         {
            // heap[ci] should be larger child of i
            if (ci < size &&
                heap[ci].compareTo(heap[ci + 1]) < 0)
               ci++;
      
            // can we put y in heap[i]?
            if (y.compareTo(heap[ci]) >= 0)
               break;   // yes
      
            // no
            heap[i] = heap[ci]; // move child up
            i = ci;             // move down a level
            ci *= 2;
         }
      }
      else
         // y is >= all elements in subtree with root i
         // move up the tree looking for place for y
         while (i != 1 && heap[i / 2].compareTo(y) < 0)
         {// cannot put y in heap[i]
            heap[i] = heap[i / 2]; // move element down
            i /= 2;                // move to parent
         }
   
      // put y into heap[i]
      heap[i] = y;

      return x;
   }

   /** test program */
   public static void main(String [] args)
   {
      MaxHeapWithRemoveElementAt h = new MaxHeapWithRemoveElementAt();
      Comparable [] z = new Comparable [10];
      for (int i = 1; i < 10; i++)
         z[i] = new Integer(i);
      h.initialize(z, 9);
      System.out.println("Elements in array order are");
      System.out.println(h);

      // test removeElementAt
      System.out.println("Element " + h.removeElementAt(12) + " removed from "
                  + "position 12");
      System.out.println("Elements in array order are");
      System.out.println(h);

      System.out.println("Element " + h.removeElementAt(1) + " removed from "
                  + "position 1");
      System.out.println("Elements in array order are");
      System.out.println(h);

      System.out.println("Element " + h.removeElementAt(8) + " removed from "
                  + "position 8");
      System.out.println("Elements in array order are");
      System.out.println(h);

      System.out.println("Element " + h.removeElementAt(4) + " removed from "
                  + "position 4");
      System.out.println("Elements in array order are");
      System.out.println(h);

      System.out.println("Element " + h.removeElementAt(5) + " removed from "
                  + "position 5");
      System.out.println("Elements in array order are");
      System.out.println(h);

      // try a bigger heap
      Comparable [] y = new Comparable [16];
      int [] data = {20, 10, 18, 9, 8, 5, 17, 8, 9, 8, 7, 4, 5, 2, 16};
      for (int i = 1; i < 16; i++)
         y[i] = new Integer(data[i - 1]);
      h.initialize(y, 15);
      System.out.println("Elements in array order are");
      System.out.println(h);

      System.out.println("Element " + h.removeElementAt(4) + " removed from "
                  + "position 4");
      System.out.println("Elements in array order are");
      System.out.println(h);

      System.out.println("Element " + h.removeElementAt(2) + " removed from "
                  + "position 2");
      System.out.println("Elements in array order are");
      System.out.println(h);
   }
}
