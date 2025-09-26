

/** array-based implementation of LinearList using Equation 5.3 */

package dataStructures;

public class ArrayCircularList implements LinearList
{
   // data members
   protected Object [] element;  // array of elements
   protected int first;          // location of first element
   protected int last;           // location of last element

   // constructors
   /** create a list with initial capacity initialCapacity
     * @throws IllegalArgumentException when
     * initialCapacity < 1 */
   public ArrayCircularList(int initialCapacity)
   {
      if (initialCapacity < 1)
         throw new IllegalArgumentException
               ("initialCapacity must be >= 1");
      // size has the default initial value of 0
      element = new Object [initialCapacity];

      first = -1;   // list is empty
   }

   /** create a list with initial capacity 10 */
   public ArrayCircularList()
   {// use default capacity of 10
      this(10);
   }

   // methods
   /** @return true iff list is empty */
   public boolean isEmpty()
       {return first == -1;}

   /** @return current number of elements in list */
   public int size()
   {
      if (first == -1)
         return 0;
      else
         return (element.length + last - first) % element.length + 1;
   }


   /** @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1 */
   void checkIndex(int index)
   {
      if (index < 0 || index >= size())
         throw new IndexOutOfBoundsException
             ("index = " + index + "  size = " + size());
   }

   /** @return element with specified index
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1 */
   public Object get(int index)
   {
      checkIndex(index);
      return element[(first + index) % element.length];
   }
   
   /** @return index of first occurrence of elem,
     * return -1 if theElement not in list */
   public int indexOf(Object theElement)
   {
      // search element[] for theElement
      int size = size();
      for (int i = 0; i < size; i++)
         if (element[(first + i) % element.length].equals(theElement))
            return i;

      // elem not found
      return -1;
   }      
   
   /** Remove the element with specified index.
     * All elements with higher index have their
     * index reduced by 1.
     * @throws IndexOutOfBoundsException when
     * index is not between 0 and size - 1
     * @return removed element */
   public Object remove(int index)
   {
      Object elementToReturn = get(index);

      // no exception thrown, valid index, remove element
      if (size() == 1)
      {// list becomes empty
         element[first] = null;   // enable garbage collection
         first = -1;
         return elementToReturn;
      }

      // determine which side has fewer elements
      // and shift the smaller number of elements
      int size = size();
      if (index <= (size - 1) / 2)
      {// shift elements index - 1 ... 0
         for (int i = index - 1; i >= 0; i--)
             element[(first + i + 1) % element.length]
                = element[(first + i) % element.length];
         element[first] = null;     // enable gc
         first = (first + 1) % element.length;
      }
      else
      {// shift elements index + 1 ... size() - 1
         for (int i = index + 1; i < size(); i++)
             element[(first + i - 1) % element.length]
                = element[(first + i) % element.length];
         element[last] = null;      // enable gc
         last = (element.length + last - 1) % element.length;
      }
      return elementToReturn;
   }
   
   /** Insert an element with specified index.
     * All elements with equal or higher index
     * have their index increased by 1.
     * @throws InexOutOfBoundsException when
     * index is not between 0 and size */
   public void add(int index, Object theElement)
   {
      if (index < 0 || index > size())
         // invalid list position
         throw new IndexOutOfBoundsException
             ("index = " + index + "  size = " + size());

      // valid index, handle empty list as special case
      if (first == -1)
      {// insert into empty list
         first = last = 0;
         element[0] = theElement;
         return;
      }

      // insert into a nonempty list, make sure we have space
      if (size() == element.length)
      {// no space, double capacity
         // allocate a new array
         Object [] newArray = new Object [2 * element.length];

         // copy elements into new array
         int j = 0;   // position in newArray to copy to
         for (int i = first;
                  i != last; i = (i + 1) % element.length)
            newArray[j++] = element[i];
         newArray[j] = element[last];  // copy last element

         // switch to newArray and set first and last
         element = newArray;
         first = 0;
         last = j;
      }

      // create space for new element
      if (index <= size() / 2)
      {// shift elements 0 through index - 1
            for (int i = 0; i < index; i++)
                element[(element.length + first + i - 1) % element.length]
                   = element[(first + i) % element.length];
            first = (element.length + first - 1) % element.length;
      }
      else
      {// shift elements size() - 1  ... index
          for (int i = size() - 1; i >= index; i--)
              element[(first + i + 1) % element.length]
                 = element[(first + i) % element.length];
          last = (last + 1) % element.length;
      }
          
      // insert new element
      element[(first + index) % element.length] = theElement;
   }
   
   /** convert to a string */
   public String toString()
   {
      int size = size();
      StringBuffer s = new StringBuffer("["); 
      if (size != 0)
      {// nonempty list
         // do first element
         s.append(element[first % element.length].toString());
         for (int i = 1; i < size; i++)
            s.append(", " + element[(first + i) % element.length].toString());
      }
      s.append("]");

      // create equivalent String
      return new String(s);
   }

   /** test program */
   public static void main(String [] args)
   {
      // test default constructor
      LinearList x = new ArrayCircularList();

      // test size
      System.out.println("Initial size is " + x.size());

      // test isEmpty
      if (x.isEmpty())
         System.out.println("The list is empty");
      else System.out.println("The list is not empty");

      // test add
      x.add(0, new Integer(2));
      x.add(1, new Integer(6));
      x.add(0, new Integer(1));
      x.add(2, new Integer(4));
      System.out.println("List size is " + x.size());

      // test toString
      System.out.println("The list is " + x);

      // test indexOf
      int index = x.indexOf(new Integer(4));
      if (index < 0)
         System.out.println("4 not found");
      else System.out.println("The index of 4 is " + index);

      index = x.indexOf(new Integer(3));
      if (index < 0)
         System.out.println("3 not found");
      else 
         System.out.println("The index of 3 is " + index);

      // test get
      System.out.println("Element at 0 is " + x.get(0));
      System.out.println("Element at 3 is " + x.get(3));

      // test remove
      x.remove(1);
      System.out.println("The list is " + x);
      x.remove(2);
      System.out.println("The list is " + x);

      if (x.isEmpty())
         System.out.println("The list is empty");
      else
         System.out.println("The list is not empty");

      System.out.println("List size is " + x.size());
   }
}
