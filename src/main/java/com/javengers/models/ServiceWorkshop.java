package com.javengers.models;

import java.util.ArrayList;
import java.util.Arrays;

public class ServiceWorkshop {
    private String name;
    private String type;
    private String address;
    private ArrayList<ServiceActivity> serviceActivities = new ArrayList<ServiceActivity>();
    private ArrayList<MaintenanceActivity> maintenanceActivities = new ArrayList<MaintenanceActivity>();
    public static final ArrayList<String> SERVICE_WORKSHOP_TYPES = new ArrayList<>(Arrays.asList("Internal", "External"));

    @Override
    public String toString() {
        return this.getType() + " - " + this.getName();
    }

    //Constructors
    public ServiceWorkshop(){
    }

    public ServiceWorkshop(String name, String type, String address){
        this.name = name;
        this.type = type;
        this.address = address;
    }

    //Getters and setters
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public ArrayList <ServiceActivity> getServiceActivities(){
        return serviceActivities;
    }

    public void setServiceActivities(ArrayList <ServiceActivity> serviceActivities){
        this.serviceActivities = serviceActivities;
    }

    public ArrayList <MaintenanceActivity> getMaintenanceActivities(){
        return maintenanceActivities;
    }

    public void setMaintenenceActivities(ArrayList <MaintenanceActivity> maintenanceActivities){
        this.maintenanceActivities = maintenanceActivities;
    }

    //Other methods
    public void addServiceActivity(ServiceActivity serviceActivity){
        this.serviceActivities.add(serviceActivity);
    }

    public void addMaintenenceActivity(MaintenanceActivity maintenanceActivity){
        this.maintenanceActivities.add(maintenanceActivity);
    }

    public void removeServiceActivity(ServiceActivity serviceActivity){
        this.serviceActivities.remove(serviceActivity);
    }

    public void removeMaintenanceActivity(MaintenanceActivity maintenanceActivity){
        this.maintenanceActivities.remove(maintenanceActivity);
    }

    public double getWorkshopCost(){
        double totalCost = 0;
        for(ServiceActivity serviceActivity : serviceActivities) {
            totalCost += serviceActivity.getCostOfService();
        }
        return totalCost;
    }
}