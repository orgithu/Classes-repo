
/** circular linked list extended to include an arbirary remove method */

package dataStructures;

public class CircularListWithRemove extends CircularList
{
   /** remove the element in the node x
     * @return removed element */
   public Object remove(ChainNode x)
   {
      if (size == 1)
         // list is empty after removal
         lastNode = null;
      else
      {
          // move successor of node x to node x and remove from list
          ChainNode succ = x.next;
          x.element = succ.element;
          x.next = succ.next;

         // update lastNode if it was removed
         if (succ == lastNode)
            lastNode = x;
      }
      size--;
      return x.element;
   }

   /** @return node with specified index
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1 */
   public ChainNode node(int index)
   {
      checkIndex(index);

      // move to desired node
      ChainNode currentNode = lastNode.next;
      for (int i = 0; i < index; i++)
         currentNode = currentNode.next;

      return currentNode;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      CircularListWithRemove x = new CircularListWithRemove();
      x.add(0, new Integer(2));
      x.add(1, new Integer(6));
      x.add(2, new Integer(1));
      x.add(3, new Integer(4));
      x.add(4, new Integer(7));
      x.add(5, new Integer(9));
      System.out.println("The list is " + x);

      // test node
      System.out.println("Element at 0 is " +
                         x.node(0).element);
      System.out.println("Element at 3 is " +
                         x.node(3).element);

      // test remove
      x.remove(x.node(5));
      System.out.println("Element at 5 removed. The list is " + x);
      x.remove(x.node(2));
      System.out.println("Element at 2 removed. The list is " + x);
      x.remove(x.node(2));
      System.out.println("Element at 2 removed. The list is " + x);
      x.remove(x.node(2));
      System.out.println("Element at 2 removed. The list is " + x);
   }
}
