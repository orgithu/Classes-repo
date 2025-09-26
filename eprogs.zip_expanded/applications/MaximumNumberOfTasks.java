/** greedy single-machine scheduling */

package applications;

import dataStructures.*;
import utilities.*;

public class MaximumNumberOfTasks
{
   // top-level nested class
   static class Task implements Comparable
   {
      // instance data members
      int start;                  // task start time
      int finish;                 // task finish time
      int id;                     // task ID

      // constructor
      Task(int theStart, int theFinish, int theId)
      {
         start = theStart;
         finish = theFinish;
         id = theId;
      }

      // method of Comparable 
      public int compareTo(Object x)
      {
         int xStart = ((Task) x).start;
         if (start < xStart)
            return -1;
         if (start == xStart)
            return 0;
         return 1;
      }
   
      /** @return true iff this = x */
      public boolean equals(Object x)
         {return start == ((Task) x).start;}
   }
   
   /** input tasks and output a maximum selection that can be done
     * on a single machine */
   public static void main(String [] args)
   {
      // establish input stream as System.in
      MyInputStream keyboard = new MyInputStream();

      // input the number of tasks
      System.out.println("Enter the number of tasks");
      int n = keyboard.readInteger();
      if (n < 1)
      {
         System.out.println("Number of tasks must be more than 0");
         System.exit(1);
      }
   
      // input and store the n tasks in a task array
      Task [] t = new Task [n + 1];
      for (int i = 1; i <= n; i++)
      {
         System.out.println("Enter the start and finish time of task " + i);
         t[i] = new Task(keyboard.readInteger(),
                         keyboard.readInteger(),
                         i);
         if (t[i].start < 0 || t[i].finish <= 0
             || t[i].start >= t[i].finish)
         {
             System.out.println("Bad start and/or finish time");
             System.exit(1);
          }
      }
   
      // initialize a min heap with n tasks
      MinHeap h = new MinHeap(1);
      h.initialize(t, n);

      // select tasks
      System.out.print("The selected tasks are ");
      int avail = 0;                // time machine gets free
      for (int i = 1; i <= n; i++)
      {
         // get task with least finish time
         Task w = (Task) h.removeMin();
         if (w.start >= avail)
         {// select the task
            System.out.print(w.id + " ");
            avail = w.finish;
         }
      }  
      System.out.println();
   }
}
