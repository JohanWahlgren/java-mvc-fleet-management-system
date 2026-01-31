package com.javengers.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.javengers.exceptions.DuplicateVinException;

public class VehicleRegister {
    private ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();

    public VehicleRegister() {
    }

    public void addVehicle(Vehicle vehicle) throws DuplicateVinException {
        if (findVehicle(vehicle.getVin()) != null) {
            throw new DuplicateVinException("A vehicle with VIN " + vehicle.getVin() + " already exists.");
        }

        if (vehicle.getCapacity() < 0) {
            throw new IllegalArgumentException("Vehicle capacity cannot be negative.");
    }

        vehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public Vehicle findVehicle(String vin) {
        for (Vehicle vehicle : vehicles) {
            String vehicleVin = vehicle.getVin();
            if (vehicleVin != null && vehicleVin.equals(vin)) {
                return vehicle;
            }
        }
        return null;
    }

    public double calculateAverageCostForVehicleService() {
        double totalCost = 0.0;
        int totalServiceCount = 0;

        for (Vehicle vehicle : vehicles) {
            ServiceHistory serviceHistory = vehicle.getServiceHistory();
            if (serviceHistory == null) continue;
            
            for (ServiceActivity activity : serviceHistory.getServiceActivities()) {
                totalCost += activity.getCostOfService();
                totalServiceCount++;
            }
        }

        return totalServiceCount > 0 ? totalCost / totalServiceCount : 0.0;
    }

    public ServiceActivity getMostExpensiveService() {
        double maxCost = 0.0;
        ServiceActivity mostExpensive = null;

        for (Vehicle vehicle : vehicles) {
            ServiceHistory serviceHistory = vehicle.getServiceHistory();
            if (serviceHistory == null) continue;

            for (ServiceActivity activity : serviceHistory.getServiceActivities()) {
                double currentCost = activity.getCostOfService();
                if (currentCost <= maxCost) continue;

                maxCost = currentCost;
                mostExpensive = activity;
            }
        }

        return mostExpensive;
    }


    public double getTotalCostOfService() {
        double totalCost = 0.0;

        for (Vehicle vehicle : vehicles) {
            ServiceHistory serviceHistory = vehicle.getServiceHistory();
            if (serviceHistory == null) continue;
            
            for (ServiceActivity activity : serviceHistory.getServiceActivities()) {
                totalCost += activity.getCostOfService();
            }
        }

        return totalCost;
    }

    public ObservableList<Vehicle> getVehicles() {
        return FXCollections.unmodifiableObservableList(vehicles);
    }
}