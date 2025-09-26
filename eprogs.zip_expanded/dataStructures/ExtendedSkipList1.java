/** SkipList extended to include methods to remove the elements with
  * smallest and largest keys */

package dataStructures;

public class ExtendedSkipList1 extends SkipList
{
   // constructor
   public ExtendedSkipList1(Comparable largeKey, int maxElements, float theProb)
      {super(largeKey, maxElements, theProb);}

   /** @return element with smallest key and remove it
     * @return null if no element */
   public Object removeMinElement()
   {
      // make sure there is a min element
      if (headNode.next[0] == tailNode)
         // dictionary is empty
         return null;
   
      // remove the minimum element
      return remove(headNode.next[0].key); // min key
   }
   
   /** @return element with largest key and remove it
     * @return null if no element */
   public Object removeMaxElement()
   {
      // make sure there is a maximum element
      if (headNode.next[0] == tailNode)
         // dictionary is empty
         return null;
    
      // search for tail key
      search(tailKey);
      // remove element with largest key
      return remove(last[0].key);
   }

   
   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      ExtendedSkipList1 x = 
              new ExtendedSkipList1(new Integer(1001), 100, (float) 0.5);

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
