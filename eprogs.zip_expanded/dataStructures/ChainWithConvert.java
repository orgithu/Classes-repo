
/** extension of Chain to include the methods
  * fromList and toList */

package dataStructures;

public class ChainWithConvert extends Chain
{
   /** convert f into its chain representation */
   public void fromList(ArrayLinearList f)
   {
      // first make this empty
      firstNode = null;
      ChainNode lastNode = null;
      
      // copy elements from f
      int len = f.size();
      for (int i = 0; i < len; i++)
      {
         // create a node for the next element
         ChainNode node = new ChainNode(f.get(i));
   
         // put this node into the chain
         if (firstNode == null)  // chain is empty
            firstNode = node;
         else // chain is not empty
            lastNode.next = node;
   
         lastNode = node;
      }
   
      // set next field of last node
      if (lastNode != null)
         lastNode.next = null;
   }
   
   /** convert this into its array-based representation f */
   public void toList(ArrayLinearList f)
   {
      // first make f empty
      int len = f.size() - 1;
      for (int i = len; i >= 0; i--)
         f.remove(i);
   
      // copy from this
      ChainNode current = firstNode;
      int i = 0;       // index of current element
      while (current != null)
      {// copy current element
         f.add(i, current.element);
         current = current.next;  // move to next node
         i++;
      }
   }
   
   /** test program */
   public static void main(String [] args)
   {
      ChainWithConvert c = new ChainWithConvert();
      ArrayLinearList f = new ArrayLinearList();
      int n = 10;

      // put n elements into f
      for (int i = 0; i < n; i++)
         f.add(i, new Integer(i));
      System.out.println("The array-based list is " + f);

      // convert to a chain
      c.fromList(f);
      System.out.println("The chain is " + c);

      // convert to an array-based list
      c.toList(f);
      System.out.println("The array-based list is " + f);
   }
}
