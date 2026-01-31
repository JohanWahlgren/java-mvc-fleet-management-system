package com.javengers.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import com.javengers.controllers.MainViewController;

public class ServiceHistoryContextMenu extends ContextMenu {
    private MainViewController controller;

    public ServiceHistoryContextMenu(MainViewController mainViewController) {
        super();
        this.controller = mainViewController;

        // Add Activity
        MenuItem addActivity = new MenuItem("Add Activity");
        addActivity.setOnAction(e -> {
            try {
                controller.addServiceActivityFromFXML();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // Remove Activity
        MenuItem removeActivity = new MenuItem("Remove Activity");
        removeActivity.setOnAction(e -> {
            controller.removeSelectedServiceActivity();
        });

        // Edit Activity
        MenuItem editActivityItem = new MenuItem("Edit Activity");
        editActivityItem.setOnAction(e -> {
            controller.editSelectedServiceActivity();
        });

        // Edit Parts
        MenuItem editPartsMenuItem = new MenuItem("Edit Parts");
        editPartsMenuItem.setOnAction(e -> {
            controller.editSelectedServiceActivityParts();
        });

        // Add Menu Items to Context Menu
        this.getItems().addAll(addActivity, editActivityItem, removeActivity, editPartsMenuItem);
    }
}
