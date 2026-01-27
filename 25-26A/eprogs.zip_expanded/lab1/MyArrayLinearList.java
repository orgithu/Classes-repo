package lab1;
import dataStructures.ArrayLinearList;
import java.util.*;
import java.util.Random;
public class MyArrayLinearList extends ArrayLinearList {
	public MyArrayLinearList(int initCapacity) {
		super(initCapacity);
	}

	public MyArrayLinearList() {
		super(10);
	}

	public MyArrayLinearList(MyArrayLinearList mylist) {
		super(mylist.size());
		for(int i=0;i<mylist.size();i++) {
			this.add(i, mylist.element[i]);
		}
	}
	//O(n)
	public MyArrayLinearList reverse() {
		MyArrayLinearList temp = new 
		MyArrayLinearList(this.size());
		int j = 0;
		for(int i=this.size()-1;i>=0;i--)
			temp.add(j++, this.element[i]);
		return temp;
		}
	//O(n^2) 
	public MyArrayLinearList unique() {
		MyArrayLinearList temp = new MyArrayLinearList();
        for(int i = 0; i < this.size(); i++) {
            boolean exists = false;
            for(int j = 0; j < temp.size(); j++) {
            		if ((int) temp.get(j) == (int)this.get(i)) {
            		exists = true;
            		break;
            		}
            }
            if (!exists) temp.add(temp.size(), (int)this.get(i));
        }
        return temp; 
	}
	//Odoogiin listee arraylist bolgood shuffle-dd butsaad list bolgoj bna, O(n)
	public MyArrayLinearList rand() {
		ArrayList<Integer> tempList = new ArrayList<>();
		for(int i = 0; i < this.size(); i++) {
			tempList.add((Integer) this.get(i));
		}
		Collections.shuffle(tempList);
		MyArrayLinearList temp = new MyArrayLinearList();
		for(int i = 0; i < tempList.size(); i++) {
			temp.add(i, tempList.get(i));
		}
		return temp;
	}

	public MyArrayLinearList merge(MyArrayLinearList arrayList) {
		MyArrayLinearList temp = new MyArrayLinearList(this);
		for(int i = 0; i < arrayList.size(); i++) {
			temp.add(temp.size(), arrayList.get(i));
		}
		return temp;
	}
	//O(n)
	public int max() {
		int max = (int)this.get(0);
		for(int i = 1; i < this.size(); i++) {
			if(max < (int)this.get(i))
				max = (int)this.get(i);
		}
		return max;
	}
	
	public int min() {
		int min = (int)this.get(0);
		for(int i = 1; i < this.size(); i++) {
			if(min > (int)this.get(i))
				min = (int)this.get(i);
		}
		return min;
	}
	
	public int sum() {
		int sum = 0;
		for(int i = 1; i < this.size(); i++) {
			sum += (int)this.get(i);
		}
		return sum;
	}
	
	public int average() {
		int avg = (int)this.sum() / (int)this.size();
		return avg;
	}
	
	public void removeOdd() {
		for(int i = 1; i < this.size(); i++) {
			if (((int) this.get(i)) % 2 != 0) {
				this.remove(i);
			}
		}
	}
	//O(n^2)
	public void sort() {
		for(int i = 0; i < this.size() - 1; i++) {
			for(int j = i + 1; j < this.size(); j++) {
				if((int)this.get(i) < (int)this.get(j)) {
					Object tmp = this.get(i); 
					this.element[i] = this.get(j);
					this.element[j] = tmp;
				}
			}	
		}
	}

	public static void printMenu() {
		System.out.println("----------------");
	    System.out.println("Do what?");
	    System.out.println("1.create");
	    System.out.println("2.print");
	    System.out.println("3.max");
	    System.out.println("4.min");
	    System.out.println("5.sum");
	    System.out.println("6.average");
	    System.out.println("7.removeOdd");
	    System.out.println("8.sort");
	    System.out.println("9.unique");
	    System.out.println("10.reverse");
	    System.out.println("11.rand");
	    System.out.println("12.merge with");
	    System.out.println("0.exit");
	    System.out.println("----------------");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyArrayLinearList l1 = new MyArrayLinearList();
		Random random = new Random();
		Scanner scan = new Scanner(System.in);
	    printMenu();
	    while (true) {
	        int choice;
	        try {
	            choice = Integer.parseInt(scan.nextLine().trim());
	        } catch (Exception e) {
	            continue;
	        }
	        switch (choice) {
	        		case 1:
	        			printMenu();
	        			System.out.println("n: ");
	        			int n = scan.nextInt();
	        			System.out.println(n);
	        			for( int i = 1; i < n + 1; i++) {
	        				try {
	        				l1.add(i - 1, random.nextInt(10));
	        			} catch(Exception e) {
	        				scan.close();
	        				break;
	        				}
	        			}
	        		case 2:
	        			printMenu();
	        			System.out.println("l1: "+l1.toString());
	        			break;
	            case 3: 
	            		printMenu();
	            		System.out.println("Max = " + l1.max()); 
	            		break;
	            case 4:
	            		printMenu();
	            		System.out.println("Min = " + l1.min());
	            		break;
	            case 5: 
	            		printMenu();
	            		System.out.println("Sum = " + l1.sum()); 
	            		break;
	            case 6: 
	            		printMenu();
	            		System.out.println("Average = " + l1.average()); 
	            		break;
	            case 7: 
	            		printMenu();
	            		l1.removeOdd(); System.out.println("Oddless: "+l1.toString()); 
	            		break;
	            case 8: 
	            		printMenu();
	            	//O n hurdnii tootsoo
	            		l1.sort(); System.out.println("Sorted: "+l1.toString()); 
	            		break;
	            case 9: 
	            		printMenu();
	            		System.out.println("Unique: "+l1.unique().toString()); 
	            		break;
	            case 10: 
	            		printMenu();
	            		System.out.println("reversed: "+l1.reverse().toString()); 
	            		break;
	            case 11: 
            			printMenu();
	            		System.out.println("Shuffled/Rand: "+l1.rand().toString()); 
	            		break;
	            case 12:
	            		printMenu();
	            		MyArrayLinearList l2 = new MyArrayLinearList();
	            		for (int i = 1; i > 0; i++) {
	            			try {
	            				System.out.println("Enter int for index " + i);
	            				l2.add(i - 1, scan.nextInt());
	            			} catch(Exception e) {
	            			scan.nextLine();
	            			break;
	            			}
	            		}
	            		System.out.println("Merged: " + l1.merge(l2).toString());
	            		break;
	            case 0: 
	            		System.out.println("Bye!"); 
	            		return;
	            default: 
	            		printMenu();
	            		System.out.println("Invalid"); 
	        }
	    }
	}
}

