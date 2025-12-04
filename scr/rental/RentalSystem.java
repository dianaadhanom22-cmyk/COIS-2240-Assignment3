package rental;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileWriter;

public class RentalSystem {

    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    // -------- SINGLETON --------
    private static RentalSystem instance_vai = null;

    private RentalSystem() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentalHistory = new RentalHistory();
        loadData_vai();   // load saved vehicles & customers
    }

    public static RentalSystem getInstance_vai() {
        if (instance_vai == null) {
            instance_vai = new RentalSystem();
        }
        return instance_vai;
    }

    // -------- ADD VEHICLE --------
    public boolean addVehicle(Vehicle vehicle) {
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("Duplicate vehicle detected. Cannot add.");
            return false;
        }

        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
    }

    // -------- ADD CUSTOMER --------
    public boolean addCustomer(Customer customer) {
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("Duplicate customer detected. Cannot add.");
            return false;
        }

        customers.add(customer);
        saveCustomer(customer);
        return true;
    }

    // -------- RENT VEHICLE --------
    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {

            vehicle.setStatus(Vehicle.VehicleStatus.Rented);

            RentalRecord rec = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(rec);
            saveRecord(rec);

            System.out.println("Vehicle rented to " + customer.getCustomerName());
        } else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    // -------- RETURN VEHICLE --------
    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {

            vehicle.setStatus(Vehicle.VehicleStatus.Available);

            RentalRecord rec = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(rec);
            saveRecord(rec);

            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }

    // -------- DISPLAY VEHICLES --------
    public void displayVehicles(Vehicle.VehicleStatus status) {
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }

        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n",
                "Type", "Plate", "Make", "Model", "Year", "Status");

        System.out.println("|--------------------------------------------------------------------------------------------|");

        boolean found = false;

        for (Vehicle v : vehicles) {
            if (status == null || v.getStatus() == status) {
                found = true;

                String type = (v instanceof Car) ? "Car" :
                        (v instanceof Minibus) ? "Minibus" :
                        (v instanceof PickupTruck) ? "PickupTruck" : "Unknown";

                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n",
                        type, v.getLicensePlate(), v.getMake(), v.getModel(),
                        v.getYear(), v.getStatus());
            }
        }

        if (!found) System.out.println("No vehicles found.");
    }

    // -------- DISPLAY CUSTOMERS --------
    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println(c.toString());
        }
    }

    // -------- DISPLAY RENTAL HISTORY --------
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("No rental history available.");
            return;
        }

        System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n",
                "Type", "Plate", "Customer", "Date", "Amount");

        System.out.println("|-------------------------------------------------------------------------------|");

        for (RentalRecord r : rentalHistory.getRentalHistory()) {
            System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n",
                    r.getRecordType(),
                    r.getVehicle().getLicensePlate(),
                    r.getCustomer().getCustomerName(),
                    r.getRecordDate(),
                    r.getTotalAmount());
        }
    }

    // -------- FIND VEHICLE --------
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }

    // -------- FIND CUSTOMER --------
    public Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id)
                return c;
        }
        return null;
    }

    // ================================
    //  SUBTASK 2 — SAVE METHODS (APPEND MODE)
    // ================================
    private void saveVehicle(Vehicle v) {
        try (FileWriter fw = new FileWriter("vehicles.txt", true)) {
            fw.write(v.getLicensePlate() + "," +
                    v.getMake() + "," +
                    v.getModel() + "," +
                    v.getYear() + "," +
                    v.getStatus() + "\n");
        } catch (Exception e) {
            System.out.println("Error saving vehicle.");
        }
    }

    private void saveCustomer(Customer c) {
        try (FileWriter fw = new FileWriter("customers.txt", true)) {
            fw.write(c.getCustomerId() + "," +
                    c.getCustomerName() + "\n");
        } catch (Exception e) {
            System.out.println("Error saving customer.");
        }
    }

    private void saveRecord(RentalRecord r) {
        try (FileWriter fw = new FileWriter("rental_records.txt", true)) {
            fw.write(r.getRecordType() + "," +
                    r.getVehicle().getLicensePlate() + "," +
                    r.getCustomer().getCustomerName() + "," +
                    r.getRecordDate() + "," +
                    r.getTotalAmount() + "\n");
        } catch (Exception e) {
            System.out.println("Error saving record.");
        }
    }

    // ================================
    //  SUBTASK 3 — LOAD DATA
    // ================================
    private void loadData_vai() {
        try {
            // ---- Load Vehicles ----
            File vFile = new File("vehicles.txt");
            if (vFile.exists()) {
                Scanner vScan = new Scanner(vFile);
                while (vScan.hasNextLine()) {
                    String[] p = vScan.nextLine().split(",");
                    Vehicle v = new Car(p[1], p[2], Integer.parseInt(p[3]), 5);
                    v.setLicensePlate(p[0]);
                    v.setStatus(Vehicle.VehicleStatus.valueOf(p[4]));
                    vehicles.add(v);
                }
                vScan.close();
            }

            // ---- Load Customers ----
            File cFile = new File("customers.txt");
            if (cFile.exists()) {
                Scanner cScan = new Scanner(cFile);
                while (cScan.hasNextLine()) {
                    String[] p = cScan.nextLine().split(",");
                    customers.add(new Customer(Integer.parseInt(p[0]), p[1]));
                }
                cScan.close();
            }
            
         // --- Load Rental Records ---
            File hFile = new File("rental_records.txt");
            if (hFile.exists()) {
                Scanner hScan = new Scanner(hFile);
                while (hScan.hasNextLine()) {
                    String[] parts = hScan.nextLine().split(",");

                    String type = parts[0];              // RENT or RETURN
                    String plate = parts[1];
                    String custName = parts[2];
                    LocalDate date = LocalDate.parse(parts[3]);
                    double amount = Double.parseDouble(parts[4]);

                    // Find vehicle
                    Vehicle v = findVehicleByPlate(plate);

                    // Find customer by name
                    Customer c = null;
                    for (Customer temp : customers) {
                        if (temp.getCustomerName().equals(custName)) {
                            c = temp;
                            break;
                        }
                    }

                    if (v != null && c != null) {
                        RentalRecord rec = new RentalRecord(v, c, date, amount, type);
                        rentalHistory.addRecord(rec);
                    }
                }
                hScan.close();
            }
  
            

        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }
}
