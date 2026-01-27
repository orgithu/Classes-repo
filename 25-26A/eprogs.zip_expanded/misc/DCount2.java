


package misc;

public class DCount2
{
   static int count;

   public static void d(int [] x, int n)
   {
      for (int i = 0; i < n; i += 2)
         count +=2;

      count += 2;

      int i = 1;
      while (i <= n/2)
      {
         count += 3;
         i++;
      }

      count++;         // last time of while
   }
   
   public static void main(String [] args)
   {
      int [] y = {1,2,3,4,5,6,7,8,9,10,0,0,0,0,0};
      int n = 10;
      d(y,n);
      System.out.println("Step count is " + count);
   }
}
