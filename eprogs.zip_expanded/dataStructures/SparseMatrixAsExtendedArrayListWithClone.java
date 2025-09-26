
/** extension of SparseMatrixASExtendedArrayList to include a clone method */

package dataStructures;

import utilities.*;
import wrappers.*;
import java.util.*;

public class SparseMatrixAsExtendedArrayListWithClone
              extends SparseMatrixAsExtendedArrayList
              implements CloneableObject
{
   // constructors
   public SparseMatrixAsExtendedArrayListWithClone
                (int theRows, int theColumns,
                 int estimatedMaxSize, Object theZero)
      {super(theRows, theColumns, estimatedMaxSize, theZero);}

   /** use a default estimated maximum size of 1 */
   public SparseMatrixAsExtendedArrayListWithClone
                (int theRows, int theColumns,
                 Object theZero)
      {this(theRows, theColumns, 1, theZero);}
   
   /** defaults are rows = cols = estimatedMaxSize = 1 */
   public SparseMatrixAsExtendedArrayListWithClone(Object theZero)
      {this(1, 1, 1, theZero);}

   /** read in a sparse matrix into this from the given input stream */
   public void read(Object theZero, MyInputStream stream)
   {
      SparseMatrixAsExtendedArrayList x = input(theZero, stream);
      // transfer to SparseMatrixAsExtendedArrayListWithClone
      rows = x.rows;
      cols = x.cols;
      zero = x.zero;
      terms = x.terms;
   }

   /** @return a clone of this */
   public Object clone()
   {
      // define the clone w
      SparseMatrixAsExtendedArrayListWithClone w =
            new SparseMatrixAsExtendedArrayListWithClone
                      (rows, cols, terms.size(), zero);

      // set the matrix elements by using a ExtendedArrayList enumerator
      Iterator it = terms.iterator();
      int cw = 0;    // cursor into w
      while (it.hasNext())
      {
         MatrixTerm nextTerm = (MatrixTerm) it.next();
         MatrixTerm clonedTerm = new MatrixTerm(nextTerm.row, nextTerm.col,
                    ((CloneableObject) nextTerm.value).clone());
         w.terms.add(cw++, clonedTerm);
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
      SparseMatrixAsExtendedArrayListWithClone a =
            new SparseMatrixAsExtendedArrayListWithClone(myZero);
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
