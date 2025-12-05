package lab3;
import java.util.Scanner;
import java.util.Stack;

public class MultiDigitPostFix {
	//operation order
    static int Prec(char ch) {
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

    // infix → postfix (shunting yard)
    static String infixToPostfix(String exp) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            if (Character.isDigit(c)) {
                //read full number
                while (i < exp.length() && Character.isDigit(exp.charAt(i))) {
                    result.append(exp.charAt(i));
                    i++;
                }
                result.append(' ');
                i--; //adjust position
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(')
                    result.append(stack.pop()).append(' ');
                stack.pop();
            } else if (c == ' ') {
                continue;
            } else {
                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek()))
                    result.append(stack.pop()).append(' ');
                stack.push(c);
            }
        }

        while (!stack.isEmpty())
            result.append(stack.pop()).append(' ');

        return result.toString().trim();
    }

    // evaluate postfix expression
    static int evaluatePostfix(String exp) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = exp.split("\\s+");

        for (String token : tokens) {
            if (token.matches("-?\\d+")) {
                stack.push(Integer.parseInt(token));
            } else {
                int val1 = stack.pop();
                int val2 = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(val2 + val1); break;
                    case '-': stack.push(val2 - val1); break;
                    case '*': stack.push(val2 * val1); break;
                    case '/': stack.push(val2 / val1); break;
                }
            }
        }
        return stack.pop();
    }
    public static void printMenu() {
		System.out.println("--------^Result^--------");
		System.out.println("Do what?");
		System.out.println("1.string to postfix & evaluate");
		System.out.println("--------⌄Result⌄--------");
	}
    public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
        while (true) {
			printMenu();
			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine().trim());
			} catch (Exception e) {
				continue;
			}
			switch (choice) {
				case 1:
					System.out.print("string: ");
					try {
						String s = scanner.nextLine().trim();
						String pf = infixToPostfix(s);
						int res = evaluatePostfix(pf);
						System.out.println("Postfix: " + pf);
						System.out.println("Result: " + res);
					} catch (Exception ex) {
						System.out.println("Invalid string");
					}
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6: 
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 0:
					System.out.println("Bye!");
					scanner.close();
					return;
				default:
					printMenu();
					System.out.println("Invalid");
			}
		}
    }
}
