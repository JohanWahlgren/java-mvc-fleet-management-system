package com.javengers.factories;

import java.time.LocalDate;

import com.javengers.models.MaintenanceSchedule;
import com.javengers.models.ServiceActivity;
import com.javengers.models.ServiceHistory;
import com.javengers.models.ServiceWorkshop;
import com.javengers.models.Vehicle;
import com.javengers.models.MaintenanceActivity;
import com.javengers.models.Part;
import javafx.collections.ObservableList;

public class ActivityFactory {
    // this class is made so that we can control the assosiation assignments when creating new activities.
    private Vehicle vehicle;

    public ActivityFactory(Vehicle vehicle) {
        this.vehicle = vehicle;
        // Initialize ServiceHistory and MaintenanceSchedule if they don't exist. 
        // this is to make sure we don't have vehicles that are missing schedule or history
        if (vehicle.getServiceHistory() == null) {
            vehicle.setServiceHistory(new ServiceHistory());
        }
        if (vehicle.getMaintenanceSchedule() == null) {
            vehicle.setMaintenanceSchedule(new MaintenanceSchedule());
        }
    }

    public MaintenanceActivity addMaintenanceActivity(String description, LocalDate date, ServiceWorkshop serviceWorkshop) {
        MaintenanceActivity maintenanceActivity = new MaintenanceActivity(description, date, serviceWorkshop);
        MaintenanceSchedule schedule = vehicle.getMaintenanceSchedule();
        serviceWorkshop.addMaintenenceActivity(maintenanceActivity);
        // Connect both ways
        schedule.addActivity(maintenanceActivity);
        maintenanceActivity.setMaintenanceSchedule(schedule);
        return maintenanceActivity;
    }

    public ServiceActivity addServiceActivity(ObservableList<Part> partsReplaced, LocalDate date, String description, double costOfService, ServiceWorkshop serviceWorkshop) {
        ServiceActivity serviceActivity = new ServiceActivity(partsReplaced, date, description, costOfService, serviceWorkshop);
        ServiceHistory history = vehicle.getServiceHistory();
        serviceWorkshop.addServiceActivity(serviceActivity);
        // Connect both ways
        history.addActivity(serviceActivity);
        serviceActivity.setServiceHistory(history);
        // Decomission newly created vehicles directly.
        updatedecomissionStatus();
        return serviceActivity;
    }

    private void updatedecomissionStatus(){
        if (this.vehicle.getServiceHistory().getTotalPartsReplaced() > 100){
            this.vehicle.setDecommissioned(true);
        } else {
            this.vehicle.setDecommissioned(false);
        }
    }
}