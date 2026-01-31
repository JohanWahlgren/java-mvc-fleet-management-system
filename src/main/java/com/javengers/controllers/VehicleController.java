package com.javengers.controllers;

import com.javengers.exceptions.DuplicateVinException;
import com.javengers.factories.VehicleFactory;
import com.javengers.models.Vehicle;
import com.javengers.ui.Utils;
import com.javengers.models.VehicleRegister;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VehicleController {

    public boolean isEditMode = false;
    private VehicleRegister vehicleRegister;

    @FXML
    private TextField textFieldVehicleName;

    @FXML
    private ComboBox<String> comboBoxVehicleType;

    @FXML
    private TextField textFieldVehicleCapacity;

    @FXML
    private TextField textFieldVehicleCurrentLocation;

    @FXML
    private Button buttonVehicleAdd;

    @FXML
    private Label labelVehicleName;

    @FXML
    private Label labelVehicleType;

    @FXML
    private Label labelVehicleCapacity;

    @FXML
    private Label labelVehicleCurrentLocation;

    private Vehicle vehicle;

    public void initialize() {
        // Populate the ComboBox with vehicle type options
        comboBoxVehicleType.setItems(FXCollections.observableArrayList(Vehicle.VEHICLE_TYPES));

        // Set the default value
        comboBoxVehicleType.setValue(Vehicle.VEHICLE_TYPES.get(0));
        if (isEditMode){
            populateFieldsForEdit();
        }
    }

    private void populateFieldsForEdit() {
        textFieldVehicleName.setText(vehicle.getName());
        comboBoxVehicleType.setValue(vehicle.getType());
        textFieldVehicleCapacity.setText(Double.toString(vehicle.getCapacity()));
        textFieldVehicleCurrentLocation.setText(vehicle.getCurrentLocation());
    }

    // Method to get data from text fields
    public String getVehicleName() {
        return textFieldVehicleName.getText();
    }

    //Fetches the selected vehicle type from a ComboBox and returns it as a string. 
    //If no vehicle is selected, an empty string is returned
    public String getVehicleType() {
        Object selectedValue = comboBoxVehicleType.getValue();
        return selectedValue != null ? selectedValue.toString() : "";
    }
    

    public String getVehicleCapacity() {
        return textFieldVehicleCapacity.getText();
    }

    public String getVehicleCurrentLocation() {
        return textFieldVehicleCurrentLocation.getText();
    }

    public void close_window(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleButtonVehicleCancelAction(ActionEvent event){
        close_window(event);
    }

    @FXML
    private void handleButtonVehicleAcceptAction(ActionEvent event) {
        String vehicleName = getVehicleName();
        String vehicleType = getVehicleType();
        String stringVehicleCapacity = getVehicleCapacity();
        String vehicleCurrentLocation = getVehicleCurrentLocation();
        double vehicleCapacity = 0;
    
        if (vehicleName.isEmpty()) {
            Utils.showAlert("Wrong Inputs", "Missing Name", "Please provide a name for the vehicle.");
            return;
        }
    
        if (vehicleType.isEmpty()) {
            Utils.showAlert("Wrong Inputs", "Missing Type", "Please provide a type for the vehicle.");
            return;
        }
    
        if (stringVehicleCapacity.isEmpty()) {
            Utils.showAlert("Wrong Inputs", "Missing Capacity", "Please provide the capacity for the vehicle.");
            return;
        }
    
        try {
            vehicleCapacity = Double.parseDouble(stringVehicleCapacity);
            if (vehicleCapacity < 0) {
                Utils.showAlert("Wrong Inputs", "Invalid Capacity", "Vehicle capacity cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            Utils.showAlert("Wrong Inputs", "Invalid Number", "The capacity must be a valid numeric value.");
            return;
        }

        if(vehicleCapacity > 74000) {
            Utils.showAlert("STOP", "Breaking the law", "The maximum weight for a truck in Sweden is 74000 kg, please lower the capacity");
            textFieldVehicleCapacity.setText("");
            return;
        }
    
        if (vehicleCurrentLocation.isEmpty()) {
            Utils.showAlert("Wrong Inputs", "Missing Location", "Please provide the current location of the vehicle.");
            return;
        }
    
        // Additional logic for editing or creating a new vehicle
        if (isEditMode) {
            vehicle.setName(vehicleName);
            vehicle.setType(vehicleType);
            vehicle.setCapacity(vehicleCapacity);
            vehicle.setCurrentLocation(vehicleCurrentLocation);
        } else {
            vehicle = VehicleFactory.createVehicle(vehicleName, vehicleType, vehicleCapacity, vehicleCurrentLocation);
            try {
                vehicleRegister.addVehicle(vehicle);
            } catch (DuplicateVinException e) {
                Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
                e.printStackTrace();
            }
        }
        
        close_window(event);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public VehicleRegister getVehicleRegister(){
        return vehicleRegister;
    }

    public void setVehicleRegister(VehicleRegister vehicleRegister){
        this.vehicleRegister = vehicleRegister;
    }
}