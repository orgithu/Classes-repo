
/** univariate polynomials with integer coefficients represented as
  * circular linked lists with head nodes */

package applications;

import utilities.*;
import exceptions.*;

public class CircularPolynomial
{
   // top-level class
   private static class PolyNode
   {
      // data members
      int coeff;          // coefficient
      int exp;            // exponent
      PolyNode next;      // pointer to next node

      // constructors
      PolyNode(int theCoeff, int theExp, PolyNode theNext)
      {
         coeff = theCoeff;
         exp = theExp;
         next = theNext;
      }

      PolyNode(int theCoeff, int theExp)
      {
         coeff = theCoeff;
         exp = theExp;
      }

      PolyNode() {}
   }
   
   // data members of CircularPolynomial
   PolyNode headNode;
   static PolyNode lastNode;

   // constructors
   /** create the zero polynomial */
   public CircularPolynomial()
   {
      headNode = new PolyNode(0, -1);
      headNode.next = headNode;
   }

   /** @return polynomial degree */
   public int degree()
   {
      if (headNode.next == headNode)   // zero polynomial
         return 0;
      else                             // nonzero polynomial
         return headNode.next.exp;
   }
   

   /** add a new term to the end of the list */
   private void append(int theCoeff, int theExp)
   {
      lastNode.next = new PolyNode(theCoeff, theExp);
      lastNode = lastNode.next;
   }

   /** make a clone */
   public Object clone()
   {
       // w will be the clone
       CircularPolynomial w = new CircularPolynomial();
       lastNode = w.headNode;

       // use currentNode to march through the polynomial this
       PolyNode currentNode = headNode.next;
       while (currentNode != headNode)
       {
          append(currentNode.coeff, currentNode.exp);
          currentNode = currentNode.next;
       }
       // close circular list
       lastNode.next = w.headNode;

       return w;
   }

   /** input the polynomial from the standard input stream */
   public void input(MyInputStream inStream)
   {
      lastNode = headNode;  // use old head node
   
      // input number of terms
      System.out.println("Enter the number of nonzero terms");
      int terms = inStream.readInteger();
      if (terms < 0)
         throw new MyInputException
                   ("number of terms must be >= 0, it is " + terms);

      if (terms > 0)
      {// at least one nonzero term, input the nonzero terms
       // in decreasing order of exponents
         System.out.println("Enter the nonzero terms "
              + "in decreasing order of exponents.\n"
              + "Give input as a sequence e_1, c_1, e_2, c_2, ..."
             );
      
         // get first term
         int exponent = inStream.readInteger();
         int coefficient = inStream.readInteger();
         // exponent must be >= 0 and coefficient must be nonzero
         if (exponent < 0 || coefficient == 0)
            throw new MyInputException
                  ("exponent must be >= 0 and coefficient must " +
                    "not equal 0, they are exponent = " + exponent +
                    " coefficient = " + coefficient);
         append(coefficient, exponent);
         int lastExponent = exponent;
   
         // get the remaining terms
         for (int i = 2; i <= terms; i++)
         {// get next term
            exponent = inStream.readInteger();
            coefficient = inStream.readInteger();
            // exponent must be > lastExponent and coefficient must be nonzero
            if (exponent >= lastExponent || coefficient == 0)
               throw new MyInputException
                     ("exponents must be in descending order " +
                      "and coefficients must not equal 0");
            append(coefficient, exponent);
            lastExponent = exponent;
         }
      }
      
      // complete circular linkage
      lastNode.next = headNode;
   }
   
   /** output the polynomial */
   public void output()
   {
      PolyNode currentNode = headNode.next;
      while (currentNode != headNode)
      {// output a term
         System.out.print(currentNode.exp + " " + currentNode.coeff + " ");
         currentNode = currentNode.next;
      }
      System.out.println();
   }
   
