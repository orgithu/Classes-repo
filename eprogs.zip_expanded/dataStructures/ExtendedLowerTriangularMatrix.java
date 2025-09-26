/** transpose a lower triangular matrix */

package dataStructures;

import wrappers.*;

public class ExtendedLowerTriangularMatrix extends LowerTriangularMatrix
{
   // constructor
   public ExtendedLowerTriangularMatrix(int theRows, Object theZero)
      {super(theRows, theZero);}

   /** @return the transpose of this */
   public UpperTriangularMatrix transpose()
   {
      // create result matrix
      UpperTriangularMatrix u = new UpperTriangularMatrix(rows, zero);

      // form the transpose
      for (int i = 0; i < rows * (rows + 1) / 2; i++)
         u.element[i] = element[i];
   
      return u;
   }

   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      int n = 4;
      ExtendedLowerTriangularMatrix x = 
             new ExtendedLowerTriangularMatrix(n, new MyInteger(0));

      // set a few elements
      x.set(1, 1, new MyInteger(2));
      x.set(2, 1, new MyInteger(4));
      x.set(3, 2, new MyInteger(5));
      x.set(3, 3, new MyInteger(3));
      x.set(4, 1, new MyInteger(1));
      x.set(4, 3, new MyInteger(2));

      // create the transpose
      UpperTriangularMatrix u = x.transpose();

      // output
      for (int i = 1; i <= 4; i++)
      {
         for (int j = 1; j <= 4; j++)
            System.out.print(u.get(i, j) + " ");
         System.out.println();
      }
   }
}
