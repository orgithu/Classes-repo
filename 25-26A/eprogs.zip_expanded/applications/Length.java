
package applications;

import utilities.*;

public class Length
{
   // class constants
   public static final boolean PLUS = true;
   public static final boolean MINUS = false;

   // instance data members
   private boolean sign;
   private long meters;
   private byte centimeters;
   private byte millimeters;

   // constructors
   /** @throws IllegalArgumentException when theMeters < 0
     * or theCentimeters < 0 or theCentimeters > 99
     * or theMilliMeters < 0 or theMillimeters > 9 */
   public Length(boolean theSign, long theMeters,
                 byte theCentimeters, byte theMillimeters)
   {
      sign = theSign;

      if (theMeters < 0)
         throw new IllegalArgumentException
               ("Meters value must be >= 0, it is " + theMeters);
      meters = theMeters;

      if (theCentimeters < 0 || theCentimeters > 99)
         throw new IllegalArgumentException
               ("Centimeters must be between 0 and 99, it is "
                + theCentimeters);
      centimeters = theCentimeters;

      if (theMillimeters < 0 || theMillimeters > 9)
         throw new IllegalArgumentException
               ("Millimeters must be between 0 and 9, it is "
                + theMillimeters);
      millimeters = theMillimeters;
   }

   /** initialize instance to 0 */
   public Length()
      {this(PLUS, 0L, (byte) 0, (byte) 0);}

   /** initialize with length = theValue meters */
   public Length(double theValue)
      {setValue(theValue);}

   // accessor methods
   /** @return sign */
   public boolean getSign()
      {return sign;}

   /** @return meters */
   public long getMeters()
      {return meters;}

   /** @return centimeters */
   public byte getCentimeters()
      {return centimeters;}

   /** @return millimeters */
   public byte getMillimeters()
      {return millimeters;}

   // mutator methods
   /** set sign = theSign */
   public void setSign(boolean theSign)
      {sign = theSign;}
 
   /** set meters = theMeters
     * @throws IllegalArgumentException when theMeters < 0 */
   public void setMeters(long theMeters)
   {
      if (theMeters < 0)
         throw new IllegalArgumentException
               ("Meters value must be >= 0, it is " + theMeters);
      meters = theMeters;
   }

   /** set centimeters = theCentimeters
     * @throws IllegalArgumentException when theCentimeters < 0 or > 99 */
   public void setCentimeters(byte theCentimeters)
   {
      if (theCentimeters < 0 || theCentimeters > 99)
         throw new IllegalArgumentException
               ("Centimeters must be between 0 and 99, it is "
                + theCentimeters);
      centimeters = theCentimeters;
   }

   /** set millimeters = theMillimeters
     * @throws IllegalArgumentException when theMillimeters < 0 or > 9 */
   public void setMillimeters(byte theMillimeters)
   {
      if (theMillimeters < 0 || theMillimeters > 9)
         throw new IllegalArgumentException
               ("Millimeters must be between 0 and 9, it is "
                + theMillimeters);
      millimeters = theMillimeters;
   }

   /** set length = theValue meters */
   public void setValue(double theValue)
   {
      if (theValue < 0)
      {
         sign = MINUS;
         theValue = -theValue;
      }
      else
         sign = PLUS;

      meters = (long) (theValue + 0.0005); // extract integral part

      // get three decimal digits
      int three = (int) ((theValue + 0.0005 - meters) * 1000);

      centimeters = (byte) (three / 10);
      millimeters = (byte) (three - centimeters * 10);
   }

   public void setValue(Length x)
   {
      sign = x.sign;
      meters = x.meters;
      centimeters = x.centimeters;
      millimeters = x.millimeters;
   }

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
      if (sign == PLUS)
         {return meters + "." + centimeters + millimeters;}
      else
         {return "-" + meters + "." + centimeters + millimeters;}
   }

   // arithmetic methods
   /** @return this + x */
   public Length add(Length x)
   {
      long a1, a2, a3;
      // convert this to a long
      a1 = meters * 1000 + centimeters * 10 + millimeters;
      if (sign == MINUS)
         a1 = -a1;
      
      // convert x to a long
      a2 = x.meters * 1000 + x.centimeters * 10 + x.millimeters;
      if (x.sign == MINUS)
         a2 = -a2;
      
      a3 = a1 + a2;
       
      // convert result to Length object
      Length answer = new Length();
      if (a3 < 0)
      {
         answer.sign = MINUS;
         a3 = -a3;
      }
      else
         answer.sign = PLUS;
      answer.meters = a3 / 1000;
      a3 -= answer.meters * 1000;  // subtract the meters
      answer.centimeters = (byte) (a3 / 10);
      answer.millimeters = (byte) (a3 - answer.centimeters * 10);
      
      return answer;
   }
   
   /** @return this - x */
   public Length subtract(Length x)
   {
      long a1, a2, a3;
      // convert this to a long
      a1 = meters * 1000 + centimeters * 10 + millimeters;
      if (sign == MINUS)
         a1 = -a1;
      
      // convert x to a long
      a2 = x.meters * 1000 + x.centimeters * 10 + x.millimeters;
      if (x.sign == MINUS)
         a2 = -a2;
      
      a3 = a1 - a2;
       
      // convert result to Length object
      Length answer = new Length();
      if (a3 < 0)
      {
         answer.sign = MINUS;
         a3 = -a3;
      }
      else
         answer.sign = PLUS;
      answer.meters = a3 / 1000;
      a3 -= answer.meters * 1000;  // subtract the meters
      answer.centimeters = (byte) (a3 / 10);
      answer.millimeters = (byte) (a3 - answer.centimeters * 10);
      
      return answer;
   }
   
   
   /** @return this * x */
   public Length multiply(int x)
   {
      // convert this to a long
      long a1 = meters * 1000 + centimeters * 10 + millimeters;
      if (sign == MINUS)
         a1 = -a1;
      
      long a2 = a1 * x;
       
      // convert result to Length object
      Length answer = new Length();
      if (a2 < 0)
      {
         answer.sign = MINUS;
         a2 = -a2;
      }
      else
         answer.sign = PLUS;
      answer.meters = a2 / 1000;
      a2 -= answer.meters * 1000;  // subtract the meters
      answer.centimeters = (byte) (a2 / 10);
      answer.millimeters = (byte) (a2 - answer.centimeters * 10);
      
      return answer;
   }
   
   /** @return value of this computed at the rate $x per meter */
   public Currency value(Currency x)
   {
      // convert this to a double
      double a1 = meters + ((double) centimeters) / 100
                         + ((double) millimeters) / 1000;
      if (sign == MINUS)
         a1 = -a1;
      
      // convert x to a double
      double a2 = x.getDollars() + ((double) x.getCents()) / 100;
      if (x.getSign() == MINUS)
         a2 = -a2;

      return new Currency(a1 * a2);
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // test constructors
      Length g = new Length(),
             h = new Length(PLUS, 3L, (byte) 50, (byte) 4),
             i = new Length(-2.507),
             j = new Length();

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
