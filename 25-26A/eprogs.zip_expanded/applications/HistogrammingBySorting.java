/** histogramming by sorting */

package applications;

import wrappers.*;
import utilities.*;

public class HistogrammingBySorting
{
   public static void main(String [] args)
   {
      // define input stream
      MyInputStream keyboard = new MyInputStream();

      System.out.println("Enter number of elements");
      int n = keyboard.readInteger();
   
      // create array to hold the keys
      MyInteger [] key = new MyInteger [n + 1]; 

      // input the keys into the array key[1:n]
      for (int i = 1; i <= n; i++)
      {
         System.out.println("Enter element " + i);
         key[i] = MyInteger.input(keyboard);
      }
   
      // sort the keys
      HeapSort.heapSort(key);  
   
      // output distinct keys and their counts
      System.out.println("Distinct elements and frequencies are");
      int c = 1;                // cursor into keys
      while (c <= n)
      {// new key at key[c]
         // scan over keys equal to key[c]
         int j = c + 1;
         while (j <= n && key[j].equals(key[c]))
            j++;
   
         // number of elements equal to key[c] is j - c
         System.out.print(key[c] + " " + (j - c) + "    ");
   
         // set c to next new key
         c = j;
      }
      System.out.println();
   }
}
