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
		MyStack temp = new MyStack(this.size());
		return temp;
	}
	
	public MyStack unique() {
		MyStack temp = new MyStack();
		return temp;
	}
	
	public MyStack addRange(Object[] elements) {
		MyStack temp = new MyStack();
		return temp;
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
		System.out.println("MENU");
	}
	
	public static void main(String[] args) {
		MyStack ms = new MyStack();
		Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
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
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("!TRY AGAIN!");
            }
        }
    }
}
