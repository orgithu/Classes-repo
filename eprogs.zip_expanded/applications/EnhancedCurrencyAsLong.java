
/** Enhancement of original CurrencyAsLong to include the methods input,
  * subtract, percent, multiply, and divide */

package applications;

import utilities.*;

public class EnhancedCurrencyAsLong
{
   // class constants
   public static final boolean PLUS = true;
   public static final boolean MINUS = false;

   // instance data member
   private long amount;

   // constructors
   /** initialize instance to
     * theSign $ theDollars.theCents */

   public EnhancedCurrencyAsLong(boolean theSign, long theDollars,
                   byte theCents)
   {
      if (theDollars < 0)
         throw new IllegalArgumentException
               ("EnhancedCurrencyAsLong: Dollar value must be >= 0");

      if (theCents < 0 || theCents > 99)
         throw new IllegalArgumentException
               ("EnhancedCurrencyAsLong: Cents must be between 0 and 99");

      amount = theDollars * 100 + theCents;
      if (theSign == MINUS) amount = -amount;
   }

   /** initialize instance to $0.00 */
   public EnhancedCurrencyAsLong()
      {this(PLUS, 0L, (byte) 0);}

   /** initialize with double */
   public EnhancedCurrencyAsLong(double theValue)
      {setValue(theValue);}

   /** initialize with amount value */
   private EnhancedCurrencyAsLong(long theAmount)
      {amount = theAmount;}

   // accessor methods

   /** @return sign */
   public boolean getSign()
   {
      if (amount < 0) return MINUS;
      else return PLUS;
   }

   /** @return dollars */
   public long getDollars()
   {
      if (amount < 0) return - amount / 100;
      else return amount / 100;
   }

   /** @return cents */
   public byte getCents()
   {
      if (amount < 0)
         return (byte) (-amount - getDollars() * 100);
      else return (byte) (amount - getDollars() * 100);
   }

   // mutator methods
   /** Set the sign of amount to theSign.
     * For this to work properly amount must be nonzero. */

   public void setSign(boolean theSign)
   {
      // change the sign as necessary
      if ((amount < 0 && theSign == PLUS) ||
          (amount > 0 && theSign == MINUS))
         amount = -amount;
   }
 
   /** set dollars = theDollars */
   public void setDollars(long theDollars)
   {
      if (theDollars < 0)
         throw new IllegalArgumentException
               ("EnhancedCurrencyAsLong: Dollar value must be >= 0");
      else {boolean sign = getSign();
            byte cents = getCents();
            amount = theDollars * 100 + cents;
            if (sign == MINUS) amount = -amount;
           }
   }

   /** set cents = theCents */
   public void setCents(byte theCents)
   {
      if (theCents < 0 || theCents > 99)
         throw new IllegalArgumentException
               ("EnhancedCurrencyAsLong: Cents must be between 0 and 99");
      else {boolean sign = getSign();
            long dollars = getDollars();
            amount = dollars * 100 + theCents;
            if (sign == MINUS) amount = -amount;
           }
   }

   /** set amount */
   public void setValue(double theValue)
   {
      if (theValue < 0) amount = (long) ((theValue - 0.005) * 100);
      else amount = (long) ((theValue + 0.005) * 100);
   }

   public void setValue(EnhancedCurrencyAsLong x)
      {amount = x.amount;}
   
   /** convert to a string */
   public String toString()
   {
      if (amount >= 0)
         {return "$" + getDollars() + "." + getCents();}
      else {return "-$" + getDollars() + "." + getCents();}
   }

   // arithmetic methods

   /** return this + x */
   public EnhancedCurrencyAsLong add(EnhancedCurrencyAsLong x)
   {
      return new EnhancedCurrencyAsLong(amount + x.amount);
   }
   
   /** return this incremented by x */
   public EnhancedCurrencyAsLong increment(EnhancedCurrencyAsLong x)
   {
      amount += x.amount;
      return this;
   }
   
   // new instance methods
   public void input()
   {
      // define the input stream to be the standard input stream
      MyInputStream keyboard = new MyInputStream();

      // input the amount as a double
      System.out.println("Enter the currency amount as a real number");
      double theValue = keyboard.readDouble();

      // set the value
      setValue(theValue);
   }

   /** return this - x */
   public EnhancedCurrencyAsLong subtract(EnhancedCurrencyAsLong x)
      {return new EnhancedCurrencyAsLong(amount - x.amount);}

   /** return x percent of this */
   public EnhancedCurrencyAsLong percent(float x)
      {return new EnhancedCurrencyAsLong((long) (amount * x / 100));}

   /** return this * x */
   public EnhancedCurrencyAsLong multiply(float x)
      {return new EnhancedCurrencyAsLong((long) (amount * x));}

   /** return this / x */
   public EnhancedCurrencyAsLong divide(float x)
      {return new EnhancedCurrencyAsLong((long) (amount / x));}

   /** test program for new methods */
   public static void main(String [] args)
   {
      EnhancedCurrencyAsLong g = new EnhancedCurrencyAsLong(),
                       h = new EnhancedCurrencyAsLong(PLUS, 3L, (byte) 50),
                       i = new EnhancedCurrencyAsLong(2.50),
                       j = new EnhancedCurrencyAsLong();

      // test input
      j.input();
      System.out.println("The input value is " + j);
      
      // test remaining new methods
      System.out.println(h + " - " + i + " = " + h.subtract(i));
      System.out.println("10 percent of " + i + " is " + i.percent(10.0F));
      System.out.println("2 * " + i + " = " + i.multiply(2.0F));
      System.out.println(i + " / 5 = " + i.divide(5.0F));
   }
}
