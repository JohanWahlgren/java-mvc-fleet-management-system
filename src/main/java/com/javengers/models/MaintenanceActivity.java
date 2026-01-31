package com.javengers.models;
import java.time.LocalDate;

public class MaintenanceActivity extends Activity{

    private MaintenanceSchedule maintenanceSchedule;

    // Constructors
    public MaintenanceActivity(String description, LocalDate date, ServiceWorkshop serviceWorkshop) {
        super(description, date, serviceWorkshop);
    }

    public MaintenanceActivity() {
    }

    //Getters and setters

    public MaintenanceSchedule getMaintenanceSchedule(){
        return maintenanceSchedule;
    }

    public void setMaintenanceSchedule(MaintenanceSchedule maintenanceSchedule){
        this.maintenanceSchedule = maintenanceSchedule;
    }
}