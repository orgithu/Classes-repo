
package applications;

public class CurrencyWithEquals extends Currency
{
   // constructors
   public CurrencyWithEquals(boolean theSign, long theDollars, byte theCents)
      {super(theSign, theDollars, theCents);}

   public CurrencyWithEquals()
      {this(PLUS, 0L, (byte) 0);}

   public CurrencyWithEquals(double theValue)
      {setValue(theValue);}

   /** @return true iff this and theCurrency are equal */
   public boolean equals(CurrencyWithEquals theCurrency)
      {return (getSign() == theCurrency.getSign() &&
               getDollars() == theCurrency.getDollars() &&
               getCents() == theCurrency.getCents());}

   /** test program */
   public static void main(String [] args)
   {
      CurrencyWithEquals
               i = new CurrencyWithEquals(2.50),
               j = new CurrencyWithEquals(2.50);


      System.out.println("The initial values are " + i + " " + j);
      //if (!i.equals(j))
         //System.out.println("Object.equals says they are not equal");
      if (i.equals(j))
         System.out.println("CurrencyWithEquals.equals says they are equal");
   }
}
