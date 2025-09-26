
/** measure height of randomly generated binary search trees */

package misc;

import applications.*;
import dataStructures.*;

public class BinarySearchTreeHeight
{
   public static void main(String [] args)
   {
      // values for n, number of nodes in tree
      int [] size = {100, 500, 1000, 10000, 20000, 50000};
      int numOfTests = 6;
      int repeats = 10;
      int heightSum;
   
      for (int i = 0; i < numOfTests; i++)
      {// measure average height for size[i] elements
         heightSum = 0;
         Integer [] element = new Integer [size[i]];
         // generate size[i] elements to insert
         for (int j = 0; j < size[i]; j++)
            element[j] = new Integer(j);
           
         for (int r = 0; r < repeats; r++)
         {
            // randomly permute the elements
            Permute.permute(element);
         
            // create a binary search tree
            BinarySearchTree tree = new BinarySearchTree();
            // insert elements
            for (int j = 0; j < size[i]; j++)
               tree.put(element[j], element[j]);

            // add in its height
            heightSum += tree.height();
         }
      
         // output average tree height
         System.out.println("Number of nodes is " + size[i] +
              " Average height is " + heightSum / repeats);
      }
   }
}
