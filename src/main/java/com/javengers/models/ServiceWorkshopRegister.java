package com.javengers.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServiceWorkshopRegister {
    private ObservableList<ServiceWorkshop> workshops = FXCollections.observableArrayList();

    //Constructor
    public ServiceWorkshopRegister() {
        this.workshops = FXCollections.observableArrayList();
    }

    public ObservableList<ServiceWorkshop> getWorkshops() {
        return FXCollections.unmodifiableObservableList(workshops);
    }

    //Other methods
    public void addWorkshop(ServiceWorkshop workshop) {
        workshops.add(workshop);
    }

    public void removeWorkshop(ServiceWorkshop workshop) {
        workshops.remove(workshop);
    }

    public ServiceWorkshop getMostExpensiveWorkshop() {
        ServiceWorkshop mostExpensiveWorkshop = null;
        double totalCost = 0;
        double highestCost = 0;

        for(ServiceWorkshop workshop : workshops) {
            totalCost = workshop.getWorkshopCost();
            if(totalCost > highestCost) {
                highestCost = totalCost;
                mostExpensiveWorkshop = workshop;
            }
        }
        return mostExpensiveWorkshop;
    }
}