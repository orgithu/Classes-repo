
/** time for some ExtendedLinkedSparseMatrix operations */

package misc;

import dataStructures.*;
import wrappers.*;
import utilities.*;

public class TimeExtendedLinkedSparseMatrix
{
   public static void main(String [] args)
   {
      MyInputStream keyboard = new MyInputStream();
      ExtendedLinkedSparseMatrix a = ExtendedLinkedSparseMatrix.
                               inputWithValidate(new MyInteger(0), keyboard);
      ExtendedLinkedSparseMatrix b = ExtendedLinkedSparseMatrix.
                               inputWithValidate(new MyInteger(0), keyboard);
                     
      int m = 200;  // repetition factor
      long startTime = System.currentTimeMillis();
      // add the matrices
      for (int i = 0; i < m; i++)
         a.add(b);
      long elapsedTime = System.currentTimeMillis() - startTime;
      System.out.println("ExtendedLinkedSparseMatrix add took "
                         + (elapsedTime / m) + "ms");


      startTime = System.currentTimeMillis();
      // transpose the matrices
      for (int i = 0; i < m; i++)
         a.transpose();
      elapsedTime = System.currentTimeMillis() - startTime;
      System.out.println("ExtendedLinkedSparseMatrix transpose took "
                         + (elapsedTime / m) + "ms");


   }
}
