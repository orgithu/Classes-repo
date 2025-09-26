
/** generate all subsets of n elements */

package applications;

public class AllSubsets
{
   // class data member
   static int [] x;  // subset vector
   
   /** define array x and invoke private method subsets */
   public static void allSubsets(int n)
   {
      x = new int [n + 1];
      // output all subsets of x[1:n]
      subsets(1);
   }

   /** output x[1:i-1] followed by all subsets of x[i:x.length-1] */
   private static void subsets(int i)
   {
      if (i == x.length - 1)
      {// x[x.length - 1] can be 0 or 1
         // output subset without last element
         x[x.length - 1] = 0;
         for (int j = 1; j <= x.length - 1; j++)
            System.out.print(x[j] + " ");
         System.out.println();
         
         // output subset with last element
         x[x.length - 1] = 1;
         for (int j = 1; j <= x.length - 1; j++)
            System.out.print(x[j] + " ");
         System.out.println();
         return;
      }
                   
      // leave element i out
      x[i] = 0;
      // generate all subsets with i excluded
      subsets(i + 1);
                   
       // put element i into subset
       x[i] = 1;
       // generate all subsets with i included
       subsets(i + 1);
   }
    
   /** test program */
   public static void main(String [] args)
   {
      allSubsets(4);
   }
}
