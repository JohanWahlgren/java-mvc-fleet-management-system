package com.javengers.models;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Vehicle {
    
    private String name;
    private final String vin;
    private String currentLocation;
    private String type;
    private double capacity;
    private boolean isDecommissioned;
    private ServiceHistory serviceHistory;
    private MaintenanceSchedule maintenanceSchedule;
    public static final ArrayList<String> VEHICLE_TYPES = new ArrayList<>(Arrays.asList("Large Truck", "Medium Truck", "Van"));

    public Vehicle(String vin) {
        this.serviceHistory = new ServiceHistory();
        this.maintenanceSchedule = new MaintenanceSchedule();
        this.vin = vin;
    }

    public Vehicle(String name, String type, double capacity, String currentLocation, String vin) {
        this.name = name;
        this.currentLocation = currentLocation;
        this.type = type;
        this.capacity = capacity;
        this.isDecommissioned = false;
        this.vin = vin;
    }

    // Getters and setters for all properties
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVin() {
        return vin;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public boolean getIsDecommissioned() {
        return isDecommissioned;
    }

    public void setDecommissioned(boolean isDecommissioned) {
        this.isDecommissioned = isDecommissioned;
    }

    public ServiceHistory getServiceHistory() {
        return serviceHistory;
    }

    public void setServiceHistory(ServiceHistory serviceHistory) {
        this.serviceHistory = serviceHistory;
    }

    public MaintenanceSchedule getMaintenanceSchedule() {
        return maintenanceSchedule;
    }

    public void setMaintenanceSchedule(MaintenanceSchedule maintenanceSchedule) {
        this.maintenanceSchedule = maintenanceSchedule;
    }

    //Other methods

    public double getTotalServiceCost() {
        ServiceHistory serviceHistory = getServiceHistory();
        double serviceCost = 0.0;

        if (serviceHistory != null) {
            for (ServiceActivity serviceActivity : serviceHistory.getServiceActivities()) {
                serviceCost += serviceActivity.getCostOfService();
            }
        }

        serviceCost = Math.round(serviceCost * 10.0) / 10.0;
        return serviceCost;
    }

    public ObservableList<ServiceActivity> getServiceActivities() {
        if (serviceHistory != null) {
            return serviceHistory.getServiceActivities();
        } else {
            return FXCollections.observableArrayList(); 
        }
    }

    public ObservableList<ServiceWorkshop> getVisitedWorkshops() {
        if (serviceHistory != null) {
            return FXCollections.observableArrayList(serviceHistory.getAllServiceWorkshops());
        } else {
            return FXCollections.observableArrayList(); // Om det inte finns någon servicehistorik returneras en tom ObservableList.
        }
    }
}