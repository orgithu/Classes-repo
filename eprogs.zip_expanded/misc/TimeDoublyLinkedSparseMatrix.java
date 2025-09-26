
/** time for some DoublyLinkedSparseMatrix operations */

package misc;

import dataStructures.*;
import wrappers.*;
import utilities.*;

public class TimeDoublyLinkedSparseMatrix
{
   public static void main(String [] args)
   {
      MyInputStream keyboard = new MyInputStream();
      DoublyLinkedSparseMatrix a = DoublyLinkedSparseMatrix.
                               inputWithValidate(new MyInteger(0), keyboard);
      DoublyLinkedSparseMatrix b = DoublyLinkedSparseMatrix.
                               inputWithValidate(new MyInteger(0), keyboard);
                     
      int m = 200;  // repetition factor
      long startTime = System.currentTimeMillis();
      // add the matrices
      for (int i = 0; i < m; i++)
         a.add(b);
      long elapsedTime = System.currentTimeMillis() - startTime;
      System.out.println("DoublyLinkedSparseMatrix add took "
                         + (elapsedTime / m) + "ms");


      startTime = System.currentTimeMillis();
      // transpose the matrices
      for (int i = 0; i < m; i++)
         a.transpose();
      elapsedTime = System.currentTimeMillis() - startTime;
      System.out.println("DoublyLinkedSparseMatrix transpose took "
                         + (elapsedTime / m) + "ms");


   }
}
