package com.javengers.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.IOException;

import com.javengers.controllers.MainViewController;

public class MaintenanceScheduleContextMenu extends ContextMenu {
    MainViewController controller;

    public MaintenanceScheduleContextMenu(MainViewController mainViewController) {
        super();
        this.controller = mainViewController;

        // Add Activity
        MenuItem addActivity = new MenuItem("Add Activity");
        addActivity.setOnAction(e -> {
            try {
                controller.addMaintenanceActivityFromFXML();
            } catch (IOException e1) {
                Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
                e1.printStackTrace();
            }
        });

        // Remove Activity
        MenuItem removeActivity = new MenuItem("Remove Activity");
        removeActivity.setOnAction(e -> {
            controller.removeSelectedMaintenanceActivity();
        });

        // Edit Activity
        MenuItem editActivityItem = new MenuItem("Edit Activity");
        editActivityItem.setOnAction(e -> {
            controller.editSelectedMaintenanceActivity();
        });

        // Add Menu Items to Context Menu
        this.getItems().addAll(addActivity, editActivityItem, removeActivity);
    }
}
