package biyDaalt2;

/**
 * Car class representing a car with a license plate.
 */
public class Car {
    private String licensePlate;

    public Car(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Car car = (Car) obj;
        return licensePlate.equals(car.licensePlate);
    }
    @Override
    public String toString() {
        return licensePlate;
    }
}