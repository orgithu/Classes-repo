/** randomly permute an array of elements */


package applications;

import java.util.*;
import utilities.*;

public class Permute
{
   /** permute a[0:a.length-1] */
   public static void permute(Object [] a)
   {
      Random rand = new Random();
      for (int i = a.length - 1; i > 0; i--)
         // swap a[i] with a randomly selected element from a[0:i]
         MyMath.swap(a, i, Math.abs(rand.nextInt()) % (i + 1));
   }

   /** test program */
   public static void main(String [] args)
   {
      int n = 10;
      Integer [] z = new Integer [n];

      // initialize z
      for (int i = 0; i < n; i++)
         z[i] = new Integer(i);

      System.out.print("The permutation is ");

      permute(z);

      // output z
      for (int i = 0; i < n; i++)
         System.out.print(z[i] + " ");
      System.out.println();
   }
}
