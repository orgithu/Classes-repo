package dataStructures;

import java.lang.reflect.*;
import utilities.*;

public interface ExtendedQueue extends Queue
{
   public int size();
   public void input(Method inputMethod, MyInputStream inStream);
   public String toString();
   public void split(ExtendedQueue a, ExtendedQueue b);
   public void combine(ExtendedQueue a, ExtendedQueue b);
}
