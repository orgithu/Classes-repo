
package misc;

public class F
{
   public static int recursiveF(int n)
      {return (n % 2 == 0) ? n / 2 : recursiveF(3 * n + 1);}
   
   public static int iterativeF(int n)
      {return (n % 2 == 0) ? n / 2 : (3 * n + 1) / 2;}
   
   public static void main(String [] args)
   {
      System.out.println("Recursive Computations");
      System.out.println("f(10) = " + recursiveF(10));
      System.out.println("f(5) = " + recursiveF(5));
      System.out.println("f(7) = " + recursiveF(7));

      System.out.println("Iterative Computations");
      System.out.println("f(10) = " + iterativeF(10));
      System.out.println("f(5) = " + iterativeF(5));
      System.out.println("f(7) = " + iterativeF(7));
   }
}
