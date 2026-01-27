
/** extension of ExtendedChain to include the method circularShift */

package dataStructures;

public class ExtendedChainWithCircularShift extends ExtendedChain
{
   /** shift the elements clockwise circularly by shiftAmount
     * @throws IllegalArgumentException when
     * shiftAmount < 0 */
   private void circularShift(int shiftAmount)
   {
      // empty list is easily shifted by any amount
      if (size == 0)
         return;

      // validate shiftAmount
      if (shiftAmount < 0)
         throw new IllegalArgumentException
                   ("shift amount must be >= 0, it is " + shiftAmount);

      // get equivalent amount in the range 0 though size - 1
      shiftAmount %= size;
  
      // expedite zero shift, makes rest of code easier
      if (shiftAmount == 0)
         return;

      // find last node to be moved to right end
      ChainNode lastNodeToMove = firstNode;
      for (int i = 1; i < shiftAmount; i++)
         lastNodeToMove = lastNodeToMove.next;

      // move nodes from front to rear
      lastNode.next = firstNode;
      firstNode = lastNodeToMove.next;
      lastNode = lastNodeToMove;
      lastNode.next = null;
   }


   /** test program */
   public static void main(String [] args)
   {
      int numberOfSizes = 2;
      int [] size = {5, 8};   // list sizes to test
      
      for (int j = 0; j < numberOfSizes; j++)
      {
         int n = size[j];
         for (int k = 0; k <= n; k++)
         {// k is shift amount
            ExtendedChainWithCircularShift x =
               new ExtendedChainWithCircularShift();
            
            //  put elements in x
            for (int i = 0; i < n; i++)
               x.add(0, new Integer(n - i - 1));
            
            System.out.println("The list is " + x);
            x.circularShift(k);
            System.out.println("The list shifted by " + k + " is " + x);
            System.out.println();
         }   
         System.out.println();
      }
   }   
}
