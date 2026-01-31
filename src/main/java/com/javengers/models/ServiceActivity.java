package com.javengers.models;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceActivity extends Activity {
    private ObservableList<Part> partsReplaced = FXCollections.observableArrayList();
    private ServiceHistory serviceHistory;
    private double costOfService;

    // Constructors
    public ServiceActivity(ObservableList<Part> partsReplaced, LocalDate date, String description, double costOfService, ServiceWorkshop serviceWorkshop) {
        super(description, date, serviceWorkshop);
        this.partsReplaced = partsReplaced;
        this.costOfService = costOfService;
    }

    public ServiceActivity() {
    }

    // Getters and Setters
    public ObservableList<Part> getPartsReplaced() {
        return partsReplaced;
    }

    public void setPartsReplaced(ObservableList<Part> partsReplaced) {
        this.partsReplaced = partsReplaced;
    }

    public ServiceHistory getServiceHistory(){
        return this.serviceHistory;
    }

    public void setServiceHistory(ServiceHistory serviceHistory){
        this.serviceHistory = serviceHistory;
    }

    public double getCostOfService() {
        return costOfService;
    }

    public void setCostOfService(double costOfService) {
        this.costOfService = costOfService;
    }

    //Other methods
    public int getNumberOfPartsReplaced() {
        return partsReplaced.size();
    }

    public void addPart(Part part){
        this.partsReplaced.add(part);
    }
    
    public void removePart(Part part){
        this.partsReplaced.remove(part);
    }
}