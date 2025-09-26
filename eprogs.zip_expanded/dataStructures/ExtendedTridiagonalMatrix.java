

/** extension of tridiagonal matrix, methods to input, output,
  * add, subtract, and transpose included */

package dataStructures;

import java.lang.reflect.*;
import utilities.*;
import wrappers.*;

public class ExtendedTridiagonalMatrix extends TridiagonalMatrix
{
   // constructor
   public ExtendedTridiagonalMatrix(int theRows, Object theZero)
      {super(theRows, theZero);}
   
  // methods
  /** create string suitable for output */
  public String toString()
  {
     StringBuffer s = new StringBuffer(); 
  
     s.append("Lower diagonal is \n");
     for (int i = 0; i < rows - 1; i++)
        s.append(element[i] + "  ");
     s.append("\n");
  
     s.append("Main diagonal is \n");
     for (int i = rows - 1; i < 2 * rows - 1; i++)
        s.append(element[i] + "  ");
     s.append("\n");
  
     s.append("Upper diagonal is \n");
     for (int i = 2 * rows - 1; i < 3 * rows - 2; i++)
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
        for (int i = 0; i < rows - 1; i++)
           element[i] = inputMethod.invoke(null, inputMethodArgs);
  
        System.out.println("Enter main diagonal");
        for (int i = rows - 1; i < 2 * rows - 1; i++)
           element[i] = inputMethod.invoke(null, inputMethodArgs);
  
        System.out.println("Enter upper diagonal");
        for (int i = 2 * rows - 1; i < 3 * rows - 2; i++)
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

  /** @return this + b */
  public ExtendedTridiagonalMatrix add(ExtendedTridiagonalMatrix b)
  {
     if (rows != b.rows)
        throw new IllegalArgumentException
                  ("Matrices must have same dimensions");
  
     // create result array w
     ExtendedTridiagonalMatrix w = new ExtendedTridiagonalMatrix(rows, zero);
     for (int i = 0; i < 3 * rows - 2; i++)
         w.element[i] = ((Computable) element[i]).add(b.element[i]);
  
     return w;
  }
  
  /** @return this - b */
  public ExtendedTridiagonalMatrix subtract(ExtendedTridiagonalMatrix b)
  {
     if (rows != b.rows)
        throw new IllegalArgumentException
                  ("Matrices must have same dimensions");
  
     // create result array w
     ExtendedTridiagonalMatrix w = new ExtendedTridiagonalMatrix(rows, zero);
     for (int i = 0; i < 3 * rows - 2; i++)
         w.element[i] = ((Computable) element[i]).subtract(b.element[i]);
  
     return w;
  }
  
  /** @return the transpose of this */
  public ExtendedTridiagonalMatrix transpose()
  {
  
     // create result array w
     ExtendedTridiagonalMatrix w = new ExtendedTridiagonalMatrix(rows, zero);
  
     // copy lower diagonal of this to upper diagonal of w and copy
     // the upper diagonal of this to lower diagonal of w
     for (int i = 0; i < rows - 1; i++)
     {
         w.element[2 * rows - 1 + i] = element[i];
         w.element[i] = element[2 * rows - 1 + i];
     }
  
     // copy main diagonal of this to main dagonal of w
     for (int i = rows - 1; i < 2 * rows - 1; i++)
         w.element[i] = element[i];
  
     return w;
  }
   
   /** test program */
   public static void main(String [] args)
   {
      // create the matrix
      ExtendedTridiagonalMatrix x =
              new ExtendedTridiagonalMatrix(1, new MyInteger(0));
      ExtendedTridiagonalMatrix y =
              new ExtendedTridiagonalMatrix(1, new MyInteger(0));

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
