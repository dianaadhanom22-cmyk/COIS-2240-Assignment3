package rental;

public abstract class Vehicle {

    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus {
        Available, Held, Rented, UnderMaintenance, OutOfService
    }

    // ---------- HELPER METHOD ----------
    private String capitalize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Unknown";
        }
        input = input.trim().toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    // ---------- CONSTRUCTOR ----------
    public Vehicle(String make, String model, int year) {
        this.make = capitalize(make);
        this.model = capitalize(model);
        this.year = year;
        this.status = VehicleStatus.Available;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }

    // ---------- SETTERS ----------
    public void setLicensePlate(String plate) {
        this.licensePlate = (plate == null ? null : plate.toUpperCase());
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    // ---------- GETTERS ----------
    public String getLicensePlate() { return licensePlate; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }
}
