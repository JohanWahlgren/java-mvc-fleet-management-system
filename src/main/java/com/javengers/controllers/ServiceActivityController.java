package com.javengers.controllers;

import java.io.IOException;
import java.time.LocalDate;

import com.javengers.factories.ActivityFactory;
import com.javengers.models.ServiceActivity;
import com.javengers.models.ServiceWorkshop;
import com.javengers.models.ServiceWorkshopRegister;
import com.javengers.models.Vehicle;
import com.javengers.ui.Utils;

import javafx.collections.FXCollections;
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
import com.javengers.models.Part;

public class ServiceActivityController {

    private ServiceActivity serviceActivity;
    private Vehicle vehicle;
    private ServiceWorkshopRegister serviceWorkshopRegister;
    private MainViewController mainViewController;
    private ObservableList<Part> partsReplaced = FXCollections.observableArrayList();
    public boolean isEditMode = false;

    @FXML
    private Label labelDescriptionOfService;

    @FXML
    private Label labelWhichPartsWereReplaced;

    @FXML
    private Label labelServiceDate;

    @FXML
    private Label labelCostOfService;

    @FXML
    private TextField textFieldDescriptionOfService;

    @FXML
    private TextField textFieldCostOfService;

    @FXML
    private ComboBox<ServiceWorkshop> comboBoxWorkshop;

    @FXML
    private Label labelWorkshop;

    @FXML
    private Button buttonServiceActivityAccept;

    @FXML 
    private DatePicker datePickerServiceActivity;
    
    // Constructor to receive MainViewController instance
    public ServiceActivityController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    public void initialize() {

        if(serviceWorkshopRegister != null) {
            ObservableList<ServiceWorkshop> workshops = serviceWorkshopRegister.getWorkshops();
            comboBoxWorkshop.setItems(workshops);
        } else {
            comboBoxWorkshop.setPromptText("No workshops are currently in the system");
        } 
        // Set up DayCellFactory to restrict dates to the past
        datePickerServiceActivity.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isAfter(LocalDate.now()));
            }
        });

        if (this.isEditMode){
            populateFieldsForEdit();
        }
    }

    private void populateFieldsForEdit() {
        textFieldDescriptionOfService.setText(serviceActivity.getDescription());
        textFieldCostOfService.setText(String.valueOf(serviceActivity.getCostOfService()));
        datePickerServiceActivity.setValue(serviceActivity.getDate());
        comboBoxWorkshop.setValue(serviceActivity.getServiceWorkshop());
        partsReplaced.addAll(serviceActivity.getPartsReplaced());
    }
    
    public String getDescriptionOfService() {
        return textFieldDescriptionOfService.getText();
    }

    private ObservableList<Part> getPartsReplaced(){
        return partsReplaced;
    }

    public double getCostOfService() {
        String stringCost = textFieldCostOfService.getText();
        try {
            double costOfService = Double.parseDouble(stringCost);
            return costOfService;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Please enter the cost as numbers");
        }
    }

    @FXML
    private void handleDatePickerServiceActivityAction(ActionEvent event) {
        if(datePickerServiceActivity.getValue() == null) {
            Utils.showAlert("Wrong Inputs", null, "Please choose a date");
        }

    }

    public void close_window(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleButtonServiceActivityAcceptAction(ActionEvent event) throws IOException {

        if (vehicle == null) {
            Utils.showAlert("No Vehicle Selected", 
                            "Unable to Add Activity", 
                            "Please select a vehicle before attempting to add an activity. " +
                            "If you are currently in the 'Workshops' tab, switch back to the 'Vehicles' tab " +
                            "to view and select a vehicle. You can choose a vehicle from the vehicle list or dropdown menu. " +
                            "If your vehicle is not listed, ensure it is properly registered in the system.");
        }
        

        //Create new service activity
        double costOfService;
        String descriptionOfService = getDescriptionOfService();
        ObservableList<Part> partsReplaced = getPartsReplaced();
        ServiceWorkshop selectedWorkshop = comboBoxWorkshop.getValue();
        LocalDate serviceDate = datePickerServiceActivity.getValue();

        try {
            costOfService = getCostOfService();
        } catch (NumberFormatException e) {
            Utils.showAlert("Wrong Inputs", "Invalid Cost", "The cost of service must be a numeric value.");
            return;
        }
    
        if (descriptionOfService.isEmpty()) {
            Utils.showAlert("Wrong Inputs", "Missing Description", "Please provide a description for the service activity.");
            return;
        }
    
        if (selectedWorkshop == null) {
            Utils.showAlert("Wrong Inputs", "No Workshop Selected", "Please select a workshop for the service activity.");
            return;
        }
    
        if (serviceDate == null) {
            Utils.showAlert("Wrong Inputs", "No Date Selected", "Please choose a date for the service activity.");
            return;
        }


        //Checking so that Large truck does not have internal service workshop
        if(vehicle.getType().equals("Large Truck") && selectedWorkshop.getType().equals("Internal")) {
            Utils.showAlert("Wrong Inputs", null, "The vehicle type Large Truck cannot be serviced at an Internal workshop");
            return;
        }

        if (this.isEditMode){
            //remoe existing workshop association
            System.out.println(selectedWorkshop);
            ServiceWorkshop workshop = serviceActivity.getServiceWorkshop();
            if (workshop != null){
                workshop.removeServiceActivity(serviceActivity);
            }
            serviceActivity.setCostOfService(costOfService);
            serviceActivity.setDate(serviceDate);
            serviceActivity.setDescription(descriptionOfService);
            serviceActivity.setPartsReplaced(partsReplaced);
            serviceActivity.setServiceWorkshop(selectedWorkshop);
            selectedWorkshop.addServiceActivity(serviceActivity);
        } else {
            // Add an activity using the activity factory
            // The reason we do this is so that we always create an activity the same way
            // which means that we can avoid mistakes where we mis setting certain attributes or
            // Forget to do an association.
            ActivityFactory activityFactory = new ActivityFactory(vehicle);
            serviceActivity = activityFactory.addServiceActivity(partsReplaced, serviceDate, descriptionOfService, costOfService, selectedWorkshop);

        }

        // Calculate the new total cost after adding the new activity.
        double newTotalCost = vehicle.getTotalServiceCost();

        // Compare with 100,000 and display the warning message if it exceeds.
        if (newTotalCost > 100000) {
            Utils.showAlert("Wrong Inputs", "High Total Service Cost", "The total cost of servicing this vehicle exceeds 100000!\nNew total cost: " + newTotalCost);
        }
        
        close_window(event);
    }  


    @FXML
    private void handleReplacedPartsAction(ActionEvent event) throws IOException{
        showReplacedPartsDialog();
    }

    public ServiceActivity getServiceActivity() {
        return serviceActivity;
    }

    @FXML
    private void handleButtonCancelAction(ActionEvent event) {
        close_window(event);
    }

    public void showReplacedPartsDialog() throws IOException {
        mainViewController.editParts(partsReplaced);
    }
    public void setServiceWorkshopRegister(ServiceWorkshopRegister serviceWorkshopRegister){
        this.serviceWorkshopRegister = serviceWorkshopRegister;
    }
    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public void setActivity(ServiceActivity serviceActivity){
        this.serviceActivity = serviceActivity;
    }

}
