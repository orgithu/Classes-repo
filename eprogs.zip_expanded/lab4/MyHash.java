package lab4;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import lab3.MyStack;

//зөвхөн search, put, get.
public class MyHash extends dataStructures.HashTable{
	public MyHash(int theDivisor) {
		super(theDivisor);
	}

	// use protected search from superclass (HashTable.search was made protected)

	public Object updateElement(Object theKey, Object theElement) {
		// use put which already overwrites existing element and returns old element
		return put(theKey, theElement);
	}

	public Object updateKey(Object theKey, Object theNewKey) {
		// remove returns the element associated with theKey (or null)
		Object element = remove(theKey);
		if (element == null)
			return null;
		put(theNewKey, element);
		return element;
	}

	public void delete(Object theKey) {
		remove(theKey);
	}
	public static void printMenu() {
		System.out.println("--------^Result^--------");
		System.out.println("Do what?");
		System.out.println("1.create (random n elements)");
		System.out.println("--------⌄Result⌄--------");
	}
	public static void main (String [] args) {
		MyHash h = new MyHash(10);
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