

/** tridiagonal matrix mapped into a one-dimensional array by rows*/

package dataStructures;

import java.lang.reflect.*;
import utilities.*;
import wrappers.*;

public class TridiagonalByRows
{
   // data members
   int rows;            // matrix dimension
   Object zero;         // zero element
   Object [] element;   // element array

   // constructor
   public TridiagonalByRows(int theRows, Object theZero)
   {
      // validate theRows
      if (theRows < 1)
         throw new IllegalArgumentException
                   ("number of rows must be > 0");
   
      // create and initialize the matrix
      rows = theRows;
      zero = theZero;
      element = new Object [3 * rows - 2];
      for (int i = 0; i < 3 * rows - 2; i++)
         element[i] = zero;
   }
   
   // methods
   /** create string suitable for output */
   public String toString()
   {
      StringBuffer s = new StringBuffer(); 
   
      s.append("Lower diagonal is \n");
      for (int i = 2; i < 3 * rows - 2; i += 3)
         s.append(element[i] + "  ");
      s.append("\n");
   
      s.append("Main diagonal is \n");
      for (int i = 0; i < 3 * rows - 2; i += 3)
         s.append(element[i] + "  ");
      s.append("\n");
   
      s.append("Upper diagonal is \n");
      for (int i = 1; i < 3 * rows - 2; i += 3)
         s.append(element[i] + "  ");
      s.append("\n");
   
      // create equivalent String
      return new String(s);
   }
   
   /** input a tridiagonal matrix from the given input stream */
   public void input(Object theZero, MyInputStream inStream)
   {
      Method inputMethod;
      Object [] inputMethodArgs = {inStream};
      Class [] parameterTypes = {inStream.getClass()};
      zero = theZero;
      try
      {
         // get the proper method to be used to read in the values
         inputMethod = theZero.getClass().
                          getMethod("input", parameterTypes);
   
         // input number of rows in the matrix
         System.out.println("Enter number of rows");
         rows = inStream.readInteger();
         // validate input
         if (rows < 1)
            throw new IllegalArgumentException
                  ("number of rows must be > 0");
      
         // create the element array
         element = new Object [3 * rows - 2];
   
         // input elements
         System.out.println("Enter lower diagonal");
         for (int i = 2; i < 3 * rows - 2; i += 3)
            element[i] = inputMethod.invoke(null, inputMethodArgs);
   
         System.out.println("Enter main diagonal");
         for (int i = 0; i < 3 * rows - 2; i += 3)
            element[i] = inputMethod.invoke(null, inputMethodArgs);
   
         System.out.println("Enter upper diagonal");
         for (int i = 1; i < 3 * rows - 2; i += 3)
            element[i] = inputMethod.invoke(null, inputMethodArgs);
      }
      catch (Exception e)
      {
         System.out.println(e);
         throw new IllegalArgumentException
               ("matrix element type "
                + "must have the static method input() defined");
      }
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
 
   /** @return the element this(i,j)
     * @throws IndexOutOfBoundsException when i or j invalid */
   public Object get(int i, int j)
   {
      checkIndex(i, j);

      // determine element to return
      switch (i - j)
      {
         case 1: case 0: case -1: // in tridiagonal
               return element[2 * i + j - 3];
         default: return zero;
      }
   }
   
   /** set this(i,j) = newValue
     * @throws IndexOutOfBoundsException when i or j invalid */
   public void set(int i, int j, Object newValue)
   {
      checkIndex(i, j);

      // store newValue
      switch (i - j)
      {
         case 1: case 0: case -1: // in tridiagonal
               element[2 * i + j - 3] = newValue;
               break;
         default: if (!((Zero) newValue).equalsZero())
                      throw new IllegalArgumentException
                            ("elements not on tridiagonal must be zero");
      }
   }

   /** @return this + b */
   public TridiagonalByRows add(TridiagonalByRows b)
   {
      if (rows != b.rows)
         throw new IllegalArgumentException
                   ("Matrices must have same dimensions");
   
      // create result array w
      TridiagonalByRows w = new TridiagonalByRows(rows, zero);
      for (int i = 0; i < 3 * rows - 2; i++)
          w.element[i] = ((Computable) element[i]).add(b.element[i]);
   
      return w;
   }
   
   /** @return this - b */
   public TridiagonalByRows subtract(TridiagonalByRows b)
   {
      if (rows != b.rows)
         throw new IllegalArgumentException
                   ("Matrices must have same dimensions");
   
      // create result array w
      TridiagonalByRows w = new TridiagonalByRows(rows, zero);
      for (int i = 0; i < 3 * rows - 2; i++)
          w.element[i] = ((Computable) element[i]).subtract(b.element[i]);
   
      return w;
   }
   
   /** @return the transpose of this */
   public TridiagonalByRows transpose()
   {
   
      // create result array w
      TridiagonalByRows w = new TridiagonalByRows(rows, zero);
   
      // copy lower diagonal of this to upper diagonal of w and copy
      // the upper diagonal of this to lower diagonal of w
      for (int i = 1; i < 3 * rows - 2; i += 3)
      {
          w.element[i] = element[i + 1];
          w.element[i + 1] = element[i];
       }

      // copy main diagonal of this to main diagonal of w
      for (int i = 0; i < 3 * rows - 2; i += 3)
          w.element[i] = element[i];
   
      return w;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      TridiagonalByRows x = new TridiagonalByRows(20, new MyInteger(0));

      // set a few elements
      x.set(1, 1, new MyInteger(22));
      x.set(5, 5, new MyInteger(44));
      x.set(8, 5, new MyInteger(0));
      x.set(7, 8, new MyInteger(55));

      // retrieve a few elements
      System.out.println(x.get(7,8));
      System.out.println(x.get(5,5));
      System.out.println(x.get(1,1));
      System.out.println(x.get(10,1));
      System.out.println(x.get(1,5));

      TridiagonalByRows y =
              new TridiagonalByRows(1, new MyInteger(0));

      MyInputStream keyboard = new MyInputStream();

      // test input
      x.input(new MyInteger(0), keyboard);
      y.input(new MyInteger(0), keyboard);

      // test output
      System.out.println(x);
      System.out.println();
      System.out.println(y);
      System.out.println();

      // test add, subtract, and transpose
      System.out.println("The sum of the matrices is\n" + x.add(y));
      System.out.println();
      System.out.println("Their difference is\n" + x.subtract(y));
      System.out.println();
      System.out.println("The transpose of the first matrix is\n"
                         + x.transpose());
   }
}
