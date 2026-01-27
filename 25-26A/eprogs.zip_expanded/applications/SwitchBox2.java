/** early terminating switch box routing */

package applications;

import java.util.*;
import utilities.*;

public class SwitchBox2
{
   /** determine whether the switch box is routable
     * @param net[0..net.length-1] array of pin to net assignments
     * net numbers are between 1 and net.length / 2
     * @param n number of pins */
   public static boolean checkBox(int [] net)
   {
      int n = net.length;   // number of pins
      Stack s = new Stack();
   
      // define and initialize the array onStack so that
      // onStack[i] = true iff net i is on the stack
      // default initial value of onStack[i] is false
      boolean [] onStack = new boolean [n / 2 + 1];

      // scan nets clockwise
      for (int i = 0; i < n; i++)
         // examine pin i
         if (!s.empty())
            // check with top net
            if (net[i] == net[((Integer) s.peek()).intValue()])
               // net[i] is routable, delete from stack
               s.pop();
            else
            {// put i on the stack only if its partner pin is not already there
               if (onStack[net[i]])
                  break;
               s.push(new Integer(i));
               onStack[net[i]] = true;
            }
         else
            {// stack is empty, i cannot be on the stack
               s.push(new Integer(i));
               onStack[net[i]] = true;
            }
   
      // any unrouted nets left?
      if (s.empty())
      {// no nets remain
          System.out.println("Switch box is routable");
          return true;
       }
   
      System.out.println("Switch box is not routable");
      return false;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // define the input stream to be the standard input stream
      MyInputStream keyboard = new MyInputStream();

      // input the number of pins and their net assignment
      System.out.println("Type number of pins in switch box");
      int n = keyboard.readInteger();

      // create net assignment array
      int [] net = new int[n];

      // input the net assignments
     System.out.println("Type net numbers for pins 1 through " + n);
     for (int i = 0; i < n; i++)
        net[i] = keyboard.readInteger();

     // see if the switch box is routable
     checkBox(net);
   }
}
