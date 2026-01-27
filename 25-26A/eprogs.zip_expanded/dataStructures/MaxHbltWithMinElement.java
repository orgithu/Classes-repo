   
/** height biased leftist trees with minElement */

package dataStructures;

public class MaxHbltWithMinElement implements MaxPriorityQueue
{
   // top-level nested class 
   static class HbltNode
   {
      // data members
      Comparable element;
      int s;                 // shortest value
      HbltNode leftChild;    // pointer to left subtree
      HbltNode rightChild;   // pointer to right subtree

      // constructor
      private HbltNode(Comparable theElement, int theS, HbltNode theChild)
      {
         element = theElement;
         s = theS;
         leftChild = theChild;
         rightChild = theChild;
      }
   }

   // data members of MaxHbltWithMinElement
   HbltNode root;                       // pointer to tree root
   int size;                            // number of elements in tree
   public static HbltNode minNode;      // node with minElement

   // constructor
   public MaxHbltWithMinElement()
      {root = minNode;}

   // methods
   /** @return true iff the tree is empty */
   public boolean isEmpty()
      {return size == 0;}

   /** @return number of elements in the tree */
   public int size()
      {return size;}

   /** @return maximum element
     * @return null if the tree is empty */
   public Comparable getMax()
   {
      if (size == 0) return null;
      else return root.element;
   }

   /** meld the max leftist trees this and x
     * on exit, this is the result */
   public void meld(MaxHbltWithMinElement x)
      {root = meld(root, x.root);}
   
   /** meld the leftist trees with roots x and y
     * @return pointer to root of resulting tree */
   private static HbltNode meld(HbltNode x, HbltNode y)
   {
      if (y == minNode)
         return x;   // y is empty
      if (x == minNode)
         return y;   // x is empty
   
      // neither is empty, swap x and y if necessary
      if (x.element.compareTo(y.element) < 0)
      {// swap x and y
         HbltNode t = x;
         x = y;
         y = t;
      }
      // now x.element >= y.element

      x.rightChild = meld(x.rightChild, y);

      // see if subtrees to be swapped, need not check for empty subtree
      // swap only if left subtree has smaller s value
      if (x.leftChild.s < x.rightChild.s)
      {// swap subtrees
          HbltNode t = x.leftChild;
          x.leftChild = x.rightChild;  
          x.rightChild = t;
      }
      // update s value
      x.s = x.rightChild.s + 1;

      return x;
   }
   

   /** put theElement into the heap */
   public void put(Comparable theElement)
   {
      if (theElement.compareTo(minNode.element) < 0)
         throw new IllegalArgumentException
                   ("cannot insert element smaller than " + minNode.element);

      HbltNode q = new HbltNode (theElement, 1, minNode);
      // meld q and original tree
      root = meld(root, q);
      size++;
   }
   
   /** remove max element and return it */
   public Comparable removeMax()
   {
      if (size == 0)
         return null;   // tree is empty
   
      // tree not empty
      Comparable x = root.element;  // save max element
      root = meld(root.leftChild, root.rightChild);
      size--;
      return x;
   }
   
   /** initialize leftist tree to elements in array theElements */
   public void initialize(Comparable [] theElements, int theSize)
   {
      size = theSize;
      ArrayQueue q = new ArrayQueue(size);
      // initialize queue of trees
      for (int i = 1; i <= size; i++)
      {// create trees with one node each
         if (theElements[i].compareTo(minNode.element) < 0)
         throw new IllegalArgumentException
               ("cannot have elements smaller than " + minNode.element);
         q.put(new HbltNode(theElements[i], 1, minNode));
      }
   
      // repeatedly meld from queue q
      for (int i = 1; i <= size - 1; i++)
      {  // remove and meld two trees from the queue
         HbltNode b = (HbltNode) q.remove();
         HbltNode c = (HbltNode) q.remove();
         b = meld(b, c);
         // put melded tree on queue
         q.put(b);
      }
   
      if (size > 0)
         root = (HbltNode) q.remove();
   }
   
   /** postorder listing */
   private static StringBuffer s;
   public String toString()
   {
      s = new StringBuffer(); 
      s.append("The " + size + " elements, in postorder, are [");
      if (root == minNode)
         s.append("]");
      else
      {
         postOrderString(root);
         s.setCharAt(s.length() - 2, ']');  // replace last comma
      }
      return new String(s);
   }

   /** actual postorder string generator */
   private static void postOrderString(HbltNode t)
   {
      if (t != minNode)
      {
         postOrderString(t.leftChild);
         postOrderString(t.rightChild);
         s.append(t.element + ", ");
      }
  }

   /** test program */
   public static void main(String [] args)
   {
      // static data members
      MaxHbltWithMinElement.minNode = new HbltNode(new Integer(0), 0, null);

      // test constructor and put
      MaxHbltWithMinElement h = new MaxHbltWithMinElement();
      h.put(new Integer(10));
      h.put(new Integer(20));
      h.put(new Integer(5));

      // test toString
      System.out.println("Elements in postorder are");
      System.out.println(h);
      System.out.println();

      h.put(new Integer(15));
      h.put(new Integer(30));

      System.out.println(h);
      System.out.println();

      // test remove max
      System.out.println("The max element is " + h.getMax());
      System.out.println("Deleted max element " + h.removeMax());
      System.out.println("Deleted max element " + h.removeMax());
      System.out.println(h);
      System.out.println();

      // test initialize
      Comparable [] z = new Comparable [10];
      for (int i = 1; i < 10; i++)
         z[i] = new Integer(i);
      h.initialize(z, 9);
      System.out.println(h);
   }
}
