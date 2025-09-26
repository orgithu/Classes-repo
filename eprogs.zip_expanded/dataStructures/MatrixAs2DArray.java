/** matrix as a 2D array */

package dataStructures;

import utilities.*;
import wrappers.*;

public class MatrixAs2DArray implements CloneableObject
{
   // data members
   int rows, cols;        // matrix dimensions
   Object [][] element;   // element array

   // constructor
   public MatrixAs2DArray(int theRows, int theColumns)
   {
      // validate theRows and theColumns
      if (theRows < 0 || theColumns < 0)
         throw new IllegalArgumentException
             ("number of rows and columns must be >= 0");
      if ((theRows == 0 && theColumns != 0) ||
          (theRows != 0 && theColumns == 0))
         throw new IllegalArgumentException
             ("both the number of rows and columns must equal " +
              "zero or both must be > 0");
   
      // create the matrixAs2DArray
      rows = theRows; cols = theColumns;
      element = new Object [rows][cols];
   }
   
   // methods
   /** @return a clone of the matrix */
   public Object clone()
   {
      // create a new matrixAs2DArray
      MatrixAs2DArray x = new MatrixAs2DArray(rows, cols);
   
      // clone each element of this into x
      for (int i = 0; i < rows; i++)
         for (int j = 0; j < cols; j++)
            x.element[i][j] = ((CloneableObject) element[i][j]).clone();
     
      return x;
   }
   
   /** copy the references in m into this */
   public void copy(MatrixAs2DArray m)
   {
      if (this != m) // not a copy to self
      {
         rows = m.rows;
         cols = m.cols; 
         element = new Object [rows][cols];
   
         // copy each reference
         for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
               element[i] = m.element[i];
      }
   }
   
   /** @throws IndexOutOfBoundsException when i < 1
     * or j < 1 or i > rows or j > cols */
   void checkIndex(int i, int j)
   {
      if (i < 1 || j < 1 || i > rows || j > cols)
         throw new IndexOutOfBoundsException
                   ("i = " + i + " j = " + j +
                    " rows = " + rows + " cols = " + cols);
   }
   
   /** @return the element this(i,j) */
   public Object get(int i, int j)
   {
      checkIndex(i, j);
      return element[i - 1][j - 1];
   }
   
   /** set this(i,j) = newValue */
   public void set(int i, int j, Object newValue)
   {
      checkIndex(i, j);
      element[i - 1][j - 1] = newValue;
   }
   
   /** @return this + m */
   public MatrixAs2DArray add(MatrixAs2DArray m)
   {
      if (rows != m.rows || cols != m.cols)
         throw new IllegalArgumentException
               ("the matrices are incompatible");
   
      // create result matrix w
      MatrixAs2DArray w = new MatrixAs2DArray(rows, cols);
      for (int i = 0; i < rows; i++)
         for (int j = 0; j < cols; j++)
            w.element[i][j] = ((Computable) element[i][j]).
                                            add(m.element[i][j]);
   
      return w;
   }

   /** @return this - m */
   public MatrixAs2DArray subtract(MatrixAs2DArray m)
   {
      if (rows != m.rows || cols != m.cols)
         throw new IllegalArgumentException
            ("the matrices are incompatible");
   
      // create result matrix w
      MatrixAs2DArray w = new MatrixAs2DArray(rows, cols);
      for (int i = 0; i < rows; i++)
         for (int j = 0; j < cols; j++)
            w.element[i][j] = ((Computable) element[i][j]).
                                            subtract(m.element[i][j]);
   
      return w;
   }


   /** @return this * m */
   public MatrixAs2DArray multiply(MatrixAs2DArray m)
   {
      if (cols != m.rows)
         throw new IllegalArgumentException
               ("the matrices are incompatible");
   
      MatrixAs2DArray w = new MatrixAs2DArray(rows, m.cols);  // result matrix
   
      for (int i = 0; i < rows; i++)
         for (int j = 0; j < w.cols; j++)
         {// compute [i][j] term of result
            
            // compute first term of w(i,j)
            Computable sum =  (Computable) ((Computable)element[i][0])
                               .multiply(m.element[0][j]);
   
            // add in remaining terms
            for (int k = 1; k < cols; k++)
               sum.increment(((Computable) element[i][k]).multiply
                              (m.element[k][j]));

            w.element[i][j] = sum;
         }
   
      return w;
   }

   /** return the transpose of this */
   public MatrixAs2DArray transpose()
   {
      // create result matrix w
      MatrixAs2DArray w = new MatrixAs2DArray(cols, rows);

      // set up the transpose of this
      for (int i = 0; i < rows; i++)
         for (int j = 0; j < cols; j++)
            w.element[j][i] = element[i][j];
   
      return w;
   }

   
   /** increment all elements of this by x */
   public void increment(Object x)
   {
      for (int i = 0; i < rows; i++)
         for (int j = 0; j < cols; j++)
             ((Computable) element[i][j]).increment(x);
   }
   
   /** convert the matrix into a string so it can be output */
   public String toString()
   {
      StringBuffer s = new StringBuffer(); 
      int k = 0;  // index into element array

      for (int i = 0; i < rows; i++)
      {// do row i
         for (int j = 0; j < cols; j++)
            s.append(element[i][j].toString() + " ");
   
         // row i finished
         s.append("\n");
      }
   
      // create equivalent String
      return new String(s);
   }
   
   /** test program */
   public static void main(String [] args)
   {
      try {
         MatrixAs2DArray x = new MatrixAs2DArray(3,2);
         MatrixAs2DArray y, z = null;
         MyInteger q = null;
         for (int i = 1; i <= 3; i++)
            for (int j = 1; j <= 2; j++)
            {
               q = new MyInteger(2 * i + j);
               x.set(i, j, q);
            }

         System.out.println("x(3,1) = " + x.get(3,1));
         System.out.println("Matrix x is");
         System.out.println(x);

         // test clone and copy
         y = (MatrixAs2DArray) x.clone();
         // cannot copy into a null z
         z = new MatrixAs2DArray(0,0);
         z.copy(x);
         // change the last matrixAs2DArray entry of x
         q.setValue(0);  // also changes z but not y
         System.out.println("Matrix x is now");
         System.out.println(x);
         System.out.println("Matrix y (the clone of original x) is");
         System.out.println(y);
         System.out.println("Matrix z (the copy of original x) is");
         System.out.println(z);

         // test increment
         y.increment(new MyInteger(2));
         System.out.println("y incremented by 2 is");
         System.out.println(y);
         System.out.println("x is");
         System.out.println(x);

         // test add
         System.out.println("y + x is");
         System.out.println(y.add(x));


         // create a compatile matrixAs2DArray for multiply
         MatrixAs2DArray w = new MatrixAs2DArray(2,3);
         for (int i = 1; i <= 2; i++)
            for (int j = 1; j <= 3; j++)
               w.set(i, j, new MyInteger(i + j));
         System.out.println("Matrix w is");
         System.out.println(w);

         // test multiply
         System.out.println("y * w is");
         System.out.println(y.multiply(w));

         // test transpose
         System.out.println("The transpose is");
         System.out.println(w.transpose());
      }
      catch (Exception e)
      {System.out.println(e);}
   }
}
