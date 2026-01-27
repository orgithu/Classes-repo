/** extension of lower triangular matrix to include
  * a matrix multiplication method */

package dataStructures;

import utilities.*;
import wrappers.*;

public class LowerTriangularMatrixWithMultiply extends LowerTriangularMatrix
{
   public LowerTriangularMatrixWithMultiply(int theRows, Object theZero)
      {super(theRows, theZero);}
   
   /** @return the this * b */
   public LowerTriangularMatrixWithMultiply multiply
                         (LowerTriangularMatrixWithMultiply b)
   {
     if (rows != b.rows)
        throw new IllegalArgumentException
                  ("Matrices must have same dimensions");
  
     // create result array w
     LowerTriangularMatrixWithMultiply w =
                    new LowerTriangularMatrixWithMultiply(rows, zero);

     for (int i = 1; i <= rows; i++)
        for (int j = 1; j <= i; j++)
        {// compute w(i,j)
           // compute this(i,j) * b(j,j)
           Computable sum = (Computable)
                            ((Computable) element[i * (i - 1) / 2 + j - 1]).
                            multiply(b.element[j * (j - 1) / 2 + j - 1]);
           // add in remaining terms
           for (int k = j + 1; k <= i; k++)
              sum.increment(((Computable) element[i * (i - 1) / 2 + k - 1]).
                            multiply(b.element[k * (k - 1) / 2 + j - 1]));

           // save as w(i,j)
           w.element[i * (i - 1) / 2 + j - 1] = sum;
      }
  
     return w;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      int n = 3;

      // create and output matrix x
      LowerTriangularMatrixWithMultiply x =
           new LowerTriangularMatrixWithMultiply(3, new MyInteger(0));
      x.set(1, 1, new MyInteger(1));
      x.set(2, 1, new MyInteger(2));
      x.set(2, 2, new MyInteger(3));
      x.set(3, 1, new MyInteger(2));
      x.set(3, 2, new MyInteger(0));
      x.set(3, 3, new MyInteger(1));
      System.out.println("Matrix x is");
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= n; j++)
            System.out.print(x.get(i, j) + " ");
         System.out.println();
      }
      System.out.println();

      // create and output matrix y
      LowerTriangularMatrixWithMultiply y =
           new LowerTriangularMatrixWithMultiply(3, new MyInteger(0));
      y.set(1, 1, new MyInteger(1));
      y.set(2, 1, new MyInteger(2));
      y.set(2, 2, new MyInteger(3));
      y.set(3, 3, new MyInteger(4));
      y.set(3, 2, new MyInteger(5));
      y.set(3, 3, new MyInteger(6));
      System.out.println("Matrix y is");
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= n; j++)
            System.out.print(y.get(i, j) + " ");
         System.out.println();
      }
      System.out.println();

      // compute and output the product
      LowerTriangularMatrixWithMultiply z = x.multiply(y);
      System.out.println("The product x * y is");
      for (int i = 1; i <= n; i++)
      {
         for (int j = 1; j <= n; j++)
            System.out.print(z.get(i, j) + " ");
         System.out.println();
      }
   }
}