   /** @return this + b */
   public CircularPolynomial add(CircularPolynomial b)
   {
      // define result polynomial w
      CircularPolynomial w = new CircularPolynomial();
      lastNode = w.headNode;          // last node in w

      // define cursors for b and this
      PolyNode cb = b.headNode.next,  // b cursor
               ct = headNode.next;    // cursor for this
   
      // compute result
      while (true)
         if (ct.exp > cb.exp)
         {// this has higher exponent, append ct term to w
            append(ct.coeff, ct.exp);
            ct = ct.next;
         }
         else
           if (ct.exp < cb.exp)
           {// b has higher exponent, append cb term to w
              append(cb.coeff, cb.exp);
              cb = cb.next;
           }
           else
           {// equal exponents, see if we are finished
              if (ct.exp == -1)
              {// returned to headNode nodes
                 lastNode.next = w.headNode;
                 return w;
              }
   
              // not finished, add coeffecients and append if sum is nonzero
              int sum = ct.coeff + cb.coeff;
              if (sum != 0)
                 append(sum, ct.exp);
              ct = ct.next;
              cb = cb.next;
           }
   }
   
   
   /** @return this - b */
   public CircularPolynomial subtract(CircularPolynomial b)
   {
      // define result polynomial w
      CircularPolynomial w = new CircularPolynomial();
      lastNode = w.headNode;          // last node in w

      // define cursors for b and this
      PolyNode cb = b.headNode.next,  // b cursor
               ct = headNode.next;    // cursor for this
   
      // compute result
      while (true)
         if (ct.exp > cb.exp)
         {// this has higher exponent, append ct term to w
            append(ct.coeff, ct.exp);
            ct = ct.next;
         }
         else
           if (ct.exp < cb.exp)
           {// b has higher exponent, append cb term to w
              append(-cb.coeff, cb.exp);
              cb = cb.next;
           }
           else
           {// equal exponents, see if we are finished
              if (ct.exp == -1)
              {// returned to headNode nodes
                 lastNode.next = w.headNode;
                 return w;
              }
   
              // not finished, subtract coeffecients and
              // append if difference is nonzero
              int diff = ct.coeff - cb.coeff;
              if (diff != 0)
                 append(diff, ct.exp);
              ct = ct.next;
              cb = cb.next;
           }
   }
   
   /** @return w = this * b */
   public CircularPolynomial multiply(CircularPolynomial b)
   {
      CircularPolynomial w = new CircularPolynomial();  // result polynomial
   
      // define cursors for w, b, and this
      PolyNode cw,                    // cursor for w
               cb = b.headNode.next,  // b cursor
               ct;                    // cursor for this
      
      // multiply the two polynomials
      while (cb != b.headNode)
      {// multiply this and cb.coeff and add to w
         cw = w.headNode;
         ct = headNode.next;
         while (ct != headNode)
         {
            int e = ct.exp + cb.exp;  // exponent of new term
            int c = ct.coeff * cb.coeff;
   
            // find place to append new term
            while (e < cw.next.exp)
               cw = cw.next;
   
            if (e == cw.next.exp)
            {// add terms
               cw.next.coeff += c;
               if (cw.next.coeff != 0)
                  cw = cw.next;
               else // coefficient is zero, remove the node
                  cw.next = cw.next.next;
            }   
            else
            {// new exponent, insert right of cw
               cw.next = new PolyNode(c, e, cw.next);
               cw = cw.next.next;
            }
   
            ct = ct.next;
         }   
         cb = cb.next;
      }
   
      return w;
   }
   
   /** @return w = this / b, discard remainder */
   public CircularPolynomial divide(CircularPolynomial b)
   {
      if (b.headNode.next == b.headNode)                // division by zero
         throw new IllegalArgumentException
                   ("divisor cannot be zero");
   
      CircularPolynomial w = new CircularPolynomial(),  // result polynomial
            r = (CircularPolynomial) this.clone();      // current remainder
   
      // define pointers to first terms in b and r
      // and to last one in w
      PolyNode fb = b.headNode.next,     // first in b
               fr = r.headNode.next;     // first in r
      lastNode = w.headNode;             // last in w
      
      // divide the two polynomials
      while (fb.exp <= fr.exp)
      {// possible new term in answer
         // compute coefficient of new term
         int c = fr.coeff / fb.coeff;
         if (c == 0) // coefficient is zero, no more terms
            break;
   
         // compute exponent of new term
         int e = fr.exp - fb.exp;
   
         // append new term to w
         append(c, e);
   
         // check if additional terms possible
         if (c * fb.coeff != fr.coeff)
            // no more terms
            break;
         
         // update remainder r by subtracting
         // p * c * x^e from r
         PolyNode cr = r.headNode;
   
         // first term of r is eliminated
         cr.next = cr.next.next;
         
         PolyNode cb = fb.next;
         while (cb != b.headNode)
         {// multiply term of b with c * x^e
            int newCoeff = c * cb.coeff;
            int newExp = e + cb.exp;
            
            // find place for this term in r
            while (newExp < cr.next.exp)
               cr = cr.next;  // new term goes right of cr
   
            // see if r has a term with the same exponent
            if (newExp == cr.next.exp)
            {// subtract terms
               cr.next.coeff -= newCoeff;
               if (cr.next.coeff != 0)
                  cr = cr.next;
               else
                  // coefficient is zero, remove the node
                  cr.next = cr.next.next;
            }   
            else
            {// new exponent, insert right of cr
               cr.next = new PolyNode(-newCoeff, newExp, cr.next);
               cr = cr.next.next;
            }
   
            cb = cb.next;
         }
   
         fr = r.headNode.next;  // fr has changed
      }
   
      // close the list w
      lastNode.next = w.headNode;
   
      return w;
   }
   
   
   /** @return polynomial value at x */
   public int valueOf(int x)
   {
      if (headNode.next == headNode)
         // zero polynomial
         return 0;
   
      if (x == 0)
      {// x is zero
         // find coefficient of x^0
         PolyNode currentNode = headNode.next;
         while(currentNode.next != headNode)
            currentNode = currentNode.next;
         if (currentNode.exp != 0)
            // no term with exponent 0
            return 0;
       
         else  // currentNode points to node for x^0
            return currentNode.coeff;
      }
   
      // compute highest power of x that is needed
      int powerOfX = 1;
   
      for (int i = 1; i <= headNode.next.exp; i++)
         powerOfX *= x;
   
      int currentPower = headNode.next.exp;
          // powerOfX = x^currentPower
   
      // evlauate first term of polynomial
      PolyNode currentNode = headNode.next;
      int value = currentNode.coeff * powerOfX;
   
      // add in remaining terms
      currentNode = currentNode.next;
      while (currentNode != headNode)
      {
         // compute powerOfX for this term
         for (int i = currentPower; i > currentNode.exp; i--)
            powerOfX /= x;
         currentPower = currentNode.exp;
   
         // add in the term
         value += currentNode.coeff * powerOfX;
   
         // move to next term
         currentNode = currentNode.next;
      }
   
      return value;
   }
   
