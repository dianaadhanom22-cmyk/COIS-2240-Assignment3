package rental;

public class Car extends Vehicle implements Rentable {
    private int numSeats;

    public Car(String make, String model, int year, int numSeats) {
        super(make, model, year);
        this.numSeats = numSeats;
    }

    public int getNumSeats() {
        return numSeats;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " | Seats: " + numSeats;
    }

    @Override
    public void rentVehicle() {
        setStatus(VehicleStatus.Rented);
        System.out.println("Car " + getLicensePlate() + " has been rented.");
    }

    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.Available);
        System.out.println("Car " + getLicensePlate() + " has been returned.");
    }
}