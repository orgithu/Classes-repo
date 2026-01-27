/** modified min heap for elements of type WeightedEdgeNode */

package dataStructures;

import utilities.*;

public class ModifiedMinHeap
{
   // data members
   WeightedEdgeNode [] heap;   // array for complete binary tree
   int [] location;            // current location of an element
   int size;                   // number of elements in heap

   // constructors
   /** create a heap with the given initial capacity */
   public ModifiedMinHeap(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
                   ("initialCapacity must be >= 1");
      heap = new WeightedEdgeNode [initialCapacity + 1];
      location = new int [initialCapacity + 1];
      size = 0;
   }
   
   /** create a heap with initial capacity 10 */
   public ModifiedMinHeap()
      {this(10);}

   // methods
   /** @return true iff the heap is empty */
   public boolean isEmpty()
      {return size == 0;}

   /** @return number of elements in the heap */
   public int size()
      {return size;}

   /** @return minimum element
     * @return null if the heap is empty */
   public WeightedEdgeNode getMin()
   {
      if (size == 0)
         return null;
      else
         return heap[1];
   }

   /** put theElement into the heap */
   public void put(WeightedEdgeNode theElement)
   {
      // increase array size if necessary
      if (size == heap.length - 1)
         heap = (WeightedEdgeNode []) ChangeArrayLength.changeLength1D
                                    (heap, 2 * heap.length);
   
      // find place for theElement
      // i starts at new leaf and moves up tree
      int i = ++size;
      while (i != 1 && ((Comparable) heap[i/2].weight)
                         .compareTo(theElement.weight) > 0)
      {
         // cannot put theElement in heap[i]
         heap[i] = heap[i/2]; // move element down
         location[heap[i].vertex] = i;
         i /= 2;              // move to parent
      }
   
      heap[i] = theElement;
      location[theElement.vertex] = i;
   }
   
   /** remove min element and return it */
   public WeightedEdgeNode removeMin()
   {
      // if heap is empty return null
      if (size == 0) return null;       // heap empty
   
      WeightedEdgeNode x = heap[1];           // min element
      location[x.vertex] = 0;
   
      // restucture heap
      WeightedEdgeNode y = heap[size--];      // last element
   
      // find place for y starting at root
      int i = 1,  // current node of heap
          ci = 2; // child of i
      while (ci <= size)
      {
         // heap[ci] should be smaller child of i
         if (ci < size && ((Comparable) heap[ci].weight)
                            .compareTo(heap[ci+1].weight) > 0)
                               ci++;
   
         // can we put y in heap[i]?
         if (((Comparable) y.weight).compareTo(heap[ci].weight) <= 0)
            break;   // yes
   
         // no
         heap[i] = heap[ci];             // move child up
         location[heap[i].vertex] = i;
         i = ci;                         // move down a level
         ci *= 2;
      }
      heap[i] = y;
      location[y.vertex] = i;
   
      return x;
   }
   

   /** decrease weight of x.vertex to x.weight */
   public void decreaseWeight(WeightedEdgeNode x)
   {
      // check if x.vertex in heap
      if (location[x.vertex] == 0)
         // not in heap
         throw new IllegalArgumentException
                   ("illegal id value");
   
      // make sure new distance is smaller
      if (((Comparable) x.weight)
            .compareTo(heap[location[x.vertex]].weight) >= 0)
         throw new IllegalArgumentException
           ("new distance value must be less than old one");
   
      // find new place for x
      // i starts at old location of x and moves up tree
      int i = location[x.vertex];
      while (i != 1 && ((Comparable) x.weight)
                         .compareTo(heap[i/2].weight) < 0)
      {// cannot put x in heap[i]
         heap[i] = heap[i/2];              // move element down
         location[heap[i].vertex] = i;
         i /= 2;                           // move to parent
      }
      heap[i] = x;
      location[x.vertex] = i;
   }
}
