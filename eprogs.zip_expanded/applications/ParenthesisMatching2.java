/** determine whether all parentheses are matched */

package applications;

import utilities.*;

public class ParenthesisMatching2
{
   /** @return true iff all parentheses in the string expr are matched */
   public static boolean matched(String expr)
   {
      int length = expr.length();
      int count = 0;  // number of unmatched left parentheses so far
   
      // scan expression expr for ( and )
      for (int i = 0; i < length; i++)
         if (expr.charAt(i) == '(')
            count++;  // one more '(' to be matched later
         else
            if (expr.charAt(i) == ')')
               if (count == 0)
                return false;
               else
                  count--;  // taken care of 1 '('
   
      // remaining '(' are unmatched
      return (count > 0) ? false : true;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // define the input stream to be the standard input stream
      MyInputStream keyboard = new MyInputStream();

      // try 3 expressions
      for (int i = 0; i < 3; i++)
      {
         System.out.println("Type an expression with no spaces");
         String expression = keyboard.readString();
   
         // output whether or not parentheses are matched
         System.out.println("The parentheses in");
         System.out.println(expression);
         if (matched(expression))
            System.out.println("are matched");
         else
            System.out.println("are not matched");
      }
   }
}
