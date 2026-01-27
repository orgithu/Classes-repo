


package misc;

public class DCount1
{
   static int count;

   public static void d(int [] x, int n)
   {
      for (int i = 0; i < n; i += 2)
      {
         count++;      // for the for statement
         x[i] += 2;
         count++;      // for the assignment statement
      }
      count++;         // last time of for

      int i = 1;
      count++;         // assignment statement
      while (i <= n/2)
      {
         count++;      // while conditional
         x[i] += x[i+1];
         count++;      // assignment statement
         i++;
         count++;      // increment i statement
      }
      count++;         // last time of while
   }
   
   public static void main(String [] args)
   {
      int [] y = {1,2,3,4,5,6,7,8,9,10,0,0,0,0,0};
      int n = 10;
      d(y,n);
      for (int i = 0; i < n; i++)
         System.out.print(y[i] + " ");
      System.out.println();
      System.out.println("Step count is " + count);
   }
}
