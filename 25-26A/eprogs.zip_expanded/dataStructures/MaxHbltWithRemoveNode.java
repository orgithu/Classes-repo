   
/** height biased leftist trees with the methods
  * putAndReturnNode and removeElementInNode 
  * each node other than the root points to its parent
  * the parent field of the root may have any value */

package dataStructures;

public class MaxHbltWithRemoveNode implements MaxPriorityQueue
{
   // top-level nested class 
   public static class HbltNode
   {
      // data members
      Comparable element;
      int s;                 // shortest value
      HbltNode leftChild;    // pointer to left subtree
      HbltNode rightChild;   // pointer to right subtree
      HbltNode parent;       // pointer to parent

      // constructor
      private HbltNode(Comparable theElement, int theS)
      {
         element = theElement;
         s = theS;
      }
   }

   // data members of MaxHblt
   HbltNode root;   // pointer to tree root
   int size;        // number of elements in tree

   // only default constructor available

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
      if (size == 0)
         return null;
      else
         return root.element;
   }

   /** meld the max leftist trees this and x
     * on exit, this is the result */
   public void meld(MaxHbltWithRemoveNode x)
      {root = meld(root, x.root);}
   
   /** meld the leftist trees with roots x and y
     * @return pointer to root of resulting tree */
   private static HbltNode meld(HbltNode x, HbltNode y)
   {
      if (y == null)
         return x;   // y is empty
      if (x == null)
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
      x.rightChild.parent = x;   // x.rightChild cannot be null

      // swap subtrees of x if necessary and set x.s
      if (x.leftChild == null)
      {// left subtree is empty, swap the subtrees
          x.leftChild = x.rightChild;
          x.rightChild = null;
          x.s = 1;
       }
      else
      {// swap only if left subtree has smaller s value
         if (x.leftChild.s < x.rightChild.s)
         {// swap subtrees
             HbltNode t = x.leftChild;
             x.leftChild = x.rightChild;  
             x.rightChild = t;
         }
         // update s value
         x.s = x.rightChild.s + 1;
      }
      return x;
   }
   

   /** put theElement into the heap */
   public void put(Comparable theElement)
   {
      HbltNode q = new HbltNode (theElement, 1);
      // meld q and original tree
      root = meld(root, q);
      size++;
   }

   /** put theElement into the heap
     * @return node into which theElement is put */
   public HbltNode putAndReturnNode(Comparable theElement)
   {
      HbltNode q = new HbltNode (theElement, 1);
      // meld q and original tree
      root = meld(root, q);
      size++;
      return q;
   }
   
   /** remove max element and return it */
   public Comparable removeMax()
   {
      if (size == 0) return null;   // tree is empty
   
      // tree not empty
      Comparable x = root.element;  // save max element
      root = meld(root.leftChild, root.rightChild);
      size--;
      return x;
   }
   
   /** remove element in node theNode
     * @return removed element */
   public Comparable removeElementInNode(HbltNode theNode)
   {
      if (theNode == null)
         return null;   // no node given
   
      // meld subtrees of theNode
      HbltNode t = meld(theNode.leftChild, theNode.rightChild);

      if (theNode == root)
         // t is the new root
         root = t;
      else
      {// replace theNode by t
          HbltNode p = theNode.parent;
          if (theNode == p.leftChild)
             p.leftChild = t;
          else
             p.rightChild = t;
          if (t != null)
             t.parent = p;

          // swap left and right subtrees of p if necessary and update p.s
          int leftS = (p.leftChild == null) ? 0 : p.leftChild.s;
          int rightS = (p.rightChild == null) ? 0 : p.rightChild.s;
          if (leftS < rightS)
          {// swap left and right subtrees
             HbltNode temp = p.rightChild;
             p.rightChild = p.leftChild;
             p.leftChild = temp;
          }

          int newS = Math.min(leftS, rightS) + 1;  // new p.s
        
          // see if s value of p changes
          if (newS != p.s)
          {// s value changes
             p.s = newS;
             // other s values on path to root may also change
             while (p != root && p.parent.s != 1)
             {// p has a parent whose s value may change
                p = p.parent;
                // p has 2 children because p.s != 1
                if (p.leftChild.s < p.rightChild.s)
                {// swap subtrees
                   HbltNode temp = p.leftChild;
                   p.leftChild = p.rightChild;
                   p.rightChild = temp;
                }

                // determine new s value of p
                newS = p.rightChild.s + 1;
      
                // does s change
                if (newS != p.s)
                   p.s = newS;
                else
                   // no change in p.s, no other s values can change
                   break;
            }
         }   
      }  

      size--;
      return theNode.element;
   }
   
   /** initialize leftist tree to elements in array theElements */
   public void initialize(Comparable [] theElements, int theSize)
   {
      size = theSize;
      ArrayQueue q = new ArrayQueue(size);
      // initialize queue of trees
      for (int i = 1; i <= size; i++)
         // create trees with one node each
         q.put(new HbltNode(theElements[i], 1));
   
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
      if (root == null)
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
      if (t != null)
      {
         postOrderString(t.leftChild);
         postOrderString(t.rightChild);
         s.append(t.element + ", ");
      }
  }

   /** test program */
   public static void main(String [] args)
   {
      // test constructor and put
      MaxHbltWithRemoveNode h = new MaxHbltWithRemoveNode();

      int [] data = {5, 4, 3, 6, 2, 1, 0, 20, 15, 12, 10, 8, 7, 11, 30, 40, 50,
                     35, 36};
      HbltNode [] node = new HbltNode [data.length];
      for (int i = 0; i < data.length; i++)
         node[i] = h.putAndReturnNode(new Integer(data[i]));
      System.out.println(h);

      System.out.println("Removed element " + h.removeElementInNode(node[1]));
      System.out.println(h);

      System.out.println("Removed element " + h.removeElementInNode(node[3]));
      System.out.println(h);

      System.out.println("Removed element " + h.removeElementInNode(node[2]));
      System.out.println(h);

      System.out.println("Removed element " + h.removeElementInNode(node[8]));
      System.out.println(h);

      System.out.println("Removed element " + h.removeElementInNode(node[16]));
      System.out.println(h);
   }
}
