

/** array-based loser trees using complete binary trees */

package dataStructures;

import utilities.*;
import exceptions.*;

public class CompleteLoserTree
{
   // data members
   int lowExt;           // lowest-level external nodes
   int offset;           // 2^k - 1
   int [] tree;          // array for loser tree
   Playable [] player;   // array of players

   // only default constructor available

   /** @return the winner of the tournament
     * @return 0 if there are no players */
   public int getWinner()
      {return (tree == null) ? 0 : tree[0];}

   /** initialize loser tree for thePlayer[1:thePlayer.length-1] */
   public void initialize(Playable [] thePlayer)
   {
      int n = thePlayer.length - 1;
      if (n < 2)
         throw new IllegalArgumentException
                   ("must have at least 2 players");

      player = thePlayer;
      tree = new int [n];
   
      // compute  s = 2^log (n-1)
      int i, s;
      for (s = 1; 2 * s <= n - 1; s += s);
   
      lowExt = 2 * (n - s);
      offset = 2 * s - 1;
   
      // first record winners in tree[1:n-1]
      // play matches for lowest-level external nodes
      for (i = 2; i <= lowExt; i += 2)
         play((offset + i) / 2, i - 1, i);
   
      // handle remaining external nodes
      if (n % 2 == 1)
      {// special case for odd n, play internal and exteral node
         play(n/2, tree[n - 1], lowExt + 1);
         i = lowExt + 3;
      }
      else i = lowExt + 2;
   
      // i is left-most remaining external node
      for (; i <= n; i += 2)
         play((i - lowExt + n - 1) / 2, i - 1, i);

      // record overall winner in tree[0]
      tree[0] = tree[1];
   
      // now make a level-order traversal of tree
      // replacing winners by losers
      for (i = 1; i < n; i++)
      {// set tree[i] to loser of match played at tree[i]
         int lc = 2 * i;                        // left child of i
         int rc = lc + 1;                       // right child of i
         // eventually player[leftPlayer] denotes left player of match
         // at tree[i] and player[rightPlayer] denotes the other player
         int leftPlayer, rightPlayer;
   
         // determine leftPlayer
         if (lc <= n - 1)
            leftPlayer = tree[lc];
         else
            // left child is an external node
            if (lc <= offset)
               leftPlayer = lc + lowExt - n + 1;
            else
               leftPlayer = lc - offset;
   
         // determine rightPlayer
         if (rc <= n - 1)
            rightPlayer = tree[rc];
         else
            // right child is an external node
            if (rc <= offset)
               rightPlayer = rc + lowExt - n + 1;
            else
               rightPlayer = rc - offset;
   
         // determine loser of match
         if (leftPlayer == tree[i])
            // rightPlayer is loser
            tree[i] = rightPlayer;
         else
            // leftPlayer is loser
            tree[i] = leftPlayer;
      }
   }

   /** play matches beginning at tree[p]
     * @param lc is left child of p
     * @param rc is right child of p */
   void play(int p, int lc, int rc)
   {
      tree[p] = player[lc].winnerOf(player[rc]) ? lc : rc;
   
      // more matches possible if at right child
      while (p > 1 && p % 2 == 1)
      {// at a right child
         tree[p / 2] = player[tree[p - 1]].winnerOf(player[tree[p]]) ?
                          tree[p - 1] : tree[p];
         p /= 2;  // go to parent
      }
   }

   /** replay matches for previous winner */
   public void rePlay()
   {
      if (player == null)
         throw new IllegalArgumentException
                   ("tree must be initialized first");

      int n = player.length - 1;  // number of players
   
      // find first match node
      int p;                        // match node
      int i = tree[0];              // player[i] is previous winner
      if (i <= lowExt)
         // begin at lowest level
         p = (offset + i)/2;
      else
         p = (i - lowExt + n - 1) / 2;
   
      int lastWinner = i;
   
      // play matches
      for (; p >= 1; p /= 2)
      {// play match at tree[p]
         int newWinner = player[lastWinner].winnerOf(player[tree[p]]) ?
                           lastWinner : tree[p];
         // update loser if loser has changed
         if (tree[p] == newWinner)
         {// player[tree[p]] is no longer a loser
            tree[p] = lastWinner;
            lastWinner = newWinner;
         }
      }

      // put overall winner in tree[0]
      tree[0] = lastWinner;
   }

   public void output()
   {
      int n = player.length - 1;
      System.out.println("size = " + n  + " lowExt = "  + lowExt
                          + " offset = "  + offset); 
      System.out.println("Loser tree pointers are");
      for (int i = 0; i < n; i++)
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

      CompleteLoserTree w = new CompleteLoserTree();
      w.initialize(thePlayer);

      System.out.println("The loser tree is");
      w.output();

      // change player 10's value
      thePlayer[10].value = 20;
      w.rePlay();
      System.out.println("Changed player 10 to 20, new tree is");
      w.output();

      // change player 3's value
      thePlayer[3].value = 7;
      w.rePlay();
      System.out.println("Changed player 3 to 7, new tree is");
      w.output();
 
      // change player 1's value
      thePlayer[1].value = 25;
      w.rePlay();
      System.out.println("Changed player 1 to 25, new tree is");
      w.output();
   }
}
