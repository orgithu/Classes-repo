
package dataStructures;

import utilities.*;

public interface ExtendedBinaryTree extends BinaryTree,
                                            CloneableObject
{
   public boolean compare(ExtendedBinaryTree theTree);
   // public Object clone() is in CloneableObject
   public void swapSubtrees();
}
