package com.javengers.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {
    // Utils method for showing alert dialogs. 
    // Allows us to reuse same code for all alert dialogs. 
    public static void showAlert(String title, String header, String message){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait(); 
    }
}
