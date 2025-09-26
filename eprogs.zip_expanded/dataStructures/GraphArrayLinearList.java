
/** ArrayLinearList extended to include a method to remove by
  * matching vertex field */

/** this class is for use by the array-adjacency-list graph classes */

package dataStructures;

public class GraphArrayLinearList extends ArrayLinearList
{
   /** delete element with element.vertex = theVertex
     * @return deleted element */
   public Object removeElement(int theVertex)
   {
      // search for matching element
      for (int i = 0; i < size; i++)
         if (((EdgeNode) element[i]).vertex == theVertex)
         {// found matching element
            // save matching element
            Object theElement = element[i];

            // remove matching element
            System.arraycopy(element, i + 1, element, i, size - i - 1);
            size--;

            return theElement;
         }

      // no matching element
      return null;
   }
}
