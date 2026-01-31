package com.javengers.factories;

import com.javengers.models.MaintenanceSchedule;
import com.javengers.models.ServiceHistory;
import com.javengers.models.Vehicle;

public class VehicleFactory {

    private static int vinCounter = 0;

    public static Vehicle createVehicle(String name, String type, double capacity, String location) {
        // This static method is used to produce vehicles in a more controlled manner
        // Inisde of this class we can make sure that we have a history, scehdule always associated with the vehicle
        // Also, we can make sure that the vin is always unique.
        
        Vehicle vehicle = new Vehicle(name, type, capacity, location, Integer.toString(++vinCounter));

        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule();
        ServiceHistory serviceHistory = new ServiceHistory();
        // Connect vehicles to history and schedule
        vehicle.setMaintenanceSchedule(maintenanceSchedule);
        vehicle.setServiceHistory(serviceHistory);
        // Connect both ways
        serviceHistory.setVehicle(vehicle);
        maintenanceSchedule.setVehicle(vehicle);
        return vehicle;
    }
}