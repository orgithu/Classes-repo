/** symmetricMatrix matrix mapped into a one-dimensional array, only the
  * lower triangle is stored */

package dataStructures;

import utilities.*;
import wrappers.*;

public class LowerSymmetricMatrix
{
   // data members
   int rows;            // matrix dimension
   Object zero;         // zero element
   Object [] element;   // element array

   // constructor
   public LowerSymmetricMatrix(int theRows, Object theZero)
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

      if (i >= j)
         return element[i * (i - 1) / 2 + j - 1];
      else
         // interchange roles of i and j
         return element[j * (j - 1) / 2 + i - 1];
   }
   
   /** set this(i,j) = newValue
     * @throws IndexOutOfBoundsException when i or j invalid */
   public void set(int i, int j, Object newValue)
   {
      checkIndex(i, j);

      // store newValue
      if (i >= j)
         element[i * (i - 1) / 2 + j - 1] = newValue;
      else
         // interchange roles of i and j
         element[j * (j - 1) / 2 + i - 1] = newValue;
   }
   
   
   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      LowerSymmetricMatrix x = new LowerSymmetricMatrix(20, new MyInteger(0));

      // set a few elements
      x.set(1, 1, new MyInteger(22));
      x.set(5, 3, new MyInteger(44));
      x.set(3, 5, new MyInteger(11));
      x.set(8, 5, new MyInteger(15));
      x.set(10, 2, new MyInteger(55));

      // retrieve a few elements
      System.out.println(x.get(10, 2));
      System.out.println(x.get(5, 3));
      System.out.println(x.get(1, 1));
      System.out.println(x.get(10, 14));
      System.out.println(x.get(5, 8));
   }
}
