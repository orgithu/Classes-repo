
/** binary tree traversal methods using a formula-based representation */

package dataStructures;

public class FormulaBasedBinaryTreeTraversal
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

   /** level order traversal */
   public static void levelOrder(Object theArray, int theLast)
   {
      if (last < 1)
         return; // empty tree

      LinkedQueue q = new LinkedQueue();
      int i = 1; // start at root
      while (true)
      {
         visit(i);  
   
         // put children on queue
         if (2 * i <= last && a[2 * i] != null)
            q.put(new Integer(2 * i));       // put left child to queue
         if (2 * i + 1 <= last && a[2 * i + 1] != null)
            q.put(new Integer(2 * i + 1));   // add right child to queue
   
         // get next node to visit
         if (q.isEmpty())
            // no next node
            return;
         i = ((Integer) q.remove()).intValue();
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

      System.out.println("The elements in level order are");
      levelOrder(a, 11);
      System.out.println();
   }
}
