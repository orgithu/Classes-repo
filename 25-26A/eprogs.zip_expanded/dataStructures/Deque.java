
package dataStructures;

import java.lang.reflect.*;
import utilities.*;

public interface Deque
{
   public boolean isEmpty();
   public Object getLeftElement();
   public Object getRightElement();
   public void putAtLeft(Object theElement);
   public void putAtRight(Object theElement);
   public Object removeFromLeft();
   public Object removeFromRight();
}
