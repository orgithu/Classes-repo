package lab5;
import java.util.*;
import dataStructures.ArrayStack;
enum ExpressionType {
	INFIX,
	PREFIX,
	POSTFIX;
}
public class Expression extends dataStructures.LinkedBinaryTree{
	
	private String notationExp;
	private ExpressionType eType;
	Scanner scan;

	public Expression() {
		super();
		notationExp="";
		eType = ExpressionType.INFIX;
		scan = new Scanner(System.in);
	}
	public void readInfixExpression() {
		eType = ExpressionType.INFIX;
		System.out.println(eType + "enter expression");
		this.notationExp = scan.nextLine();
	}
	public void readPostfixExpression() {
		eType = ExpressionType.POSTFIX;
		System.out.println(eType + "enter expression");
		this.notationExp = scan.nextLine();
	}
	public void readPrefixExpression() {
		eType = ExpressionType.PREFIX;
		System.out.println(eType + "enter expression");
		this.notationExp = scan.nextLine();
	}
	private int Prec(char ch) {
		switch (ch) {
			case '+':
			case '-':
				return 1;
			case '*':
			case '/':
				return 2;
			case '^':
				return 3;
		}
		return -1;
	}
	public void infixToPostfix() {
		if(eType == ExpressionType.INFIX) {
			eType = ExpressionType.POSTFIX;
			String result = new String("");
			ArrayStack stack = new ArrayStack();
			String [] elems = notationExp.split(" ");
			for (int i = 0; i < elems.length;i++) {
				if (elems[i].length() == 0) continue;
				String token = elems[i].trim();
				if (Character.isLetterOrDigit(token.charAt(0)))
					result += token+" ";
				else if (token.charAt(0) == '(')
					stack.push(token);
				else if (token.charAt(0) == ')') {
					while (!stack.empty() && stack.peek().toString().charAt(0) != '(') {
						result += stack.peek()+" ";
						stack.pop();
					}
					stack.pop();
				}
				else {
					while (!stack.empty() && Prec(token.charAt(0)) <= Prec(stack.peek().toString().charAt(0))) {
						result += stack.peek()+" ";
						stack.pop();
					}
					stack.push(token);
				}
			}
			while (!stack.empty()) {
				if (stack.peek().toString().charAt(0) == '(')
					System.out.println("wrong expression");
				result += stack.peek()+" ";
				stack.pop();
			}
			this.notationExp = result;

		}
	}
	public void prefixToPostfix() {
		if (eType == ExpressionType.PREFIX) {
			eType = ExpressionType.POSTFIX;
			String result = new String("");
			ArrayStack stack = new ArrayStack();
			String [] elems = notationExp.split(" ");
			for (int i = elems.length - 1; i>=0; i--) {
				if (elems[i].length() == 0) continue;
				String token = elems[i].trim();
				if (token.length() == 1 && !Character.isLetterOrDigit(token.charAt(0))) {
					String op1 = stack.peek().toString();
					stack.pop();
					String op2 = stack.peek().toString();
					stack.pop();
					String temp = op1 + op2 + token.charAt(0)+" ";
					stack.push(temp);
				} else {
				stack.push(token+ " ");
				}
			}
			this.notationExp = stack.peek().toString();
		}
	}
	public void expressionTreeFromPostFix() {
		ArrayStack stN = new ArrayStack();
		Expression t1,t2,temp;
		String [] elems = notationExp.split(" ");
		for (int i = 0; i < elems.length; i++) {
			// Skip empty tokens (may appear from trailing spaces)
			if (elems[i].length() == 0) continue;
			if(Character.isLetterOrDigit(elems[i].charAt(0))) {
				temp = new Expression();
				// create leaf with operand as root and empty subtrees
				temp.makeTree(elems[i], new Expression(), new Expression());
				stN.push(temp);
			} else {
				temp = new Expression();
				t1 = (Expression)stN.pop();
				t2 = (Expression)stN.pop();
				temp.makeTree(elems[i].charAt(0), t2, t1);
				stN.push(temp);
			}
		}
		temp = (Expression)stN.pop();
		this.root = temp.root;
	}
	public void evaluate() {
		if (eType == ExpressionType.POSTFIX && !notationExp.isEmpty()) {
			int result = evaluatePostfix();
			System.out.println("Result: " + result);
		} else {
			System.out.println("No postfix expression to evaluate");
		}
	}
	
	private int evaluatePostfix() {
		ArrayStack stack = new ArrayStack();
		String[] tokens = notationExp.split(" ");
		for (String token : tokens) {
			if (token.isEmpty()) continue;
			if (Character.isDigit(token.charAt(0))) {
				stack.push(Integer.parseInt(token));
			} else {
				int b = (Integer) stack.pop();
				int a = (Integer) stack.pop();
				switch (token.charAt(0)) {
					case '+': stack.push(a + b); break;
					case '-': stack.push(a - b); break;
					case '*': stack.push(a * b); break;
					case '/': stack.push(a / b); break;
					case '^': stack.push((int) Math.pow(a, b)); break;
				}
			}
		}
		return (Integer) stack.pop();
	}
	public void menu() {
		System.out.println("\n***** menu ***********");
		System.out.println("1) Infix Ilerhiilel oruulah");
		System.out.println("2) Prefix Ilerhiilel oruulah");
		System.out.println("3) Postfix Ilerhiilel oruulah");
		System.out.println("4) Postfix Ilerhiilel hevleh");
		System.out.println("5) Prefix Ilerhiilel hevleh");
		System.out.println("6) Infix Ilerhiilel hevleh");
		System.out.println("7) Ilerhiilel bodoh");
		System.out.println("0) Garah");
		System.out.println("---Uildliin dugaar songooroi---");
	}

	public static void main(String[] args) {
		try {
			Expression myexp = new Expression();
			int command;
			while(true) {
				myexp.menu();
				command = Integer.parseInt(myexp.scan.nextLine());
				switch (command) {
					case 0:
						System.exit(0);
						break;
					case 1:
						myexp.readInfixExpression();
						myexp.infixToPostfix();
						myexp.expressionTreeFromPostFix();
						break;
					case 2:
						myexp.readPrefixExpression();
						myexp.prefixToPostfix();
						myexp.expressionTreeFromPostFix();
						break;
					case 3:
						myexp.readPostfixExpression();
						myexp.expressionTreeFromPostFix();
						break;
					case 4:
						System.out.println("\npostfix expression: ");
						myexp.postOrderOutput();
						break;
					case 5:
						System.out.println("\nprefix expression: ");
						myexp.preOrderOutput();
						break;
					case 6:
						System.out.println("\ninfix expression: ");
						myexp.inOrderOutput();
						break;
					case 7:
						myexp.evaluate();
						break;
					default:
						System.out.println("wrong choice!");
						break;
				}
			}
		} catch(Exception e) {
			System.out.println("ALDAA");
		}
	}
}