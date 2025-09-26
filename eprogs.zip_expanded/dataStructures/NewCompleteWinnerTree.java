
/** array-based winner trees using complete binary trees,
  * method rePlay plays only those matches that are necessary */

package dataStructures;

import utilities.*;
import exceptions.*;

public class NewCompleteWinnerTree extends CompleteWinnerTree
{
   /** replay matches for player i, overrides CompleteWinnerTree.rePlay */
   public void rePlay(int i)
   {
      int n = player.length - 1;  // number of players
      if (i <= 0 || i > n)
         throw new IllegalArgumentException("No player " + i);
   
      int p,   // match node
          lc,  // left child of p
          rc;  // right child of p
   
      // find first match node and its children
      if (i <= lowExt)
      {// begin at lowest level
         p = (offset + i) / 2;
         lc = 2 * p - offset; // left child of p
         rc = lc + 1;
      }
      else
      {
         p = (i - lowExt + n - 1) / 2;
         if (2 * p == n - 1)
         {
            lc = tree[2 * p];
            rc = i;
         }
         else
         {
            lc = 2 * p - n + 1 + lowExt;
            rc = lc + 1;
         }
      }
   
      int newWinner = player[lc].winnerOf(player[rc]) ? lc : rc;
      if (newWinner != i && newWinner == tree[p])
         // no need to play additional matches
         return;

      tree[p] = newWinner;
   
      // play remaining matches
      p /= 2;  // move to parent
      for (; p >= 1; p /= 2)
      {
         newWinner = player[tree[2 * p]].winnerOf(
             player[tree[2 * p + 1]]) ? tree[2 * p] : tree[2 * p + 1];
         if (newWinner != i || newWinner == tree[p])
            // no need to play additional matches
            return;
         tree[p] = newWinner;
      }
   }
   
   /** test program */
   public static void main(String [] args)
   {

      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();
   
      // get data from System.in
      System.out.println("Enter number of players");
      int n = keyboard.readInteger();
      if (n < 2)
         throw new MyInputException
                   ("must have >= 2 players");

      Player [] thePlayer = new Player [n + 1];

      System.out.println("Enter player values");
      for (int i = 1; i <= n; i++)
         thePlayer[i] = new Player(i, keyboard.readInteger());

      NewCompleteWinnerTree w = new NewCompleteWinnerTree();
      w.initialize(thePlayer);

      System.out.println("The winner tree is");
      w.output();

      // change player 2's value
      thePlayer[2].value = 0;
      w.rePlay(2);
      System.out.println("Changed player 2 to zero, new tree is");
      w.output();

      // change player 3's value
      thePlayer[3].value = -1;
      w.rePlay(3);
      System.out.println("Changed player 3 to -1, new tree is");
      w.output();
 
      // change player 7's value
      thePlayer[7].value = 2;
      w.rePlay(7);
      System.out.println("Changed player 7 to 2, new tree is");
      w.output();
   }
}
