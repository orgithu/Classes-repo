/** average completion time with one person */

package applications;

import dataStructures.*;

public class AverageCompletionTime
{
   // top-level nested class
   static class Task implements Comparable
   {
      // instance data members
      int time;                  // task length
      int id;                    // task id

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
     * average completion time is minimized */
   public static void minimizeAverageCompletionTime(Task [] task)
      {HeapSort.heapSort(task);}
   
   /** test minimizeAverageCompletionTime */
   public static void main(String [] args)
   {
      int n = 12;  // number of tasks
      Task [] t = new Task [13];
      // create n tasks
      for (int i = 1; i <= n; i++)
         t[i] = new Task(20 - i, i);

      minimizeAverageCompletionTime(t);

      System.out.println("The tasks should be done in the order");
      for (int i = 1; i <= n; i++)
         System.out.print(t[i].id + " ");
      System.out.println();
   }
}
