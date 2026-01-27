package lab4;
import java.util.Scanner;

public class MyHashTable extends dataStructures.HashTable{
	public MyHashTable(int theDivisor) {
		super(theDivisor);
	}
	public Object insert(Object theKey, Object theElement) {
		Object element = get(theKey);
		if(element == null) {
			put(theKey, theElement);
			return null;
		} else {
			int key = theKey.hashCode() + 1;
			put(Integer.valueOf(key), theElement);
			return null;
		}
	}
	public Object updateElement(Object theKey, Object theElement) {
		return put(theKey, theElement);
	}

	public Object updateKey(Object theKey, Object theNewKey) {
		Object element = get(theKey);
		if (element == null) {
			return null;
		}
		put(theNewKey, element);
		delete(theKey);
		return null;
	}
	public void delete(Object theKey) {
		int b = search(theKey);
		if (b < 0 || b >= table.length || table[b] == null || !table[b].key.equals(theKey)) {
			System.out.println("Nothing to delete here!");
			return;
		}
		table[b] = null;
		size--;
	}
	public static void printMenu() {
		System.out.println("--------^ Result ^--------");
		System.out.println("Do what?");
		System.out.println("1.insert(theKey, theElement)");
		System.out.println("2.output()");
		System.out.println("3.delete(theKey)");
		System.out.println("4.updateElement(theKey, theElement)");
		System.out.println("5.updateKey(theKey, theNewKey)");
		System.out.println("6.get(theKey)");
		System.out.println("--------⌄ Result ⌄--------");
	}
	public static void main (String [] args) {
		MyHashTable h = new MyHashTable(10);
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
					System.out.println("enter string to leave");
					try {
						int i = 1;
						System.out.println(i+". Enter theKey:");
						String key = scanner.nextLine().trim();
						if(key.equals("q")) {
							break;
						}
						System.out.println(i+". Enter theElement:");
						String element = scanner.nextLine().trim();
						h.insert(key, element);
						i++;
					} catch(Exception e) {
						break;
					}
					break;
				case 2:
					h.output();
					break;
				case 3: 
					//delete
					System.out.println("Enter theKey to delete:");
					try {
						String delKey = scanner.nextLine().trim();
						h.delete(delKey);
					} catch(Exception e) {
						break;
					}
					break;
				case 4:
					//updateElement(theKey, theElement)
					try {
						System.out.println("updateElement");
						System.out.println("Enter theKey");
						String key = scanner.nextLine().trim();
						System.out.println("Enter theElement");
						String element = scanner.nextLine().trim();
						h.updateElement(key, element);
					} catch(Exception e) {
						break;
					}
					break;
				case 5:
					//updateKey(theKey, theNewKey)
					try {
						System.out.println("updateKey");
						System.out.println("Enter theKey");
						String key = scanner.nextLine().trim();
						System.out.println("Enter theNewKey");
						String newKey = scanner.nextLine().trim();
						h.updateKey(key, newKey);
					} catch (Exception e) {
						break;
					}
					break;
				case 6:
					System.out.println("enter theKey to get()");
					String key = scanner.nextLine().trim();
					System.out.println("Element at '"+key+"' key is "+h.get(key));
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