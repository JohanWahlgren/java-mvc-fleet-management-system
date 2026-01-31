package com.javengers.models;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceHistory {

    //Association with other classes
    private Vehicle vehicle;
    private ObservableList<ServiceActivity> serviceActivities = FXCollections.observableArrayList();

    //Constructors
    public ServiceHistory() {}

    public ServiceHistory(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

     //Getters and setters
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ObservableList<ServiceActivity> getServiceActivities() {
        return serviceActivities;
    }

    public void setActivities(ObservableList<ServiceActivity> serviceActivities) {
        this.serviceActivities = serviceActivities;
    }

    //Other methods
    public int getTotalPartsReplaced() {
        int totalPartsReplaced = 0;
        for (ServiceActivity serviceActivity : serviceActivities) {
            totalPartsReplaced += serviceActivity.getNumberOfPartsReplaced();
        }
        return totalPartsReplaced;
    }

    public ArrayList<Part> getPartsReplaced(){
        ArrayList<Part> partsReplaced = new ArrayList<Part>();
        for (ServiceActivity activity : serviceActivities){
            partsReplaced.addAll(activity.getPartsReplaced());
        }
        return partsReplaced;
    }

    public ArrayList<ServiceWorkshop> getAllServiceWorkshops() {
        ArrayList<ServiceWorkshop> serviceWorkshops = new ArrayList<ServiceWorkshop>();
        for (ServiceActivity serviceActivity : serviceActivities) {
            ServiceWorkshop serviceWorkshop = serviceActivity.getServiceWorkshop();
            boolean isAlreadyAdded = false;
    
            for (ServiceWorkshop workshop : serviceWorkshops) {
                if (workshop == serviceWorkshop) { 
                    isAlreadyAdded = true;
                    break; 
                }
            }
    
            if (!isAlreadyAdded) {
                serviceWorkshops.add(serviceWorkshop);
            }
        }
        return serviceWorkshops;
    }

    public double getTotalCostOfService() {
        double totalCostOfService = 0;
        for (ServiceActivity serviceActivity : serviceActivities) {
            totalCostOfService += serviceActivity.getCostOfService();
        }
        return totalCostOfService;
    }

    public void addActivity(ServiceActivity serviceActivitity){
        this.serviceActivities.add(serviceActivitity);
    }

    public void removeActivity(ServiceActivity serviceActivitity){
        this.serviceActivities.remove(serviceActivitity);
    }

}