

/** extension of DoublyLinkedList to include the method reverse */

package dataStructures;

public class DoublyLinkedListWithReverse extends DoublyLinkedList
{
   /** in-place reversal of a doubly linked list */
   public void reverse()
   {
      DoubleNode currentNode = firstNode;

      while (currentNode != null)
      {
         // change pointer directions
         DoubleNode nextNode = currentNode.next;
         currentNode.next = currentNode.previous;
         currentNode.previous = nextNode;
   
         // move to next node
         currentNode = nextNode;
      }
      
      // swap values of firstNode and lastNode
      DoubleNode temp = firstNode;
      firstNode = lastNode;            
      lastNode = temp;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      DoublyLinkedListWithReverse c = new DoublyLinkedListWithReverse();
      int n = 10;

      // put n elements into c
      for (int i = 0; i < n; i++)
         c.add(i, new Integer(i));
      System.out.println("The list is " + c);

      // reverse the list
      c.reverse();
      System.out.println("The reversed list is " + c);
   }
}
