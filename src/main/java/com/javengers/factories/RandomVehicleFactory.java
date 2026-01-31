package com.javengers.factories;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.Year;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.javengers.models.Vehicle;
import com.javengers.models.Part;
import com.javengers.models.ServiceWorkshop;
import com.javengers.models.ServiceWorkshopRegister;

public class RandomVehicleFactory {
    private Random random = new Random();
    private ServiceWorkshopRegister serviceWorkshopRegister;

    private String[] vehicleNames = {"Volvo-fmx22 crewcab", "Volvo-fx14", "Volvo-fx16", "Scania-xt22", "Scania-p220", "Scania-r560", "Mercedes-rgs17", "Mercedes-rgs21", "Mercedes-eActros23", "Volvo-eMOB22"};
    private int[] capacities = {5000, 1500, 3000, 2500, 4000, 1000, 3500, 1200, 500, 100};
    private String[] locations = {"Ole Römers Väg 6", "Stålgjuterivägen 38", "Doktorgatan 13", "Malmviksgatan 41", "Bergsvägen 19", "Norra björkvägen 2", "Västra Hamngatan 78", "Östra Tornallén 52", "Avenyn 1", "Luosavaara 9"};

    private ArrayList<Part> parts = new ArrayList<>();

    private String[] descriptions = {"Routine Maintenance", "Annual Checkup", "Emergency Repair", "Scheduled Upgrade"};

    public RandomVehicleFactory(ServiceWorkshopRegister serviceWorkshopRegister) {
        this.serviceWorkshopRegister = serviceWorkshopRegister;
        parts.add(new Part("Oil"));
        parts.add(new Part("Tire"));
        parts.add(new Part("Engine"));
        parts.add(new Part("Brake"));
        parts.add(new Part("Water Pump"));
        parts.add(new Part("Paint"));
        parts.add(new Part("Battery"));
        parts.add(new Part("Windshield"));
        parts.add(new Part("Alignment"));
        parts.add(new Part("AC"));
        parts.add(new Part("Headlight"));
        parts.add(new Part("Cylinder"));
    }

    public Vehicle generateRandomVehicle() {
        int index = random.nextInt(vehicleNames.length);
        int typeIndex = random.nextInt(Vehicle.VEHICLE_TYPES.size());

        Vehicle vehicle = VehicleFactory.createVehicle(vehicleNames[index], Vehicle.VEHICLE_TYPES.get(typeIndex), capacities[index], locations[index]);

        int activityCount = 1 + random.nextInt(3);
        for (int i = 0; i < activityCount; i++) {
            createRandomServiceActivity(vehicle);
        }

        int maintenanceActivityCount = 1 + random.nextInt(20);
        for (int i = 0; i < maintenanceActivityCount; i++) {
            createRandomMaintenanceActivity(vehicle);
        }
        return vehicle;
    }

    private double roundToOneDecimalPlace(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private LocalDate getRandomPastDate() {
        // Generate a random year within the last 5 years
        int year = LocalDate.now().getYear() - random.nextInt(5) - 1;
    
        // Generate a random month and day
        int month = random.nextInt(12) + 1; // Adding 1 to ensure month is in the range [1, 12]
        int day;
    
        if (month == 2) {
            // Check if the year is a leap year
            boolean isLeapYear = Year.of(year).isLeap();
            day = isLeapYear ? random.nextInt(29) + 1 : random.nextInt(28) + 1;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            day = random.nextInt(30) + 1;
        } else {
            day = random.nextInt(31) + 1;
        }
    
        return LocalDate.of(year, month, day);
    }
    
    private LocalDate getRandomFutureDate() {
        // Generate a random year within the next 5 years
        int year = 2024 + random.nextInt(2);
    
        // Generate a random month and day
        int month = random.nextInt(12) + 1; // Adding 1 to ensure month is in the range [1, 12]
        int day;
    
        if (month == 2) {
            // Check if the year is a leap year
            boolean isLeapYear = Year.of(year).isLeap();
            day = isLeapYear ? random.nextInt(29) + 1 : random.nextInt(28) + 1;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            day = random.nextInt(30) + 1;
        } else {
            day = random.nextInt(31) + 1;
        }
    
        return LocalDate.of(year, month, day);
    }
    
    private void createRandomServiceActivity(Vehicle vehicle) {
        int descriptionIndex = random.nextInt(descriptions.length);
        int randomAmountReplacedParts = random.nextInt(90);
        ObservableList<Part> partsReplaced = FXCollections.observableArrayList();;

        for (int i = 0; i < randomAmountReplacedParts; i++){
            partsReplaced.add(parts.get(random.nextInt(parts.size())));
        }

        if(vehicle.getType().equals("Large Truck")) {
            ServiceWorkshop selectedWorkshop = getExternalWorkshop();
            ActivityFactory activityFactory = new ActivityFactory(vehicle);     

            
            LocalDate randomDate = getRandomPastDate();
            double cost = 100.0 + random.nextDouble() * 400.0;
            cost = roundToOneDecimalPlace(cost);

            activityFactory.addServiceActivity(
                partsReplaced,
                randomDate,
                descriptions[descriptionIndex],
                cost,
                selectedWorkshop);
        } else {
            ServiceWorkshop selectedWorkshop = getRandomWorkshop();
            ActivityFactory activityFactory = new ActivityFactory(vehicle);
            LocalDate randomDate = getRandomPastDate();
            double cost = 100.0 + random.nextDouble() * 400.0;
            cost = roundToOneDecimalPlace(cost);

            activityFactory.addServiceActivity(
                partsReplaced,
                randomDate,
                descriptions[descriptionIndex],
                cost,
                selectedWorkshop);
        }  
    }

    private void createRandomMaintenanceActivity(Vehicle vehicle) {
        int descriptionIndex = random.nextInt(descriptions.length);

        if(vehicle.getType().equals("Large Truck")) {
            ServiceWorkshop selectedWorkshop = getExternalWorkshop();
            ActivityFactory activityFactory = new ActivityFactory(vehicle);

            LocalDate randomDate = getRandomFutureDate();

            activityFactory.addMaintenanceActivity(
                descriptions[descriptionIndex],
                randomDate,
                selectedWorkshop);
        } else {
            ServiceWorkshop selectedWorkshop = getRandomWorkshop();
            ActivityFactory activityFactory = new ActivityFactory(vehicle);
            LocalDate randomDate = getRandomFutureDate();

            activityFactory.addMaintenanceActivity(
                descriptions[descriptionIndex],
                randomDate,
                selectedWorkshop);
        }
    }

    private ServiceWorkshop getRandomWorkshop() {
        int workshopIndex = random.nextInt(serviceWorkshopRegister.getWorkshops().size());
        return serviceWorkshopRegister.getWorkshops().get(workshopIndex);
    }

    public ServiceWorkshop getExternalWorkshop() {
        for(ServiceWorkshop workshop : serviceWorkshopRegister.getWorkshops()) {
            if(workshop.getType().equals("External")) {
                return workshop;
            }
        }
        return null;
    }
}