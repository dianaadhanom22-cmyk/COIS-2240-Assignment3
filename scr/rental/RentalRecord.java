package rental;

import java.time.LocalDate;

public class RentalRecord {
    private Vehicle vehicle;
    private Customer customer;
    private LocalDate recordDate;
    private double totalAmount;
    private String recordType; // "RENT" or "RETURN"

    public RentalRecord(Vehicle vehicle, Customer customer, LocalDate recordDate, double totalAmount, String recordType) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.recordDate = recordDate;
        this.totalAmount = totalAmount;
        this.recordType = recordType;
    }

    public Customer getCustomer(){
    	return customer;
    }
    
    public Vehicle getVehicle(){
    	return vehicle;
    }
    
    public LocalDate getRecordDate() {
        return recordDate;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public String getRecordType() {
        return recordType;
    }
    
    @Override
    public String toString() {
        return recordType + " | Plate: " + vehicle.getLicensePlate() + 
               " | Customer: " + customer.getCustomerName() + 
               " | Date: " + recordDate + 
               " | Amount: $" + totalAmount;
    }
}