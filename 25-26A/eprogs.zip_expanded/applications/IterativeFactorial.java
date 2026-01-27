
/** iterative mthod to compute factorial */

package applications;

public class IterativeFactorial
{
   /** @return n! */
   public static int factorial (int n)
   {
      if (n <= 1)
         return 1;
      int fact = 2;
      for (int i = 3; i <= n; i++)
         fact *= i;
      return fact;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      System.out.println("4! = " + factorial(4));
   }
}
