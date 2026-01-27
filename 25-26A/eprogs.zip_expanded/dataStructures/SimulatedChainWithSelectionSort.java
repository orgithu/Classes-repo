
/** selection sort method to sort a simulated chain */

package dataStructures;

public class SimulatedChainWithSelectionSort extends SimulatedChain
{
   // constructors
   public SimulatedChainWithSelectionSort(int initialCapacity)
      {super(initialCapacity);}

   public SimulatedChainWithSelectionSort()
      {super();}

   /** sort the chain using the selection sort method */
   public void selectionSort()
   {
      for (int i = size; i > 1; i--)
      {
         // find max object from first i nodes
         Comparable currentMax = (Comparable) S.node[firstNode].element;
         int maxNode = firstNode;
         int currentNode = firstNode;
         for (int j = 1; j < i; j++)
         {
            int nextNode = S.node[currentNode].next;
            if (currentMax.compareTo(S.node[nextNode].element) < 0)
            {// found a larger element
               currentMax = (Comparable) S.node[nextNode].element;
               maxNode = nextNode;
            }

            // move to next node
            currentNode = nextNode;
         }

         // move max object to right end
         S.node[maxNode].element = S.node[currentNode].element;
         S.node[currentNode].element = currentMax;
      }
   }

   /** test program */
   public static void main(String [] args)
   {
      SimulatedChain.S = new SimulatedSpace1(100);
      SimulatedChainWithSelectionSort x = new SimulatedChainWithSelectionSort();
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
   }
}
