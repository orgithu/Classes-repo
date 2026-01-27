
/** array-based winner trees using complete binary trees with
  * an even number of external nodes */

package dataStructures;

import utilities.*;
import exceptions.*;

public class CompleteEvenWinnerTree implements WinnerTree
{
   // data members
   int m;                // m = n when n is even and n+1 otherwise
   int lowExt;           // lowest-level external nodes
   int offset;           // 2^log(m-1) - 1
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

      player = thePlayer;
      if (n % 2 == 0)
         // n is even
         m = n;
      else
         // n is odd
         m = n + 1;
      tree = new int [m];
   
      // compute  s = 2^log (m-1)
      int i, s;
      for (s = 1; 2 * s <= m - 1; s += s);
   
      lowExt = 2 * (m - s);
      offset = 2 * s - 1;
   
      // play matches for lowest-level external nodes
      for (i = 2; i <= lowExt; i += 2)
         play((offset + i) / 2, i - 1, i);
   
      // handle remaining external nodes
      for (; i <= m; i += 2)
         play((i - lowExt + m - 1) / 2, i - 1, i);
   }
   
   /** play matches beginning at tree[p]
     * @param lc is left child of p
     * @param rc is right child of p */
   void play(int p, int lc, int rc)
   {
      if (rc >= player.length)
         tree[p] = lc;
      else
         tree[p] = player[lc].winnerOf(player[rc]) ? lc : rc;
   
      // more matches possible if at right child
      while (p > 1 && p % 2 == 1)
      {// at a right child
         tree[p / 2] = player[tree[p - 1]].winnerOf(player[tree[p]]) ?
                          tree[p - 1] : tree[p];
         p /= 2;  // go to parent
      }
   }
   
   /** replay matches for player i */
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
         p = (i - lowExt + m - 1) / 2;
         lc = 2 * p - m + 1 + lowExt;
         rc = lc + 1;
      }
   
      // play first match
      if (rc >= player.length)
         tree[p] = lc;
      else
         tree[p] = player[lc].winnerOf(player[rc]) ? lc : rc;

      // play remaining matches
      p /= 2;  // move to parent
      for (; p >= 1; p /= 2)
         tree[p] = player[tree[2 * p]].winnerOf(
             player[tree[2 * p + 1]]) ? tree[2 * p] : tree[2 * p + 1];
   }
   
   public void output()
   {
      int n = player.length - 1;
      System.out.println("size = " + n  + " lowExt = "  + lowExt
                          + " offset = "  + offset); 
      System.out.println("Winner tree pointers are");
      for (int i = 1; i < m; i++)
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

      CompleteEvenWinnerTree w = new CompleteEvenWinnerTree();
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
