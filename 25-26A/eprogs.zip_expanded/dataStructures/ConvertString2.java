
/** convert a character string into an integer using the suggestion of
  * Example 11.11 */

package dataStructures;

public class ConvertString2
{
   /** fold c into two 8-bit segments and add */
   private static int fold(char c)
   {
      // convert into an integer
      int i = (int) c;

      // extract least significant 8 bits
      int least8 = i % 256;

      // extract most significant 8 bits
      int most8 = i / 256;

      return least8 + most8;
   }

   /** covert string s into an integer that depends on all
     * characters of s */
   public static int integer(String s)
   {
      int length = s.length();   // number of characters in s
      int answer = 0;

      for (int i = 0; i < length; i += 4)
      {// do four characters at a time
         answer += fold(s.charAt(i));

         try  // do three more characters if they exist
         {
            answer += fold(s.charAt(i + 1)) << 8;
            answer += fold(s.charAt(i + 2)) << 16;
            answer += fold(s.charAt(i + 3)) << 24;
         }
         catch (StringIndexOutOfBoundsException e)
         {// ignore the exception
         }
      }

      return (answer < 0) ? -answer : answer;
   }

   /** test program */
   public static void main(String [] args)
   {
       String s = "abcde";
       for (int i = 0; i < 5; i++)
          System.out.println(s.charAt(i) + " " + ((int) s.charAt(i)));

       System.out.println(s + " " +  integer(s));
   }
}
