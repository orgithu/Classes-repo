

package applications;

import utilities.*;

public class LengthAsLong
{
   // class constants
   public static final boolean PLUS = true;
   public static final boolean MINUS = false;

   // instance data members
   private long millimeters;

   // constructors
   /** @throws IllegalArgumentException when theMeters < 0
     * or theCentimeters < 0 or theCentimeters > 99
     * or theMilliMeters < 0 or theMillimeters > 9 */
   public LengthAsLong(boolean theSign, long theMeters,
                 byte theCentimeters, byte theMillimeters)
   {
      if (theMeters < 0)
         throw new IllegalArgumentException
               ("Meters value must be >= 0, it is " + theMeters);

      if (theCentimeters < 0 || theCentimeters > 99)
         throw new IllegalArgumentException
               ("Centimeters must be between 0 and 99, it is "
                + theCentimeters);

      if (theMillimeters < 0 || theMillimeters > 9)
         throw new IllegalArgumentException
               ("Millimeters must be between 0 and 9, it is "
                + theMillimeters);
      millimeters = theMeters * 1000 + theCentimeters * 10 + theMillimeters;
      if (theSign == MINUS)
         millimeters = -millimeters;
   }

   /** initialize instance to 0 */
   public LengthAsLong()
      {this(PLUS, 0L, (byte) 0, (byte) 0);}

   /** initialize with length = theValue meters */
   public LengthAsLong(double theValue)
      {setValue(theValue);}

   /** initialize with length = theMillimeters */
   public LengthAsLong(long theMillimeters)
      {millimeters = theMillimeters;}

   // accessor methods
   /** @return sign 
     * For this to work millimeters amount must be nonzero. */
   public boolean getSign()
   {
      if (millimeters < 0)
         return MINUS;
      else
         return PLUS;
   }

   /** @return meters */
   public long getMeters()
   {
      if (millimeters < 0)
         return - millimeters / 1000;
      else
         return millimeters / 1000;
   }

   /** @return centimeters */
   public byte getCentimeters()
   {
      if (millimeters < 0)
         return (byte) ((-millimeters - getMeters() * 1000) / 10);
      else
         return (byte) ((millimeters - getMeters() * 1000) / 10);
   }

   /** @return millimeters */
   public byte getMillimeters()
   {
      if (millimeters < 0)
         return (byte) ((-millimeters - getMeters() * 1000) % 10);
      else
         return (byte) ((millimeters - getMeters() * 1000) % 10);
   }

   // mutator methods
   /** set sign = theSign
     * For this to work millimeters amount must be nonzero. */
   public void setSign(boolean theSign)
   {
      // change the sign as necessary
      if ((millimeters < 0 && theSign == PLUS) ||
          (millimeters > 0 && theSign == MINUS))
         millimeters = -millimeters;
   }
 
   /** set meters = theMeters
     * @throws IllegalArgumentException when theMeters < 0 */
   public void setMeters(long theMeters)
   {
      if (theMeters < 0)
         throw new IllegalArgumentException
               ("Meters value must be >= 0, it is " + theMeters);

      boolean sign = getSign();           // save the sign
      if (millimeters < 0)                // make sign PLUS
         millimeters = -millimeters;
      long three = millimeters % 1000;    // save cm and mm
      millimeters = theMeters * 1000 + three;
      if (sign == MINUS)
         millimeters = -millimeters;
   }

   /** set centimeters = theCentimeters
     * @throws IllegalArgumentException when theCentimeters < 0 or > 99 */
   public void setCentimeters(byte theCentimeters)
   {
      if (theCentimeters < 0 || theCentimeters > 99)
         throw new IllegalArgumentException
               ("Centimeters must be between 0 and 99, it is "
                + theCentimeters);

      boolean sign = getSign();           // save the sign
      if (millimeters < 0)                // make sign PLUS
         millimeters = -millimeters;
      long meters = millimeters / 1000;    // save meters
      long oldMillimeters = millimeters % 10;  // save mm
      millimeters = meters * 1000 + theCentimeters * 10 + oldMillimeters;
      if (sign == MINUS)
         millimeters = -millimeters;
   }

   /** set millimeters = theMillimeters
     * @throws IllegalArgumentException when theMillimeters < 0 or > 9 */
   public void setMillimeters(byte theMillimeters)
   {
      if (theMillimeters < 0 || theMillimeters > 9)
         throw new IllegalArgumentException
               ("Millimeters must be between 0 and 9, it is "
                + theMillimeters);

      boolean sign = getSign();           // save the sign
      if (millimeters < 0)                // make sign PLUS
         millimeters = -millimeters;
      millimeters = (millimeters / 10) * 10 + theMillimeters;
      if (sign == MINUS)
         millimeters = -millimeters;
   }

   /** set length = theValue meters */
   public void setValue(double theValue)
   {
      if (theValue < 0 )
         millimeters = (long) (theValue * 1000 - 0.5);
      else
         millimeters = (long) (theValue * 1000 + 0.5);
   }

   public void setValue(LengthAsLong x)
      {millimeters = x.millimeters;}

   /** input a length */
   public void input()
   {
      // define the input stream to be the standard input stream
      MyInputStream keyboard = new MyInputStream();

      // input the length as a double
      System.out.println("Enter the length in meters as a real number");
      double theValue = keyboard.readDouble();

      // set the value
      setValue(theValue);
   }
   
   /** convert to a string */
   public String toString()
   {
      if (millimeters >= 0)
         {return getMeters() + "." + millimeters % 1000;}
      else
         {return "-" + getMeters() + "." + (-millimeters) % 1000;}
   }

   // arithmetic methods
   /** @return this + x */
   public LengthAsLong add(LengthAsLong x)
      {return new LengthAsLong(millimeters + x.millimeters);}
   
   /** @return this - x */
   public LengthAsLong subtract(LengthAsLong x)
      {return new LengthAsLong(millimeters - x.millimeters);}
   
   
   /** @return this * x */
   public LengthAsLong multiply(int x)
      {return new LengthAsLong(x * millimeters);}
   
   /** @return value of this computed at the rate $x per meter */
   public Currency value(Currency x)
   {
      // convert x to a double
      double a1 = x.getDollars() + ((double) x.getCents()) / 100;
      if (x.getSign() == MINUS)
         a1 = -a1;
      return new Currency(millimeters * a1 / 1000);
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test constructors
      LengthAsLong g = new LengthAsLong(),
             h = new LengthAsLong(PLUS, 3L, (byte) 50, (byte) 4),
             i = new LengthAsLong(-2.507),
             j = new LengthAsLong();

      // test input
      j.input();

      // test toString
      System.out.println("The initial values are " + g +
                         " " + h + " " + i + " " + j);
      System.out.println();

      // test mutators
      // first make g nonzero
      g.setMeters(2);
      g.setSign(MINUS);
      g.setCentimeters((byte) 25);
      g.setMillimeters((byte) 2);
      i.setValue(-6.453);
      System.out.println("New values are " + g + " " + i);
      System.out.println();

      // do some arithmetic
      j = h.add(g);
      System.out.println(h + " + " + g + " = " + j);

      j = i.add(g).subtract(h);
      System.out.println(i + " + " + g + " - " + h +
                         " = " + j);
      System.out.println();

      j = i.multiply(2);
      System.out.println(i + " * 2 = "  + j);
      System.out.println();

      Currency x = new Currency(3.0);
      Currency y = i.value(x);
      System.out.println("Value of " + i + " at $3 per meter is "  + y);
      System.out.println();
   }
}