   /** test program */
   public static void main(String [] args)
   {
      // establish input stream
      MyInputStream keyboard = new MyInputStream();

      CircularPolynomial a = new CircularPolynomial();
      CircularPolynomial b = new CircularPolynomial();
      CircularPolynomial c = new CircularPolynomial();
      a.input(keyboard);
      System.out.println("Polynomial a is");
      a.output();
   
      // polynomial with smaller degree than a
      b.input(keyboard);
      System.out.println("Polynomial b is");
      b.output();

      System.out.println("a + b =");
      a.add(b).output();

      System.out.println("a - b =");
      a.subtract(b).output();

      System.out.println("a * b =");
      a.multiply(b).output();

      System.out.println("a / b =");
      a.divide(b).output();
   
      // polynomial with higher degree than a
      b.input(keyboard);
      System.out.println("Polynomial b is");
      b.output();

      System.out.println("a + b =");
      a.add(b).output();

      System.out.println("a - b =");
      a.subtract(b).output();

      System.out.println("a * b =");
      a.multiply(b).output();

      System.out.println("a / b =");
      a.divide(b).output();
   
      // polynomial with same degree as a
      c.input(keyboard);
      System.out.println("Polynomial a is");
      a.output();
      System.out.println("Polynomial b is");
      b.output();
      System.out.println("Polynomial c is");
      c.output();

      System.out.println("a + c =");
      a.add(c).output();

      System.out.println("a - c =");
      a.subtract(c).output();

      System.out.println("a * c =");
      a.multiply(c).output();

      System.out.println("a / c =");
      a.divide(c).output();

      System.out.println("a + b + c =");
      a.add(b).add(c).output();
   
      // a is zero
      a.input(keyboard);
      System.out.println("Polynomial a is");
      a.output();
      System.out.println("Polynomial c is");
      c.output();

      System.out.println("a + c =");
      a.add(c).output();

      System.out.println("a - c =");
      a.subtract(c).output();

      System.out.println("a * c =");
      a.multiply(c).output();
      
      // multiply and divide by 1
      a.input(keyboard);
      System.out.println("Polynomial a is");
      a.output();
      System.out.println("Polynomial c is");
      c.output();

      System.out.println("a * c =");
      a.multiply(c).output();
      
      System.out.println("a / c =");
      a.divide(c).output();
      
      // another two multiplies and  divides
      for (int i = 1; i <= 2; i++)
      {
         a.input(keyboard);
         System.out.println("Polynomial a is");
         a.output();
         c.input(keyboard);
         System.out.println("Polynomial c is");
         c.output();
         System.out.println("a * c =");
         a.multiply(c).output();
         System.out.println("a / c =");
         a.divide(c).output();
      }
   
      // do three evaluations
      System.out.println("a(0) = " + a.valueOf(0));
      System.out.println("a(1) = " + a.valueOf(1));
      System.out.println("a(2) = " + a.valueOf(2));
   }
}
