
/** hash tables using chaining and division, sorted chains with
  * tail nodes are used */

package dataStructures;

public class HashChainsWithTails
{
   // data members
   private int divisor;               // hash function divisor
   private SortedChainWithTail [] table;      // hash table array
   private int size;                  // number of elements in table

   // constructor
   public HashChainsWithTails(int theDivisor)
   {
      divisor = theDivisor;
   
      // allocate hash table array
      table = new SortedChainWithTail [divisor];

      // allocate the chains
      for (int i = 0; i < divisor; i++)
         table[i] = new SortedChainWithTail();
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
      {return table[theKey.hashCode() % divisor].get(theKey);}
   
   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key
     * @return old element (if any) with key = theKey */
   public Object put(Object theKey, Object theElement)
   {
      int b = theKey.hashCode() % divisor;  // home bucket

      // insert
      Object elementToReturn = table[b].put(theKey, theElement);
      if (elementToReturn == null)
         // wasn't a duplicate
         size++;
      return elementToReturn;
   }

   /** @return matching element and remove it
     * @return null if no matching element */
   public Object remove(Object theKey)
   {
      Object elementToReturn =
             table[theKey.hashCode() % divisor].remove(theKey);
      if (elementToReturn != null)
         size--;
      return elementToReturn;
   }
   
   /** output the hash table */
   public void output()
   {
      for (int i = 0; i < divisor; i++)
         System.out.println(table[i]);
      System.out.println("Table size is " + size);
   }
   
   /** test method */
   public static void main (String [] args)
   {
      HashChainsWithTails h = new HashChainsWithTails(11);
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
