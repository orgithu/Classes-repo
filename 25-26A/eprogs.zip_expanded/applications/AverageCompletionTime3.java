/** average completion time with m persons */

package applications;

import dataStructures.*;

public class AverageCompletionTime3
{
   // top-level nested class
   static class Task implements Comparable
   {
      // instance data members
      int time;                  // task length
      int id;                    // task id
      int person;                // person who is to do the task

      // constructor
      Task(int theTime, int theId)
      {
         time = theTime;
         id = theId;
      }

     // method of Comparable
     /** @return true iff this < x */
     public int compareTo(Object x)
     {
        int xTime = ((Task) x).time;
        if (time < xTime)
           return -1;
        if (time == xTime)
           return 0;
        return 1;
      }

      /** @return true iff this == x */
      public boolean equals(Object x)
         {return time == ((Task) x).time;}
   }

   /** reorder the tasks task[1:task.length-1] so that the
     * average completeion time is minimized
     * @param m is the number of persons available to do tasks */
   public static void minimizeAverageCompletionTime(Task [] task, int m)
   {
      HeapSort.heapSort(task);
      // assign tasks to persons in round-robin fashion
      for (int i = 1; i <= task.length - 1; i++)
         task[i].person = (i-1) % m + 1;
   }
   
   /** test minimizeAverageCompletionTime */
   public static void main(String [] args)
   {
      int n = 12;  // number of tasks
      int m = 5;   // number of persons
      Task [] t = new Task [13];
      // create n tasks
      for (int i = 1; i <= n; i++)
         t[i] = new Task(20 - i, i);

      minimizeAverageCompletionTime(t, m);

      System.out.println("The tasks should be done in the order");
      for (int i = 1; i <= n; i++)
         System.out.println(t[i].id + " by person " + t[i].person);
   }
}
