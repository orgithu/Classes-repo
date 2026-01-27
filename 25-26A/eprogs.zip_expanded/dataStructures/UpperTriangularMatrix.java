/** upper triangular matrix mapped into a one-dimensional array */

package dataStructures;

import utilities.*;
import wrappers.*;

public class UpperTriangularMatrix
{
   // data members
   int rows;            // matrix dimension
   Object zero;         // zero element
   Object [] element;   // element array

   // constructor
   public UpperTriangularMatrix(int theRows, Object theZero)
   {
      // validate theRows
      if (theRows < 1)
         throw new IllegalArgumentException
               ("number of rows must be > 0");
   
      // create and initialize the matrix
      rows = theRows;
      zero = theZero;
      element = new Object [rows * (rows + 1) / 2];
      for (int i = 0; i <  rows * (rows + 1) / 2; i++)
         element[i] = zero;
   }
   
   /** @throws IndexOutOfBoundsException when i < 1
     * or j < 1 or i > rows or j > rows */
   void checkIndex(int i, int j)
   {
      if (i < 1 || j < 1 || i > rows || j > rows)
         throw new IndexOutOfBoundsException
                   ("i = " + i + " j = " + j +
                    " rows = " + rows + " cols = " + rows);
   }
   
   // methods
   /** @return the element this(i,j)
     * @throws IndexOutOfBoundsException when i or j invalid */
   public Object get(int i, int j)
   {
      checkIndex(i, j);

      // (i,j) in upper triangle iff i <= j
      if (i <= j) return element[j * (j - 1) / 2 + i - 1];
      else return zero;
   }
   
   /** set this(i,j) = newValue
     * @throws IndexOutOfBoundsException when i or j invalid */
   public void set(int i, int j, Object newValue)
   {
      checkIndex(i, j);

      // store newValue
      // (i,j) in upper triangle iff i <= j
      if (i <= j) element[j * (j - 1) / 2 + i - 1] = newValue;
      else if (!((Zero) newValue).equalsZero())
              throw new IllegalArgumentException
                    ("elements not in upper triangle must be zero");
   }
   
   
   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      UpperTriangularMatrix x = new UpperTriangularMatrix(20, new MyInteger(0));

      // set a few elements
      x.set(1, 1, new MyInteger(22));
      x.set(3, 5, new MyInteger(44));
      x.set(5, 8, new MyInteger(0));
      x.set(2, 10, new MyInteger(55));

      // retrieve a few elements
      System.out.println(x.get(2,10));
      System.out.println(x.get(3,5));
      System.out.println(x.get(1,1));
      System.out.println(x.get(14,10));
      System.out.println(x.get(5,8));
   }
}
