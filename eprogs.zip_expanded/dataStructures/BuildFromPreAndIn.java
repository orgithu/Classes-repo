/** build a binary tree from its preorder and inorder listings */

package dataStructures;

public class BuildFromPreAndIn
{
   // class data members
   static int [] inMap;
   static int [] preList;
   static int startPre;

   /** construct the unique binary tree with preorder listing
     * thePreList[] and preorder listing theInList.
     * The tree elements are assumed to be the distinct integers
     * 0 through theInList.length - 1.
     * @return root of constructed binary tree */
   public static BinaryTreeNode buildFromPreAndIn
                 (int [] thePreList, int [] theInList)
   {// set class data members and invoke theBuildFromPreAndIn

      preList = thePreList;
      startPre = 0;   // preorder list for subtree begins at preList[startPre]

      // set inMap so that inMap[i] is the location of
      // thePreList[i] in theInList[]
      // first construct inverse of theInList
      int [] inverse = new int[theInList.length];
      for (int i = 0; i < theInList.length; i++)
         inverse[theInList[i]] = i;
      // now construct inMap
      inMap = new int [theInList.length];
      for (int i = 0; i < theInList.length; i++)
         inMap[i] = inverse[preList[i]];

      return theBuildFromPreAndIn(0, inMap.length - 1);
   }


   /** construct the unique binary tree with inorder listing
     * inList[startIn:endIn] and whose preorder listing is
     * preList[startPre ...]. inMap[i] is the location of
     * preList[i] in inList[]. Note that inList is not a
     * parameter of the method.
     * @return root of constructed binary tree */
   static BinaryTreeNode theBuildFromPreAndIn(int startIn, int endIn)
   {
      if (startIn > endIn)
         // tree is empty
         return null;
     
      // create a node for the root and set its element field
      BinaryTreeNode root = new BinaryTreeNode(new Integer(preList[startPre]));

      // verify that element is in the proper subtree
      int inLocation = inMap[startPre++];
      if (inLocation < startIn || inLocation > endIn)
         throw new IllegalArgumentException
                   ("incompatible preorder and inorder listings");

      // construct left subtree recursively
      root.leftChild = theBuildFromPreAndIn(startIn, inLocation - 1);
   
      // construct right subtree recursively
      root.rightChild = theBuildFromPreAndIn(inLocation + 1, endIn);
   
      return root;
   }
   
   
   /** test program */
   public static void main(String [] args)
   {
      int [][] preList = {
            {0, 1, 3, 5, 2, 4, 6},
            {0, 1, 3, 7, 8, 11, 4, 2, 5, 9, 6, 10},
            {0, 9, 3, 7, 8, 11, 4, 2, 5, 1, 6, 10},
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
      
         System.out.print("and the given preorder list is ");
         for (int i = 0; i < preList[j].length; i++)
            System.out.print(preList[j][i] + " ");
         System.out.println();

         // construct the binary tree
         try
         {
            BinaryTreeNode p = buildFromPreAndIn(preList[j], inList[j]);
            System.out.println("Construction of binary tree is complete");
   
            System.out.print("Preorder sequence of constructed tree is ");
            BinaryTreeTraversal.preOrder(p);
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
