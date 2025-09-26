
/** array-based winner trees using full binary trees */

package dataStructures;

import utilities.*;
import exceptions.*;

public class FullWinnerTree implements WinnerTree
{
   // data members
   int offset;           // 2^ceil(log n) - 1
   int [] tree;          // array for winner tree
   Playable [] player;   // array of players

   // only default constructor available

   /** @return the winner of the tournament
     * @return 0 if there are no players */
   public int getWinner()
      {return (tree == null) ? 0 : tree[1];}

   /** initialize winner tree for thePlayer[1:thePlayer.length-1] */
   public void initialize(Playable [] thePlayer)
   {
      int n = thePlayer.length - 1;
      if (n < 2)
         throw new IllegalArgumentException
                   ("must have at least 2 players");

      // compute  smallest m >= n and a power of 2
      int m;
      for (m = 1; m < n; m += m);
   
      // set instance data members
      player = thePlayer;
      tree = new int [m];
      offset = m - 1;
   
      int p = (n + offset) / 2;  // tree[p] is largest p where a match is played

      // play match at tree[p]
      if (n % 2 == 1)
         // n is odd, tree[p] has only one child
          tree[p] = n;
      else
         // n is even, tree[p] has two children
         tree[p] = player[n - 1].winnerOf(player[n]) ? n - 1 : n;

      // play remaining matches at lowest level
      int lp = 1;  // player[lp] is left child player
      for (int i = m / 2; i < p; i++)
      {
         tree[i] = player[lp].winnerOf(player[lp + 1]) ? lp : lp + 1;
         lp += 2;
      }

      // play matches at remaining levels
      for (int i = p / 2; i >= 1; i--)
      {
          int lc = 2 * i;  // index of left child of node i
          if (tree[lc + 1] == 0)
             // no right child
             tree[i] = tree[lc];
          else
             tree[i] = player[tree[lc]].winnerOf(player[tree[lc + 1]]) ?
                                        tree[lc] : tree[lc + 1];
      }
   }
   
   
   /** replay matches for player i */
   public void rePlay(int i)
   {
      int n = player.length - 1;  // number of players
      if (i <= 0 || i > n)
         throw new IllegalArgumentException("No player " + i);
   
      // play first match
      int p = (i + offset) / 2;  // node for first match
      if (2 * p + 1 > n)
         // only player for this match
         tree[p] = i;
      else
      {// two players
         int lp = 2 * p - offset;   // left player
         tree[p] = player[lp].winnerOf(player[lp + 1]) ? lp : lp + 1;
      }

      // play remaining matches
      p /= 2;  // move to parent
      for (; p >= 1; p /= 2)
         if (tree[2 * p + 1] == 0)
            // only one player
            tree[p] = tree[2 * p];
         else
            // two players
            tree[p] = player[tree[2 * p]].winnerOf(
                      player[tree[2 * p + 1]]) ? tree[2 * p] : tree[2 * p + 1];
   }
   
   public void output()
   {
      int n = player.length - 1;
      System.out.println("size = " + n  + " offset = "  + offset);
      System.out.println("Winner tree pointers are");
      for (int i = 1; i <= offset; i++)
         System.out.print(tree[i] + " ");
      System.out.println();
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

      FullWinnerTree w = new FullWinnerTree();
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
 
      // change player 9's value
      thePlayer[9].value = -2;
      w.rePlay(9);
      System.out.println("Changed player 9 to -2, new tree is");
      w.output();
   }
}
