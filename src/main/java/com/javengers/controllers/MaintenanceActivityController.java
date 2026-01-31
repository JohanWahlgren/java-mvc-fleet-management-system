package com.javengers.controllers;
import java.time.LocalDate;

import com.javengers.factories.ActivityFactory;
import com.javengers.models.MaintenanceActivity;
import com.javengers.models.ServiceWorkshop;
import com.javengers.models.ServiceWorkshopRegister;
import com.javengers.models.Vehicle;
import com.javengers.ui.Utils;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;


public class MaintenanceActivityController {
    private MaintenanceActivity maintenanceActivity;
    private Vehicle vehicle;
    public boolean isEditMode = false;

    @FXML
    private Label labelDescriptionOfService;

    @FXML
    private Label labelServiceDate;

    @FXML
    private TextField textFieldDescriptionOfService;

    @FXML
    private ComboBox<ServiceWorkshop> comboBoxWorkshop;

    @FXML
    private Label labelWorkshop;

    @FXML
    private Button buttonServiceActivityAccept;

    @FXML 
    private DatePicker datePickerServiceActivity;

    private ServiceWorkshopRegister serviceWorkshopRegister;

    @FXML
    public void initialize() {

        if(serviceWorkshopRegister != null) {
            ObservableList<ServiceWorkshop> workshops = serviceWorkshopRegister.getWorkshops();
            comboBoxWorkshop.setItems(workshops);
        } else {
            comboBoxWorkshop.setPromptText("No workshops are currently in the system");
        } 
        // Set up DayCellFactory to restrict dates to the future
        datePickerServiceActivity.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });

        if (isEditMode && maintenanceActivity != null) {
            populateFieldsForEdit();
        }
    }

    private void populateFieldsForEdit() {
        textFieldDescriptionOfService.setText(maintenanceActivity.getDescription());
        datePickerServiceActivity.setValue(maintenanceActivity.getDate());
        comboBoxWorkshop.setValue(maintenanceActivity.getServiceWorkshop());
    }
    

    public String getDescriptionOfService() {
        return textFieldDescriptionOfService.getText();
    }

    @FXML
    private void handleDatePickerServiceActivityAction(ActionEvent event) {
        if(datePickerServiceActivity.getValue() == null) {
             Utils.showAlert("Wrong Input", null, "Please choose a date.");
        }
    }

    public void close_window(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleButtonServiceActivityAcceptAction(ActionEvent event) {

        if (vehicle == null) {
            Utils.showAlert("No Vehicle Selected", 
                            "Unable to Add Activity", 
                            "Please select a vehicle before attempting to add an activity. " +
                            "If you are currently in the 'Workshops' tab, switch back to the 'Vehicles' tab " +
                            "to view and select a vehicle. You can choose a vehicle from the vehicle list or dropdown menu. " +
                            "If your vehicle is not listed, ensure it is properly registered in the system.");
        }

        // Set instance variables for new service activity
        String descriptionOfService = getDescriptionOfService();
        ServiceWorkshop selectedWorkshop = comboBoxWorkshop.getValue();
        LocalDate serviceDate = datePickerServiceActivity.getValue();

        if (descriptionOfService.trim().isEmpty()) {
            Utils.showAlert("Wrong Input", "No Description", "Please provide a description for the service activity.");
            return;
        }

        if (selectedWorkshop == null) {
            Utils.showAlert("Wrong Input", "No Workshop Selected", "Please select a workshop for the service activity.");
            return;
        }

        if (serviceDate == null){
            Utils.showAlert("Wrong Inputs", "No Date", "Please assign a date to the activity.");
            return;
        }

        //Checking so that Large truck does not have internal service workshop
        if(vehicle.getType().equals("Large Truck") && selectedWorkshop.getType().equals("Internal")) {
            Utils.showAlert("Wrong Inputs", null, "The vehicle type Large Truck cannot be serviced at an Internal workshop");
            return;
        }

        if (isEditMode){

            ServiceWorkshop workshop = maintenanceActivity.getServiceWorkshop();
            if (workshop != null){
                workshop.removeMaintenanceActivity(maintenanceActivity);
            }
            maintenanceActivity.setDate(serviceDate);
            maintenanceActivity.setDescription(descriptionOfService);
            maintenanceActivity.setServiceWorkshop(selectedWorkshop);
            selectedWorkshop.addMaintenenceActivity(maintenanceActivity);
        } else { 
            // Add an activity using the activity factory
            // The reason we do this is so that we always create an activity the same way
            // which means that we can avoid mistakes where we mis-set certain attributes or
            // forget to do an association.

            ActivityFactory activityFactory = new ActivityFactory(this.vehicle);
            activityFactory.addMaintenanceActivity(descriptionOfService, serviceDate, selectedWorkshop);
        }
        close_window(event);

    }

    public MaintenanceActivity getMaintenanceActivity() {
        return maintenanceActivity;
    }

    public void setMaintenanceActivity(MaintenanceActivity maintenanceActivity) {
        this.maintenanceActivity = maintenanceActivity;
    }

    @FXML
    private void handleButtonCancelAction(ActionEvent event) {
        close_window(event);
    }
    public void setServiceWorkshopRegister(ServiceWorkshopRegister serviceWorkshopRegister){
        this.serviceWorkshopRegister = serviceWorkshopRegister;
    }

    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }
}