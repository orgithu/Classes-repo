/** reverse a circular list with a header node */

package dataStructures;

public class HeadCircularListWithReverse
       extends HeadCircularList
{
   /** reverse the linear list */
   public void reverse()
   {
      ChainNode previousNode = headerNode,
                currentNode = headerNode.next,
                nextNode;
      lastNode = currentNode;  // will be when we are done

      while (currentNode != headerNode)
      {// change pointer direction
         nextNode = currentNode.next;
         currentNode.next = previousNode;
   
         // move to next node
         previousNode = currentNode;
         currentNode = nextNode;
      }
      currentNode.next = previousNode;
      
   }

   
   /** test program */
   public static void main(String [] args)
   {
      HeadCircularListWithReverse c = new HeadCircularListWithReverse();
      int n = 10;

      // put n elements into c
      for (int i = 0; i < n; i++)
         c.add(i, new Integer(i));
      System.out.println("The list is " + c);

      // reverse the chain
      c.reverse();
      System.out.println("The reversed list is " + c);
   }
}
