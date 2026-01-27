
/** rank sort method to sort a simulated chain */

package dataStructures;

public class SimulatedChainWithRankSort extends SimulatedChain
{
   // constructors
   public SimulatedChainWithRankSort(int initialCapacity)
      {super(initialCapacity);}

   public SimulatedChainWithRankSort()
      {super();}

   /** sort the chain using the rank sort method */
   public void rankSort()
   {
      if (size == 0)
         return;  // empty chain
   
      // r[i] will be rank of element i
      int [] r = new int [size];
   
      // compute ranks by comparing all pairs of elements
      int currentNode = firstNode;
      for (int i = 0; i < size; i++)
      {// compare S.node[currentNode].element with all elements
         int compareNode = firstNode;
         for (int j = 0; j < i; j++)
         {// S.node[currentNode].element is element i and
          // and S.node[compareNode].element is element j, j < i
            if (((Comparable) S.node[compareNode].element).compareTo
                                     (S.node[currentNode].element) <= 0)
               r[i]++;
            else
               r[j]++;
            compareNode = S.node[compareNode].next;
         }
         currentNode = S.node[currentNode].next;
      }
   
      // distribute nodes to bins by rank
      int [] bin = new int [size];
      currentNode = firstNode;
      for (int i = 0; i < size; i++)
      {
         bin[r[i]] = currentNode;
         currentNode = S.node[currentNode].next;
      }
   
      // collect from bins
      firstNode = bin[0];
      int lastNode = firstNode;  // last node on chain
      for (int i = 1; i < size; i++)
         lastNode = S.node[lastNode].next = bin[i];
      S.node[lastNode].next = -1;
   }

   /** test program */
   public static void main(String [] args)
   {
      SimulatedChain.S = new SimulatedSpace1(100);
      SimulatedChainWithRankSort x = new SimulatedChainWithRankSort();
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
