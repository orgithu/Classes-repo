package dataStructures;

import java.lang.reflect.*;
import utilities.*;

public interface ExtendedStack extends Stack
{
   public int size();
   public void input(Method inputMethod, MyInputStream inStream);
   public String toString();
   public void split(ExtendedStack a, ExtendedStack b);
   public void combine(ExtendedStack a, ExtendedStack b);
}
