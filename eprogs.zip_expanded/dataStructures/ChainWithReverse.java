

/** extension of Chain to include the method reverse */

package dataStructures;

public class ChainWithReverse extends Chain
{
   /** in-place reversal of a chain */
   public void reverse()
   {
      ChainNode lastNode = null,
                currentNode = firstNode,
                nextNode;

      while (currentNode != null)
      {
         // change pointer direction
         nextNode = currentNode.next;
         currentNode.next = lastNode;
   
         // move to next node
         lastNode = currentNode;
         currentNode = nextNode;
      }
      
      firstNode = lastNode; // new first node
   }
   
   /** test program */
   public static void main(String [] args)
   {
      ChainWithReverse c = new ChainWithReverse();
      int n = 10;

      // put n elements into c
      for (int i = 0; i < n; i++)
         c.add(i, new Integer(i));
      System.out.println("The chain is " + c);

      // reverse the chain
      c.reverse();
      System.out.println("The reversed chain is " + c);
   }
}
