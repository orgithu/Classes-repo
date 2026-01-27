

/** extension of chain that includes a method to set a specified element */

package dataStructures;

public class ChainWithSet extends Chain
{
   /** set the element whose index is theIndex to theElement
     * @throws IndexOutOfBoundsException when theIndex &lt; 0
     * or theIndex >= size 
     * @returns old element with index equal to theIndex */
   public Object set(int theIndex, Object theElement)
   {
      checkIndex(theIndex);

      // move to node with element whose index is theIndex
      ChainNode currentNode = firstNode;
      for (int i = 0; i < theIndex; i++)
         currentNode = currentNode.next;

      Object elementToReturn = currentNode.element;
      currentNode.element = theElement;
      return elementToReturn;
   }

   /** test program */
   public static void main(String [] args)
   {
      ChainWithSet x = new ChainWithSet();

      x.add(0, new Integer(0));
      x.add(1, new Integer(1));
      x.add(2, new Integer(2));
      x.add(3, new Integer(3));
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);

      System.out.println("Element " + x.set(2, new Integer(4))
                         + " changed to 4");
      System.out.println("List size is " + x.size());
      System.out.println("The list is " + x);
   }
}
