package lab2;
import java.util.*;
import java.io.*;
import dataStructures.Chain;

public class MyChain extends Chain {
	public MyChain(String ss) {
		super(0);
		String [] strval;
		strval = ss.split(" ");
		for(int i = 0; i < strval.length; i++) {
			this.add(i, Integer.parseInt(strval[i]));
		}
	}
	public MyChain() {
		super(0);
	}
	public MyChain(MyChain original) {
        // Deep copy
        for (int i = 0; i < original.size(); i++) {
            this.add(i, original.get(i));
        }
    }
	
	public void clear() {
	    firstNode = null; 
	    size = 0; 
	}

	public static void MergeSort(MyChain mlist) {
	    int q, i, j, k;
	    MyChain lefthalf, righthalf;
	    if (mlist.size() > 1) {
	        q = mlist.size() / 2; 
	        lefthalf = new MyChain();
	        righthalf = new MyChain();
	        for (i = 0; i < mlist.size(); i++) {
	            if (i < q)
	                lefthalf.add(i, mlist.get(i));
	            else
	                righthalf.add(i - lefthalf.size(), mlist.get(i));
	        }
	        MergeSort(lefthalf);
	        MergeSort(righthalf);
	        i = j = k = 0;
	        mlist.clear();   // ✅ fixed (was mlist.empty())
	        while (i < lefthalf.size() && j < righthalf.size()) {
	            if ((Integer) lefthalf.get(i) < (Integer) righthalf.get(j)) {
	                mlist.add(k, lefthalf.get(i));
	                i++;
	            } else {
	                mlist.add(k, righthalf.get(j));
	                j++;
	            }
	            k++;
	        }
	        while (i < lefthalf.size()) {
	            mlist.add(k, lefthalf.get(i));
	            i++;
	            k++;
	        }
	        while (j < righthalf.size()) {
	            mlist.add(k, righthalf.get(j));
	            j++;
	            k++;
	        }
	    }
	}

	public Object[] toArray() {
	    Object[] tempArray = new Object[this.size()];
	    for (int i = 0; i < this.size(); i++) {
	    	tempArray[i] = this.get(i);
	    }
	    return tempArray;
	}
	
	public void addRange(Object[] elements) {
	    for (int i = 0; i < elements.length; i++) {
	        this.add(this.size(), elements[i]);
	    }
	}

	public MyChain union(MyChain chain) {
		MyChain temp = new MyChain();
		for(int i = 0; i < this.size(); i++) {
			temp.add(i, this.get(i));
		}
		for(int i = 0; i < chain.size(); i++) {
			Object element = chain.get(i);
			Boolean exists = false;
			for(int j = 0; j < temp.size(); j++) {
				if (temp.get(j).equals(element)) {
					exists = true;
					break;
				}
			}
			if(!exists) {
				temp.add(temp.size(), element);
			}
	    }
		return temp;
	}
	
	public MyChain intersection(MyChain chain) {
	    Object[] arr1 = this.toArray();
	    Object[] arr2 = chain.toArray();
	    Arrays.sort(arr1);
	    Arrays.sort(arr2);
	    MyChain intersectCh = new MyChain();
	    int x = 0, y = 0, k = 0;
	    while (x < arr1.length && y < arr2.length) {
	        if ((Integer)arr1[x] < (Integer)arr2[y])   // ✅ fixed cast
	            x++;
	        else if ((Integer)arr1[x] > (Integer)arr2[y])
	            y++;
	        else {
	            intersectCh.add(k, arr1[x]);
	            x++; y++; k++;
	        }
	    }
	    return intersectCh;
	}

	public MyChain intersectionBrute(MyChain chain) {
		MyChain intersectCh = new MyChain();
		int k = 0;
		for(int i = 0; i < this.size(); i++) {
			for(int j = 0; j < chain.size(); j++) {
				if ((Integer)this.get(i) == (Integer)chain.get(j)) {  // ✅ fixed cast
					intersectCh.add(k, chain.get(j));
					k++;
				}
			}
		}
		return intersectCh;
	}

	public MyChain intersectMerge(MyChain mlist) {
	    MyChain ch1 = new MyChain(this);
	    MyChain ch2 = new MyChain(mlist);
	    MergeSort(ch1);
	    MergeSort(ch2);
	    int x=0,y=0,k=0;
	    MyChain intersectCh = new MyChain();
	    while(x<ch1.size() && y<ch2.size()) {
	        if(((Integer)ch1.get(x))<((Integer)ch2.get(y)))   // ✅ fixed cast
	            x++;
	        else if(((Integer)ch1.get(x))>((Integer)ch2.get(y)))
	            y++;
	        else {
	            intersectCh.add(k,ch1.get(x));
	            x++;
	            y++;
	            k++;
	        }
	    }
	    return intersectCh;
	}

