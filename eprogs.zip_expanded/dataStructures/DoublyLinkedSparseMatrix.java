/** ExtendedLinkedSparseMatrix using Java's LinkedList class */

package dataStructures;

import java.util.*;
import java.lang.reflect.*;
import wrappers.*;
import utilities.*;
import exceptions.*;

public class DoublyLinkedSparseMatrix
{
   // nested top-level classes
   static class RowElement
   {
      // data members
      int col;       // column index of the term
      Object value;  // term value
   
      // constructors
      RowElement(int theColumn, Object theValue)
      {
         col = theColumn;
         value = theValue;
      }

      RowElement() {}
   
      /** convert the term into a string so it can be output */
      public String toString()
         {return new String("column = " + col + " value = " + value);}
   }

   static class HeaderElement
   {
      // data members
      int row;                 // row index of chain
      LinkedList rowChain;  // chain of nonzero terms for this row
   
      // constructors
      HeaderElement(int theRow, LinkedList theChain)
      {
         row = theRow;
         rowChain = theChain;
      }

      HeaderElement(int theRow)
         {this(theRow, new LinkedList());}

      HeaderElement() {}
   
      /** convert the row value into a string so it can be output */
      public String toString()
         {return new String("row = " + row);}
   }


   // data members
   int rows;                    // number of rows in matrix
   int cols;                    // number of columns in matrix
   Object zero;                 // zero element
   LinkedList headerChain;   // header node chain

   // constructors
   // parameter estimatedMaxSize provided for compatibility
   // with SparseMatrixAsExtendedArrayList
   public DoublyLinkedSparseMatrix(int theRows, int theColumns,
                             int estimatedMaxSize, Object theZero)
   {
      // validate theRows and theColumns
      if (theRows < 0 || theColumns < 0)
         throw new IllegalArgumentException
               ("number of rows and columns must be >= 0");
      if ((theRows == 0 && theColumns != 0) ||
          (theRows != 0 && theColumns == 0))
         throw new IllegalArgumentException
               ("both the number of rows and columns must equal " +
                "zero or both must be > 0");
   
      // create the matrix
      rows = theRows;
      cols = theColumns;
      zero = theZero;
      headerChain = new LinkedList();
   }
   
   /** use a default estimated maximum size of 1 */
   public DoublyLinkedSparseMatrix(int theRows, int theColumns, Object theZero)
      {this(theRows, theColumns, 1, theZero);}
   
   /** defaults are rows = cols = estimatedMaxSize = 1 */
   public DoublyLinkedSparseMatrix(Object theZero)
      {this(1, 1, 1, theZero);}
   
   // methods
   /** convert the matrix into a string so it can be output */
   public String toString()
   {
      StringBuffer s = new StringBuffer(); 

      // put matrix characteristics into s
      s.append("rows = " + rows + "  columns = " + cols + "\n");
   
      if (headerChain.isEmpty())  // matrix is empty
         s.append("No nonzero terms \n");
      else
      {// at least one nonzero term
         // access rows using an iterator for headerChain
         Iterator ih = headerChain.iterator();
         while (ih.hasNext())
         {
            HeaderElement hElement = (HeaderElement) ih.next(); 
            // put the row number into the string
            s.append(hElement.toString() + "\n");

            // put the row chain elements into the string
            // indent each element
            Iterator ir = hElement.rowChain.iterator();
            while (ir.hasNext())
               s.append("   " + ir.next() + "\n");
         }
      }
   
      // create equivalent String
      return new String(s);
   }
   

   /** @return the transpose of this
     * matrix values are not cloned */
   public DoublyLinkedSparseMatrix transpose()
   {
      // create result matrix
      DoublyLinkedSparseMatrix t =
            new DoublyLinkedSparseMatrix(cols, rows, zero);
   
      // create bins to collect rows of t
      LinkedList [] bin = new LinkedList [cols + 1];
      for (int i = 1; i <= cols; i++)
          bin[i] = new LinkedList();
       
      // create row i of t in bin[i], 1 <= i <= cols
      // iterator for header node chain
      Iterator ih = headerChain.iterator();
      while (ih.hasNext())
      {// copy all terms in a row chain
          HeaderElement header = (HeaderElement) ih.next();  
          // iterator for the row chain
          Iterator ir = header.rowChain.iterator();
          while (ir.hasNext())
          {// copy a term from the row chain into a bin
              RowElement y = (RowElement) ir.next();

              // create term for a bin
              RowElement z = new RowElement(header.row, y.value);
              // z will eventually be in row y.col of t
              bin[y.col].add(z);
          }
      }

      // assemble header node chain of t
      for (int i = 1; i <= cols; i++)
         if (!bin[i].isEmpty())
            // row i of t is nonempty
            t.headerChain.add(new HeaderElement(i, bin[i]));

      return t;
   }

