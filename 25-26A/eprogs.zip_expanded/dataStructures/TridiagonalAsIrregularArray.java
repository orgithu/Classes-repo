/** tridiagonal matrix represented as an irregular array */

package dataStructures;

import utilities.*;
import wrappers.*;

public class TridiagonalAsIrregularArray
{
   // data members
   int rows;              // matrix dimension
   Object zero;           // zero element
   Object [][] element;   // element array

   // constructor
   public TridiagonalAsIrregularArray(int theRows, Object theZero)
   {
      // validate theRows
      if (theRows < 1)
         throw new IllegalArgumentException
                   ("number of rows must be > 0");
   
      rows = theRows;
      zero = theZero;

      // create and initialize the irregular array
      element = new Object [rows][];
      // do row 1 of the matrix
      element[0] = new Object [2];
      element[0][0] = element[0][1] = zero;
      // do row rows of the matrix
      element[rows - 1] = new Object [2];
      element[rows - 1][0] = element[rows - 1][1] = zero;
      // do the remaining ros
      for (int i = 1; i < rows - 1; i++)
      {
         element[i] = new Object[3];
         element[i][0] = element[i][1] = element[i][2] = zero;
      }

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
      if (i - j >= -1 && i - j <= 1)
      {// in the tridiagonal
         if (i == 1)
            return element[i - 1][j - 1];
         else
            return element[i - 1][j - i + 1];
       }
       else
         // not in the tridiagonal
         return zero;
   }
   
   /** set this(i,j) = newValue
     * @throws IndexOutOfBoundsException when i or j invalid */
   public void set(int i, int j, Object newValue)
   {
      checkIndex(i, j);

      // store newValue
      if (i - j >= -1 && i - j <= 1)
      {// in the tridiagonal
         if (i == 1)
            element[i - 1][j - 1] = newValue;
         else
            element[i - 1][j - i + 1] = newValue;
      }
      else
         // not in the tridiagonal
         if (!((Zero) newValue).equalsZero())
                   throw new IllegalArgumentException
                         ("elements not on tridiagonal must be zero");
   }
   
   
   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      TridiagonalAsIrregularArray x =
                   new TridiagonalAsIrregularArray(10, new MyInteger(0));

      // set a few elements
      x.set(1, 1, new MyInteger(22));
      x.set(1, 2, new MyInteger(12));
      x.set(5, 5, new MyInteger(44));
      x.set(8, 5, new MyInteger(0));
      x.set(7, 8, new MyInteger(55));

      // retrieve a few elements
      System.out.println(x.get(7,8));
      System.out.println(x.get(5,5));
      System.out.println(x.get(1,1));
      System.out.println(x.get(1,2));
      System.out.println(x.get(10,1));
      System.out.println(x.get(1,5));
   }
}
