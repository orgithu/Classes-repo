


/** build a binary tree from its postorder and inorder listings */

package dataStructures;

public class BuildFromPostAndIn
{
   // class data members
   static int [] inMap;
   static int [] postList;
   static int endPost;

   /** construct the unique binary tree with postorder listing
     * thePostList[] and inorder listing theInList.
     * The tree elements are assumed to be the distinct integers
     * 0 through theInList.length - 1.
     * @return root of constructed binary tree */
   public static BinaryTreeNode buildFromPostAndIn
                 (int [] thePostList, int [] theInList)
   {// set class data members and invoke theBuildFromPostAndIn

      postList = thePostList;
      endPost = postList.length - 1; 
                // postorder list for subtree ends at postList[endPost]

      // set inMap so that inMap[i] is the location of
      // thePostList[i] in theInList[]
      // first construct inverse of theInList
      int [] inverse = new int[theInList.length];
      for (int i = 0; i < theInList.length; i++)
         inverse[theInList[i]] = i;
      // now construct inMap
      inMap = new int [theInList.length];
      for (int i = 0; i < theInList.length; i++)
         inMap[i] = inverse[postList[i]];

      return theBuildFromPostAndIn(0, inMap.length - 1);
   }


   /** construct the unique binary tree with inorder listing
     * inList[startIn:endIn] and whose postorder listing is
     * postList[... endPost]. inMap[i] is the location of
     * postList[i] in inList[]. Note that inList is not a
     * parameter of the method.
     * @return root of constructed binary tree */
   static BinaryTreeNode theBuildFromPostAndIn(int startIn, int endIn)
   {
      if (startIn > endIn)
         // tree is empty
         return null;
     
      // create a node for the root and set its element field
      BinaryTreeNode root = new BinaryTreeNode(new Integer(postList[endPost]));

      // verify that element is in the proper subtree
      int inLocation = inMap[endPost--];
      if (inLocation < startIn || inLocation > endIn)
         throw new IllegalArgumentException
                   ("incompatible postorder and inorder listings");
   
      // construct right subtree recursively
      root.rightChild = theBuildFromPostAndIn(inLocation + 1, endIn);

      // construct left subtree recursively
      root.leftChild = theBuildFromPostAndIn(startIn, inLocation - 1);
   
      return root;
   }
   
   
   /** test program */
   public static void main(String [] args)
   {
      int [][] postList = {
            {5, 3, 1, 6, 4, 2, 0},
            {7, 11, 8, 3, 4, 1, 9, 5, 10, 6, 2, 0},
            {7, 11, 9, 3, 4, 1, 8, 5, 10, 6, 2, 0},
                       };
      int [][] inList = {
            {3, 5, 1, 0, 2, 6, 4},
            {7, 3, 8, 11, 1, 4, 0, 9, 5, 2, 6, 10},
            {7, 3, 8, 11, 1, 4, 0, 9, 5, 2, 6, 10},
                         };
   
      int n = 3;   // number of data sets

      for (int j = 0; j < n; j++)
      {
         System.out.print("The given inorder list is ");
         for (int i = 0; i < inList[j].length; i++)
            System.out.print(inList[j][i] + " ");
         System.out.println();

         System.out.print("and the given postorder list is ");
         for (int i = 0; i < postList[j].length; i++)
            System.out.print(postList[j][i] + " ");
         System.out.println();

         // construct the binary tree
         try
         {
            BinaryTreeNode p = buildFromPostAndIn(postList[j], inList[j]);
            System.out.println("Construction of binary tree is complete");
   
            System.out.print("Postorder sequence of constructed tree is ");
            BinaryTreeTraversal.postOrder(p);
            System.out.println();
            System.out.print("Inorder sequence of constructed tree is ");
            BinaryTreeTraversal.inOrder(p);
            System.out.println();
            System.out.println();
         }
         catch (Exception e)
         {
            System.out.println(e);
         }
      }
   }
}
