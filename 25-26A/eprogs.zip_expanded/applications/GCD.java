

package applications;

public class GCD
{
   /** @return the gcd of x and y
     * @throws IllegalArgumentException when x or y < 0 */
   public static int gcd(int x, int y)
   {
      if (x < 0 || y < 0)
         throw new IllegalArgumentException("x and y must be >= 0, "
                                  + "x = " + x + "  y = " + y);

      return rgcd(x, y);
   }
   
   /** @return the gcd of x and y
     * assumes x,y >= 0 */
   private static int rgcd(int x, int y)
      {return (y == 0) ? x : rgcd(y, x % y);}
   
   public static void main(String [] args)
   {
      System.out.println("gcd(0,1) = " + gcd(0,1));
      System.out.println("gcd(20,30) = " + gcd(20,30));
      System.out.println("gcd(112,42) = " + gcd(112,42));
   }
}
