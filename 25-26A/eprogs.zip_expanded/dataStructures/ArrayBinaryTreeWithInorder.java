
/** inorder traversal of an array-based binary tree */

package dataStructures;

public class ArrayBinaryTreeWithInorder
{
   // data members
   static Object [] a;   // array that contains the tree
   static int last;      // position of last element in array a

   /** visit method that prints the element in a[i] */ 
   public static void visit(int i)
      {System.out.print(a[i] + " ");}


   /** inorder traversal */
   public static void inOrder(Object [] theArray, int theLast)
   {
      // set static data members
      a = theArray;
      last = theLast;

      // start the recursive traversal method at the root
      theInOrder(1);
   }

   /** actual method to do the inorder traversal */
   static void theInOrder(int i)
   {// traverse subtree rooted at a[i]
      if (i <= last && a[i] != null)
      {// root exists
         theInOrder(2 * i);         // do left subtree
         visit(i);                  // visit tree root
         theInOrder(2 * i + 1);     // do right subtree
      }
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

      System.out.println("The elements in inorder are");
      inOrder(a, 11);
      System.out.println();
   }
}
