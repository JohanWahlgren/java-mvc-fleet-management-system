package com.javengers.models;

import java.util.ArrayList;

public class MaintenanceSchedule {
    private Vehicle vehicle;
    private ArrayList<MaintenanceActivity> maintenanceActivities;

    public MaintenanceSchedule() {
        this.maintenanceActivities = new ArrayList<MaintenanceActivity>();
    }

    //Getters and setters

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public ArrayList<MaintenanceActivity> getMaintenanceActivities() {
        return maintenanceActivities;
    }

    public void setMaintenanceActivities(ArrayList<MaintenanceActivity> maintenanceActivities) {
        this.maintenanceActivities = maintenanceActivities;
    }

    //Other methods
    
    public void addActivity(MaintenanceActivity maintenanceActivity) {
        maintenanceActivities.add(maintenanceActivity);
    }

    public void removeActivity(MaintenanceActivity maintenanceActivity){
        maintenanceActivities.remove(maintenanceActivity);
    }
}