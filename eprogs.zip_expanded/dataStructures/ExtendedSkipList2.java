
/** SkipList extended to include methods to remove the elements with
  * smallest and largest keys, FASTER VERSION */

package dataStructures;

public class ExtendedSkipList2 extends SkipList
{
   // constructor
   public ExtendedSkipList2(Comparable largeKey, int maxElements, float theProb)
      {super(largeKey, maxElements, theProb);}

   /** @return element with smallest key and remove it
     * @return null if no element */
   public Object removeMinElement()
   {
      // make sure there is a min element
      if (headNode.next[0] == tailNode)
         // dictionary is empty
         return null;
   
      // save pointer to node with min element
      SkipNode minNode = headNode.next[0];
   
      // remove minNode from structure
      for (int i = 0; i <= levels &&
               headNode.next[i] == minNode; i++)
         headNode.next[i] = headNode.next[i].next[i];
   
      // update levels
      while (levels > 0 && headNode.next[levels] == tailNode)
         levels--;
   
      // return min element
      return minNode.element;
   }
   
   /** @return element with largest key and remove it
     * @return null if no element */
   public Object removeMaxElement()
   {
      // make sure there is a maximum element
      if (headNode.next[0] == tailNode)
         // dictionary is empty
         return null;
    
      // search for max element
      SkipNode y = headNode;
      for (int i = levels; i >= 0; i--)
         while (y.next[i] != tailNode)
            y = y.next[i];
   
      // remove element with largest key
      return remove(y.key);
   }

   
   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      ExtendedSkipList2 x = 
              new ExtendedSkipList2(new Integer(1001), 100, (float) 0.5);

      // insert elements
      int n = 20;
      for (int i = 1; i <= n; i++)
         x.put(new Integer(2 * i), new Integer(i));
      System.out.println("The list is");
      System.out.println(x);

      // test removeMinElement and removeMaxElement
      for (int i = 1; i <= n + 1; i += 2)
      {
         System.out.println("removed min element " + x.removeMinElement());
         System.out.println("removed max element " + x.removeMaxElement());
      }
      System.out.println("The list is\n" + x);

      // insert elements and output
      n = 20;
      for (int i = 1; i <= n; i++)
         x.put(new Integer(2 * i), new Integer(i));
      System.out.println("The list is");
      System.out.println(x);
   }
}
