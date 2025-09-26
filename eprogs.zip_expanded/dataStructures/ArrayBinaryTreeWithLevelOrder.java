
/** preorder traversal of an array-based binary tree */

package dataStructures;

public class ArrayBinaryTreeWithLevelOrder
{
   static Object [] a;   // array that contains the tree

   /** visit method that prints the element in a[i] */ 
   public static void visit(int i)
      {System.out.print(a[i] + " ");}

   /** level order traversal */
   public static void levelOrder(Object [] theArray, int last)
   {
      a = theArray;
      for (int i = 1; i <= last; i++)
         if (a[i] != null)
            visit(i);  
   }

   /** test program */
   public static void main(String [] args)
   {
      Integer [] a = new Integer [15];
      a[1] = new Integer(1);
      a[2] = new Integer(2);
      a[5] = new Integer(5);
      a[10] = new Integer(10);
      a[11] = new Integer(11);

      System.out.println("The elements in level order are");
      levelOrder(a, 11);
      System.out.println();
   }
}
