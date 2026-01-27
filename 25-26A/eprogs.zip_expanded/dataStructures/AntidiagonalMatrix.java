
/** antidiagonal matrix mapped into a one-dimensional array */

package dataStructures;

import utilities.*;
import wrappers.*;

public class AntidiagonalMatrix
{
   // data members
   int rows;            // matrix dimension
   int rowsPlus1;       // rows + 1
   Object zero;         // zero element
   Object [] element;   // element array

   // constructor
   public AntidiagonalMatrix(int theRows, Object theZero)
   {
      // validate theRows
      if (theRows < 1)
         throw new IllegalArgumentException
               ("number of rows must be > 0");
   
      // create and initialize the matrix
      rows = theRows;
      zero = theZero;
      rowsPlus1 = rows + 1;
      element = new Object [rows];
      for (int i = 0; i < rows; i++)
         element[i] = zero;
   }
   
   // methods
   /** @throws IndexOutOfBoundsException when i < 1
     * or j < 1 or i > rows or j > rows */
   void checkIndex(int i, int j)
   {
      if (i < 1 || j < 1 || i > rows || j > rows)
         throw new IndexOutOfBoundsException
                   ("i = " + i + " j = " + j +
                    " rows = " + rows + " cols = " + rows);
   }

   /** @return the element this(i,j)
     * @throws IndexOutOfBoundsException when i or j invalid */
   public Object get(int i, int j)
   {
      checkIndex(i, j);

      // determine element to return
      if (i + j == rowsPlus1)
         return element[i - 1];  // antidiagonal element
      else return zero;          // nonantidiagonal element
   }
   
   /** set this(i,j) = newValue
     * @throws IndexOutOfBoundsException when i or j invalid */
   public void set(int i, int j, Object newValue)
   {
      checkIndex(i, j);

      if (i + j == rowsPlus1)
         // save the antidiagonal element
         element[i - 1] = newValue;
      else
         // newValue should be zero
         if (!((Zero) newValue).equalsZero())
            throw new IllegalArgumentException
                  ("nonantidiagonal elements must be zero");
   }
   
   
   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      AntidiagonalMatrix x = new AntidiagonalMatrix(10, new MyInteger(0));

      // set a few elements
      x.set(1, 10, new MyInteger(22));
      x.set(5, 6, new MyInteger(44));
      x.set(8, 5, new MyInteger(0));

      // retrieve a few elements
      System.out.println(x.get(5, 6));
      System.out.println(x.get(1, 1));
      System.out.println(x.get(1, 10));
   }
}
