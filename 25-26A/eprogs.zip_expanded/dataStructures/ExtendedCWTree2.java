/** extension of ExtendedCWTree to include the method parent */

package dataStructures;

public class ExtendedCWTree2 extends ExtendedCWTree
{
   /** @return parent of the external node i */
   public int parent(int i)
   {
      if (i < 1 || i >= player.length)
         throw new IllegalArgumentException
                   ("tree does not have an external node " + i);

      if (i <= lowExt)
         return (i + offset) / 2;
      else
         return (i - lowExt + player.length) / 2;
   }
}