   /** input a sparse matrix into this from the given input stream */
   public static DoublyLinkedSparseMatrix inputWithValidate(Object theZero,
                                                     MyInputStream stream)
   {
      Method inputMethod;
      Object [] inputMethodArgs = {stream};
      Class [] parameterTypes = {MyInputStream.class};
      try
      {
         // get the proper method to be used to read in the values
         inputMethod = theZero.getClass().
                          getMethod("input", parameterTypes);
      }
      catch (Exception e)
      {
         System.out.println(e); 
         throw new IllegalArgumentException
          ("matrix element type "
           + "must have the static method input() defined");
      }

      // input matrix characteristics
      System.out.println("Enter number of rows, columns, " +
                           "and nonzero terms");
      int theRows = stream.readInteger();
      int theCols = stream.readInteger();
      int theSize = stream.readInteger();
      
      // constructor validates theRows and theCols
      DoublyLinkedSparseMatrix x =
                 new DoublyLinkedSparseMatrix(theRows, theCols, theZero);
  
      // create fictional row zero
      HeaderElement rowHead = new HeaderElement(0);  // current row head node
      int currentRow = 0;
      int currentCol = 0;
     
      // input the nonzero terms
      for (int i = 0; i < theSize; i++)
      {  
         System.out.println("Enter row and column of term " + (i+1));
         RowElement newTerm = new RowElement();
         int newTermRow = stream.readInteger();
         newTerm.col = stream.readInteger();
         try 
            {newTerm.value = inputMethod.invoke(null, inputMethodArgs);}
         catch (Exception e)
         {
            System.out.println(e); 
            throw new IllegalArgumentException
             ("matrix element type "
              + "must have the static method input() defined");
         }

         // validate input
         if (newTermRow < 1 || newTermRow > x.rows ||
             newTerm.col < 1 || newTerm.col > x.cols)
            throw new IndexOutOfBoundsException
                      ("illegal row and/or column index");

         if (newTerm.value == x.zero)
            throw new MyInputException
                      ("term must have nonzero value");

         if (newTermRow < currentRow)
            throw new MyInputException
                      ("terms must be in row-major order");

         // check if new term is part of current row
         if (newTermRow > rowHead.row)
         {// start a new row
            // append head node rowHead of current row to head
            // node chain x.headerChain only if row is not zero
            if (rowHead.row > 0)
               x.headerChain.add(rowHead);
       
            // prepare rowHead for new row
            rowHead = new HeaderElement(newTermRow);
            currentRow = newTermRow;
            currentCol = 0;
         }
         if (newTerm.col <= currentCol)
            throw new MyInputException
                      ("terms must be in row-major order");
       
         // append new term to row chain
         rowHead.rowChain.add(newTerm);
      }     
   
      // take care of last row of matrix
      if (rowHead.row > 0)
      x.headerChain.add(rowHead);
   
      return x;
   }  

   
   /** return a copy of a row chain with head node */
   static HeaderElement copy(HeaderElement h)
   {
      // copy the head node
      HeaderElement hCopy = new HeaderElement(h.row, new LinkedList());
   
      // use a chain iterator to traverse row nodes
      Iterator ir = h.rowChain.iterator();
      while (ir.hasNext())
      {// copy and append the element
         RowElement r = (RowElement) ir.next();
         hCopy.rowChain.add(new RowElement(r.col, r.value));
      }
      return hCopy;
   }
   
