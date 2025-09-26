   
/** max heap with modified remove max method */

package dataStructures;

public class NewMaxHeap extends MaxHeap
{
   // constructors
   /** create a heap with the given initial capacity */
   public NewMaxHeap(int initialCapacity)
      {super(initialCapacity);}
   
   /** create a heap with initial capacity 10 */
   public NewMaxHeap()
      {super(10);}

   // override MaxHeap.removeMax
   /** remove max element and return it */
   public Comparable removeMax()
   {
      // if heap is empty return null
      if (size == 0) return null;       // heap empty
   
      Comparable x = heap[1];           // max element
   
      // restucture heap
      Comparable y = heap[size--];      // last element
   
      // first propagate vacancy from root to a leaf
      int i = 1,                        // current position of vacancy
          ci = 2;                       // child of i
      while (ci <= size)
      {
         // heap[ci] should be larger child of i
         if (ci < size && heap[ci].compareTo(heap[ci+ 1]) < 0)
            ci++;
   
         // move larger child to heap[i]
         heap[i] = heap[ci];            // move child up
         i = ci;                        // move down a level
         ci *= 2;
      }
   
      i = ci / 2;
      // vacancy is now at the leaf heap[i], start from here and insert y
      while (i != 1 && y.compareTo(heap[i / 2]) > 0)
      {// cannot put y in heap[i]
         heap[i] = heap[i / 2];         // move element down
         i /= 2;                        // move to parent
      }
   
      heap[i] = y;
   
      return x;
   }
   

   /** test program */
   public static void main(String [] args)
   {
      // test constructor and put
      NewMaxHeap h = new NewMaxHeap(4);
      h.put(new Integer(10));
      h.put(new Integer(20));
      h.put(new Integer(5));
      h.put(new Integer(7));
      h.put(new Integer(40));
      h.put(new Integer(25));

      // test toString
      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      h.put(new Integer(15));
      h.put(new Integer(30));

      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      // test remove max
      System.out.println("The max element is " + h.getMax());
      System.out.println("Deleted max element " + h.removeMax());
      System.out.println("Deleted max element " + h.removeMax());
      System.out.println("Elements in array order are");
      System.out.println(h);
      System.out.println();

      // test initialize
      Comparable [] z = new Comparable [10];
      for (int i = 1; i < 10; i++)
         z[i] = new Integer(i);
      h.initialize(z, 9);
      System.out.println("Elements in array order are");
      System.out.println(h);
   }
}
