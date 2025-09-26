/** LinkedSparseMatrix extended to include get, set, add, and
  * subtract operations */

package dataStructures;

import java.util.*;
import java.lang.reflect.*;
import wrappers.*;
import utilities.*;
import exceptions.*;

public class ExtendedLinkedSparseMatrix extends LinkedSparseMatrix
{
   // constructors
   public ExtendedLinkedSparseMatrix(int theRows, int theColumns,
                              int estimatedMaxSize, Object theZero)
      {super(theRows, theColumns, estimatedMaxSize, theZero);}

   public ExtendedLinkedSparseMatrix(int theRows, int theColumns,
                              Object theZero)
      {super(theRows, theColumns, theZero);}

   // new methods
   /** input a sparse matrix into this from the given input stream */
   public static ExtendedLinkedSparseMatrix inputWithValidate(Object theZero,
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
      ExtendedLinkedSparseMatrix x =
                 new ExtendedLinkedSparseMatrix(theRows, theCols, theZero);
  
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
   
   /** @throws IndexOutOfBoundsException when i < 1
     * or j < 1 or i > rows or j > cols */
   void checkIndex(int i, int j)
   {
      if (i < 1 || j < 1 || i > rows || j > cols)
         throw new IndexOutOfBoundsException
                   ("i = " + i + " j = " + j +
                    " rows = " + rows + " cols = " + cols);
   }

   /** save theValue as element (theRow,theCol)
     * @throws IndexOutOfBoundsException when i or j invalid */
   public void set(int theRow, int theCol, Object theValue)
   {
      checkIndex(theRow, theCol);
   
      // define an iterator ih for the head node chain
      Iterator ih = headerChain.iterator();
   
      // advance to the head node for row theRow
      int hCount = 0;  // distance from start of head node chain
      HeaderElement rowHead = null;
      while (ih.hasNext())
      {
         rowHead = (HeaderElement) ih.next();
         hCount++;
         if (rowHead.row >= theRow) 
            break;
      }
      if (hCount > 0 && rowHead.row >= theRow)
         hCount--;
     
      if (rowHead == null || rowHead.row != theRow)
      {// no row chain for row theRow
         // do not store a zero element
         if (theValue.equals(zero))
            return;
   
         // create a new row chain
         // first create the chain for the new row
         ExtendedChain newChain = new ExtendedChain();
         // now create a row chain node for new element
         RowElement rowElement = new RowElement(theCol, theValue);
         // now add the row chain element to the new chain
         newChain.add(rowElement);
   
         // now create a head element for the new chain
         HeaderElement headElement = new HeaderElement(theRow, newChain);
   
   
         // insert headElement into the head node chain
         headerChain.add(hCount, headElement);
         return;
      }
   
      // chain for row theRow exists, search this chain for
      // a column theCol element
      // define an iterator ir for the row chain
      Iterator ir = rowHead.rowChain.iterator();
    
      // advance to node with column value theCol
      RowElement rowElement = null;
      int rCount = 0;  // distance from start of row chain
    
      while (ir.hasNext())
      {
         rowElement = (RowElement) ir.next();
         rCount++;
         if (rowElement.col >= theCol)
            break;
      }
      if (rCount > 0 && rowElement.col >= theCol)
         rCount--;
       
      if (rowElement == null || rowElement.col > theCol)
      {// no node for (theRow,theCol) element
         // do not store a zero element
         if (theValue == zero)
            return;
         // create node for new element
         rowElement = new RowElement(theCol, theValue);
         // insert rowElement into row chain
         rowHead.rowChain.add(rCount, rowElement);
         return;
      }
   
      // there is already a (theRow, theCol) element, change it
      rowHead.rowChain.remove(rCount);
      if (theValue.equals(zero))
      {// check if row chain empty
         if (rowHead.rowChain.isEmpty())
            // delete row chain
            headerChain.remove(hCount);
         return;
      }
   
      // insert new value for (theRow,theCol)
      rowHead.rowChain.add(rCount, new RowElement(theCol, theValue));
   }
   
   /** @return value of matrix element (theRow, theCol)
     * @throws IndexOutOfBoundsException when i or j invalid */
   public Object get(int theRow, int theCol)
   {
      checkIndex(theRow, theCol);
   
      // define an iterator ih for the head node chain
      Iterator ih = headerChain.iterator();
   
      // advance to head node for proper row
      HeaderElement rowHead = null;
      while (ih.hasNext())
      {
         rowHead = (HeaderElement) ih.next();
         if (rowHead.row >= theRow)
            break;
      }
     
      if (rowHead == null || rowHead.row != theRow) // no row chain for theRow
         return zero;
   
      // define an iterator ir for the row chain
      Iterator ir = rowHead.rowChain.iterator();
    
      // define rowElement to point to current row chain node
      RowElement rowElement = null;
    
      // advance ir to node with column value theCol
      while (ir.hasNext())
      {
         rowElement = (RowElement) ir.next();
         if (rowElement.col >= theCol)
            break;
      }
       
      if (rowElement == null || rowElement.col != theCol)
         // no node for (theRow,theCol) element
         return zero;
      else
         return rowElement.value;
   }
    
   
   /** return a copy of a row chain with head node */
   static HeaderElement copy(HeaderElement h)
   {
      // copy the head node
      HeaderElement hCopy = new HeaderElement(h.row, new ExtendedChain());
   
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
      HeaderElement sumHead = new HeaderElement(aHead.row, new ExtendedChain());
   
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
   public ExtendedLinkedSparseMatrix add(ExtendedLinkedSparseMatrix b)
   {
   
      // verify compatibility
      if (rows != b.rows || cols != b.cols)
        throw new IllegalArgumentException
                  ( "the dimensions of the two matrices must be the same");
   
      // create result matrix c
      ExtendedLinkedSparseMatrix c =
              new ExtendedLinkedSparseMatrix(rows, cols, zero);
   
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
      HeaderElement hCopy = new HeaderElement(h.row, new ExtendedChain());
   
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
      HeaderElement diffHead = new HeaderElement(aHead.row, new ExtendedChain());
   
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
   public ExtendedLinkedSparseMatrix subtract(ExtendedLinkedSparseMatrix b)
   {
   
      // verify compatibility
      if (rows != b.rows || cols != b.cols)
        throw new IllegalArgumentException
        ("the dimensions of the two matrices must be the same");
   
      // create result matrix c
      ExtendedLinkedSparseMatrix c =
              new ExtendedLinkedSparseMatrix(rows, cols, zero);
   
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

   /** test program */
   public static void main(String [] args)
   {
      // establish input stream as standard input stream
      MyInputStream keyboard = new MyInputStream();

      ExtendedLinkedSparseMatrix a =
              inputWithValidate(new MyInteger(0), keyboard);
      
      // test get method
      System.out.println("Matrix A is\n" + a);
      System.out.println();
      System.out.println("A(1,2) = " + a.get(1,2));
      System.out.println("A(1,4) = " + a.get(1,4));
      System.out.println("A(1,5) = " + a.get(1,5));
      System.out.println("A(2,2) = " + a.get(2,2));
      System.out.println("A(3,1) = " + a.get(3,1));
      System.out.println("A(4,3) = " + a.get(4,3));
      System.out.println();

      // test set method
      for (int i = 1; i <= 6; i++)
      {
         System.out.println("Enter row, col, and value of term to store");
         int row = keyboard.readInteger();
         int col = keyboard.readInteger();
         int value = keyboard.readInteger();
         a.set(row, col, new MyInteger(value));
      }
      System.out.println("The matrix is now" + a);
      System.out.println();
   
      // input second matrix for add operation
      ExtendedLinkedSparseMatrix b = 
              inputWithValidate(new MyInteger(0), keyboard);
      System.out.println("The second matrix is\n" + b);
      System.out.println();
      System.out.println("Their sum is\n" + a.add(b));
   
      // lets try a subtraction
      a = inputWithValidate(new MyInteger(0), keyboard);
      b = inputWithValidate(new MyInteger(0), keyboard);
      System.out.println("The difference of the matrices\n" + a);
      System.out.println("\nand\n" + b + "\nis\n");
      System.out.println(a.subtract(b));
    }
}
