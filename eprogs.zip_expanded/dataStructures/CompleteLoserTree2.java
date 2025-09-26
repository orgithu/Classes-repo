
/** array-based loser trees using complete binary trees
  * with an additional n nodes
  * initialization done as per Exercise 14.17 (b) */

package dataStructures;

import utilities.*;
import exceptions.*;

public class CompleteLoserTree2
{
   // data members
   int lowExt;           // lowest-level external nodes
   int offset;           // 2^log(n-1) - 1
   int [] tree;          // array for winner tree
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
      tree = new int [2 * n];
   
      // compute  s = 2^log (n-1)
      int s;
      for (s = 1; 2 * s <= n - 1; s += s);
   
      lowExt = 2 * (n - s);
      offset = 2 * s - 1;
   
      // initialize nodes that lie between players and match nodes
      for (int i = 1; i <= lowExt; i ++)
         tree[i + offset] = i;
      for (int i = lowExt + 1; i <= n; i++)
         tree[i - lowExt + n - 1] = i;

      // play matches
      for (int i = n - 1; i >= 1; i--)
         tree[i] = player[tree[2 * i]].winnerOf(player[tree[2 * i + 1]]) ?
                          tree[2 * i] : tree[2 * i + 1];

      tree[0] = tree[1];  // overall winner
      // replace winners by losers
      for (int i = 1; i < n; i++)
         tree[i] = (tree[i] == tree[2 * i]) ? tree[2 * i + 1] : tree[2 * i];
   }
   
   /** replay matches for previous winner */
   public void rePlay()
   {
      if (player == null)
         throw new IllegalArgumentException
                   ("tree must be initialized first");

      // set p to first match node
      int p;
      if (tree[0] <= lowExt)
         p = (tree[0] + offset) / 2;
      else
         p = (tree[0] - lowExt + player.length - 2) / 2;

      // play matches from p to root
      int currentWinner = tree[0];
      for (; p >= 1; p /= 2)
         if (player[tree[p]].winnerOf(player[currentWinner]))
         {// currentWinner loses the match at this node
            int temp = tree[p];
            tree[p] = currentWinner;
            currentWinner = temp;
         }

      tree[0] = currentWinner;   // overall winner
   }

   public void output()
   {
      int n = player.length - 1;
      System.out.println("size = " + n  + " lowExt = "  + lowExt
                          + " offset = "  + offset); 
      System.out.println("Loser tree pointers are");
      for (int i = 0; i < 2 * n; i++)
         System.out.print(tree[i] + " ");
      System.out.println();
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // define a MyInputStream object to input from System.in
      MyInputStream keyboard = new MyInputStream();
   
      // use two data sets
      for (int j = 1; j <= 2; j++)
      {
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
   
         CompleteLoserTree2 w = new CompleteLoserTree2();
         w.initialize(thePlayer);
   
         System.out.println("The loser tree is");
         w.output();
   
         // change player n's value
         thePlayer[n].value = 20;
         w.rePlay();
         System.out.println("Changed player " + n + " to 20, new tree is");
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
}
