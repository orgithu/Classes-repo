

/** array-based loser trees using complete binary trees,
  * initialization done as per Exercise 10.9 (c) */

package dataStructures;

import utilities.*;
import exceptions.*;

public class NewCompleteLoserTree extends CompleteLoserTree
{
   // override CompleteLoserTree.initialize
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
   
      // play matches starting at lowest-level external nodes
      for (i = 2; i <= lowExt; i += 2)
         play((offset + i) / 2, i - 1, i);
   
      // handle remaining external nodes
      if (n % 2 == 1)
      {// special case for odd n, play
       // internal and external node
         play(n / 2, tree[(n - 1) / 2], lowExt + 1);
         i = lowExt + 3;
      }
      else 
         i = lowExt + 2;
   
      // i is left-most remaining external node
      for (; i <= n; i += 2)
         play((i - lowExt + n - 1) / 2, i - 1, i);
   }

   // override CompleteLoserTree.play
   /** play matches beginning at tree[p]
     * @param lc is left child of p
     * @param rc is right child of p */
   void play(int p, int lc, int rc)
   {
      int currentWinner = player[lc].winnerOf(player[rc]) ? lc : rc;
      if (currentWinner == lc)
         // loser is rc
         tree[p] = rc;
      else // loser is lc
         tree[p] = lc;
   
      // more matches possible if at right child
      while (p > 1 && p % 2 == 1)
      {// at a right child
         // parent of p has opponent information
         p /= 2;  // go to parent
         int newWinner = player[tree[p]].winnerOf(player[currentWinner]) ?
                                tree[p] : currentWinner;
         if (newWinner != currentWinner)
         {// loser is currentWinner
            tree[p] = currentWinner;
            currentWinner = newWinner;
         }
      }
   
      // record winner of last match in tree[p/2]
      tree[p / 2] = currentWinner;
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

      NewCompleteLoserTree w = new NewCompleteLoserTree();
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
