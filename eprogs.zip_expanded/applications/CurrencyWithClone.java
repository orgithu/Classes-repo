
package applications;

public class CurrencyWithClone extends Currency
{
   // constructors
   public CurrencyWithClone(boolean theSign, long theDollars, byte theCents)
      {super(theSign, theDollars, theCents);}

   public CurrencyWithClone()
      {this(PLUS, 0L, (byte) 0);}

   public CurrencyWithClone(double theValue)
      {setValue(theValue);}

   /** make a clone */
   public Object clone()
      {return new CurrencyWithClone(getSign(), getDollars(), getCents());}

   /** test program */
   public static void main(String [] args)
   {
      // test constructors
      CurrencyWithClone
               i = new CurrencyWithClone(-2.50),
               j = (CurrencyWithClone) i.clone();

      // test toString
      System.out.println("The initial values are " + i + " " + j);
      i.increment(j);
      System.out.println("The final values are " + i + " " + j);
   }
}
