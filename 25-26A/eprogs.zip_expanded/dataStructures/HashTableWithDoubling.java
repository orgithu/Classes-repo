/** extension of HashTable to double table size as needed */

package dataStructures;

import java.util.*;

public class HashTableWithDoubling extends HashTable
{
   // double hash table capacity when loading factor
   // exceeds threshold
   private double threshold;

   public HashTableWithDoubling(int theDivisor)
   {
      super(theDivisor);
      threshold = 0.75;
   }

   public HashTableWithDoubling(double theThreshold)
   {
      super(11);
      threshold = theThreshold;
   }

   /** insert an element with the specified key
     * overwrite old element if there is already an
     * element with the given key
     * double table size when loading factor exceeds threshold
     * overrides hashTable.put
     * @return old element (if any) with key theKey */
   public Object put(Object theKey, Object theElement)
   {
      if (((double)(size + 1)) / table.length > threshold)
      {// double table capacity, we assume theKey is not a duplicate
       // doesn't hurt even if theKey is a duplicate
         HashEntry [] oldTable = table;
         // approximately double capacity, keeping length odd 
         table = new HashEntry [oldTable.length * 2 + 1];
         divisor = table.length;
         size = 0;

         // insert old elements into new table
         for (int i = 0; i < oldTable.length; i++)
            if (oldTable[i] != null)
               super.put(oldTable[i].key, oldTable[i].element);
      }

      // insert new item
      return super.put(theKey, theElement);
   }
   
   /** test method */
   public static void main (String [] args)
   {
      HashTableWithDoubling h = new HashTableWithDoubling(1);
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
      try {h.put(new Integer(99), new Integer(99));}
      catch (Exception e)
      {System.out.println(" No memory for 99");}
      System.out.println();
      // update element
      h.put(new Integer(7), new Integer(29));
      h.output();
   }
}
