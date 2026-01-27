
/** extension of MyInteger to include the method input */

package wrappers;

import utilities.*;
import exceptions.*;

public class ExtendedMyInteger extends MyInteger
{
   // same constructor methods as MyInteger
   public ExtendedMyInteger(int theValue)
      {super(theValue);}

   public ExtendedMyInteger()
      {super();}

   /** MyInteger initialized to s */
   public ExtendedMyInteger(String s)
          throws NumberFormatException
      {super(s);}

   /** try 3 times to input the value */
   public void input()
   {
      int NumOfAttempts = 3;
   
      // define the input stream to be the standard input stream
      MyInputStream keyboard = new MyInputStream();

      // try at most NumOfAttempts times
      for (int i = 0; i < NumOfAttempts; i++)
      {
         try
         {
            System.out.println("Enter an integer value");
            int x = keyboard.readInteger();
            setValue(x);
            return;
         }
         catch (Exception e)
         {
             System.out.println("Bad input, try again");
         }
      }

      // did not get proper value
      System.out.println("Sorry, you get only " + NumOfAttempts + " chances");
      throw new MyInputException("ExtendedMyInteger.input: " +
                                 "failed to input an integer");
   }

   /** test program */
   public static void main(String [] args)
   {
      ExtendedMyInteger a = new ExtendedMyInteger();
      a.input();
      System.out.println("The number is " + a);
   }
}
