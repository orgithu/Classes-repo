package lab5;
import java.util.Scanner;

import dataStructures.LinkedBinaryTree;

public class Expression extends dataStructures.LinkedBinaryTree {
    public static void printMenu() {
        System.out.println("1.input");
        System.out.println("2.");
        System.out.println("3.");
        System.out.println("4.");
        System.out.println("5.");
        System.out.println("6.");
        System.out.println("7.");
        System.out.println("8.");
        System.out.println("9.");

    }
    public static void main(String[] args) {
        LinkedBinaryTree a = new LinkedBinaryTree(),
                        x = new LinkedBinaryTree(), 
                        y = new LinkedBinaryTree(), 
                        z = new LinkedBinaryTree();
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
