

package dataStructures;

import utilities.*;

public interface MaxPriorityQueueWithKey
{
   public boolean isEmpty();
   public int size();
   public Object getMax();
   public void put(Comparable theKey, Object theObject);
   public Object removeMax();
}
