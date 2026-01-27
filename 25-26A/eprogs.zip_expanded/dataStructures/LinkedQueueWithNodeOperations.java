/** extension of LinkedQueue to include methods to add and
  * remove nodes */

package dataStructures;


public class LinkedQueueWithNodeOperations
             extends LinkedQueue
{
   // constructors
   /** create an empty queue */
   public LinkedQueueWithNodeOperations(int initialCapacity)
      {super(initialCapacity);}

   public LinkedQueueWithNodeOperations()
      {this(0);}

   // new methods
   /** insert theNode at the rear of the queue */
   public void putNode(ChainNode theNode)
   {
      // append p to the chain
      if (front == null)
         front = theNode;                 // empty queue
      else 
         rear.next = theNode;             // nonempty queue

      rear = theNode;
      rear.next = null;
   }

   /** remove a node from the front of the queue
     * @return removed node
     * @return null if the queue is empty */
   public ChainNode removeNode()
   {
      if (isEmpty())
         return null;
      ChainNode frontNode = front;
      front = front.next;
      return frontNode;
   }
   
   /** test program */
   public static void main(String [] args)
   {  
      int x;
      LinkedQueueWithNodeOperations q = new LinkedQueueWithNodeOperations(3);
      // add a few elements
      q.put(new Integer(1));
      q.put(new Integer(2));
      q.put(new Integer(3));
      q.put(new Integer(4));


      ChainNode [] oldNode = new ChainNode [4];
      int index = 0;
      // delete all elements
      while (!q.isEmpty())
      {
         System.out.println("Rear element is " + q.getRearElement());
         System.out.println("Front element is " + q.getFrontElement());
         oldNode[index] = q.removeNode();
         System.out.println("Removed the element " + oldNode[index++].element);
      }

      // reuse the nodes
      q.putNode(oldNode[3]);
      q.putNode(oldNode[2]);
      q.putNode(oldNode[1]);
      q.putNode(oldNode[0]);

      // delete all elements
      while (!q.isEmpty())
      {
         System.out.println("Rear element is " + q.getRearElement());
         System.out.println("Front element is " + q.getFrontElement());
         System.out.println("Removed the element " + q.remove());
      }
   }  
}
