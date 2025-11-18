package biyDaalt2;
import dataStructures.ArrayStack;
import java.io.*;
import java.util.ArrayList;

public class carParking extends ArrayStack {
    private ArrayList<String> commands;
    private ArrayList<String> outputs;

    public carParking() {
        super();
        commands = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public carParking(int initialCapacity) {
        super(initialCapacity);
        commands = new ArrayList<>();
        outputs = new ArrayList<>();
    }
    public void input() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/home/orgdg/Documents/Classes-repo/eprogs.zip_expanded/biyDaalt2/cars.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                commands.add(line.trim());
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    public void process() {
        for (String command : commands) {
            String[] parts = command.split(" ");
            if (parts.length != 2) continue;
            String action = parts[0];
            String plate = parts[1];
            Car car = new Car(plate);
            if (action.equals("A")) {
            	boolean found = false;
            	ArrayStack temp = new ArrayStack();
            	while (!empty()) {
            		Car topCar = (Car) pop();
            		if (topCar.equals(car)) {
            			found = true;
            		} else {
            			temp.push(topCar);
            		}
            	}
                //arrival
                if (size() < 10 && !found) {
                    push(car);
                    outputs.add("Arrival " + plate + " -> There is room.");
                } else if(!found) {
                    outputs.add("Arrival " + plate + " -> Garage full, this car cannot enter.");
                } else {
                	outputs.add("Arrival "+ plate + " This car is already in garage.");
                }
                while (!temp.empty()) {
                    push(temp.pop());
                }
                //outputs.add("Current stack: " + getStackString());
            } else if (action.equals("D")) {
                //departure
                boolean found = false;
                int moved = 0;
                ArrayStack temp = new ArrayStack();
                while (!empty()) {
                    Car topCar = (Car) pop();
                    if (topCar.equals(car)) {
                        found = true;
                        break;
                    } else {
                        temp.push(topCar);
                        moved++;
                    }
                }
                if (found) {
                    outputs.add("Departure " + plate + " -> " + moved + " cars moved out.");
                } else {
                    outputs.add("Departure " + plate + " -> This car not in the garage.");
                }
                //push back the temp stack
                while (!temp.empty()) {
                    push(temp.pop());
                }
                //outputs.add("Current stack: " + getStackString());
            }
        }
    }
    public void output() {
        for (String out : outputs) {
            System.out.println(out);
        }
    }
    public String getStackString() {
        ArrayList<String> list = new ArrayList<>();
        ArrayStack temp = new ArrayStack();
        while (!empty()) {
            Car c = (Car) pop();
            list.add(c.getLicensePlate()); //add to end for top to bottom
            temp.push(c);
        }
        while (!temp.empty()) {
            push(temp.pop());
        }
        return list.toString();
    }

    public static void main(String[] args) {
        carParking cp = new carParking();
        cp.input();
        cp.process();
        cp.output();
    }
}