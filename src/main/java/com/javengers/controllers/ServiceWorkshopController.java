package com.javengers.controllers;

import com.javengers.models.ServiceWorkshop;
import com.javengers.models.ServiceWorkshopRegister;

import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class ServiceWorkshopController {

    private ServiceWorkshop serviceWorkshop = new ServiceWorkshop();
    private ServiceWorkshopRegister serviceWorkshopRegister;

    @FXML
    private TextField textFieldWorkshopName;

    @FXML
    private TextField textFieldWorkshopAddress;

    @FXML
    private ComboBox<String> comboBoxWorkshopType;

    public boolean isEditMode = false;

    public void initialize() {
        
        comboBoxWorkshopType.setItems(FXCollections.observableArrayList(ServiceWorkshop.SERVICE_WORKSHOP_TYPES));
    
        // If you want to set a default value
        comboBoxWorkshopType.setValue(ServiceWorkshop.SERVICE_WORKSHOP_TYPES.get(0));

        if (isEditMode){
            populateFieldsForEdit();
        }
    }

    private void populateFieldsForEdit(){
        textFieldWorkshopName.setText(serviceWorkshop.getName());
        textFieldWorkshopAddress.setText(serviceWorkshop.getAddress());
        comboBoxWorkshopType.setValue(serviceWorkshop.getType());
    }

    public String getName() {
        return textFieldWorkshopName.getText();
    }

    public String getAdress() {
        return textFieldWorkshopAddress.getText();
    }

    public String getType() {
        return comboBoxWorkshopType.getValue();
    }

    public void close_window(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleButtonWorkshopAcceptAction(ActionEvent event) {
        // Set instance variables for new service activity
        String name = getName();
        String address = getAdress();
        String type = getType();
        // Add an activity using the activity factory
        // The reason we do this is so that we always create an activity the same way
        // which means that we can avoid mistakes where we miss setting certain attributes or
        // Forget to do an association.
        if (isEditMode){
            serviceWorkshop.setAddress(address);
            serviceWorkshop.setName(name);
            serviceWorkshop.setType(type);
        } else {
            serviceWorkshop = new ServiceWorkshop(name, type, address);
            serviceWorkshopRegister.addWorkshop(serviceWorkshop);
        }

        close_window(event);
    }

    @FXML 
    private void handleButtonWorkshopCancelAction(ActionEvent e){
        close_window(e);
    }

    public ServiceWorkshop getServiceWorkshop() {
        return serviceWorkshop;
    }

    @FXML
    private void handleButtonCancelAction(ActionEvent event) {

    }

    public void setServiceWorkshop(ServiceWorkshop serviceWorkshop){
        this.serviceWorkshop = serviceWorkshop;
    }

    public void setServiceWorkshopRegister(ServiceWorkshopRegister serviceWorkshopRegister){
        this.serviceWorkshopRegister = serviceWorkshopRegister;
    }
}