
/** next-fit bin packing */

package applications;

import dataStructures.*;
import utilities.*;

public class NextFit
{
   /** output next fit packing into bins of size c
     * @param s[1:s.length-1] gives object size */
   public static void nextFitPack(int s[], int c)
   {
      int n = s.length - 1;         // number of objects
      FirstFit.Bin [] bin = new FirstFit.Bin [n + 1]; // bins
      ExtendedCWTree2 winTree = new ExtendedCWTree2();
      
      
      // initialize n bins and winner tree
      for (int i = 1; i <= n; i++)
         bin[i] = new FirstFit.Bin(c);  // initial unused capacity
      winTree.initialize(bin);

      int last = 0; // bin used for last object packed
      
      // put objects in bins
      for (int i = 1; i <= n; i++)
      {// put s[i] in a bin
         // see if there is a nonempty bin to the
         // right of bin last that has enough capcity
   
         // set j to next bin with enough capacity
         int j = last + 1;
         if (bin[j].unusedCapacity < s[i])
            // there must be another bin j+1
            if (bin[j + 1].unusedCapacity >= s[i])
               j++;
            else
            {// move up the tree
               // set p to parent of bin j
               int p = winTree.parent(j);
               boolean done = false;
               if (p == n - 1)
               {// special case
                  int q;
                  if (j % 2 == 1)
                     q = j + 1;
                  else
                     q = j + 2;

                  // q must be <= n
                  if (bin[q].unusedCapacity >= s[i])
                  {
                     j = q;
                     done = true;
                  }
               }
               
               if (!done)
               {// move to nearest ancestor of p with enough capacity
                  p /= 2;
                  while (bin[winTree.getWinner(p)].unusedCapacity < s[i])
                     p /= 2;
   
                  // now move to leftmost child of p that has enough capacity
                  p *= 2;  // start search at left child
                  while (p < n)
                  {
                     int winp = winTree.getWinner(p);
                     if (bin[winp].unusedCapacity < s[i])
                        // first bin is in right subtree
                        p++ ;                   
                     p *= 2;   // move to left child
                  }
            
                  p /= 2;  // undo last left child move
                  if (p < n)
                  {// at a tree node
                     j = winTree.getWinner(p);
                     // if j is right child, need to check
                     // bin j-1.  No harm done by checking
                     // bin j-1 even if j is left child.
                     if (j > 1 && bin[j - 1].unusedCapacity >= s[i])
                        j--;
                  }
                  else  // arises when n is odd
                     j = winTree.getWinner(p / 2);
               }
            }  
   
         // see if bin j is nonempty
         if (bin[j].unusedCapacity == c)
         {// empty bin, execute step 2
            // find leftmost bin with enough capacity
            int p = 2;  // start search at left child of root
            while (p < n)
            {
               int winp = winTree.getWinner(p);
               if (bin[winp].unusedCapacity < s[i])
                  // first bin is in right subtree
                  p++ ; 
               p *= 2;   // move to left child
            }
      
            p /= 2;  // undo last left child move
            if (p < n)
            {// at a tree node
               j = winTree.getWinner(p);
               // if j is right child, need to check
               // bin j-1.  No harm done by checking
               // bin j-1 even if j is left child.
               if (j > 1 && bin[j - 1].unusedCapacity >= s[i])
                  j--;
            }
            else  // arises when n is odd
               j = winTree.getWinner(p / 2);
         }
   
         System.out.println("Pack object " + i + " in bin " + j);
         bin[j].unusedCapacity -= s[i]; 
         winTree.rePlay(j);
         last = j;
      }
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // define the input stream to be the standard input stream
      MyInputStream keyboard = new MyInputStream();

      System.out.println("Enter number of objects and bin capacity");
      int n = keyboard.readInteger();   // number of objects
      int c = keyboard.readInteger();   // bin capacity
      if (n < 2)
      {
         System.out.println("Too few objects");
         System.exit(1);
      }

      // input the object sizes s[1:n]
      int [] s = new int [n + 1];
      for (int i = 1; i <= n; i++)
      {
         System.out.println("Enter space requirement of object " + i);
         s[i] = keyboard.readInteger();
         if (s[i] > c)
         {
           System.out.println("Object too large to fit in a bin");
           System.exit(1);
         }
      }

      // output the packing
      nextFitPack(s, c);
   }
}
