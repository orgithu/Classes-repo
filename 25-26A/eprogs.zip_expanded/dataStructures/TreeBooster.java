/** booster placement for tree distribution
  * networks represented as binary trees */

package dataStructures;

public class TreeBooster extends LinkedBinaryTree
{
   // top-level nested class
   private static class Booster
   {
      // data members
      int degradeToLeaf,       // degradation to leaf
          degradeFromParent;   // degradation from parent
      boolean boosterHere;     // true iff booster placed here

      // constructor
      public Booster(int toLeaf, int fromParent, boolean booster)
         {degradeFromParent = fromParent;}

      public String toString()
         {return boosterHere + " " + degradeToLeaf + " " 
                 + degradeFromParent;}
   }
   
   // class data member of TreeBooster
   static int tolerance;

   /** public method to place boosters */
   public void placeBoosters(int theTolerance)
   {
      tolerance = theTolerance;
      thePlaceBoosters(root);
   }

   /** recursive method to place boosters in subtree with root theRoot
     * @return max of degradations from parent of theRoot to nearest
     * booster or leaf in each right sibling subtree of parent in
     * the original distribution tree */
   static int thePlaceBoosters(BinaryTreeNode theRoot)
   {
      if (theRoot == null)
         // subtree is empty, parent must be a leaf
         return 0; 
                  
      ((Booster) theRoot.element).degradeToLeaf =
                                  thePlaceBoosters(theRoot.leftChild);

      // compute max degradation from parent of theRoot to nearest booster
      // or leaf in each subtree of theRoot in original distribution tree
      int degrade = ((Booster) theRoot.element).degradeToLeaf
                + ((Booster) theRoot.element).degradeFromParent;
   
      // see if we need a booster at theRoot
      if (degrade > tolerance)
      {
         ((Booster) theRoot.element).boosterHere = true;
         degrade = ((Booster) theRoot.element).degradeFromParent;
      }
   
      // compute max degradation for remaining children of parent of theRoot
      // in original distribution tree; the max of this value
      // and degrade is the degradation value for parent of theRoot
      return Math.max(degrade, thePlaceBoosters(theRoot.rightChild));
      
   }

   /** test program */
   public static void main(String [] args)
   {
      // construct tree of Figure 8.15(b)
      TreeBooster t = new TreeBooster();
      TreeBooster u = new TreeBooster();
      TreeBooster v = new TreeBooster();
      TreeBooster w = new TreeBooster();
      TreeBooster x = new TreeBooster();
      TreeBooster y = new TreeBooster();
      u.makeTree(new Booster(0, 2, false), x, x);
      y.makeTree(new Booster(0, 3, false), x, u);
      w.makeTree(new Booster(0, 3, false), x, x);
      v.makeTree(new Booster(0, 2, false), x, w);
      w.makeTree(new Booster(0, 2, false), x, v);
      t.makeTree(new Booster(0, 2, false), x, x);
      v.makeTree(new Booster(0, 3, false), w, t);
      t.makeTree(new Booster(0, 3, false), x, v);
      u.makeTree(new Booster(0, 2, false), y, t);
      v.makeTree(new Booster(0, 0, false), u, x);
      v.placeBoosters(3);
      System.out.println("The postorder listing after " +
                         "placement of boosters is");
      v.postOrderOutput();
   }
}
