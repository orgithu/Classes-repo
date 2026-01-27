/** extensions to Matrix, methods decrement, input,
  * multiplyByConstant, and divideByConstant added */

package dataStructures;

import java.lang.reflect.*;
import utilities.*;
import exceptions.*;
import wrappers.*;

public class ExtendedMatrix extends Matrix
{
   // constructor
   public ExtendedMatrix(int theRows, int theColumns)
      {super(theRows, theColumns);}

   
   /** decrement all elements of this by x */
   public void decrement(Object x)
   {
      for (int i = 0; i < rows * cols; i++)
          ((Computable) element[i]).decrement(x);
   }
   
   /** multiply all elements of this by x */
   public void multiplyByConstant(Object x)
   {
      for (int i = 0; i < rows * cols; i++)
          element[i] = ((Computable) element[i]).multiply(x);
   }
   
   /** divide all elements of this by x */
   public void divideByConstant(Object x)
   {
      for (int i = 0; i < rows * cols; i++)
          element[i] = ((Computable) element[i]).divide(x);
   }

   /** input a matrix from the given input stream */
   public void input(Object theZero, MyInputStream inStream)
   {
      Method inputMethod;
      Object [] inputMethodArgs = {inStream};
      Class [] parameterTypes = {inStream.getClass()};
      try
      {
         // get the proper method to be used to read in the values
         inputMethod = theZero.getClass().
                          getMethod("input", parameterTypes);
   
         // input matrix characteristics
         System.out.println("Enter number of rows and columns");
         rows = inStream.readInteger();
         cols = inStream.readInteger();
         // validate rows and cols
         if (rows < 0 || cols < 0)
            throw new MyInputException
                  ("number of rows and columns must be >= 0");
         if ((rows == 0 && cols != 0) ||
             (rows != 0 && cols == 0))
            throw new MyInputException
                  ("both the number of rows and columns must equal " +
                   "zero or both must be > 0");
   
         // create the element array
         element = new Object [rows * cols];
      
         // input the elements
         for (int i = 1; i <= rows; i++)
            for (int j = 1; j <= cols; j++)
               set(i, j, inputMethod.invoke(null, inputMethodArgs));
      }
      catch (Exception e)
      {
         System.out.println(e);
         throw new IllegalArgumentException
                   ("must have the static method input() defined");
      }
   }

   /** test program */
   public static void main(String [] args)
   {
      // establish input stream
      MyInputStream keyboard = new MyInputStream();

      try {
         ExtendedMatrix x = new ExtendedMatrix(1, 1);
         ExtendedMatrix y = new ExtendedMatrix(1, 1);
         ExtendedMatrix z = new ExtendedMatrix(1, 1);

         // test input
         x.input(new MyInteger(0), keyboard);
         System.out.println("Matrix x is");
         System.out.println(x);
         y.input(new MyInteger(0), keyboard);
         System.out.println("Matrix y is");
         System.out.println(y);
         z.input(new MyInteger(0), keyboard);
         System.out.println("Matrix z is");
         System.out.println(z);

         // test decrement
         y.decrement(new MyInteger(2));
         System.out.println("y decremented by 2 is");
         System.out.println(y);

         // test multiplyByConstant
         y.multiplyByConstant(new MyInteger(2));
         System.out.println("y multiplied by 2 is");
         System.out.println(y);

         // test divideByConstant
         y.divideByConstant(new MyInteger(2));
         System.out.println("y divided by 2 is");
         System.out.println(y);
      }
      catch (Exception e)
      {System.out.println(e);}
   }
}
