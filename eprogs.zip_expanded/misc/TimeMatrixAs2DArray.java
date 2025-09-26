
/** time for some MatrixAs2DArray operations */

package misc;

import dataStructures.*;
import wrappers.*;

public class TimeMatrixAs2DArray
{
   public static void main(String [] args)
   {
      int n = 500;  // number of rows and columns
      MatrixAs2DArray a = new MatrixAs2DArray(n, n);
      MatrixAs2DArray b = new MatrixAs2DArray(n, n);
      for (int i = 1; i <= n; i++)
         for (int j = 1; j <= n; j++)
         {
            if (i >= j && i - j < 4)
               a.set(i, j, new MyInteger(3));
            else
               a.set(i, j, new MyInteger(0));
            if (i <= j && j - i < 2)
               b.set(i, j, new MyInteger(5));
            else
               b.set(i, j, new MyInteger(0));
         }
    
      int m = 20;  // repetition factor
      long startTime = System.currentTimeMillis();
      // add the matrices
      for (int i = 0; i < m; i++)
         a.add(b);
      long elapsedTime = System.currentTimeMillis() - startTime;
      System.out.println("MatrixAs2DArray add took " + (elapsedTime / m) + "ms");

      m = 50;
      startTime = System.currentTimeMillis();
      // transpose
      for (int i = 0; i < m; i++)
         a.transpose();
      elapsedTime = System.currentTimeMillis() - startTime;
      System.out.println("MatrixAs2DArray transpose took "
                         + (elapsedTime / m) + "ms");

      startTime = System.currentTimeMillis();
      // multiply the matrices
      a.multiply(b);

      elapsedTime = System.currentTimeMillis() - startTime;
      System.out.println("MatrixAs2DArray multiply took " + elapsedTime + "ms");

   }
}
