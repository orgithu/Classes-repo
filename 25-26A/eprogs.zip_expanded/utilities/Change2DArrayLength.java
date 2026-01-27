

/** Change the dimensions of a 2D array */
package utilities;

import java.lang.reflect.*;

public class Change2DArrayLength 
{
   /** @param rows is number of rows of a to be copied into new array
     * @param columns is number of columns of a to be copied into new array
     * @return a newRows x newColumns array
     * @throws IllegalArgumentException when rows > newRows
     * or columns > newColumns
     * input array a must have at least 1 row */
   public static Object [][] changeLength2D(Object [][] a,
                     int rows, int columns, int newRows, int newColumns)
   {
      // make sure new dimensions are adequate
      if (rows > newRows || columns > newColumns)
         throw new IllegalArgumentException
                   ("new dimensions are too small, rows = " + rows
                    + " columns = " + columns + " newRows = " + newRows
                    + " newColumns = " + newColumns);
   
      // set up array of new dimensions
      int [] dimensions = {newRows, newColumns};

      // allocate a new array of desired dimensions and type
      Object [][] newArray = (Object [][]) Array.newInstance
                (a[0].getClass().getComponentType(), dimensions);
   
      // copy from old space to new space
      for (int i = 0; i < rows; i++)
         // copy row i
         System.arraycopy(a[i], 0, newArray[i], 0, columns);
   
      return newArray;
   }

   /** test program */
   public static void main(String [] args)
   {
      // create a 2 x 3 array
      Integer [][] x = {{new Integer(1), new Integer(2), new Integer(3)},
                        {new Integer(4), new Integer(5), new Integer(6)}};

      // output
      System.out.println("Array dimensions are " + x.length
                         + " x " + x[0].length);
      System.out.println("The elements are ");
      for (int i = 0; i < 2; i++)
      {
         for (int j = 0; j < 3; j++)
            System.out.print(x[i][j] + " ");
         System.out.println();
      }
   
      // change dimensions to 3 x 4
      x = (Integer [][]) changeLength2D(x, 2, 3, 3, 4);

      // add elements to x
      for (int i = 0; i < 2; i++)
         x[i][3] = new Integer(10 + i);
      for (int j = 0; j < 4; j++)
         x[2][j] = new Integer(j);
   
      // output
      System.out.println("Array dimensions are " + x.length
                         + " x " + x[0].length);
      System.out.println("The elements are ");
      for (int i = 0; i < 3; i++)
      {
         for (int j = 0; j < 4; j++)
            System.out.print(x[i][j] + " ");
         System.out.println();
      }
   }
}
