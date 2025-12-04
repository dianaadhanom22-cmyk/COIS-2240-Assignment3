package rental;

import java.util.List;
import java.util.ArrayList;

public class RentalHistory {
    private List<RentalRecord> rentalRecords = new ArrayList<>();

    public void addRecord(RentalRecord record) {
        rentalRecords.add(record);
    }

    public List<RentalRecord> getRentalHistory() {
        return rentalRecords;
    }

    public List<RentalRecord> getRentalRecordsByCustomer(String customerName) {
        List<RentalRecord> result = new ArrayList<>();
        for (RentalRecord record : rentalRecords) {
            if (record.getCustomer().toString().toLowerCase().contains(customerName.toLowerCase())) {
                result.add(record);
            }
        }
        return result;
    }

    public List<RentalRecord> getRentalRecordsByVehicle(String licensePlate) {
        List<RentalRecord> result = new ArrayList<>();
        for (RentalRecord record : rentalRecords) {
            if (record.getVehicle().getLicensePlate().equalsIgnoreCase(licensePlate)) {
                result.add(record);
            }
        }
        return result;
    }
}