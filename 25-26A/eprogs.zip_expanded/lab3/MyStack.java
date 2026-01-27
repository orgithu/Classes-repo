package lab3;

import java.util.*;
import dataStructures.*;

public class MyStack extends ArrayStack {
	
	public MyStack() {
		super();
	}
	
	public MyStack(int initialCapacity) {
		super(initialCapacity);
	}
	
	public MyStack(MyStack ms) {
		super(ms.size());
		MyStack temp = new MyStack();
		while(!ms.empty()) {
			temp.push(ms.pop());
		}
		while(!temp.empty()) {
			this.push(temp.pop());
			ms.push(temp.pop());
		}
	}
	
	public MyStack rand() {
		// simple implementation using basic for-loops
		Object[] arr = this.toArray(); // arr[0] is top
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		Collections.shuffle(list);
		MyStack temp = new MyStack(list.size());
		// push so that list.get(0) becomes the top
		for (int i = list.size() - 1; i >= 0; i--) {
			temp.push(list.get(i));
		}
		return temp;
	}
	
	public MyStack unique() {
		Object[] arr = this.toArray(); // arr[0] is top
		ArrayList<Object> uniq = new ArrayList<Object>();
		for (int i = 0; i < arr.length; i++) {
			Object cur = arr[i];
			boolean exists = false;
			for (int j = 0; j < uniq.size(); j++) {
				if (uniq.get(j).equals(cur)) {
					exists = true;
					break;
				}
			}
			if (!exists) uniq.add(cur);
		}
		MyStack temp = new MyStack(uniq.size());
		for (int i = uniq.size() - 1; i >= 0; i--) {
			temp.push(uniq.get(i));
		}
		return temp;
	}
	
	public MyStack addRange(Object[] elements) {
		for (int i = 0; i < elements.length; i++) {
			this.push(elements[i]);
		}
		return this;
	}
	
	Object[] toArray() {
		Object[] arr = new Object[this.size()];
		MyStack temp = new MyStack();
		int i = 0;
		while(!this.empty()) {
			Object x = this.pop();
			temp.push(x);
			arr[i++]=x;
		}
		while(!temp.empty()) {
			this.push(temp.pop());
		}
		return arr;
	}
	
	public static void printMenu() {
		System.out.println("--------^Result^--------");
		System.out.println("Do what?");
		System.out.println("1.create (random n elements)");
		System.out.println("2.print");
		System.out.println("3.size");
		System.out.println("4.peek");
		System.out.println("5.pop");
		System.out.println("6.push (single int)");
		System.out.println("7.toArray");
		System.out.println("8.addRange");
		System.out.println("9.rand (shuffled copy)");
		System.out.println("10.unique");
		System.out.println("0.exit");
		System.out.println("--------⌄Result⌄--------");
	}
	
	public static void main(String[] args) {
		MyStack ms = new MyStack();
		Random random = new Random();
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
				case 1: // create random n elements
					System.out.print("n for stack: ");
					try {
						int n = Integer.parseInt(scanner.nextLine().trim());
						ms = new MyStack();
						for (int i = 0; i < n; i++) ms.push(random.nextInt(15));
						System.out.println("Stack: " + ms.toString());
					} catch (Exception ex) {
						System.out.println("Invalid number");
					}
					break;
				case 2: // print
					System.out.println("Stack: " + ms.toString());
					break;
				case 3: // size
					System.out.println("Size: " + ms.size());
					break;
				case 4: // peek
					try { System.out.println("Top: " + ms.peek()); } catch (Exception e) { System.out.println("Stack is empty"); }
					break;
				case 5: // pop
					try {
						System.out.println("before Stack: " + ms.toString());
						System.out.println("Popped: " + ms.pop());
						System.out.println("after Stack: " + ms.toString());
						} catch (Exception e) { 
							System.out.println("Stack is empty"); 
						}
					break;
				case 6: // push single
					System.out.print("Enter int to push: ");
					try {
						int v = Integer.parseInt(scanner.nextLine().trim());
						ms.push(v);
						System.out.println("Pushed " + v);
					} catch (Exception e) {
						System.out.println("Invalid input");
					}
					break;
				case 7: // toArray
					Object[] arr = ms.toArray();
					System.out.println("toArray: " + Arrays.toString(arr));
					break;
				case 8: // addRange
					System.out.print("How many elements to add: ");
					try {
						int k = Integer.parseInt(scanner.nextLine().trim());
						Object[] elems = new Object[k];
						for (int i = 0; i < k; i++) {
							System.out.print("element[" + i + "]: ");
							elems[i] = Integer.parseInt(scanner.nextLine().trim());
						}
						ms.addRange(elems);
						System.out.println("Added " + k + " elements.");
					} catch (Exception e) {
						System.out.println("Invalid input");
					}
					System.out.println("Stack: " + ms.toString());
					break;
				case 9: // rand
					System.out.println("Rand copy: " + ms.rand().toString());
					break;
				case 10: // unique
					System.out.println("Unique copy: " + ms.unique().toString());
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