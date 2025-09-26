
/** step count for matrix transpose */

package misc;

public class MatrixTransposeStepCount1
{
   static int count;

   public static void transpose(int [][] a, int rows)
   {
      for (int i = 0; i < rows; i++)
      {
         count++;                            // for the for i statement
         for (int j = i+1; j < rows; j++)
         {
            count++;                         // for the for j statement
            // swap a[i][j] and a[j][i]
            int t = a[i][j];
            count++;
            a[i][j] = a[j][i];
            count++;
            a[j][i] = t;
            count++;
         }  
         count++;                            // last time of for j
      }
      count++;                               // last time of for i
   }
   
   public static void main(String [] args)
   {
      // define variables
      int rows = 2,
          cols = 2;
      int [][] a = new int [rows][cols],
               b = new int [rows][cols],
               c = new int [rows][cols];


      transpose(a, rows);

      System.out.println("The step count is " + count);

   }
}
