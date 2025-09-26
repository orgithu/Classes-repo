/** average time for union-find problem
  * average over at least 10 sequences for each n
  * each sequence has 5*n combine operations */

package misc;

import java.util.*;
import applications.*;

public class TimeUnionFind
{
   public static void main(String [] args)
   {
      int [] n = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
                  // number elements
      int numberOfNs = 8;
      int minSequences = 10;  // average over at least this many sequences
      int sequenceFactor = 5; // sequence length = sequenceFactor * n
      for (int i = 0; i < numberOfNs; i++)
      {// find average time for n[i] elements
         int counter = 0;  // number of sequences done so far
         long startTime = System.currentTimeMillis();
         int numberOfCombines = 5 * n[i];
         Random r = new Random();

         // use at least minSequences sequences of 5*n[i] combine operations
         // also make sure at least 1 second has elapsed
         while (counter < minSequences ||
                System.currentTimeMillis() - startTime < 1000)
         {
            // initialize for n[i] elements
            // FastUnionFind s = new FastUnionFind(n[i]);
            UnionFindWithTrees s = new UnionFindWithTrees(n[i]);

            // do the combine operations
            for (int q = 0; q <= numberOfCombines; q++)
            {
               // select two elements at random
               int u = r.nextInt(n[i]) + 1;             
               int v = r.nextInt(n[i]) + 1;             

               // do two find operations
               int uSet = s.find(u);
               int vSet = s.find(v);

               // do a union only if uSet != vSet
               if (uSet != vSet)
                  s.union(uSet, vSet);
            }
            counter++;
         }

         long timePerSequence = (System.currentTimeMillis() - startTime)
                                / counter;
         
         System.out.println("n = " + n[i] + "   number of sequences = "
                            + counter + "   time/sequence = " +
                            timePerSequence + " ms"); 
      }
   }
}