   /** @return sum of the row chains with head nodes aHead and bHead */
   static HeaderElement addRows(HeaderElement aHead, HeaderElement bHead,
                              Computable theZero)
   {
      // create head node for result
      HeaderElement sumHead = new HeaderElement(aHead.row, new LinkedList());
   
      // define iterators for the two input chains
      Iterator ia = aHead.rowChain.iterator();
      Iterator ib = bHead.rowChain.iterator();
      RowElement aElement = (RowElement) ia.next();
      RowElement bElement = (RowElement) ib.next();
      
      // go down the chains adding like terms
      while (true)
         if (aElement.col > bElement.col)
         {// copy bElement into sum and advance to next b element
            sumHead.rowChain.add(new RowElement(bElement.col,
                                                bElement.value));
            if (ib.hasNext())
               bElement = (RowElement) ib.next();
            else
            {
               bElement = null;
               break;
            }
         }
         else if (aElement.col < bElement.col)
              {// copy aElement into sum and advance to next a element
                 sumHead.rowChain.add(new RowElement(aElement.col,
                                                     aElement.value));
                 if (ia.hasNext())
                    aElement = (RowElement) ia.next();
                 else
                 {
                    aElement = null;
                    break;
                 }
              }
              else
              {// like terms, add values and append if sum not zero
                 Object x = ((Computable) aElement.value).add
                                          (bElement.value);
                 if (!x.equals(theZero))
                    sumHead.rowChain.add(new RowElement(aElement.col, x));
                 // advance to next element in bHead
                 if (ib.hasNext())
                    bElement = (RowElement) ib.next();
                 else
                    bElement = null;
                 
                 // advance to next element in aHead
                 if (ia.hasNext())
                    aElement = (RowElement) ia.next();
                 else
                    aElement = null;

                if (aElement == null || bElement == null)
                   break;
              }
                 
      // copy remaining terms
      if (aElement != null)
         sumHead.rowChain.add(new RowElement(aElement.col, aElement.value));
      if (bElement != null)
         sumHead.rowChain.add(new RowElement(bElement.col, bElement.value));
      
      while (ia.hasNext())
      {
         aElement = (RowElement) ia.next();
         sumHead.rowChain.add(new RowElement(aElement.col, aElement.value));
      }
      while (ib.hasNext())
      {
         bElement = (RowElement) ib.next();
         sumHead.rowChain.add(new RowElement(bElement.col, bElement.value));
      }
   
      if (sumHead.rowChain.isEmpty())
         return null;
      else
         return sumHead;
   }
    
   
   /** @return this + b */
   public DoublyLinkedSparseMatrix add(DoublyLinkedSparseMatrix b)
   {
   
      // verify compatibility
      if (rows != b.rows || cols != b.cols)
        throw new IllegalArgumentException
                  ( "the dimensions of the two matrices must be the same");
   
      // create result matrix c
      DoublyLinkedSparseMatrix c =
              new DoublyLinkedSparseMatrix(rows, cols, zero);
   
      // define iteratorsit and ib for head node chains of this and b
      Iterator it = headerChain.iterator();
      Iterator ib = b.headerChain.iterator();
      HeaderElement tElement = null;
      if (it.hasNext())
         tElement = (HeaderElement) it.next();
      HeaderElement bElement = null;
      if (ib.hasNext())
         bElement = (HeaderElement) ib.next();
   
      // go down the head node chains until we fall off one of them
      while (tElement != null && bElement != null)
      {
         // compare row numbers
         if (tElement.row < bElement.row)
         {// copy row of this and append to c.headerChain
            c.headerChain.add(copy(tElement));
            if (it.hasNext())
               tElement = (HeaderElement) it.next();
            else
               tElement = null;
         }
         else if (tElement.row > bElement.row)
              {// copy row of b and append to c.headerChain
                 c.headerChain.add(copy(bElement));
                 if (ib.hasNext())
                    bElement = (HeaderElement) ib.next();
                 else
                    bElement = null;
              }
              else
              {// rows are the same, add and append to c.headerChain
                 HeaderElement sum = addRows(tElement, bElement,
                                           (Computable) zero);
                 if (sum != null)
                    // append to head node chain
                    c.headerChain.add(sum);
                 if (it.hasNext())
                    tElement = (HeaderElement) it.next();
                 else
                    tElement = null;
                      if (ib.hasNext())
                         bElement = (HeaderElement) ib.next();
                      else
                         bElement = null;
              }
      }
   
      // take care of remaining rows
      if (tElement != null)
         c.headerChain.add(copy(tElement));
      if (bElement != null)
         c.headerChain.add(copy(bElement));
      while (it.hasNext())
         // copy a row of this
         c.headerChain.add(copy((HeaderElement) it.next()));
            
      while (ib.hasNext())
         // copy a row of b
         c.headerChain.add(copy((HeaderElement) ib.next()));
   
      return c;
   }
   
   /** return a copy of the negative of a row chain with head node */
   static HeaderElement minusCopy(HeaderElement h, Computable theZero)
   {
      // copy the head node
      HeaderElement hCopy = new HeaderElement(h.row, new LinkedList());
   
      // use a chain iterator to traverse row nodes
      Iterator ir = h.rowChain.iterator();
      while (ir.hasNext())
      {// copy and append the negative of the element
         RowElement r = (RowElement) ir.next();
         hCopy.rowChain.add(new RowElement(r.col,
                                   theZero.subtract(r.value)));
      }
      return hCopy;
   }
   
