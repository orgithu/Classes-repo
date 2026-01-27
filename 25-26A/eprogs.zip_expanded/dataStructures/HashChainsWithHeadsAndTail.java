/** hash tables using chaining and division, developed from basics
  * using chains with head nodes and a common tail node */

package dataStructures;

public class HashChainsWithHeadsAndTail
{
   // top-level nested class
   private static class HashNode
   {
      // data members
      Comparable key;
      Object element;
      HashNode next;

      // constructors
      private HashNode() {}
     
      private HashNode(Comparable theKey, Object theElement)
      {
         key = theKey;
         element = theElement;
      }
   }

   // data members of HashChainsWithHeadsAndTailTail
   private int divisor;               // hash function divisor
   private HashNode [] table;         // hash table array
   private HashNode tailNode;         // common tail node
   private int size;                  // number of elements in table

   // constructor
   public HashChainsWithHeadsAndTail(int theDivisor)
   {
      divisor = theDivisor;
      tailNode = new HashNode();
      // size has default initial value 0
   
      // allocate hash table array
      table = new HashNode [divisor];

      // every chain has a head node and a tail node on it
      for (int i = 0; i < divisor; i++)
      {
         table[i] = new HashNode();   // head node for the chain
         table[i].next = tailNode;    // common tail node
      }
   }
   
   // instance methods
   /** @return true iff the hash table is empty */
   public boolean isEmpty()
      {return size == 0;}

   /** @return current number of elements in the hash table */
   public int size()
      {return size;}

   /** @return element with specified key
     * @return null if no matching element */
   public Object get(Object theKey)
   {
      int b = theKey.hashCode() % divisor;   // home bucket
   
      // search home bucket chain
      // first put theKey into the tailnode
      tailNode.key = (Comparable) theKey;
      HashNode currentNode = table[b].next;
      while (currentNode.key.compareTo(theKey) < 0)
         currentNode = currentNode.next;

      // did we find a matching element?
      if (!currentNode.key.equals(theKey) || currentNode == tailNode)
         // no
         return null;
      else
         // yes
         return currentNode.element;
   }
   
   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key 
     * @return old element (if any) with key = theKey */
   public Object put(Object theKey, Object theElement)
   {
      int b = theKey.hashCode() % divisor;  // home bucket

      // search home bucket chain
      // first put theKey into the tailnode
      tailNode.key = (Comparable) theKey;
      HashNode currentNode = table[b].next,
               previousNode = table[b];

      while (currentNode.key.compareTo(theKey) < 0)
      {// move to next node on home bucket chain
         previousNode = currentNode;
         currentNode = currentNode.next;
      }
   
      // check if element with theKey already in table
      if (currentNode != tailNode && currentNode.key.equals(theKey))
      {// update the element
         Object elementToReturn = currentNode.element;
         currentNode.element = theElement;
         return elementToReturn;
      }
   
      // insert theElement after previousNode and before currentNode
      HashNode newNode = new HashNode((Comparable) theKey, theElement);
      newNode.next = currentNode;
      previousNode.next = newNode;

      size++;
      return null;
   }

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      int b = theKey.hashCode() % divisor;   // home bucket

      // search the home bucket for matching element
      // using a trailing pointer previousNode
      // first put theKey into the tailnode
      tailNode.key = (Comparable) theKey;
      HashNode currentNode = table[b].next,
               previousNode = table[b];

      while (currentNode.key.compareTo(theKey) < 0)
      {// move to next node on home bucket chain
         previousNode = currentNode;
         currentNode = currentNode.next;
      }
   
      // check if we have a match
      if (!currentNode.key.equals(theKey) || currentNode == tailNode)
         // no element with theKey in table
         return null;
   
      // we have a match
      previousNode.next = currentNode.next;
      size--;
      return currentNode.element;
   }
   
   /** output the hash table */
   public void output()
   {
      for (int i = 0; i < divisor; i++)
      {
         // output the chain table[i]
         HashNode currentNode = table[i].next;
         System.out.print("[");
         while(currentNode != tailNode)
         {
            if (currentNode != table[i].next)
               // not first element
               System.out.print(", ");
            System.out.print(currentNode.element.toString());
            currentNode = currentNode.next;
         }
         System.out.println("]");
     }

      System.out.println("Table size is " + size);
   }
   
   /** test method */
   public static void main(String [] args)
   {
      HashChainsWithHeadsAndTail h = new HashChainsWithHeadsAndTail(11);
      h.put(new Integer(80), new Integer(80));
      h.put(new Integer(40), new Integer(40));
      h.put(new Integer(65), new Integer(65));
      h.output();
      System.out.println();
      h.put(new Integer(58), new Integer(58));
      h.put(new Integer(24), new Integer(24));
      h.output();
      System.out.println();
      h.put(new Integer(2), new Integer(2));
      h.put(new Integer(13), new Integer(13));
      h.put(new Integer(46), new Integer(46));
      h.put(new Integer(16), new Integer(16));
      h.put(new Integer(7), new Integer(7));
      h.put(new Integer(21), new Integer(21));
      h.output();
      System.out.println();
      h.put(new Integer(99), new Integer(99));

      // update element
      h.put(new Integer(7), new Integer(29));
      h.output();
   }
}
