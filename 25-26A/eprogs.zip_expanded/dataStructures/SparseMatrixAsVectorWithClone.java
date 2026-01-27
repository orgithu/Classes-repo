
/** extension of SparseMatrixASVector to include a clone method */

package dataStructures;

import utilities.*;
import wrappers.*;
import java.util.*;

public class SparseMatrixAsVectorWithClone
                   extends SparseMatrixAsVector
                   implements CloneableObject
{
   // constructors
   public SparseMatrixAsVectorWithClone(int theRows, int theColumns,
                               int estimatedMaxSize, Object theZero)
      {super(theRows, theColumns, estimatedMaxSize, theZero);}

   /** use a default estimated maximum size of 1 */
   public SparseMatrixAsVectorWithClone(int theRows, int theColumns,
                               Object theZero)
      {this(theRows, theColumns, 1, theZero);}
   
   /** defaults are rows = cols = estimatedMaxSize = 1 */
   public SparseMatrixAsVectorWithClone(Object theZero)
      {this(1, 1, 1, theZero);}

   /** read in a sparse matrix into this from the given input stream */
   public void read(Object theZero, MyInputStream stream)
   {
      SparseMatrixAsVector x = input(theZero, stream);
      // transfer to SparseMatrixAsVectorWithClone
      rows = x.rows;
      cols = x.cols;
      zero = x.zero;
      terms = x.terms;
   }

   /** @return a clone of this */
   public Object clone()
   {
      // define the clone w
      SparseMatrixAsVectorWithClone w =
            new SparseMatrixAsVectorWithClone(rows, cols, terms.size(), zero);

      // set the matrix elements by using a Vector enumerator
      Enumeration et = terms.elements();
      int cw = 0;    // cursor into w
      while (et.hasMoreElements())
      {
         MatrixTerm nextTerm = (MatrixTerm) et.nextElement();
         MatrixTerm clonedTerm = new MatrixTerm(((CloneableObject)
                          nextTerm.value).clone(), nextTerm.row, nextTerm.col);
         w.terms.insertElementAt(clonedTerm, cw++);
      }

      return w;
   }

   /** test program */
   public static void main(String [] args)
   {
      MyInteger myZero = new MyInteger(0);

      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();

      // input and output a matrix
      SparseMatrixAsVectorWithClone a = new SparseMatrixAsVectorWithClone
                                        (myZero);
      a.read(myZero, keyboard);

      System.out.println("The matrix a is");
      System.out.println(a);
      System.out.println();

      // test clone
      System.out.println("The clone of a is");
      System.out.println(a.clone());
      System.out.println();

   }
}
