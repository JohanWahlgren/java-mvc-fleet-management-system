package com.javengers.ui;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.IOException;

import com.javengers.controllers.MainViewController;

public class WorkshopTableContextMenu extends ContextMenu {
    private MainViewController controller;

    public WorkshopTableContextMenu(MainViewController mainViewController) {
        super();
        this.controller = mainViewController;

        // Add Workshop Menu Item
        MenuItem addWorkshop = new MenuItem("Add Workshop");
        addWorkshop.setOnAction(e -> {
            try {
                controller.addWorkshopFromFXML();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // Edit Workshop Menu Item
        MenuItem editWorksop = new MenuItem("Edit Workshop");
        editWorksop.setOnAction(e -> {
            try {
                controller.editSelectedWorkshopFromFXML();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // Remove Workshop Menu Item
        MenuItem removeWorkshop = new MenuItem("Remove Workshop");
        removeWorkshop.setOnAction(e -> {
            controller.removeSelectedWorkshop();
        });

        // Add Menu Items to Context Menu
        this.getItems().addAll(addWorkshop, editWorksop, removeWorkshop);
    }
}
