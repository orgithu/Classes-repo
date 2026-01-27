
/** extension of Chain to include the method lastIndexOf */

package dataStructures;

public class ChainWithLastIndexOf extends Chain
{
   /** @return index of last occurrence of theElement,
     * return -1 if theElement not in list */
   public int lastIndexOf(Object theElement)
   {
      // search the chain for theElement
      ChainNode currentNode = firstNode;
      int index = 0;              // index of currentNode
      int indexToReturn = -1;     // most recent index where theElement seen
      while (currentNode != null)
      {
         if (currentNode.element.equals(theElement))
            indexToReturn = index;

         // move to next node
         currentNode = currentNode.next;
         index++;
      }

      return indexToReturn;
   }      

   /** test program */
   public static void main(String [] args)
   {
      ChainWithLastIndexOf x = new ChainWithLastIndexOf();

      x.add(0, new Integer(0));
      x.add(1, new Integer(1));
      x.add(2, new Integer(1));
      x.add(3, new Integer(0));
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);
      System.out.println();

      System.out.println("First occurrence of 0 is "
                         + x.indexOf(new Integer(0)));
      System.out.println("First occurrence of 1 is "
                         + x.indexOf(new Integer(1)));
      System.out.println("Last occurrence of 0 is "
                         + x.lastIndexOf(new Integer(0)));
      System.out.println("Last occurrence of 1 is "
                         + x.lastIndexOf(new Integer(1)));
      System.out.println("Last occurrence of 2 is "
                         + x.lastIndexOf(new Integer(2)));
   }
}
