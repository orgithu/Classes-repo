/** MinHeap extended to include the method changeMin */

package dataStructures;

public class ExtendedMinHeap extends MinHeap
{
   // constructor
   public ExtendedMinHeap(int initialCapacity)
      {super(initialCapacity);}

   public ExtendedMinHeap()
      {super();}

   
   /** replace the current min element by theElement
     * @return original min element */
   public Comparable changeMin(Comparable theElement)
   {
      if (size == 0)
      {// heap is empty, heap capacity is at least 1
         heap[++size] = theElement;
         return null;
      }

      // save min element in e
      Comparable e = heap[1];
   
      // put replacement element into the heap
      int i = 1,                // location of vacancy
          ci = 2;               // ci is child of i
      while (ci <= size)
      {// find place to put theElement
         if (ci < size && heap[ci].compareTo(heap[ci + 1]) > 0)
            ci++;
         // now ci points to smaller child of i
   
         // can we put theElement in heap[i]?
         if (theElement.compareTo(heap[ci]) <= 0)
            break;              // yes
   
         // no
         heap[i] = heap[ci];    // move child up
    
         // move i and ci one level down
         i = ci;
         ci *= 2;
      }
   
      heap[i] = theElement;
      return e;
   }

   /** test program */
   public static void main(String [] args)
   {
      // test constructor and put
      ExtendedMinHeap h = new ExtendedMinHeap(1);
      // test initialize
      Comparable [] z = new Comparable [10];
      for (int i = 1; i < 10; i++)
         z[i] = new Integer(10 - i);
      h.initialize(z, 9);
      System.out.println("Elements in array order are");
      System.out.println(h);

      Integer w = (Integer) h.changeMin(new Integer(6));
      System.out.println("Element " + w + " has been changed to 6");
      System.out.println("Elements in array order are");
      System.out.println(h);

      w = (Integer) h.changeMin(new Integer(20));
      System.out.println("Element " + w + " has been changed to 20");
      System.out.println("Elements in array order are");
      System.out.println(h);

      w = (Integer) h.changeMin(new Integer(0));
      System.out.println("Element " + w + " has been changed to 0");
      System.out.println("Elements in array order are");
      System.out.println(h);
   }
}
