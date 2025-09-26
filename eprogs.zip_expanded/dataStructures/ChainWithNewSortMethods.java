/** bubble, selection, and rank sort methods to sort a chain */

package dataStructures;

public class ChainWithNewSortMethods extends Chain
{
   // constructors
   public ChainWithNewSortMethods(int initialCapacity)
      {super(initialCapacity);}

   public ChainWithNewSortMethods()
      {super();}

   // bubble sort
   /** bubble largest element in first n nodes to right */
   private void bubble(int n)
   {
      ChainNode currentNode = firstNode;
      for (int i = 0; i < n - 1; i++)
      {
         ChainNode nextNode = currentNode.next;
         if (((Comparable) currentNode.element)
               .compareTo(nextNode.element) > 0)
         {// swap elements
            Object temp = currentNode.element;
            currentNode.element = nextNode.element;
            nextNode.element = temp;
         }
         currentNode = nextNode;
      }
   }

   /** sort the array a using the bubble sort method */
   public void bubbleSort()
   {
      for (int i = size; i > 1; i--)
         bubble(i);
   }

   /** sort the chain using the selection sort method */
   public void selectionSort()
   {
      for (int i = size; i > 1; i--)
      {
         // find max object from first i nodes
         Comparable currentMax = (Comparable) firstNode.element;
         ChainNode maxNode = firstNode;
         ChainNode currentNode = firstNode;
         for (int j = 1; j < i; j++)
         {
            ChainNode nextNode = currentNode.next;
            if (currentMax.compareTo(nextNode.element) < 0)
            {// found a larger element
               currentMax = (Comparable) nextNode.element;
               maxNode = nextNode;
            }

            // move to next node
            currentNode = nextNode;
         }

         // move max object to right end
         maxNode.element = currentNode.element;
         currentNode.element = currentMax;
      }
   }

   /** sort the chain using the rank sort method */
   public void rankSort()
   {
      if (size == 0)
         return;  // empty chain
   
      // r[i] will be rank of element i
      int [] r = new int [size];
   
      // compute ranks by comparing all pairs of elements
      ChainNode currentNode = firstNode;
      for (int i = 0; i < size; i++)
      {// compare currentNode.element with all elements
         ChainNode node = firstNode;
         for (int j = 0; j < i; j++)
         {// currentNode.element is element i and node.element
          // is element j, j < i
            if (((Comparable) node.element).compareTo
                                   (currentNode.element) <= 0)
               r[i]++;
            else
               r[j]++;
            node = node.next;
         }
         currentNode = currentNode.next;
      }
   
      // distribute nodes to bins by rank
      ChainNode [] bin = new ChainNode [size];
      currentNode = firstNode;
      for (int i = 0; i < size; i++)
      {
         bin[r[i]] = currentNode;
         currentNode = currentNode.next;
      }
   
      // collect from bins
      firstNode = bin[0];
      ChainNode lastNode = firstNode;  // last node on chain
      for (int i = 1; i < size; i++)
         lastNode = lastNode.next = bin[i];
      lastNode.next = null;
   }

   /** test program */
   public static void main(String [] args)
   {
      ChainWithNewSortMethods x = new ChainWithNewSortMethods();
      x.add(0, new Integer(3));
      x.add(1, new Integer(2));
      x.add(2, new Integer(4));
      x.add(3, new Integer(1));
      x.add(4, new Integer(6));
      x.add(5, new Integer(9));
      x.add(6, new Integer(8));
      x.add(7, new Integer(7));
      x.add(8, new Integer(5));
      x.add(9, new Integer(0));
   
      // output elements to be sorted
      System.out.println("The elements are " + x);

      // sort the elements
      x.bubbleSort();

      // output in sorted order
      System.out.println("The sorted order is " + x);

      x = new ChainWithNewSortMethods();
      x.add(0, new Integer(3));
      x.add(1, new Integer(2));
      x.add(2, new Integer(4));
      x.add(3, new Integer(1));
      x.add(4, new Integer(6));
      x.add(5, new Integer(9));
      x.add(6, new Integer(8));
      x.add(7, new Integer(7));
      x.add(8, new Integer(5));
      x.add(9, new Integer(0));
   
      // output elements to be sorted
      System.out.println("The elements are " + x);

      // sort the elements
      x.selectionSort();

      // output in sorted order
      System.out.println("The sorted order is " + x);

      x = new ChainWithNewSortMethods();
      x.add(0, new Integer(3));
      x.add(1, new Integer(2));
      x.add(2, new Integer(4));
      x.add(3, new Integer(1));
      x.add(4, new Integer(6));
      x.add(5, new Integer(9));
      x.add(6, new Integer(8));
      x.add(7, new Integer(7));
      x.add(8, new Integer(5));
      x.add(9, new Integer(0));
   
      // output elements to be sorted
      System.out.println("The elements are " + x);

      // sort the elements
      x.rankSort();

      // output in sorted order
      System.out.println("The sorted order is " + x);
   }
}
