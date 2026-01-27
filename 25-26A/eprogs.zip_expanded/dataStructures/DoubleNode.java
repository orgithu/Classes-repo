
/** Node class used by doubly linked structures.
  * This class and its data members are
  * visible only within the package dataStructures. */

package dataStructures;


class DoubleNode
{
   // package visible data members
   Object element;
   DoubleNode next;
   DoubleNode previous;

   // package visible constructors
   DoubleNode() {}
     
   DoubleNode(Object theElement)
      {element = theElement;}

   DoubleNode(Object theElement, DoubleNode thePrevious, DoubleNode theNext)
   {
       element = theElement;
       previous = thePrevious;
       next = theNext;
   }
}
