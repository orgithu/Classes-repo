
/** selection and rank sort methods to sort a simulated chain */

package dataStructures;

import wrappers.*;
import utilities.*;

public class SimulatedChainWithSortMethods extends SimulatedChain
{
   // constructors
   public SimulatedChainWithSortMethods(int initialCapacity)
      {super(initialCapacity);}

   public SimulatedChainWithSortMethods()
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
            if (currentMax.lessThan(S.node[nextNode].element))
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
            if (((Comparable) S.node[compareNode].element).lessThanOrEqual
                                     (S.node[currentNode].element))
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
      SimulatedChain.S = new SimulatedSpace2(100);
      SimulatedChainWithSortMethods x = new SimulatedChainWithSortMethods();
      x.insertElementAt(new MyInteger(3), 0);
      x.insertElementAt(new MyInteger(2), 1);
      x.insertElementAt(new MyInteger(4), 2);
      x.insertElementAt(new MyInteger(1), 3);
      x.insertElementAt(new MyInteger(6), 4);
      x.insertElementAt(new MyInteger(9), 5);
      x.insertElementAt(new MyInteger(8), 6);
      x.insertElementAt(new MyInteger(7), 7);
      x.insertElementAt(new MyInteger(5), 8);
      x.insertElementAt(new MyInteger(0), 9);
   
      // output elements to be sorted
      System.out.println("The elements are " + x);

      // sort the elements
      x.selectionSort();

      // output in sorted order
      System.out.println("The sorted order is " + x);

      x = new SimulatedChainWithSortMethods();
      x.insertElementAt(new MyInteger(3), 0);
      x.insertElementAt(new MyInteger(2), 1);
      x.insertElementAt(new MyInteger(4), 2);
      x.insertElementAt(new MyInteger(1), 3);
      x.insertElementAt(new MyInteger(6), 4);
      x.insertElementAt(new MyInteger(9), 5);
      x.insertElementAt(new MyInteger(8), 6);
      x.insertElementAt(new MyInteger(7), 7);
      x.insertElementAt(new MyInteger(5), 8);
      x.insertElementAt(new MyInteger(0), 9);
   
      // output elements to be sorted
      System.out.println("The elements are " + x);

      // sort the elements
      x.rankSort();

      // output in sorted order
      System.out.println("The sorted order is " + x);
   }
}
