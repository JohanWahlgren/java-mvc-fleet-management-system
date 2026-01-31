package com.javengers.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.IOException;

import com.javengers.controllers.MainViewController;

import javafx.scene.control.SeparatorMenuItem;

public class VehicleTableContextMenu extends ContextMenu {
    MainViewController controller;

    public VehicleTableContextMenu(MainViewController mainViewController) {
        super();
        this.controller = mainViewController;

        // Add Vehicle Menu Item
        MenuItem addVehicle = new MenuItem("Add Vehicle");
        addVehicle.setOnAction(e -> {
            try {
                controller.addVehicleFromFXML();
            } catch (IOException e1) {
                Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
                e1.printStackTrace();
            }
        });

        // Remove Vehicle Menu Item
        MenuItem removeVehicle = new MenuItem("Delete Vehicle");
        removeVehicle.setOnAction(e -> {
            controller.removeSelectedVehicle();
        });

        // Edit Vehicles
        MenuItem editVehicle = new MenuItem("Edit Vehicle");
        editVehicle.setOnAction(e -> {
            try {
                controller.editSelectedVehicleFromFXML();
            } catch (IOException e1) {
                Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
                e1.printStackTrace();
            }
        });

        // Display total cost of vehicle
        MenuItem vehicleTotalCost = new MenuItem("Show Total Service Cost");
        vehicleTotalCost.setOnAction(e -> {
            controller.showTotalCostForVehicle();
        });

        // Add Service Activity
        MenuItem addServiceActivity = new MenuItem("Add Service Activity");
        addServiceActivity.setOnAction(e -> {
            try {
                controller.addServiceActivityFromFXML();
            } catch (IOException e1) {
                Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
                e1.printStackTrace();
            }
        });

        // Add Maintenance Activity
        MenuItem addMaintenanceActivity = new MenuItem("Add Maintenance Activity");
        addMaintenanceActivity.setOnAction(e -> {
            try {
                controller.addMaintenanceActivityFromFXML();
            } catch (IOException e1) {
                Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
                e1.printStackTrace();
            }
        });
        SeparatorMenuItem separator = new SeparatorMenuItem();

        MenuItem listWorkshops = new MenuItem("List Workshops");
        listWorkshops.setOnAction(e -> {
            controller.listWorkshopsForSelectedVehicle();
        });
        
        // Add Menu Items to Context Menu
        this.getItems().addAll(addVehicle, editVehicle, removeVehicle, separator, vehicleTotalCost, addServiceActivity, addMaintenanceActivity, listWorkshops);
    }
}