   /** @return the difference row chains with head nodes aHead and bHead */
   static HeaderElement subtractRows(HeaderElement aHead, HeaderElement bHead,
                                   Computable theZero)
   {
      // create head node for result
      HeaderElement diffHead = new HeaderElement(aHead.row, new LinkedList());
   
      // define iterators for the two input chains
      Iterator ia = aHead.rowChain.iterator();
      Iterator ib = bHead.rowChain.iterator();
      RowElement aElement = (RowElement) ia.next();
      RowElement bElement = (RowElement) ib.next();
      
      // go down the chains subtracting like terms
      while (true)
         if (aElement.col > bElement.col)
         {// copy negative of bElement into answer and advance to next b element
            diffHead.rowChain.add(new RowElement(bElement.col,
                                      theZero.subtract(bElement.value)));
            if (ib.hasNext())
               bElement = (RowElement) ib.next();
            else
            {
               bElement = null;
               break;
            }
         }
         else if (aElement.col < bElement.col)
              {// copy aElement into answer and advance to next a element
                 diffHead.rowChain.add(new RowElement(aElement.col,
                                                         aElement.value));
                 if (ia.hasNext())
                    aElement = (RowElement) ia.next();
                 else
                 {
                    aElement = null;
                    break;
                 }
              }
              else
              {// like terms, subtract values and append if difference not zero
                 Object x = ((Computable) aElement.value)
                            .subtract(bElement.value);
                 if (!x.equals(theZero))
                    diffHead.rowChain.add(new RowElement(aElement.col, x));
                 // advance to next element in bHead
                 if (ib.hasNext())
                    bElement = (RowElement) ib.next();
                 else
                    bElement = null;
                 
                 // advance to next element in aHead
                 if (ia.hasNext())
                    aElement = (RowElement) ia.next();
                 else
                    aElement = null;

                if (aElement == null || bElement == null)
                   break;
             }
                 
      // copy remaining terms
      if (aElement != null)
         diffHead.rowChain.add(new RowElement(aElement.col, aElement.value));
      if (bElement != null)
         diffHead.rowChain.add(new RowElement(bElement.col,
                               theZero.subtract(bElement.value)));
      
      while (ia.hasNext())
      {
         aElement = (RowElement) ia.next();
         diffHead.rowChain.add(new RowElement(aElement.col, aElement.value));
      }
      while (ib.hasNext())
      {
         bElement = (RowElement) ib.next();
         diffHead.rowChain.add(new RowElement(bElement.col,
                               theZero.subtract(bElement.value)));
      }
   
      if (diffHead.rowChain.isEmpty())
         return null;
      else
         return diffHead;
   }
    
   
   /** @return this - b */
   public DoublyLinkedSparseMatrix subtract(DoublyLinkedSparseMatrix b)
   {
   
      // verify compatibility
      if (rows != b.rows || cols != b.cols)
        throw new IllegalArgumentException
        ("the dimensions of the two matrices must be the same");
   
      // create result matrix c
      DoublyLinkedSparseMatrix c =
              new DoublyLinkedSparseMatrix(rows, cols, zero);
   
      // define iteratorsit and ib for head node chains of this and b
      Iterator it = headerChain.iterator();
      Iterator ib = b.headerChain.iterator();
      HeaderElement tElement = null;
      if (it.hasNext())
         tElement = (HeaderElement) it.next();
      HeaderElement bElement = null;
      if (ib.hasNext())
         bElement = (HeaderElement) ib.next();
   
      // go down the head node chains until we fall off one of them
      while (tElement != null && bElement != null)
      {
         // compare row numbers
         if (tElement.row < bElement.row)
         {// copy row of this and append to c.headerChain
            c.headerChain.add(copy(tElement));
            if (it.hasNext())
               tElement = (HeaderElement) it.next();
            else
               tElement = null;
         }
         else if (tElement.row > bElement.row)
              {// copy negative of row of b and append to c.headerChain
                 c.headerChain.add(minusCopy(bElement, (Computable) zero));
                 if (ib.hasNext())
                    bElement = (HeaderElement) ib.next();
                 else
                    bElement = null;
              }
              else
              {// rows are the same, subtract and append to c.headerChain
                 HeaderElement sum = subtractRows(tElement, bElement,
                                           (Computable) zero);
                 if (sum != null)
                    // append to head node chain
                    c.headerChain.add(sum);
                 if (it.hasNext())
                    tElement = (HeaderElement) it.next();
                 else
                    tElement = null;
                      if (ib.hasNext())
                         bElement = (HeaderElement) ib.next();
                      else
                         bElement = null;
              }
      }
   
      // take care of remaining rows
      if (tElement != null)
         c.headerChain.add(copy(tElement));
      if (bElement != null)
         c.headerChain.add(minusCopy(bElement, (Computable) zero));
      while (it.hasNext())
         // copy a row of this
         c.headerChain.add(copy((HeaderElement) it.next()));
            
      while (ib.hasNext())
         // copy a row of b
         c.headerChain.add(minusCopy((HeaderElement) ib.next(),
                                      (Computable) zero));
   
      return c;
   }
}
