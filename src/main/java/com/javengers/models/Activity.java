package com.javengers.models;
import java.time.LocalDate;

public class Activity {
    private String description;
    private LocalDate date;
    private ServiceWorkshop serviceWorkshop;

    // Constructors
    public Activity(String description, LocalDate date, ServiceWorkshop serviceWorkshop) {
        this.description = description;
        this.date = date;
        this.serviceWorkshop = serviceWorkshop;
    }

    public Activity() {
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ServiceWorkshop getServiceWorkshop() {
        return serviceWorkshop;
    }

    public void setServiceWorkshop(ServiceWorkshop serviceWorkshop) {
        this.serviceWorkshop = serviceWorkshop;
    }
}