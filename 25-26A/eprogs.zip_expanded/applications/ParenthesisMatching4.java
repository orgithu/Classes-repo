/** match delimiters (, [, and { */

package applications;

import dataStructures.*;
import utilities.*;

public class ParenthesisMatching4
{
   // top-level nested class  
   private static class StackElement
   {
      char theChar;         // character being stored
      int location;         // location of theChar

      // constructor
      public StackElement(char theChar, int theLocation)
      {
         location = theLocation;
         this.theChar = theChar;
      }
   }

   /** output the matched pairs of parentheses and brackets
     * in the string expr */
   public static void printMatchedPairs(String expr)
   {
      ArrayStack s = new ArrayStack();
      int length = expr.length();
   
      // scan expression expr for ( and )
      for (int i = 0; i < length; i++)
      {
         char newChar = expr.charAt(i);
         if (newChar == '(' || newChar == '[' || newChar == '{')
            // save left delimiter and its location
            s.push(new StackElement(newChar, i));
         else
            if (newChar == ')' || newChar == ']' || newChar == '}')
      	    try
            {// get possible matching left delimiter from stack
               StackElement stackElement = (StackElement) s.peek();
               if ((stackElement.theChar == '[' && newChar == ']') ||
                   (stackElement.theChar == '(' && newChar == ')') ||
                   (stackElement.theChar == '{' && newChar == '}'))
               {// we have a match
                  System.out.println(stackElement.location + "  " + i);
                  s.pop();  // matched this left delimiter
               }
               else
                  // no match
                  System.out.println("No match for right delimiter"
                                      + " at " + i);
            }
            catch (Exception e)
            {// stack was empty, no match exists
                System.out.println("No match for right delimiter"
                                    + " at " + i);
            }
         }
   
      // remaining left delimiters in stack are unmatched
      while (!s.empty())
         System.out.println("No match for left delimiter at "
                            + ((StackElement) s.pop()).location);
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
   
         // output the pairs of matched delimiters
         System.out.println("The pairs of matching delimiters in");
         System.out.println(expression);
         System.out.println("are (indexing begins at 0)");
         printMatchedPairs(expression);
      }
   }
}
