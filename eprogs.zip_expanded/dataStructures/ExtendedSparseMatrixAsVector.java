/** enhanced input method for SparseMatrixAsVector */

package dataStructures;

import java.lang.reflect.*;
import wrappers.*;
import exceptions.*;
import utilities.*;

public class ExtendedSparseMatrixAsVector extends SparseMatrixAsVector
{
   // constructor
   public ExtendedSparseMatrixAsVector(int theRows, int theColumns,
                               int estimatedMaxSize, Object theZero)
      {super(theRows, theColumns, estimatedMaxSize, theZero);}

   /** input a sparse matrix into this from the given input stream */
   public static ExtendedSparseMatrixAsVector inputWithValidate(Object theZero,
                                            MyInputStream stream)
   {
      Method inputMethod;
      Object [] inputMethodArgs = {stream};
      Class [] parameterTypes = {MyInputStream.class};
      try
      {
         // get the proper method to be used to read in the values
         inputMethod = theZero.getClass().
                          getMethod("input", parameterTypes);
      }
      catch (Exception e)
      {
         System.out.println(e); 
         throw new IllegalArgumentException
          ("ExtendedSparseMatrixAsVector.input:  matrix element type "
           + "must have the static method input() defined");
      }

      // input matrix characteristics
      System.out.println("Enter number of rows, columns, " +
                         "and nonzero terms");
      int theRows = stream.readInteger();
      int theCols = stream.readInteger();
      int theSize = stream.readInteger();

      // constructor validates theRows, theCols, and theSize
      ExtendedSparseMatrixAsVector x =
                    new ExtendedSparseMatrixAsVector(theRows,
                                theCols, theSize, theZero);
   
      // input the nonzero terms
      // oldIndex = row * theCols + col of last term
      int oldIndex = 0;
      for (int i = 0; i < theSize; i++)
      {
         System.out.println("Enter row and column of term " + (i+1));
         MatrixTerm newTerm = new MatrixTerm();
         newTerm.row = stream.readInteger();
         newTerm.col = stream.readInteger();
         try 
            {newTerm.value = inputMethod.invoke(null, inputMethodArgs);}
         catch (Exception e)
         {
            System.out.println(e); 
            throw new IllegalArgumentException
             ("ExtendedSparseMatrixAsVector.input:  matrix element type "
              + "must have the static method input() defined");
         }

         // validate input
         if (newTerm.row < 1 || newTerm.row > x.rows ||
             newTerm.col < 1 || newTerm.col > x.cols)
            throw new MyInputException("ExtendedSparseMatrixAsVector: "
                      + ".input: illegal row and/or column index");

         if (newTerm.value == x.zero)
            throw new MyInputException("ExtendedSparseMatrixAsVector: "
                      + ".input: term must have nonzero value");

         int newIndex = newTerm.row * x.cols + newTerm.col;
         if (newIndex <= oldIndex)
            throw new MyInputException("ExtendedSparseMatrixAsVector: "
                      + ".input: terms must be in row-major order");
         oldIndex = newIndex;

         x.terms.insertElementAt(newTerm, i);  // put into the Vector
      }
      return x;
   }

   /** test program */
   public static void main(String [] args)
   {
      MyInteger myZero = new MyInteger(0);
      ExtendedSparseMatrixAsVector a, b;

      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();

      // test input and output
      a = ExtendedSparseMatrixAsVector.inputWithValidate(myZero, keyboard);
      System.out.println("The matrix a is");
      System.out.println(a);
      System.out.println();
      b = ExtendedSparseMatrixAsVector.inputWithValidate(myZero, keyboard);
      System.out.println("The matrix b is");
      System.out.println(b);
      System.out.println();
   }
}