	public static void printMenu() {
		System.out.println("--------^Result^--------");
	    System.out.println("Do what?");
	    System.out.println("1.create x");
	    System.out.println("2.print");
	    System.out.println("3.size");
	    System.out.println("4.indexOf");
	    System.out.println("5.get(n)");
	    System.out.println("6.remove(n)");
	    System.out.println("7.toArray()");
	    System.out.println("8.addRange()");
	    System.out.println("9.union()");
	    System.out.println("10.intersection");
	    System.out.println("11.create x1");
	    System.out.println("12.");
	    System.out.println("0.exit");
	    System.out.println("--------⌄Result⌄--------");
	}

	public static void main(String[] args) {
		/*MyChain x = new MyChain();
	    MyChain x1 = new MyChain();
	    Random random = new Random();
	    Scanner scan = new Scanner(System.in);
	    while (true) {
	        printMenu();
	        int choice;
	        try {
	            choice = Integer.parseInt(scan.nextLine().trim());
	        } catch (Exception e) {
	            continue;
	        }
	        switch (choice) {
	            case 1:
	                System.out.println("n for x: ");
	                int n = scan.nextInt();
	                scan.nextLine();
	                for (int i = 0; i < n; i++) {
	                    x.add(i, random.nextInt(10));
	                }
	                break;
	            case 2:
	                System.out.println("The x is " + x);
	                System.out.println("The x1 is " + x1);
	                break;
	            case 3:
	                System.out.println("Size: " + x.size());
	                break;
	            case 4:
	                System.out.println("Enter n to find index: ");
	                int n1 = scan.nextInt();
	                scan.nextLine();
	                int index = x.indexOf(new Integer(n1));
	                if (index < 0) {
	                    System.out.println(n1 + " not found");
	                } else {
	                    System.out.println("The index of " + n1 + " is " + index);
	                }
	                break;
	            case 5:
	                System.out.println("Enter get index: ");
	                int n2 = scan.nextInt();
	                scan.nextLine();
	                System.out.println("Element at " + n2 + " is " + x.get(n2));
	                break;
	            case 6:
	                if (x.isEmpty()) {
	                    System.out.println("The list is empty");
	                } else {
	                    System.out.println("Remove index: ");
	                    int n3 = scan.nextInt();
	                    scan.nextLine();
	                    System.out.println(x.remove(n3) + " removed");
	                    System.out.println("The list is " + x);
	                }
	                break;
	            case 7:
	            		Object[] xArray = x.toArray();
	                System.out.println("to Array: " + java.util.Arrays.toString(xArray));
	                break;
	            case 8:
	                System.out.println("a[n] to add to x");
	                int n6 = scan.nextInt();
	                scan.nextLine();
	                Object[] addRangeArray = new Object[n6];
	                for (int i = 0; i < n6; i++) {
	                    System.out.print("element[" + i + "]");
	                    int n7 = scan.nextInt();
	                    scan.nextLine();
	                    addRangeArray[i] = n7;
	                }
	                x.addRange(addRangeArray);
	                System.out.println("The list is " + x);
	                break;
	            case 9:
	                System.out.println("x union(x1): " + x.union(x1));
	                break;
	            case 10:
	                System.out.println("x intersection(x1): " + x.intersection(x1));
	                break;
	            case 11:
	                System.out.println("n for x1: ");
	                int n5 = scan.nextInt();
	                scan.nextLine();
	                for (int i = 0; i < n5; i++) {
	                    x1.add(i, random.nextInt(10));
	                }
	                break;
	            case 12:
	            	System.out.println("nothing here!");
	                break;
	            case 0:
	                System.out.println("Bye!");
	                scan.close();
	                return;
	            default:
	                System.out.println("Invalid");
	        }
	    }*/
		/**/
		try {
			File ff = new File("input01.txt");
			Scanner sc = new Scanner(ff);
			MyChain ch1 = new MyChain(sc.nextLine());
			MyChain ch2 = new MyChain(sc.nextLine());
			long t1 = System.currentTimeMillis();
			MyChain ch = ch1.intersectionBrute(ch2);
			long t2 = System.currentTimeMillis();
			long diff = t2-t1;
			System.out.println("Time: " + (double)diff/1000);
		} catch(Exception e) {
			System.out.println("ALDAA");
		}
		/*-----------------input txt generation------------------
		int n = 10000;
		int m = 5000;
		try {
			FileWriter writer = new FileWriter("input01.txt");
			Random r = new Random();
			for(int i = 0; i < n; i++) {
				writer.write(r.nextInt(n) + " ");
			}
			writer.write("\n");
			for(int i = 0; i < m; i++) {
				writer.write(r.nextInt(m) + " ");
			}
			writer.close();
			System.out.println("done");
		} catch(Exception e) {
			System.out.println("ALDAA");
		}*/
	}
}
