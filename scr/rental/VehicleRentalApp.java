package rental;

import java.util.Scanner;
import java.time.LocalDate;

public class VehicleRentalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RentalSystem rentalSystem = RentalSystem.getInstance_vai();

        
        while (true) {
        	System.out.println("\n1: Add Vehicle\n" + 
                                  "2: Add Customer\n" + 
                                  "3: Rent Vehicle\n" + 
                                  "4: Return Vehicle\n" + 
                                  "5: Display Available Vehicles\n" + 
                                  "6: Show Rental History\n" + 
                                  "0: Exit\n");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("  1: Car\n" + 
                                       "  2: Minibus\n" + 
                                       "  3: Pickup Truck");
                    int type = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter license plate: ");
                    String plate = scanner.nextLine().toUpperCase();
                    System.out.print("Enter make: ");
                    String make = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    Vehicle vehicle;
                    if (type == 1) {
                        System.out.print("Enter number of seats: ");
                        int seats = scanner.nextInt();
                        vehicle = new Car(make, model, year, seats);
                        System.out.println("Car added successfully.");
                    } else if (type == 2) {
                        System.out.print("Is accessible? (true/false): ");
                        boolean isAccessible = scanner.nextBoolean();
                        vehicle = new Minibus(make, model, year, isAccessible);
                        System.out.println("Minibus added successfully.");
		            } else if (type == 3) {
		                System.out.print("Enter the cargo size: ");
		                double cargoSize = scanner.nextDouble();
		                scanner.nextLine();
		                System.out.print("Has trailer? (true/false): ");
		                boolean hasTrailer = scanner.nextBoolean();
		                vehicle = new PickupTruck(make, model, year, cargoSize, hasTrailer);
		                System.out.println("Pickup Truck added successfully.");
		            } else {
		            	vehicle = null;
		            }
                    
                    if (vehicle != null){
	                    vehicle.setLicensePlate(plate);
	                    rentalSystem.addVehicle(vehicle);
                    }
                    else {
	                    System.out.println("Vehicle not added successfully.");
                    }
                    break;

                case 2:
                    System.out.print("Enter customer ID: ");
                    int cid = scanner.nextInt();
                    scanner.nextLine(); // Consume the leftover newline
                    System.out.print("Enter name: ");
                    String cname = scanner.nextLine();

                    rentalSystem.addCustomer(new Customer(cid, cname));
                    System.out.println("Customer added successfully.");
                    break;
                    
                case 3:
                	rentalSystem.displayVehicles(Vehicle.VehicleStatus.Available);

                    System.out.print("Enter license plate: ");
                    String rentPlate = scanner.nextLine().toUpperCase();

                	System.out.println("Registered Customers:");
                	rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer ID: ");
                    int cidRent = scanner.nextInt();

                    System.out.print("Enter rental amount: ");
                    double rentAmount = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vehicleToRent = rentalSystem.findVehicleByPlate(rentPlate);
                    Customer customerToRent = rentalSystem.findCustomerById(cidRent);

                    if (vehicleToRent == null || customerToRent == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    rentalSystem.rentVehicle(vehicleToRent, customerToRent, LocalDate.now(), rentAmount);
                    break;

                case 4:
                	rentalSystem.displayVehicles(Vehicle.VehicleStatus.Rented);

                	System.out.print("Enter license plate: ");
                    String returnPlate = scanner.nextLine().toUpperCase();
                    
                	System.out.println("Registered Customers:");
                	rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer ID: ");
                    int cidReturn = scanner.nextInt();

                    System.out.print("Enter any additional return fees: ");
                    double returnFees = scanner.nextDouble();
                    scanner.nextLine();

                    Vehicle vehicleToReturn = rentalSystem.findVehicleByPlate(returnPlate);
                    Customer customerToReturn = rentalSystem.findCustomerById(cidReturn);

                    if (vehicleToReturn == null || customerToReturn == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    rentalSystem.returnVehicle(vehicleToReturn, customerToReturn, LocalDate.now(), returnFees);
                    break;
                    
                case 5:
                    rentalSystem.displayVehicles(Vehicle.VehicleStatus.Available);
                    break;
                
                case 6:
                    rentalSystem.displayRentalHistory();
                    break;
                    
                case 0:
                	scanner.close();
                    System.exit(0);
            }
        }
    }
}